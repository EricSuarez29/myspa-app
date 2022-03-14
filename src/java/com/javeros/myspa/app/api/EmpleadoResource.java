
package com.javeros.myspa.app.api;

import com.google.gson.Gson;
import com.javeros.myspa.app.controllers.EmpleadoController;
import com.javeros.myspa.app.models.Empleado;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("empleados")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmpleadoResource {

    @GET
    public Response readAll(@QueryParam("filter") String filter){
        String out;
        try{
            EmpleadoController dao = new EmpleadoController();
            out = new Gson().toJson(dao.readAll(filter));
        }catch(Exception e){
            e.printStackTrace();
            out = "{\"error\":\" Ha ocurrido un error Intente nuevamente\"}";
        }
        return Response.ok(out).build();
    }
    
    @POST
    public Response create(String json){
        String out;
        try{
            EmpleadoController dao = new EmpleadoController();
            Gson gson = new Gson();
            Empleado empleado = gson.fromJson(json, Empleado.class);
            if(empleado.getId() == 0){
                out = gson.toJson(dao.create(empleado));    
            }else {
                dao.update(empleado);
                out = "{\"message\":\" Empleado Actualizado Correctamente\"}";
            }
        }catch(Exception e){
            e.printStackTrace();
            out = "{\"error\":\" Ha ocurrido un error Intente nuevamente\"}";
        }
        return Response.ok(out).build();
    }
    
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") @DefaultValue("0") int id){
        String out;
        try {
            if(id == 0) Response.notModified().build();
            EmpleadoController dao = new EmpleadoController();
            dao.delete(id);
            out = "{\"message\":\" Empleado Eliminado Correctamente\"}";
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\" Ha ocurrido un error Intente nuevamente\"}";
        }
        return Response.ok(out).build();
    }
    
}
