
package com.javeros.myspa.app.controllers;

import com.javeros.myspa.app.db.ConexionMySQL;
import com.javeros.myspa.app.models.Horario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HorarioController {
    public int insertHorario(Horario o) throws Exception{
        ConexionMySQL con = new ConexionMySQL();
        Connection c = con.open();
        String sql = "INSERT INTO horario(horaInicio, horaFin) VALUES(?,?)";
        PreparedStatement s = c.prepareStatement(sql);
        s.setString(1, o.getHoraInicio());
        s.setString(2, o.getHoraFin());
        int id = s.executeUpdate();
        con.close();
        s.close();
        return id;
    }
    
    public void actualizarHorario(Horario o) throws Exception{
        ConexionMySQL con = new ConexionMySQL();
        Connection c = con.open();
        String sql = "UPDATE horario SET horaInicio=?, horaFin=? WHERE idHorario=?";
        PreparedStatement s = c.prepareStatement(sql);
        s.setString(1, o.getHoraInicio());
        s.setString(2, o.getHoraFin());
        s.setInt(3, o.getIdHorario());
        s.executeUpdate();
        con.close();
        s.close();
    }
    
    public List<Horario> read() throws Exception{
        ConexionMySQL con = new ConexionMySQL();
        Connection c = con.open();
        List<Horario> horarios = new ArrayList<>();
        Statement s = c.createStatement();
        String sql = "SELECT * FROM horario";
        ResultSet rs = s.executeQuery(sql);
        while(rs.next()){
            Horario h = new Horario();
            h.setIdHorario(rs.getInt(1));
            h.setHoraInicio(rs.getString(2));
            h.setHoraFin(rs.getString(3));
            horarios.add(h);
        }
        con.close();
        s.close();
        rs.close();
        return horarios;
    }
    
    public void eliminarHorario(int id) throws Exception{
        ConexionMySQL con = new ConexionMySQL();
        Connection c = con.open();
        String sql = "DELETE FROM horario WHERE idHorario=?";
        PreparedStatement s = c.prepareStatement(sql);
      
        s.setInt(1, id);
        s.execute();
        con.close();
        s.close();
    }
    
}
