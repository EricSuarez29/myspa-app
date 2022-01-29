
package com.javeros.myspa.app.models;

public class Sala {
    
    private Integer id;
    private String nombre;
    private String descripcion;
    private String foto;
    private Integer estatus;
    private Integer idSucursal;

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(Integer idSucursal) {
        this.idSucursal = idSucursal;
    }

    @Override
    public String toString() {
        return "Sala{" + "id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", foto=" + foto + ", estatus=" + estatus + ", idSucursal=" + idSucursal + '}';
    }
    
    
}
