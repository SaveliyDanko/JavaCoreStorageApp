package com.savadanko.server.database.collections;

import com.savadanko.server.database.sql.ISQLDataBase;
import com.savadanko.server.database.sql.Tables;

import java.util.LinkedHashMap;

public class CollectionHandler implements ICollectionHandler{
    private final FlatCollection flatCollection;
    private final UserCollection userCollection;

    public CollectionHandler() {
        this.flatCollection = new FlatCollection();
        this.userCollection = new UserCollection();
    }

    @Override
    public void fill(ISQLDataBase dataBase) {
        flatCollection.fill(dataBase);
        userCollection.fill(dataBase);
    }

    @Override
    public void create(Object object, Tables collections) {
        if (collections.equals(Tables.FLATS)){
            flatCollection.create(object);
        }
        else if (collections.equals(Tables.USERS)){
            userCollection.create(object);
        }
    }

    @Override
    public Object read(long id, Tables collections) {
        if (collections.equals(Tables.FLATS)){
            return flatCollection.read(id);
        }
        else if (collections.equals(Tables.USERS)){
            return userCollection.read(id);
        }
        return null;
    }

    @Override
    public LinkedHashMap<Long, Object> readAll(Tables collections) {
        if (collections.equals(Tables.FLATS)){
            return flatCollection.readAll();
        }
        else if (collections.equals(Tables.USERS)){
            return userCollection.readAll();
        }
        return null;
    }

    @Override
    public void update(long id, Object object, Tables collections) {
        if (collections.equals(Tables.FLATS)){
            flatCollection.update(id, object);
        }
        else if (collections.equals(Tables.USERS)){
            userCollection.update(id, object);
        }
    }

    @Override
    public void delete(long id, Tables collections) {
        if (collections.equals(Tables.FLATS)){
            flatCollection.delete(id);
        }
        else if (collections.equals(Tables.USERS)){
            userCollection.delete(id);
        }
    }
}
