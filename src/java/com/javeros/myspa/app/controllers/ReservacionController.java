package com.javeros.myspa.app.controllers;

import com.javeros.myspa.app.db.ConexionMySQL;
import com.javeros.myspa.app.models.Horario;
import com.javeros.myspa.app.models.Reservacion;
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
        try {
            this.connection = new ConexionMySQL().open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<Reservacion> find() throws Exception{
        String query = "SELECT * FROM v_reservacion";
        List<Reservacion> reservaciones = new ArrayList();
        
        statement = connection.createStatement();
        rs = statement.executeQuery(query);
        
        while(rs.next()) reservaciones.add(fill(rs));
        
        return reservaciones;
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

        statement = connection.createStatement();

        statement.execute(query);

        statement.close();
        connection.close();
    }
    
    public Reservacion fill(ResultSet rs) throws Exception{
        Reservacion reservacion = new Reservacion();
        reservacion.setId(rs.getLong("idReservacion"));
        reservacion.setEstatus(rs.getInt("estatus"));
        reservacion.setDate(rs.getDate("fecha").toLocalDate());
        reservacion.setCliente(ClienteController.fillWithoutUser(rs));
        reservacion.setSala(SalaController.fill(rs));
        reservacion.setHorario(HorarioController.fill(rs));
        return reservacion;
    }
}
