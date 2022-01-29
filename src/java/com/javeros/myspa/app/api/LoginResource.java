
package com.javeros.myspa.app.api;

import com.google.gson.Gson;
import com.javeros.myspa.app.controllers.AuthController;
import com.javeros.myspa.app.controllers.LoginController;
import com.javeros.myspa.app.models.Empleado;
import com.javeros.myspa.app.models.Usuario;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static com.javeros.myspa.app.utils.ErrorMessages.*;

@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource{
    
    private final LoginController loginController;
    private final AuthController authController;
    private final Gson gson;
    
    public LoginResource(){
        this.loginController = new LoginController();
        this.authController = new AuthController();
        this.gson = new Gson();
    }
    
    @Path("login")    
    @POST
    public Response login(@FormParam("username") String username, @FormParam("password") String password){
        try {
            Empleado empleado = loginController.login(username, password);
            if(empleado != null){
                return Response.ok().entity(gson.toJson(empleado)).build();
            } else {
                return Response.status(401).entity(NOT_AUTHORIZED).build();
            }
        } catch (Exception error) {
            error.printStackTrace();
            return Response.serverError().entity(SERVER_ERROR).build();
        }
    }
    
    @Path("logout")
    @POST
    public Response logout(@FormParam("user") String userJson){
        
        try {
            Usuario user = gson.fromJson(userJson, Usuario.class);
            authController.deleteToken(user);
            return Response.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(SERVER_ERROR).build();
        }
    }
    
    @POST
    public Response validateUser(@FormParam("token") String token){
        if(token == null || token.equals("")) return Response.status(401).entity(TOKEN_NOT_PROVIDED).build();
        try {
            if(authController.validateToken(token)){
                return Response.ok().build();
            } else {
                return Response.status(401).entity(NOT_AUTHORIZED).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(SERVER_ERROR).build();
        }
    }
}
