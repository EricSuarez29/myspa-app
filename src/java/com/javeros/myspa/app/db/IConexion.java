
package com.javeros.myspa.app.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConexion {
    Connection open() throws Exception;
    void close() throws SQLException;
}
