package com.savadanko.server.database.sql;

public interface ModelBuilder {
    void createTables();
    void createModel(Object obj);
    Object readModel(long id);
    void updateModel(long id, Object obj);
    void deleteModel(long id);
}
