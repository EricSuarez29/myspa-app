
package com.javeros.myspa.app.controllers;

import com.javeros.myspa.app.db.ConexionMySQL;
import com.javeros.myspa.app.models.Cliente;
import com.javeros.myspa.app.models.Persona;
import com.javeros.myspa.app.models.Usuario;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ClienteController {
    public List<Cliente> readAll() throws Exception{
        List<Cliente> lista = new ArrayList<>();
        
        String query = "SELECT * FROM v_clientes";
        
        ConexionMySQL con = new ConexionMySQL();
        Connection conexion = con.open();
        Statement statement = conexion.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while(rs.next()){
            lista.add(fill(rs));
        }
        
        con.close();
        statement.close();
        rs.close();
        return lista;
    }
    
    public int create(Cliente t) throws Exception{
        String query = "{call insertarCliente(?, ?, ?, ?, ?, ?, ?, " // Datos persona
                                            + "?, ?, ?, " // Data usuario
                                            + "?, ?," // Datos empleados
                                            + "?, ?, ?)}"; // valore de retorno
        
        ConexionMySQL con = new ConexionMySQL();
        Connection conexion = con.open();
        CallableStatement callable = conexion.prepareCall(query);
        
        int idPersona = -1;
        int idUsuario = -1;
        int idCliente = -1;
        
         // Persona
        callable.setString(1, t.getPersona().getNombre());
        callable.setString(2, t.getPersona().getApellidoPaterno());
        callable.setString(3, t.getPersona().getApellidoMaterno());
        callable.setString(4, t.getPersona().getGenero());
        callable.setString(5, t.getPersona().getDomicilio());
        callable.setString(6, t.getPersona().getTelefono());
        callable.setString(7, t.getPersona().getRfc());
        
        // Usuario
        callable.setString(8, t.getUsuario().getNombreUsuario());
        callable.setString(9, t.getUsuario().getContrasegna());
        callable.setString(10, t.getUsuario().getRol());
  
        // Cliente
        callable.setString(11, t.getCorreo());
        callable.setString(12, t.getNumeroUnico());
        
        callable.registerOutParameter(13, Types.INTEGER);
        callable.registerOutParameter(14, Types.INTEGER);
        callable.registerOutParameter(15, Types.INTEGER);
        
        callable.executeUpdate();
        
        idPersona = callable.getInt(13);
        idUsuario = callable.getInt(14);
        idCliente = callable.getInt(15);
        
        t.getPersona().setId(idPersona);
        t.getUsuario().setId(idUsuario);
        t.setIdCliente(idCliente);
        
        con.close();
        callable.close();
        return idCliente;
    }
    
    public void update(Cliente t) throws Exception{
        String query = "{call actualizarCliente(?, ?, ?, ?, ?, ?, ?, " // Datos persona
                                            + "?, ?, ?, " // Data usuario
                                            + "?, ?," // Datos empleados
                                            + "?, ?, ?)}"; // valore de retorno
        
        ConexionMySQL con = new ConexionMySQL();
        Connection conexion = con.open();
        CallableStatement callable = conexion.prepareCall(query);
        
         // Persona
        callable.setString(1, t.getPersona().getNombre());
        callable.setString(2, t.getPersona().getApellidoPaterno());
        callable.setString(3, t.getPersona().getApellidoMaterno());
        callable.setString(4, t.getPersona().getGenero());
        callable.setString(5, t.getPersona().getDomicilio());
        callable.setString(6, t.getPersona().getTelefono());
        callable.setString(7, t.getPersona().getRfc());
        
        // Usuario
        callable.setString(8, t.getUsuario().getNombreUsuario());
        callable.setString(9, t.getUsuario().getContrasegna());
        callable.setString(10, t.getUsuario().getRol());
  
        // Cliente
        callable.setString(11, t.getCorreo());
        callable.setString(12, t.getNumeroUnico());
        System.out.println(t.getIdCliente());
        Cliente cliente = findById(t.getIdCliente());
        // ids
        callable.setInt(13, cliente.getPersona().getId());
        callable.setInt(14, cliente.getUsuario().getId());
        callable.setInt(15, t.getIdCliente());
        
        callable.executeUpdate();
        
        con.close();
        callable.close();
    }
    
    public Cliente findById(int id) throws Exception {
        String query = "SELECT * FROM v_clientes WHERE idCliente=?";
        
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        Connection conexion = connMySQL.open();
        
        PreparedStatement statement = conexion.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        rs.next();
        
        Cliente e = new Cliente();
        Persona p = new Persona();
        p.setId(rs.getInt("idPersona"));
        Usuario u = new Usuario();
        u.setId(rs.getInt("idUsuario"));
        e.setPersona(p);
        e.setUsuario(u);
        return e;
    }
    
    public void delete(int id) throws Exception{
        String query = "UPDATE cliente SET estatus = 0 WHERE idCliente=?";
        
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conexion = connMySQL.open();
        PreparedStatement prepared = conexion.prepareStatement(query);
        prepared.setInt(1, id);
        prepared.executeUpdate();
        
        prepared.close();
        connMySQL.close();
    }
    
    private Cliente fill(ResultSet rs) throws Exception{
        Cliente cliente = new Cliente();
        Persona persona = new Persona();
        Usuario usuario = new Usuario();
        
        persona.setApellidoMaterno(rs.getString("apellidoMaterno"));
        persona.setApellidoPaterno(rs.getString("apellidoPaterno"));
        persona.setDomicilio(rs.getString("domicilio"));
        persona.setGenero(rs.getString("genero"));
        persona.setId(rs.getInt("idPersona"));
        persona.setNombre(rs.getString("nombre"));
        persona.setRfc(rs.getString("rfc"));
        persona.setTelefono(rs.getString("telefono"));
        
        usuario.setContrasegna(rs.getString("contrasenia"));
        usuario.setId(rs.getInt("idUsuario"));
        usuario.setNombreUsuario(rs.getString("nombreUsuario"));
        usuario.setRol(rs.getString("rol"));
        
        cliente.setIdCliente(rs.getInt("idCliente"));
        cliente.setCorreo(rs.getString("correo"));
        cliente.setNumeroUnico(rs.getString("numeroUnico"));
        cliente.setEstatus(rs.getInt("estatus"));
        
        cliente.setPersona(persona);
        cliente.setUsuario(usuario);
        return cliente;
    }

}
