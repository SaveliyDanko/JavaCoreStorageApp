package com.savadanko.server.database;

import com.savadanko.server.database.sql.ISQLDataBase;
import com.savadanko.server.database.sql.SQLDataBase;
import com.savadanko.server.database.sql.SQLObjectHandler;
import com.savadanko.server.database.sql.Tables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


public class DataBaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(DataBaseHandler.class);

    private final ISQLDataBase dataBase;

    public DataBaseHandler() {
        this.dataBase = SQLDataBase.getInstance(new SQLObjectHandler());
    }

    public void connect(){
        ClassLoader classLoader = DataBaseHandler.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("sql.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))){
            String url = br.readLine();
            String user = br.readLine();
            String password = br.readLine();

            dataBase.connect(url, user, password);
        }
        catch (IOException e){
            logger.error(e.getMessage());
        }
    }

    public void disconnect(){
        dataBase.disconnect();
    }

    public void create(Object object, Tables table){
        dataBase.create(object, table);
    }

    public Object read(long id, Tables table){
        return dataBase.read(id, table);
    }

    public void update(long id, Object object, Tables table){
        dataBase.update(id, object, table);
    }

    public void delete(long id, Tables table){
        dataBase.delete(id, table);
    }
}
