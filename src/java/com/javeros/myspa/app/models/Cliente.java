
package com.javeros.myspa.app.models;

public class Cliente {
    private int idCliente;
    private String numeroUnico;
    private String correo;
    private int estatus;
    private Persona persona;
    private Usuario usuario;

    public Cliente(int idCliente, String numeroUnico, String correo, int estatus, Persona persona, Usuario usuario) {
        this.idCliente = idCliente;
        this.numeroUnico = numeroUnico;
        this.correo = correo;
        this.estatus = estatus;
        this.persona = persona;
        this.usuario = usuario;
    }

    public Cliente(){
        
    }
    
    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNumeroUnico() {
        return numeroUnico;
    }

    public void setNumeroUnico(String numeroUnico) {
        this.numeroUnico = numeroUnico;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
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

    @Override
    public String toString() {
        return "Cliente{" + "idCliente=" + idCliente + ", numeroUnico=" + numeroUnico + ", correo=" + correo + ", estatus=" + estatus + ", persona=" + persona + ", usuario=" + usuario + '}';
    }

}
