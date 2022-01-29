
package com.javeros.myspa.app.models;

public class Usuario {
    private int id;
    private String nombreUsuario, 
            contrasegna,
            rol,
            token;

    public Usuario(int id, String nombreUsuario, String constrasegna, String rol, String token) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.contrasegna = constrasegna;
        this.rol = rol;
        this.token = token;
    }
    
    public Usuario(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasegna() {
        return contrasegna;
    }

    public void setContrasegna(String constrasegna) {
        this.contrasegna = constrasegna;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
}
