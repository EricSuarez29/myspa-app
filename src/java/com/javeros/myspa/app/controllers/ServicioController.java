package com.javeros.myspa.app.controllers;

import com.javeros.myspa.app.db.ConexionMySQL;
import com.javeros.myspa.app.models.Producto;
import com.javeros.myspa.app.models.Servicio;
import com.javeros.myspa.app.models.ServicioTratamiento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServicioController {
    
    public List<Servicio> find() throws Exception{
        String query = "SELECT * FROM v_servicios";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        Statement statement = conn.createStatement();
        
        ResultSet rs = statement.executeQuery(query);
        
        List<Servicio> servicios = new ArrayList<>();
        
        while(rs.next()) servicios.add(fill(rs));
        
        rs.close();
        statement.close();
        conn.close();
        
        return servicios;
    }

    public Long insert(Servicio s) throws Exception {
        //Preparamos las consultas
        String sqlServicio = "INSERT INTO servicio (fecha, idReservacion, idEmpleado) "
                + "VALUES(?, ?, ?)";
        String sqlServicioTratamiento = "INSERT INTO servicio_tratamiento(idTratamiento, idServicio) "
                + "VALUES(?, ?)";
        String sqlServicioTratamientoProducto = "INSERT INTO servicio_tratamiento_producto "
                + "(idServicioTratamiento, idProducto, precioUso) "
                + "VALUES(?, ?, ?)";
        String sqlReservacion = "UPDATE reservacion SET estatus = 2 WHERE idReservacion = ?";

        //Nos conectamos a la BD
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        //Preparamos los statements que vamos a ocupar
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        PreparedStatement pstmt4 = null;
        //Preparamos el rs
        ResultSet rs = null;

        //Preparamos objetos para colocar las listas que se incluyen en el servicio y un contador
        int cont = 0;
        List<ServicioTratamiento> serviciosTratamientos = null;
        List<Producto> productos = null;

        //Deshabilitamos la persistencia automatica de los datos:
        //para comenzar la transaccion
        conn.setAutoCommit(false);
        try {
            //Generamos un PreparedStatement que ejecutara la consulta************************************
            //y le indicamos que nos devuelva los ID's que se generen (1).
            pstmt1 = conn.prepareStatement(sqlServicio, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt1.setString(1, s.getFecha().toString());
            pstmt1.setLong(2, s.getReservacion().getId());
            pstmt1.setInt(3, s.getEmpleado().getId());

            //Ejecutamos la consulta:
            pstmt1.executeUpdate();
            //Recuperamos el ID que se genero del servicio:
            rs = pstmt1.getGeneratedKeys();
            rs.next();
            s.setId(rs.getLong(1));
            //Cerramos el ResultSet:
            rs.close();

            //Preparamos la segunda consulta**************************************************************
            pstmt2 = conn.prepareStatement(sqlServicioTratamiento, PreparedStatement.RETURN_GENERATED_KEYS);
            //Preparamos un lote de ejecuciones SQL:
            for (int i = 0; i < s.getServicioTratamientos().size(); i++) {
                //Llenamos los datos del PreparedStatement:
                pstmt2.setLong(1, s.getServicioTratamientos().get(i).getTratamiento().getId());
                pstmt2.setLong(2, s.getId());
                //Agregamos la consulta al lote:
                pstmt2.addBatch();
            }
            //Ejecutamos todo el lote de instrucciones:
            pstmt2.executeBatch();
            //Recuperamos los ID's generados:
            rs = pstmt2.getGeneratedKeys();

            //Vamos asignando cada uno de los ID's de servicioTratamiento generados
            while (rs.next()) {
                s.getServicioTratamientos().get(cont++).setId(rs.getLong(1));
            }

            //Cerramos el ResultSet:
            rs.close();

            //Preparamos la tercer consulta**************************************************************
            pstmt3 = conn.prepareStatement(sqlServicioTratamientoProducto);
            serviciosTratamientos = s.getServicioTratamientos();

            //Recorremos la lista que contiene los ServicioTratamiento:
            for (int i = 0; i < s.getServicioTratamientos().size(); i++) {
                //Recuperamos el ServicioTratamiento de la posicion i
                //y le pedimos sus productos:
                productos = serviciosTratamientos.get(i).getProductos();

                //Recorremos la lista de productos:
                for (int j = 0; j < productos.size(); j++) {
                    //Llenamos los parametros del PreparedStatement:
                    pstmt3.setLong(1, serviciosTratamientos.get(i).getId());
                    pstmt3.setInt(2, productos.get(j).getId());
                    pstmt3.setFloat(3, productos.get(j).getPrecioUso());

                    //Agregamos la consulta al lote:
                    pstmt3.addBatch();
                }
            }
            //Ejecutamos el lote de instrucciones:
            pstmt3.executeBatch();

            //Preparamos la cuarta consulta**************************************************************
            pstmt4 = conn.prepareStatement(sqlReservacion);
            pstmt4.setLong(1, s.getReservacion().getId());
            pstmt4.executeUpdate();

            //Persistimos los cambios realizados en la BD:
            conn.commit();
        } catch (Exception e) {
            //Si algo falla:
            //Imprimimos la excepcion:
            e.printStackTrace();

            //Hacemos un RollBack:
            conn.rollback();

            //Cerramos la conexion:
            connMySQL.close();

            //Lanzamos la excepcion:
            throw e;
        }
        conn.setAutoCommit(true);

        pstmt1.close();
        pstmt2.close();
        pstmt3.close();
        pstmt4.close();
        conn.close();
        connMySQL.close();

        return s.getId();
    }
    
    public static Servicio fill(ResultSet rs) throws Exception{
        Servicio servicio = new Servicio();
        servicio.setFecha(rs.getDate("fecha").toLocalDate());
        servicio.setId(rs.getLong("idServicio"));
        servicio.setTotal(rs.getDouble("totalProductos") + rs.getDouble("totalTratamientos"));
        servicio.setEmpleado(EmpleadoController.fillOnService(rs));
        servicio.setReservacion(ReservacionController.fillOnService(rs));
        return servicio;
    }
}
