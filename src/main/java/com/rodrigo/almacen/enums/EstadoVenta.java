package com.rodrigo.almacen.enums;

import com.rodrigo.almacen.exceptions.DatoInvalidException;
import com.rodrigo.almacen.utils.StringCustomUtils;
import com.rodrigo.almacen.utils.ValoresNumericosUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EstadoVenta {
    REGISTRADA(1L, "Registrada"),
    CANCELADA(0L, "Cancelada");

    private final Long codigo;

    private final String descripcion;

    public static EstadoVenta obtenerEstadoVentaPorDescripcion (String descripcion){

        StringCustomUtils.validarNoVacio(descripcion, "La descripción es requqerida");

        String descripcionNormalizada = StringCustomUtils.normalizarTexto(descripcion);

        for (EstadoVenta estadoVenta : values()){

            if (StringCustomUtils.normalizarTexto(estadoVenta.descripcion).equalsIgnoreCase(descripcionNormalizada))
                return estadoVenta;
        }

        throw new DatoInvalidException("No exíste una categoria con la descripción: "+descripcion);
    }

    public static EstadoVenta obternerEstadoVentaPorCodigo(Long codigo){

        ValoresNumericosUtils.validarNumeroRequerido(codigo,"El codigo es requerido");

        for (EstadoVenta estadoVenta : values()){

            if (estadoVenta.codigo.equals(codigo))
                return estadoVenta;

        }

        throw new DatoInvalidException("No existe un estado de venta con el codigo: " + codigo);
    }
}
