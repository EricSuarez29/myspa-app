
package com.javeros.myspa.app.models;

public class Horario {
    int IdHorario;
    String horaInicio, horaFin;

    public Horario(int IdHorario, String horaInicio, String horaFin) {
        this.IdHorario = IdHorario;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }
    
    public Horario(){
    }

    public int getIdHorario() {
        return IdHorario;
    }

    public void setIdHorario(int IdHorario) {
        this.IdHorario = IdHorario;
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
        return "Horario{" + "IdHorario=" + IdHorario + ", horaInicio=" + horaInicio + ", horaFin=" + horaFin + '}';
    }
    
    
}
