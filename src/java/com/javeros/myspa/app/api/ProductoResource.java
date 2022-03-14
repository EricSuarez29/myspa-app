/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javeros.myspa.app.api;

import com.google.gson.Gson;
import com.javeros.myspa.app.controllers.ProductoController;
import com.javeros.myspa.app.models.Producto;
import java.util.List;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Alejandro
 */
@Path("producto")
public class ProductoResource {
    @Path("save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("idProducto") @DefaultValue("0") int id,
                         @FormParam("nombre") @DefaultValue("") String nombre, 
                         @FormParam("marca") @DefaultValue("") String marca,
                         @FormParam("precioUso") @DefaultValue("0") float precioUso) // Inserta o Actualiza un registro de producto
    {
        String out = null;
        ProductoController cp = new ProductoController(); //Creamos un objeto de tipo ControllerProducto
    
        Producto producto = new Producto(); //Creamos un objeto de tipo Producto
        try {
            //Llenamos las propiedades del objeto Producto:
            producto.setMarca(marca);
            producto.setId(id);
            producto.setPrecioUso(precioUso);
            producto.setNombre(nombre);
            
            //Evaluamos su hacemos un INSERT o un UPDATE con base en el ID del Producto:
            if(producto.getId() == 0)
                cp.insert(producto);
            else
                cp.update(producto);
            
            //Devolvemos como respuesta TODOS los datos de la Producto:
            out = new Gson().toJson(producto);
        }
        catch (Exception e) {
            //Imprimimos el Error en la Consola del Servidor:
            e.printStackTrace();
            
            //Devolvemos una descripción del Error:
            out = "{\"error\":\"Algo salió mal. Intenta nuevamente o llama al administrador de sistemas.\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @Path("getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("filter") @DefaultValue("") String filtro) {
        ProductoController cp = new ProductoController();
        List<Producto> productos = null;
        String out = null;
        try {
            productos = cp.getAll(filtro);
            out = new Gson().toJson(productos);
        }
        catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"" + e.toString() + "\"}";
        }
        
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @Path("delete")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("id") @DefaultValue("0") int idProducto) {
        String out = null;
        ProductoController cp = new ProductoController();
        try {
            cp.delete(idProducto);
            out = "{\"result\":\"Registro eliminado.\"}";
        }
        catch(Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Algo salió mal. Intenta nuevamente o llama al administrador de sistemas.\"}";
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }
    
    @Path("getAll2")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll2() {
        ProductoController cp = new ProductoController();
        List<Producto> productos = null;
        String out = null;
        Gson gson = new Gson();
        try {
            productos = cp.getAll("");
            out = gson.toJson(productos);
        }
        catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Algo salió mal. Intenta nuevamente o llama al administrador de sistemas.\"}";
        }
        
        return Response.status(Response.Status.OK).entity(out).build();
    }
}
