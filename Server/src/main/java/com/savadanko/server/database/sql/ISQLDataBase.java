package com.savadanko.server.database.sql;

import java.sql.SQLException;

public interface ISQLDataBase {
    void connect(String url, String user, String pass);
    void disconnect();
    void create(Object object, Tables tables);
    Object read(long id, Tables table);
    void update(long id, Object object, Tables table);
    void delete(long id, Tables table);
}
