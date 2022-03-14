
package com.javeros.myspa.app.api;

import com.google.gson.Gson;
import com.javeros.myspa.app.controllers.ServicioController;
import com.javeros.myspa.app.models.Servicio;
import java.time.LocalDate;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("servicio")
@Produces(MediaType.APPLICATION_JSON)
public class ServicioResource {
    
    private final ServicioController controller;
    private final Gson gson;
    
    public ServicioResource(){
        this.controller = new ServicioController();
        this.gson = new Gson();
    }
    
    @GET
    public Response find(){
        try{
            return Response.ok().entity(
                    gson.toJson(controller.find())
            ).build();
        }catch(Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
    
    @POST
    public Response insert(@FormParam("servicio") String jsonServicio){
        try{
            Servicio servicio = gson.fromJson(jsonServicio, Servicio.class);
            servicio.setFecha(LocalDate.now());
            controller.insert(servicio);
            return Response.ok().build();
        }catch(Exception e){
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
}
