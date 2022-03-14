
package com.javeros.myspa.app.api;

import com.google.gson.Gson;
import com.javeros.myspa.app.controllers.TratamientoController;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("tratamientos")
@Produces(MediaType.APPLICATION_JSON)
public class TratamientoResource {
    private final TratamientoController tratamientoController;
    private final Gson gson;
    public TratamientoResource(){
        this.tratamientoController = new TratamientoController();
        this.gson = new Gson();
    }
    @GET
    public Response find(@QueryParam("filter") String filter){
        try{
            return Response.ok().entity(
                    gson.toJson(tratamientoController.find(filter))
            ).build();
        }catch(Exception e){
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
}
