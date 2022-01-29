
package com.javeros.myspa.app.controllers;

import com.javeros.myspa.app.db.ConexionMySQL;
import com.javeros.myspa.app.models.Empleado;
import com.javeros.myspa.app.models.Persona;
import com.javeros.myspa.app.models.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {
    
    private final AuthController authController;
    
    public LoginController(){
        this.authController = new AuthController();
    }
    
    public Empleado login(String nombreUsuario, String contrasegna) throws Exception{
        String query = "SELECT * FROM v_empleados WHERE nombreUsuario=? AND contrasenia=?";
        
        Empleado empleado = null;
        
        ConexionMySQL conn = new ConexionMySQL();
        
        Connection conexion = conn.open();
        
        PreparedStatement prepared = conexion.prepareStatement(query);
        prepared.setString(1, nombreUsuario);
        prepared.setString(2, contrasegna);
        
        ResultSet rs = prepared.executeQuery();
        
        if(rs.next()){
            empleado = fill(rs);
            Usuario usuario = empleado.getUsuario();
            usuario.setToken(authController.generateToken(usuario));
            authController.saveToken(usuario);
        }
        
        rs.close();
        prepared.close();
        conn.close();
        
        return empleado;
    }
    
    private Empleado fill(ResultSet rs) throws Exception{
        Empleado empleado = new Empleado();
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
        usuario.setToken(rs.getString("token"));
        
        empleado.setFoto(rs.getString("foto"));
        empleado.setId(rs.getInt("idEmpleado"));
        empleado.setNumeroEmpleado(rs.getString("numeroEmpleado"));
        empleado.setPuesto(rs.getString("puesto"));
        empleado.setRutaFoto(rs.getString("rutaFoto"));
        empleado.setEstatus(rs.getInt("estatus"));
        
        empleado.setPersona(persona);
        empleado.setUsuario(usuario);
        return empleado;
    }
}
