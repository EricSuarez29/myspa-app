
package com.javeros.myspa.app.controllers;


import com.javeros.myspa.app.db.ConexionMySQL;
import com.javeros.myspa.app.models.Empleado;
import com.javeros.myspa.app.models.Persona;
import com.javeros.myspa.app.models.Usuario;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoController {

    public List<Empleado> readAll(String word) throws Exception {
        
        List<Empleado> empleados = new ArrayList<Empleado>();
         
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        Connection conexion = connMySQL.open();
        PreparedStatement statement;
        
        if(word == null || word.equals("")){
            String query = "SELECT * FROM v_empleados";
            statement = conexion.prepareStatement(query);
        } else {
            String query = "SELECT * FROM v_empleados WHERE nombre LIKE ? OR apellidoPaterno LIKE ? OR apellidoMaterno LIKE ?";
            statement = conexion.prepareStatement(query);
            statement.setString(1, "%" + word + "%");
            statement.setString(2, "%" + word + "%");
            statement.setString(3, "%" + word + "%");
        }
        
        
        ResultSet rs = statement.executeQuery();
        
        while(rs.next()){
            empleados.add(fill(rs));
        }
        
        rs.close();
        statement.close();
        connMySQL.close();
        
        return empleados;
    }

    public Empleado findById(int id) throws Exception {
        String query = "SELECT * FROM v_empleados WHERE idEmpleado=?";
        
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        Connection conexion = connMySQL.open();
        
        PreparedStatement statement = conexion.prepareStatement(query);
        statement.setInt(1, id);
        System.out.println(id);
        ResultSet rs = statement.executeQuery();
        rs.next();
        
        Empleado e = new Empleado();
        Persona p = new Persona();
        p.setId(rs.getInt("idPersona"));
        Usuario u = new Usuario();
        u.setId(rs.getInt("idUsuario"));
        e.setPersona(p);
        e.setUsuario(u);
        return e;
    }

    public int create(Empleado t) throws Exception {
        
        String query = "{call insertarEmpleado(?, ?, ?, ?, ?, ?, ?, " // Datos persona
                                            + "?, ?, ?, " // Data usuario
                                            + "?, ?, ?, " // Datos empleados
                                            + "?, ?, ?, ?)}"; // valore de retorno
        
        int idPersona = -1;
        int idUsuario = -1;
        int idEmpleado = -1;
        String numEmpleado = "";
        
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conexion = connMySQL.open();
        
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
        
        // Empleado
        callable.setString(11, t.getPuesto());
        callable.setString(12, t.getFoto());
        callable.setString(13, t.getRutaFoto());
        
        callable.registerOutParameter(14, java.sql.Types.INTEGER);
        callable.registerOutParameter(15, Types.INTEGER);
        callable.registerOutParameter(16, Types.INTEGER);
        callable.registerOutParameter(17, Types.VARCHAR);
        
        callable.executeUpdate();
        
        idPersona = callable.getInt(14);
        idUsuario = callable.getInt(15);
        idEmpleado = callable.getInt(16);
        numEmpleado = callable.getString(17);
        
        t.getPersona().setId(idPersona);
        t.getUsuario().setId(idUsuario);
        t.setId(idEmpleado);
        t.setNumeroEmpleado(numEmpleado);
        
        callable.close();
        connMySQL.close();
        
        return idEmpleado;
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

    public void update(Empleado e) throws Exception {
        String query = "{call actualizarEmpleado(?, ?, ?, ?, ?, ?, ?," // Datos persona
                                            + "?, ?, ?," // Datos Usuario
                                            + "?, ?, ?," // Datos Empleado
                                            + "?, ?, ?)}"; // IDs de tablas realacionadas
        
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conexion = connMySQL.open();
        CallableStatement callable = conexion.prepareCall(query);
        
        Persona persona = e.getPersona();
        callable.setString(1, persona.getNombre());
        callable.setString(2, persona.getApellidoPaterno());
        callable.setString(3, persona.getApellidoMaterno());
        callable.setString(4, persona.getGenero());
        callable.setString(5, persona.getDomicilio());
        callable.setString(6, persona.getTelefono());
        callable.setString(7, persona.getRfc());
        
        Usuario usuario = e.getUsuario();
        callable.setString(8, usuario.getNombreUsuario());
        callable.setString(9, usuario.getContrasegna());
        callable.setString(10, usuario.getRol());
        
        callable.setString(11, e.getPuesto());
        callable.setString(12, e.getFoto());
        callable.setString(13, e.getRutaFoto());
        
        Empleado empleadoTemp = findById(e.getId());
        callable.setInt(14, empleadoTemp.getPersona().getId());
        callable.setInt(15, empleadoTemp.getUsuario().getId());
        callable.setInt(16, e.getId());
        
        callable.executeUpdate();
        
        callable.close();
        connMySQL.close();
        
    }

    public void delete(int id) throws Exception {
        String query = "UPDATE empleado SET estatus = 0 WHERE idEmpleado= ?";
        
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conexion = connMySQL.open();
        PreparedStatement prepared = conexion.prepareStatement(query);
        System.out.println(id);
        prepared.setInt(1, id);
        prepared.executeUpdate();
        
        prepared.close();
        connMySQL.close();
    }
    
    public static Empleado fillOnService(ResultSet rs) throws Exception{
        Empleado empleado = new Empleado();
        Persona persona = new Persona();
        
        empleado.setId(rs.getInt("idEmpleado"));
        persona.setNombre(rs.getString("nombreEmpleado"));
        persona.setApellidoMaterno(rs.getString("apellidoMaternoEmpleado"));
        persona.setApellidoPaterno(rs.getString("apellidoPaternoEmpleado"));
        
        empleado.setPersona(persona);
        return empleado;
    }
}
