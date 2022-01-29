
package com.javeros.myspa.app.utils;

public class ErrorMessages {
    private static String message(String message){
        return "{\"message\":\"" + message + "\"}";
    }
    
    public static final String TOKEN_NOT_PROVIDED = message("Token no proveido");
    public static final String NOT_AUTHORIZED = message("No autorizado");
    public static final String SERVER_ERROR = message("Ha ocurrido un error en el servidor");
    public static final String NOT_MODIFIED = message("No se ha modificado ningun elemento");
    
}
