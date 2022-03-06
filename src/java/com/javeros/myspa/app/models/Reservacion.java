
package com.javeros.myspa.app.models;

import java.time.LocalDate;

public class Reservacion {
    
    private Long id;
    private Integer estatus;
    private Cliente cliente;
    private Sala sala;
    private LocalDate date;
    private Horario horario;

    public Reservacion(Cliente cliente, Sala sala, LocalDate date, Horario horario) {
        this.cliente = cliente;
        this.sala = sala;
        this.date = date;
        this.horario = horario;
    }
    
    public Reservacion(){}

    public Integer getEstatus() {
        return estatus;
    }

    public void setEstatus(Integer estatus) {
        this.estatus = estatus;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
}
