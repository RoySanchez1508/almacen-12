package com.rodrigo.almacen.enums;

import com.rodrigo.almacen.exceptions.DatoInvalidException;
import com.rodrigo.almacen.utils.StringCustomUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.*;

//clase que almacena constantes
@RequiredArgsConstructor
@Getter
public enum Categoria {
    ALIMENTO("Alimento"),
    HIGIENE("Higiene"),
    JUGUETES("Juguetes"),
    ELECTRONICA("Electrónica"),
    ROPA("Ropa"),
    ACCESORIO("Accesorio"),
    FAMAICA("Farmacia");

    private final String descripcion;

    public static Categoria obtenerCategoriaPorDescripcion (String descripcion){

        StringCustomUtils.validarNoVacio(descripcion, "La descripción es requqerida");

        String descripcionNormalizada = StringCustomUtils.normalizarTexto(descripcion);

        for (Categoria categoria : values()){
            if (StringCustomUtils.normalizarTexto(categoria.descripcion).equalsIgnoreCase(descripcionNormalizada))
                return categoria;
        }

        throw  new DatoInvalidException("No exíste una categoria con la descripción: "+descripcion);
    }
}
