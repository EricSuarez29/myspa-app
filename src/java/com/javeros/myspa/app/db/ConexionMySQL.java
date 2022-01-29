
package com.javeros.myspa.app.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL implements IConexion{

    private final String DRIVER = "com.mysql.jdbc.Driver",
            URL = "jdbc:mysql://localhost:3306/myspa",
            USER = "root",
            PASSWORD = "";

    private static Connection conexion;
    
    @Override
    public Connection open() throws Exception {
        Class.forName(DRIVER);
        conexion = DriverManager.getConnection(URL, USER, PASSWORD);
        return conexion;
    }

    @Override
    public void close(){
        try{
            if(conexion != null) conexion.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
}
