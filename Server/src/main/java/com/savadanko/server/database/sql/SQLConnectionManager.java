package com.savadanko.server.database.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnectionManager implements ConnectionManager{
    private static final Logger logger = LoggerFactory.getLogger(SQLConnectionManager.class);

    private Connection connection;

    @Override
    public void connect(String url, String user, String pass) {
        try{
            connection = DriverManager.getConnection(url, user, pass);
        }
        catch (SQLException e){
            logger.error(e.getMessage());
        }
    }

    @Override
    public void disconnect() {
        try {
            connection.close();
        }
        catch (SQLException e){
            logger.error(e.getMessage());
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}
