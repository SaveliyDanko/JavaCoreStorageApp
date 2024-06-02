package com.savadanko.server.database;

import com.savadanko.server.database.collections.*;
import com.savadanko.server.database.sql.ISQLDataBase;
import com.savadanko.server.database.sql.SQLDataBase;
import com.savadanko.server.database.sql.SQLObjectHandler;
import com.savadanko.server.database.sql.Tables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.LinkedHashMap;


public class DataBaseHandler implements IDataBaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(DataBaseHandler.class);

    private static DataBaseHandler instance;

    private final ISQLDataBase dataBase;

    private ICollectionHandler collectionHandler;

    private DataBaseHandler(){
        this.dataBase = SQLDataBase.getInstance(new SQLObjectHandler());
        this.collectionHandler = new CollectionHandler();
    }

    public static synchronized DataBaseHandler getInstance(){
        if (instance == null){
            instance = new DataBaseHandler();
        }
        return instance;
    }

    public void connect(){
        ClassLoader classLoader = DataBaseHandler.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("sql.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))){
            String url = br.readLine();
            String user = br.readLine();
            String password = br.readLine();
            dataBase.connect(url, user, password);

            collectionHandler.fill(dataBase);

        }
        catch (IOException e){
            logger.error(e.getMessage());
        }
    }

    public void disconnect(){
        dataBase.disconnect();
    }

    public long create(Object object, Tables table){
        return dataBase.create(object, table);
    }

    public Object read(long id, Tables collections){
        return collectionHandler.read(id, collections);
    }

    @Override
    public LinkedHashMap<Long, Object> readAll(Tables collections) {
        return collectionHandler.readAll(collections);
    }

    public void update(long id, Object object, Tables table){
        dataBase.update(id, object, table);
        collectionHandler.update(id, object, table);
    }

    public void delete(long id, Tables table){
        dataBase.delete(id, table);
        collectionHandler.delete(id, table);
    }
}
