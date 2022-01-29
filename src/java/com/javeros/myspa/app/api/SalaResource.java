package com.javeros.myspa.app.api;

import com.google.gson.Gson;
import com.javeros.myspa.app.controllers.AuthController;
import com.javeros.myspa.app.controllers.SalaController;
import com.javeros.myspa.app.models.Sala;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.javeros.myspa.app.utils.ErrorMessages.*;
import static com.javeros.myspa.app.utils.SuccessMesages.*;
import javax.ws.rs.DELETE;
import javax.ws.rs.QueryParam;

@Path("salas")
@Produces(MediaType.APPLICATION_JSON)
public class SalaResource {

    private final SalaController salaController;
    private final AuthController authController;
    private final Gson gson;
    private String out;

    public SalaResource() {
        this.salaController = new SalaController();
        this.authController = new AuthController();
        this.gson = new Gson();
        this.out = "";
    }

    @POST
    public Response save(@FormParam("sala") String salaString, @FormParam("token") String token) {
        if(token == null || token.equals("")) return Response.status(401).entity(TOKEN_NOT_PROVIDED).build();
        
        try {
            if(!authController.validateToken(token)) return Response.status(401).entity(NOT_AUTHORIZED).build();
            
            Sala sala = gson.fromJson(salaString, Sala.class);
            
            if (sala.getId() == 0) {
                salaController.insert(sala);
                return Response.ok().entity(CREATED).build();
            } else {
                salaController.update(sala);
                return Response.ok().entity(MODIFIED).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(SERVER_ERROR).build();
        }

    }

    @GET
    public Response getAll(@QueryParam("token") String token) {
        if(token == null || token.equals("")) return Response.status(401).entity(TOKEN_NOT_PROVIDED).build();
        try {
            if(!authController.validateToken(token)) return Response.status(401).entity(NOT_AUTHORIZED).build();
            
            out = gson.toJson(salaController.getAll(""));
            return Response.ok().entity(out).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(SERVER_ERROR).build();
        }
    }

    @Path("{id}")
    @DELETE
    public Response delete(@PathParam("id") Integer id, @FormParam("token") String token) {
        if(token == null || token.equals("")) return Response.status(401).entity(TOKEN_NOT_PROVIDED).build();
        
        try {
            if(!authController.validateToken(token)) return Response.status(401).entity(NOT_AUTHORIZED).build();
            if (id == null || id == 0) {
                return Response.notModified().entity(NOT_MODIFIED).build();
            }
            salaController.delete(id);
            return Response.ok().entity(DELETED).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(SERVER_ERROR).build();
        }
    }
}
