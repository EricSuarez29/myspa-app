
package com.javeros.myspa.app.controllers;

import com.javeros.myspa.app.db.ConexionMySQL;
import com.javeros.myspa.app.models.Sucursal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SucursalController {
    
    public List<Sucursal> readAll(String filter) throws Exception {
        
        String query = "SELECT * FROM sucursal WHERE estatus = 1;";
        
        List<Sucursal> sucursales = new ArrayList<>();
        
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        Connection conexion = connMySQL.open();
        
        Statement statement = conexion.createStatement();
        
        ResultSet rs = statement.executeQuery(query);
        
        while(rs.next()) sucursales.add(fill(rs));
        
        rs.close();
        statement.close();
        connMySQL.close();
        
        return sucursales;
    }
    
    
    private Sucursal fill(ResultSet rs) throws Exception{
        Sucursal s = new Sucursal();
        s.setDomicilio(rs.getString("domicilio"));
        s.setEstatus(rs.getInt("estatus"));
        s.setId(rs.getInt("idSucursal"));
        s.setLatitud(rs.getDouble("latitud"));
        s.setLongitud(rs.getDouble("longitud"));
        s.setNombre(rs.getString("nombre"));
        return s;
    }

    public Sucursal findById(int id) throws Exception {
        
        String query = "SELECT * FROM sucursal WHERE idSucursal = ?";
        
        List<Sucursal> sucursales;
        
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        Connection conexion = connMySQL.open();
        
        PreparedStatement statement = conexion.prepareStatement(query);
        
        statement.setInt(1, id);
        
        ResultSet rs = statement.executeQuery();
        
        return fill(rs);
    }

    public int create(Sucursal t) throws Exception {
        String query = "INSERT INTO sucursal(nombre, domicilio, latitud, longitud, estatus)"
                + " VALUES(?,?,?,?,?);";
        
        int idGenerado = -1;
        
        ConexionMySQL connSQL = new ConexionMySQL();
        Connection conexion = connSQL.open();
        PreparedStatement statement = conexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ResultSet rs;
        statement.setString(1, t.getNombre());
        statement.setString(2, t.getDomicilio());
        statement.setDouble(3,t.getLatitud());
        statement.setDouble(4, t.getLongitud());
        statement.setInt(5, t.getEstatus());
        statement.executeUpdate();
        rs = statement.getGeneratedKeys();
        if(rs.next()){
            idGenerado = rs.getInt(1);
            t.setId(idGenerado);
        }
        rs.close();
        statement.close();
        connSQL.close();
        return idGenerado;
    }

    public void update(Sucursal t) throws Exception {
        String query = "UPDATE sucursal SET nombre = ?, domicilio = ?, latitud = ?, longitud = ? WHERE idSucursal = ?";
        
        ConexionMySQL connSQL = new ConexionMySQL();
        Connection conexion = connSQL.open();
        PreparedStatement statement = conexion.prepareStatement(query);
        statement.setString(1, t.getNombre());
        statement.setString(2, t.getDomicilio());
        statement.setDouble(3, t.getLatitud());
        statement.setDouble(4, t.getLongitud());
        statement.setInt(5, t.getId());
        statement.executeUpdate();
        connSQL.close();
        statement.close();
    }

    public void delete(int id) throws Exception {
        String query = "UPDATE sucursal SET estatus = 0 WHERE idSucursal = ?";
        
        ConexionMySQL connSQL = new ConexionMySQL();
        Connection conexion = connSQL.open();
        PreparedStatement statement = conexion.prepareStatement(query);
        statement.setInt(1, id);
        statement.execute();
        connSQL.close();
        statement.close();
    }
          
}
