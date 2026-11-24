package com.rodrigo.almacen.utils;

import com.rodrigo.almacen.exceptions.DatoInvalidException;

import java.awt.*;
import java.util.Locale;

public class StringCustomUtils {

    public static void validarNoVacio(String texto, String mensaje){
        if (texto == null || texto.isBlank())
            throw new DatoInvalidException(mensaje);
    }

    public static void validarTamanio(String texto, Integer min, Integer max, String mensaje){

        validarNoVacio(texto, mensaje);

        if(texto.length() < min || texto.length() > max)
            throw new DatoInvalidException(mensaje);
    }

    public static String normalizarTexto(String texto){
        return texto.toLowerCase()
                .replace("á", "a").replace("é","e")
                .replace("í", "i").replace("ó","o")
                .replace("ú", "u").replace("ü","u")
                .replace("ñ", "n");
    }
}
