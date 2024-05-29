package com.savadanko.server.database.collections;

public interface CrudCollection {
    void create(Object object);
    Object read(long id);
    void update(long id, Object object);
    void delete(long id);
}
