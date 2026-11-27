package com.rodrigo.almacen.dto.ventas;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Schema(description = "Detalle deproducto de la venta")
public record DetalleVentaRequest(
        @Schema(description = "ID del Producto", example = "1")
        @NotNull(message = "El ID del Producto es requerido")
        @Positive(message = "El ID del producto debe ser positivo")
        Long idProducto,

        @Schema(description = "Lista de productos en venta")
        @NotNull(message = "La cantidad del producto es requerida")
        Integer cantidadProducto
) {
}
