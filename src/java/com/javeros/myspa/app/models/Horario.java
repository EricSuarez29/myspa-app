
package com.javeros.myspa.app.models;

public class Horario {
    private Integer id;
    private String horaInicio, horaFin;

    public Horario(int id, String horaInicio, String horaFin) {
        this.id = id;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }
    
    public Horario(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    @Override
    public String toString() {
        return "Horario{" + "IdHorario=" + id + ", horaInicio=" + horaInicio + ", horaFin=" + horaFin + '}';
    }
    
    
}
