package com.javeros.myspa.app.controllers;

import com.javeros.myspa.app.db.ConexionMySQL;
import com.javeros.myspa.app.models.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import org.apache.commons.codec.digest.DigestUtils;

public class AuthController {
    
    public String generateToken(Usuario user){
        String date = new Date().toString();
        return DigestUtils.sha256Hex(user.getNombreUsuario() + ";" + date);
    }

    public void saveToken(Usuario u) throws Exception {
        String sql = "UPDATE usuario SET token = '" + u.getToken() + "' WHERE idUsuario = " + u.getId() + ";";
        ConexionMySQL connMySQL = new ConexionMySQL();

        Connection conn = connMySQL.open();

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.execute();
        pstmt.close();
        conn.close();
        connMySQL.close();
    }

    public void deleteToken(Usuario u) throws Exception {
        String sql = "UPDATE usuario SET token = '' WHERE idUsuario = " + u.getId() + ";";
        ConexionMySQL connMySQL = new ConexionMySQL();

        Connection conn = connMySQL.open();

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.execute();
        pstmt.close();
        conn.close();
        connMySQL.close();
    }

    public boolean validateToken(String token) throws Exception {
        boolean valid = false;
        String sql = "SELECT * FROM v_empleados WHERE token= '" + token + "';";
        ConexionMySQL connMySQL = new ConexionMySQL();

        Connection conn = connMySQL.open();

        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            valid = true;
        }
        pstmt.close();
        conn.close();
        connMySQL.close();

        return valid;
    }

}
