package com.savadanko.server.database.collections;

import com.savadanko.common.models.Flat;
import com.savadanko.server.database.sql.ISQLDataBase;
import com.savadanko.server.database.sql.Tables;

import java.util.LinkedHashMap;

public class FlatCollection implements CrudCollection{
    private LinkedHashMap<Long, Object> flatCollection;

    public FlatCollection() {
    }

    @Override
    public void fill(ISQLDataBase dataBase) {
        flatCollection = dataBase.readAll(Tables.FLATS);
    }

    @Override
    public void create(Object object) {
        Flat flat = (Flat) object;
        flatCollection.put(flat.getId(), flat);
    }

    @Override
    public Object read(long id) {
        return flatCollection.get(id);
    }

    @Override
    public LinkedHashMap<Long, Object> readAll() {
        return flatCollection;
    }

    @Override
    public void update(long id, Object object) {
        Flat flat = (Flat) object;
        flatCollection.put(id, flat);
    }

    @Override
    public void delete(long id) {
        flatCollection.remove(id);
    }
}
