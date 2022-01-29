
package com.javeros.myspa.app.utils;

public class SuccessMesages {
    private static String message(String message){
        return "{\"message\":\"" + message + "\"}";
    }
    
    public static final String CREATED = message("El elemnto fue creado correctamente");
    public static final String MODIFIED = message("El elemnto fue modificado correctamente");
    public static final String DELETED = message("El elemnto fue eliminado correctamente");
}
