package com.savadanko.server;


import com.savadanko.common.models.*;
import com.savadanko.server.database.DataBaseHandler;
import com.savadanko.server.database.sql.SQLDataBase;
import com.savadanko.server.database.sql.SQLObjectHandler;
import com.savadanko.server.database.sql.Tables;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class ServerApp {
    private static final Logger logger = LogManager.getLogger(ServerApp.class);

    public static void main(String[] args) {
        Flat flat = new Flat(
                "Suck Sava Danko",
                new Coordinates(12.5f, 100L),
                12.5f,
                100L,
                100.25f,
                View.PARK,
                Transport.LITTLE,
                new House("House1", 100L, 100L, 10, 10),
                "SavaDanko"
        );

        User user = new User(
                "SavaDanko",
                "sav123"
        );


        DataBaseHandler dataBase = new DataBaseHandler();

        try{
            dataBase.connect();
            //dataBase.create(user, Tables.USERS);
            dataBase.create(flat, Tables.FLATS);
            dataBase.update(2, flat, Tables.FLATS);
            dataBase.delete(1, Tables.FLATS);
            dataBase.disconnect();
        }
        catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
    }
}
