package com.rodrigo.almacen.dto.reportes;


import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Reporte de rendimiento economico por sucursal")
public record ReporteVentasSucursalResponse(

        @Schema(description = "Id de la sucursal", example = "1")
        Long id,

        @Schema(description = "Nombre de la sucursal", example = "Sucursal central")
        String nombre,

        @Schema(description = "Total facturado en ventas", example = "150050.00")
        BigDecimal totalVentas,

        @Schema(description = "Cantidad total de productos vendidos", example = "3")
        Long cantidadVentas
) {



}
