package com.javeros.myspa.app.controllers;

import com.javeros.myspa.app.db.ConexionMySQL;
import com.javeros.myspa.app.models.Cliente;
import com.javeros.myspa.app.models.Horario;
import com.javeros.myspa.app.models.Persona;
import com.javeros.myspa.app.models.Reservacion;
import com.javeros.myspa.app.models.Sala;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReservacionController {

    private Connection connection;
    private Statement statement;
    private ResultSet rs;

    public ReservacionController() {
    }
    
    public List<Reservacion> find(Integer estatus) throws Exception{
        String query = "SELECT * FROM v_reservacion WHERE estatus = " + estatus;
        List<Reservacion> reservaciones = new ArrayList();
        
        this.connection = new ConexionMySQL().open();
        
        statement = connection.createStatement();
        rs = statement.executeQuery(query);
        
        while(rs.next()) reservaciones.add(fill(rs));
        
        rs.close();
        statement.close();
        connection.close();
        
        return reservaciones;
    }
    
    public Reservacion findOne(int id) throws Exception{
        String query = "SELECT * FROM v_reservacion WHERE idReservacion=" + id;
        Reservacion reservacion = null;
        
        this.connection = new ConexionMySQL().open();
        statement = connection.createStatement();
        rs = statement.executeQuery(query);

        if(rs.next()) reservacion = fill(rs);
        
        rs.close();
        statement.close();
        connection.close();
        return reservacion;
    }

    public List<Horario> getHours(String ISODate, int idSala) throws Exception {
        String query = "SELECT H1.* FROM horario H1 "
                + " LEFT JOIN "
                + "(SELECT H.* "
                + " FROM horario H "
                + " INNER JOIN reservacion R "
                + " ON H.idHorario = R.idHorario "
                + " WHERE R.idSala=" + idSala + " AND R.fecha=STR_TO_DATE('" + ISODate + "','%Y-%m-%d') ) AS SQ2 "
                + "ON H1.idHorario=SQ2.idHorario "
                + "WHERE SQ2.idHorario IS NULL;";
        
        this.connection = new ConexionMySQL().open();

        statement = connection.createStatement();

        rs = statement.executeQuery(query);

        List<Horario> horarios = new ArrayList<>();

        while (rs.next()) {
            horarios.add(
                    new Horario(
                            rs.getInt("idHorario"),
                            rs.getString("horaInicio"),
                            rs.getString("horaFin")
                    )
            );
        }

        rs.close();
        statement.close();
        connection.close();
        return horarios;
    }

    public void insert(Reservacion r) throws Exception {
        String query = "INSERT INTO reservacion (estatus, idCliente, idSala, fecha, idHorario) "
                + "VALUES (" + 1 + ", " + r.getCliente().getIdCliente()+ ", " + r.getSala().getId()
                + ", STR_TO_DATE('" + r.getDate() + "','%Y-%m-%d'), " + r.getHorario().getId() + ")";
        
        this.connection = new ConexionMySQL().open();

        statement = connection.createStatement();

        statement.execute(query);

        statement.close();
        connection.close();
    }
    
    public Reservacion fill(ResultSet rs) throws Exception{
        Reservacion reservacion = new Reservacion();
        reservacion.setId((long) rs.getInt("idReservacion"));
        reservacion.setEstatus(rs.getInt("estatus"));
        reservacion.setDate(rs.getDate("fecha").toLocalDate());
        reservacion.setCliente(ClienteController.fillWithoutUser(rs));
        reservacion.setSala(SalaController.fillWithReservation(rs));
        reservacion.setHorario(HorarioController.fill(rs));
        return reservacion;
    }
    
    public void cancelById(int id) throws Exception{
        String query = "UPDATE reservacion SET estatus = 3 WHERE idReservacion = " + id;
        
        this.connection = new ConexionMySQL().open();
        
        statement = connection.createStatement();
        
        statement.execute(query);
        statement.close();
        connection.close();
    }
    
    public static Reservacion fillOnService(ResultSet rs) throws Exception {
        Reservacion reservacion = new Reservacion();
        reservacion.setId(rs.getLong("idReservacion"));
        reservacion.setDate(rs.getDate("fechaReservacion").toLocalDate());
        
        Horario horario = new Horario();
        horario.setId(rs.getInt("idHorario"));
        horario.setHoraInicio(rs.getString("horaInicio"));
        horario.setHoraFin(rs.getString("horaFin"));
        
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("idCliente"));
        Persona persona = new Persona();
        persona.setNombre(rs.getString("nombreCliente"));
        persona.setApellidoPaterno(rs.getString("apellidoPaternoCliente"));
        persona.setApellidoMaterno(rs.getString("apellidoMaternoCliente"));
        cliente.setPersona(persona);
        
        Sala sala = new Sala();
        sala.setId(rs.getInt("idSala"));
        sala.setNombre(rs.getString("nombreSala"));
        
        reservacion.setSala(sala);
        reservacion.setHorario(horario);
        reservacion.setCliente(cliente);
        return reservacion;
    }
}
