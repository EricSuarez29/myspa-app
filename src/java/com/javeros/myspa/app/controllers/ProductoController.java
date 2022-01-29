/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javeros.myspa.app.controllers;

import com.javeros.myspa.app.db.ConexionMySQL;
import com.javeros.myspa.app.models.Producto;
import com.mysql.jdbc.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alejandro
 */
public class ProductoController {
    /** 
     * Inserta un registro de producto en la BD y devuelve el ID generado.
     */
    public int insert(Producto p) throws Exception {
        //Definimos la consulta SQL que realiza la inserción del registro:
        String sql = "INSERT INTO producto(nombre, marca, precioUso, estatus) " + 
                "VALUES(?, ?, ?, ?)";
        
        //Aquí guardaremos el Id que se generará:
        int idGenerado = -1;
        
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexión a la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto ejecutaremos la sentencia SQL que realiza la
        //Inserción en la tabla. Debemos especificarle que queremos que nos
        //devuelva el ID que se genera al realizar la inserción del registro.
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        
        //En este objeto guardaremos el resultado de la consulta, la cual
        //nos devolverá los ID's que se generaron. En este caso, solo se
        //generará un ID:
        ResultSet rs = null;
        
        //Llenamos el valor de campo de la consulta SQL definida antes:
        pstmt.setString(1, p.getNombre());
        pstmt.setString(2, p.getMarca());
        pstmt.setDouble(3, p.getPrecioUso());
        pstmt.setInt(4, 1);
        
        //Ejecutamos la consulta:
        pstmt.executeUpdate();
        
        //Le pedimos al PreparedStatement el valor de las claves primarias
        //generadas, que en este caso, es solo un valor:
        rs = pstmt.getGeneratedKeys();
        
        //Intentamos movernos al primer registro:
        if (rs.next()) {
            idGenerado = rs.getInt(1);
            p.setId(idGenerado);
        }
        
        //Cerramos todos los objetos de conexión con la Base de Datos
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        //Devolvemos el ID que se generó:
        return idGenerado;
    }
    
    /**
     * Actualiza un registro de producto en la BD.
     */
    public void update(Producto p) throws Exception {
        String sql = "UPDATE producto SET nombre=?, marca=?, precioUso=? WHERE idProducto=?";
        
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexión a la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto ejecutaremos la sentencia SQL que realiza la actualización dde los campos
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        //Llenamos el valor de campo de la consulta SQL definifa antes:
        pstmt.setString(1, p.getNombre());
        pstmt.setString(2, p.getMarca());
        pstmt.setDouble(3, p.getPrecioUso());
        pstmt.setInt(4, p.getId());
        
        //Ejecutamos la consulta:
        pstmt.executeUpdate();
        
        //Cerramos todos los objetos de conexión con la Base de Datos
        pstmt.close();
        connMySQL.close();
    }
    
    public void delete(int idProducto) throws Exception {
        String sql = "UPDATE producto SET estatus=0 WHERE idProducto=?";
        
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexión a la Base de Datos:
        Connection conn = connMySQL.open();
        
        //Con este objeto ejecutaremos la sentencia SQL que realiza la actualización dde los campos
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        //Llenamos el valor de campo de la consulta SQL definifa antes:
        pstmt.setInt(1, idProducto);
        
        //Ejecutamos la consulta:
        pstmt.executeUpdate();
        
        //Cerramos todos los objetos de conexión con la Base de Datos
        pstmt.close();
        connMySQL.close();
    }
    
    public Producto findByID(int id) throws Exception {
        return null;
    }
    
    /**
     * Consulta todos los registros de productos en la BD y devuelve una
     * lista dinámica de objetos de tipo producto
     */
    public List<Producto> getAll(String filtro) throws Exception {
        //Definimos la consulta SQL:
        String sql = "SELECT * FROM producto WHERE estatus = 1";
        
        //Aquí guardaremos objetos de tipo producto.
        //Una lista es un contenedor dinámico de objetos.
        List<Producto> productos = new ArrayList<Producto>();
        
        //Una variable temporal para crear nuevas instancias de Producto:
        Producto p = null;
        
        //Con este objeto vamos a conectar la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        //Abrimos la conexión con MySQL:
        Connection conn = connMySQL.open();
        
        //Declaramos e inicializamos el objeto con el que ejecutaremos la consalta SQL:
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        //Ejecutamos la consulta y guardamos su resultado:
        ResultSet rs = pstmt.executeQuery();
        
        //Recorremos el ResultSet:
        while (rs.next()) {
            p = fill(rs);
            productos.add(p);
        }
        
        //Cerramos los objetos de BD:
        rs.close();
        pstmt.close();
        connMySQL.close();
        
        //Devolvemos la lista de sucursales:
        return productos;
    }
    
    /**
     * Crea un objeto de tipo Producto y llena sus propiedades
     * utilizando los datos proporcionados por un ResultSet.
     * @param rs
     * @return
     */
    private Producto fill(ResultSet rs) throws Exception {
        //Creamos una nueva instancia de Producto:
        Producto p = new Producto();
        
        //Llenamos sus propiedades:
        p.setMarca(rs.getString("marca"));
        p.setEstatus(rs.getInt("estatus"));
        p.setId(rs.getInt("idProducto"));
        p.setPrecioUso(rs.getFloat("precioUso"));
        p.setNombre(rs.getString("nombre"));
        
        //Devolvemos el objeto de tipo Producto:
        return p;
    }
}
