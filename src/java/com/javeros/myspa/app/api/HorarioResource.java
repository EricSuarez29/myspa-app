
package com.javeros.myspa.app.api;

import com.google.gson.Gson;
import com.javeros.myspa.app.controllers.HorarioController;
import com.javeros.myspa.app.models.Horario;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("horario")
@Produces(MediaType.APPLICATION_JSON)
public class HorarioResource {
    @GET
    @Path("getAll")
    public Response leer(){
        HorarioController c = new HorarioController();
        String out = null;
        try {
            out = new Gson().toJson(c.read());
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\" Ha ocurrido un error Intente nuevamente\"}";
        }
        return Response.status(200).entity(out).build();
    }
    
    @POST
    @Path("save")
    public Response guardar(@FormParam("horario") String horario){
        HorarioController c = new HorarioController();
        String out = null;
        
        try {
            Horario h = new Gson().fromJson(horario, Horario.class);
            if(h.getIdHorario() == 0){
                c.insertHorario(h);
            }else {
                c.actualizarHorario(h);
            }
            out = "{\"ok\":true}";
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\" Ha ocurrido un error Intente nuevamente\"}";
        }
        return Response.status(200).entity(out).build();
    }
    
    @POST
    @Path("delete")
    public Response eliminar(@FormParam("id") int id){
        HorarioController c = new HorarioController();
        String out = null;
        
        try {
            c.eliminarHorario(id);
            out = "{\"ok\":true}";
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\" Ha ocurrido un error Intente nuevamente\"}";
        }
        return Response.status(200).entity(out).build();
    }
}
