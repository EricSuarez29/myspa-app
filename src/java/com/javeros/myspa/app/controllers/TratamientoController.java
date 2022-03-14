
package com.javeros.myspa.app.controllers;

import com.javeros.myspa.app.db.ConexionMySQL;
import com.javeros.myspa.app.models.Tratamiento;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TratamientoController {
    private Connection connection;
    private Statement statement;
    private ResultSet rs;

    public TratamientoController() {
        try {
            this.connection = new ConexionMySQL().open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<Tratamiento> find(String filter) throws Exception{
        String query = null;
        if(filter == null || filter.equals("")){
            query = "SELECT * FROM tratamiento WHERE estatus = 1";
        } else {
            query = "SELECT * FROM tratamiento WHERE estatus = 1 AND nombre LIKE '%" + filter + "%'";
        }
        
        List<Tratamiento> tratamientos = new ArrayList();
        
        statement = connection.createStatement();
        rs = statement.executeQuery(query);
        
        while(rs.next()) tratamientos.add(fill(rs));
        
        return tratamientos;
    }
    
    public static Tratamiento fill(ResultSet rs) throws Exception{
        Tratamiento tratamiento = new Tratamiento();
        tratamiento.setId(rs.getLong("idTratamiento"));
        tratamiento.setNombre(rs.getString("nombre"));
        tratamiento.setDescripcion(rs.getString("descripcion"));
        tratamiento.setCosto(rs.getDouble("costo"));
        tratamiento.setEstatus(rs.getInt("estatus"));
        return tratamiento;
    }
}
