
package com.javeros.myspa.app.api;

import com.google.gson.Gson;
import com.javeros.myspa.app.controllers.ReservacionController;
import com.javeros.myspa.app.models.Reservacion;
import java.time.LocalDate;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("reservacion")
@Produces(MediaType.APPLICATION_JSON)
public class ReservacionResource {
    
    private final ReservacionController reservacionController;
    private final Gson gson;
    
    public ReservacionResource(){
        this.reservacionController = new ReservacionController();
        this.gson = new Gson();
    }
    
    @GET
    @Path("hours")
    public Response findHours(
            @QueryParam("date") String ISODate,
            @QueryParam("sala") Integer idSala, 
            @QueryParam("token") String token){
        try {
            return Response.ok()
                    .entity(
                        gson.toJson(reservacionController.getHours(ISODate, idSala))
                    ).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
    
    @GET
    public Response find(@QueryParam("estatus") @DefaultValue("1") Integer estatus){
        try {
            return Response.ok()
                    .entity(
                        gson.toJson(reservacionController.find(estatus))
                    ).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
    
    @GET
    @Path("{id}")
    public Response findOne(@PathParam("id") Integer id){
        try {
            return Response.ok()
                    .entity(
                        gson.toJson(reservacionController.findOne(id))
                    ).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
    
    @POST
    public Response create(@FormParam("reservation") String reservacionJson, @FormParam("date") String date){
        try {
            Reservacion reservacion = gson.fromJson(reservacionJson, Reservacion.class);
            String[] datesFormat = date.split("-");
            reservacion.setDate(LocalDate.of(Integer.parseInt(datesFormat[0]), Integer.parseInt(datesFormat[1]), Integer.parseInt(datesFormat[2])));
            reservacionController.insert(reservacion);
            return Response.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
    
    @DELETE
    @Path("{id}")
    public Response cancel(@PathParam("id") Integer id){
        try {
            reservacionController.cancelById(id);
            return Response.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
}
