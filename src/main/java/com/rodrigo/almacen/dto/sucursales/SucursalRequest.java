package com.rodrigo.almacen.dto.sucursales;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para crear o actualizar una sucursal")
public record SucursalRequest(

        @Schema(description = "Nombre de la sucursal", example = "Sucursal del Norte")
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 5, max = 50, message = "El nombre debe de tener entre 5 caracteres y 50")
        String nombre,

        @Schema(description = "Direccion de la sucursal", example = "Calle S #10")
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 10, max = 150, message = "El nombre debe de tener entre 10 caracteres y 150")
        String direccion
) {}
