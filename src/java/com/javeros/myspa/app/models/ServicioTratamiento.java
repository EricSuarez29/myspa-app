
package com.javeros.myspa.app.models;

import java.util.List;

public class ServicioTratamiento {
    private Long id;
    private Tratamiento tratamiento;
    private List<Producto> productos;

    public ServicioTratamiento() {
    }

    public ServicioTratamiento(Long id, Tratamiento tratamiento, List<Producto> productos) {
        this.id = id;
        this.tratamiento = tratamiento;
        this.productos = productos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tratamiento getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Tratamiento tratamiento) {
        this.tratamiento = tratamiento;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    @Override
    public String toString() {
        return "ServicioTratamiento{" + "id=" + id + ", tratamiento=" + tratamiento + ", productos=" + productos + '}';
    }
    
}
