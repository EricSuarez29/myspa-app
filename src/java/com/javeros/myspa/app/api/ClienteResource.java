
package com.javeros.myspa.app.api;

import com.google.gson.Gson;
import com.javeros.myspa.app.controllers.ClienteController;
import com.javeros.myspa.app.models.Cliente;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("cliente")
@Produces(MediaType.APPLICATION_JSON)
public class ClienteResource {
    @GET
    public Response readAll(){
        ClienteController c = new ClienteController();
        String out = null;
        try {
            out = new Gson().toJson(c.readAll());
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\" Ha ocurrido un error Intente nuevamente\"}";
        }
        return Response.ok(out).build();
    }
    
    @POST
    public Response createAndUpdate(String json){
        ClienteController c = new ClienteController();
        String out = null;
        try {
            Gson gson = new Gson();
            Cliente cliente = gson.fromJson(json, Cliente.class);
            if(cliente.getIdCliente() == 0){
                c.create(cliente);
                out = "{\"message\":\" El cliente se creo correctamente\"}";
            } else {
                c.update(cliente);
                out = "{\"message\":\" El cliente fue actualizado correctamente\"}";
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\" Ha ocurrido un error Intente nuevamente\"}";
            return Response.status(400).entity(out).build();
        }
        return Response.ok(out).build();
    }
    
    @DELETE
    @Path("{id}")
    public Response deleteById(@PathParam("id") int id){
        ClienteController c = new ClienteController();
        String out = null;
        try{
            c.delete(id);
            out = "{\"message\":\" El cliente fue eliminado correctamente\"}";

        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\" Ha ocurrido un error Intente nuevamente\"}";
            return Response.status(400).entity(out).build();
        }
        return Response.ok(out).build();
    }
}
