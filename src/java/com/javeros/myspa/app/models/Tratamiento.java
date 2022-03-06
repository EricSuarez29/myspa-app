
package com.javeros.myspa.app.models;

public class Tratamiento {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double costo;
    private Integer estatus;

    public Tratamiento() {
    }

    public Tratamiento(Long id, String nombre, String descripcion, Double costo, Integer estatus) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costo = costo;
        this.estatus = estatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public Integer getEstatus() {
        return estatus;
    }

    public void setEstatus(Integer estatus) {
        this.estatus = estatus;
    }
    
}
