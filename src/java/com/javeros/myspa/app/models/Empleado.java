
package com.javeros.myspa.app.models;

public class Empleado {
    private int id, estatus;
    private String numeroEmpleado, 
            puesto,
            foto,
            rutaFoto;
    private Persona persona;
    private Usuario usuario;

    public Empleado(int id, int estatus, String numeroEmpleado, String puesto, String foto, String rutaFoto, Persona persona, Usuario usuario) {
        this.id = id;
        this.estatus = estatus;
        this.numeroEmpleado = numeroEmpleado;
        this.puesto = puesto;
        this.foto = foto;
        this.rutaFoto = rutaFoto;
        this.persona = persona;
        this.usuario = usuario;
    }
    
    public Empleado(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public String getNumeroEmpleado() {
        return numeroEmpleado;
    }

    public void setNumeroEmpleado(String numeroEmpleado) {
        this.numeroEmpleado = numeroEmpleado;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getRutaFoto() {
        return rutaFoto;
    }

    public void setRutaFoto(String rutaFoto) {
        this.rutaFoto = rutaFoto;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
}
