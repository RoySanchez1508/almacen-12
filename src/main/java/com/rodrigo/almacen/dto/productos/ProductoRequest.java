package com.rodrigo.almacen.dto.productos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Datos para necesarios para actualizar o crear un nuevo producto")
public record ProductoRequest(

        @Schema(description = "Nombre del producto", example = "Laptop Game")
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 5, max = 30,message = "El nombre tiene que tener de 5 a 30 caracteres")
        String nombre,

        @Schema(description = "Categoria del producto", example = "Electromico"
        ,allowableValues = {
                "ALIMENTO",
                "HIGIENE",
                "JUGUETE",
                "ELECTRONICA",
                "ROPA",
                "ACCESORIO",
                "FARMACIA"
        })

        @NotBlank(message = "La categoria es requerida ")
        String categoria,

        @Schema(description = "Precio del producto", example = "15999.99")
        @NotNull(message = "El precio es requerido")
        @Positive(message = "El precio debe ser positivo")
        BigDecimal precio,

        @Schema(description = "Cantidad disponible del producto", example = "300")
        @NotNull(message = "El precio es requerido")
        @Positive(message = "El precio debe ser positivo")
        Integer cantidad
) {
}
