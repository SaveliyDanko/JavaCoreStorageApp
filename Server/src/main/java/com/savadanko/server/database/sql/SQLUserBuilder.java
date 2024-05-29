package com.savadanko.server.database.sql;

import com.savadanko.common.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserBuilder implements ModelBuilder {
    private static final Logger logger = LogManager.getLogger(SQLUserBuilder.class);

    private Connection connection;

    public SQLUserBuilder() {
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void createTables() {
        String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id SERIAL PRIMARY KEY," +
                "login VARCHAR(128) UNIQUE," +
                "password VARCHAR(128));";

        try (PreparedStatement statement = connection.prepareStatement(usersTable)) {
            statement.execute();
        } catch (SQLException e) {
            logger.error("Error creating table: {}", e.getMessage(), e);
        }
    }

    @Override
    public void createModel(Object object) {
        User user = (User) object;
        String query = "INSERT INTO users (login, password) VALUES (?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error saving object: {}", e.getMessage());
            throw new RuntimeException("A user with login: " + user.getLogin() + " already exists");
        }
    }

    @Override
    public Object readModel(long id) {
        String query = "SELECT * FROM users WHERE id = ?;";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    long userId = resultSet.getLong("id");
                    String login = resultSet.getString("login");
                    String password = resultSet.getString("password");

                    User user = new User(login, password);
                    user.setId(userId);
                    return user;
                }
            }
        } catch (SQLException e) {
            logger.error("Error reading object: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void updateModel(long id, Object object) {
        String query = "UPDATE users SET login = ?, password = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            User user = (User) object;
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setLong(3, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error updating object: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteModel(long id) {
        String query = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error deleting object: {}", e.getMessage(), e);
        }
    }
}
