package com.savadanko.server.database.sql;

public interface ModelHandler {
    void createTables();
    void createModel(Object obj, Tables table);
    Object readModel(long id, Tables table);
    void updateModel(long id, Object obj, Tables table);
    void deleteModel(long id, Tables table);
}
