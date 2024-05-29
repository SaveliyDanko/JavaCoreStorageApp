package com.savadanko.server;


import com.savadanko.common.models.*;
import com.savadanko.server.database.sql.SQLDataBase;
import com.savadanko.server.database.sql.SQLObjectHandler;
import com.savadanko.server.database.sql.Tables;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class ServerApp {
    private static final Logger logger = LogManager.getLogger(ServerApp.class);

    public static void main(String[] args) {
        SQLDataBase dataBase = SQLDataBase.getInstance(new SQLObjectHandler());
        Flat flat = new Flat(
                "Suck",
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

        try{
            dataBase.connect("jdbc:postgresql://localhost:5432/ItmoDataBase", "savadanko", "pass");
            //dataBase.create(user);
            dataBase.create(flat, Tables.FLATS);
            //dataBase.create(flat);
            //dataBase.delete(1);
            //dataBase.update(4, flat);
            //System.out.println(dataBase.read(1));
            dataBase.disconnect();
        }
        catch (SQLException e){
            logger.error(e.getMessage());
        }
        catch (RuntimeException e){
            logger.info(e.getMessage());
        }
    }
}
