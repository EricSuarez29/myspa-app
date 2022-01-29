
package com.javeros.myspa.app.models;

import javax.ws.rs.FormParam;

public class Sucursal {
    
    @FormParam("id")
    private int id;
    
    @FormParam("estatus")
    private int estatus;
    
    @FormParam("nombre")
    private String nombre;
    
    @FormParam("domicilio")
    private String domicilio;
    
    @FormParam("longitud")
    private double longitud;
    
    @FormParam("latitud")
    private double latitud;
    
    public Sucursal(int id, int estatus, String nombre, String domicilio, double longitud, double latitud) {
        this.id = id;
        this.estatus = estatus;
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.longitud = longitud;
        this.latitud = latitud;
    }

    public Sucursal(){}
    
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    @Override
    public String toString() {
        return "Id : " + id + "nombre : " + nombre + " domicilio : " + domicilio + " latitud : "
                + latitud + " longitud : " + longitud;
    }
    
}
