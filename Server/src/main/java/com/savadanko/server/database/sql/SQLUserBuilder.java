package com.savadanko.server.database.sql;

import com.savadanko.common.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.LinkedHashMap;

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
                "password BYTEA);";

        try (PreparedStatement statement = connection.prepareStatement(usersTable)) {
            statement.execute();
        } catch (SQLException e) {
            logger.error("Error creating table: {}", e.getMessage(), e);
        }
    }

    @Override
    public long createModel(Object object) {
        User user = (User) object;
        String query = "INSERT INTO users (login, password) VALUES (?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getLogin());
            statement.setBytes(2, user.getPasswordHash());

            statement.executeUpdate();

            try(ResultSet generatedKeys = statement.getGeneratedKeys()){
                if (generatedKeys.next()){
                    long userId = generatedKeys.getLong(1);
                    user.setId(userId);
                    return userId;
                }
            }
        } catch (SQLException e) {
            logger.error("Error saving object: {}", e.getMessage());
        }
        return 0;
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
                    byte[] passwordHash = resultSet.getBytes("password");

                    User user = new User(login, passwordHash);
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
    public LinkedHashMap<Long, Object> readAll() {
        String query = "SELECT * FROM users;";

        LinkedHashMap<Long, Object> linkedHashMap = new LinkedHashMap<>();

        try(PreparedStatement statement = connection.prepareStatement(query)){
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    long userId = resultSet.getLong("id");
                    String login = resultSet.getString("login");
                    byte[] passwordHash = resultSet.getBytes("password");

                    User user = new User(userId, login, passwordHash);

                    linkedHashMap.put(userId, user);
                }
            }
        }
        catch (SQLException e){
            logger.error(e.getMessage());
        }
        return linkedHashMap;
    }

    @Override
    public void updateModel(long id, Object object) {
        String query = "UPDATE users SET login = ?, password = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            User user = (User) object;
            statement.setString(1, user.getLogin());
            statement.setBytes(2, user.getPasswordHash());
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
