package com.savadanko.server.database.collections;

import com.savadanko.common.models.Flat;

import java.util.LinkedHashMap;

public class FlatCollection implements CrudCollection{
    private final LinkedHashMap<Long, Flat> flatCollection;

    public FlatCollection(LinkedHashMap<Long, Flat> flatCollection) {
        this.flatCollection = new LinkedHashMap<>();
    }

    @Override
    public void create(Object object) {
    }

    @Override
    public Object read(long id) {
        return null;
    }

    @Override
    public void update(long id, Object object) {

    }

    @Override
    public void delete(long id) {

    }
}
