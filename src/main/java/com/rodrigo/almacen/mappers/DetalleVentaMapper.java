package com.rodrigo.almacen.mappers;

import com.rodrigo.almacen.dto.ventas.DetalleVentaResponse;
import com.rodrigo.almacen.entities.DetalleVenta;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DetalleVentaMapper {

    public DetalleVentaResponse entidadResponse(DetalleVenta detalle){
        return detalle == null
                ? null
                : new DetalleVentaResponse(
                        detalle.getProductos().getId(),
                        detalle.getProductos().getNombre(),
                        detalle.getCantidadProducto(),
                        detalle.getPrecioProducto(),
                        BigDecimal.valueOf(detalle.getCantidadProducto())
                                .multiply(detalle.getPrecioProducto())
        );
    }
}
