package com.savadanko.server;

import com.savadanko.server.database.DataBaseHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ServerApp {
    private static final Logger logger = LogManager.getLogger(ServerApp.class);

    public static void main(String[] args) {
        DataBaseHandler dataBaseHandler = DataBaseHandler.getInstance();
        dataBaseHandler.connect();

        try {
            ServerManager networkManager = new ServerManager();
            networkManager.start();
        }
        catch (IOException e){
            logger.error(e.getMessage());
        }

        //dataBaseHandler.disconnect();
    }
}
