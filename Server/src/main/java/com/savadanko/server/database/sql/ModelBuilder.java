package com.savadanko.server.database.sql;

import java.util.LinkedHashMap;

public interface ModelBuilder {
    void createTables();
    long createModel(Object obj);
    Object readModel(long id);
    LinkedHashMap<Long, Object> readAll();
    void updateModel(long id, Object obj);
    void deleteModel(long id);
}
