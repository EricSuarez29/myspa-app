
package com.javeros.myspa.app.models;

import java.time.LocalDate;
import java.util.List;

public class Servicio {
    private Long id;
    private LocalDate fecha;
    private Empleado empleado;
    private Reservacion reservacion;
    private List<ServicioTratamiento> servicioTratamientos;
    private Double total;

    public Servicio() {
    }

    public Servicio(Long id, LocalDate fecha, Empleado empleado, Reservacion reservacion, List<ServicioTratamiento> servicioTratamientos, Double total) {
        this.id = id;
        this.fecha = fecha;
        this.empleado = empleado;
        this.reservacion = reservacion;
        this.servicioTratamientos = servicioTratamientos;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Reservacion getReservacion() {
        return reservacion;
    }

    public void setReservacion(Reservacion reservacion) {
        this.reservacion = reservacion;
    }

    public List<ServicioTratamiento> getServicioTratamientos() {
        return servicioTratamientos;
    }

    public void setServicioTratamientos(List<ServicioTratamiento> servicioTratamientos) {
        this.servicioTratamientos = servicioTratamientos;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Servicio{" + "id=" + id + ", fecha=" + fecha + ", empleado=" + empleado + ", reservacion=" + reservacion + ", servicioTratamientos=" + servicioTratamientos + ", total=" + total + '}';
    }
    
}
