package com.savadanko.server.database.collections;

import com.savadanko.common.models.Flat;
import com.savadanko.common.models.User;
import com.savadanko.server.database.sql.ISQLDataBase;
import com.savadanko.server.database.sql.Tables;

import java.util.LinkedHashMap;

public class UserCollection implements CrudCollection{
    private LinkedHashMap<Long, Object> userCollection;

    @Override
    public void fill(ISQLDataBase dataBase) {

        userCollection = dataBase.readAll(Tables.USERS);
    }

    @Override
    public void create(Object object) {
        User user = (User) object;
        userCollection.put(user.getId(), user);
    }

    @Override
    public Object read(long id) {
        return userCollection.get(id);
    }

    @Override
    public LinkedHashMap<Long, Object> readAll() {
        return userCollection;
    }

    @Override
    public void update(long id, Object object) {
        User user = (User) object;
        userCollection.put(id, user);
    }

    @Override
    public void delete(long id) {
        userCollection.remove(id);
    }
}
