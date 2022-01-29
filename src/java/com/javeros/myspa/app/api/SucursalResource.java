
package com.javeros.myspa.app.api;

import com.google.gson.Gson;
import com.javeros.myspa.app.controllers.SucursalController;
import com.javeros.myspa.app.models.Sucursal;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("sucursal")
@Produces(MediaType.APPLICATION_JSON)
public class SucursalResource {
    
    @GET
    public Response readAll(
            @QueryParam("filtro") @DefaultValue("") String filtro){
        SucursalController cs = new SucursalController();
        List<Sucursal> sucursales;
        String out;
        try {
            sucursales = cs.readAll(filtro);
            out = new Gson().toJson(sucursales);
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"" + "Ago salió mal. Intenta nuevamente o llama al administrador de sistemas" + "\"}";
        }
        return Response.ok(out).build();
    }
    
    @POST
    public Response createAndUpdate(@BeanParam Sucursal sucursal){
        SucursalController cs = new SucursalController();
        String out = "";

        try {
            if(sucursal.getId() == 0){
                out = new Gson().toJson(cs.create(sucursal));    
            }else{
                cs.update(sucursal);
                out = "{\"message\":\"" + "Objeto Actualizado Correctamente" + "\"}";    
            }
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"" + "Ago salió mal. Intenta nuevamente o llama al administrador de sistemas" + "\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build(); 
    }
    
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") @DefaultValue("0") int id){
        if(id == 0) return Response.notModified().build();
        String out = "";
        SucursalController cs = new SucursalController();
        try{
            cs.delete(id);
            out = "{\"message\":\"" + "La sucursal fue Eliminada Correctamente" + "\"}";
        }catch(Exception e){
            e.printStackTrace();
            out = "{\"error\":\"" + "Ago salió mal. Intenta nuevamente o llama al administrador de sistemas" + "\"}";
        }
        return Response.ok(out).build();
    }
}
