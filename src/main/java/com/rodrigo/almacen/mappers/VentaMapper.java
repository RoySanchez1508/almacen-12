package com.rodrigo.almacen.mappers;

import com.rodrigo.almacen.dto.ventas.VentaRequest;
import com.rodrigo.almacen.dto.ventas.VentaResponse;
import com.rodrigo.almacen.entities.Venta;
import com.rodrigo.almacen.enums.EstadoVenta;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Component
public class VentaMapper {

    private final DetalleVentaMapper detalleVentaMapper;

    private final SucursalMapper sucursalMapper;

    public VentaMapper(DetalleVentaMapper detalleVentaMapper, SucursalMapper sucursalMapper){
        this.detalleVentaMapper = detalleVentaMapper;
        this.sucursalMapper = sucursalMapper;
    }

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public VentaResponse ventaResponse(Venta venta){
        return venta == null
                ? null
                : new VentaResponse(
                        venta.getId(),
                        venta.getFecha().format(FORMATO_FECHA),
                        venta.getEstadoVenta().getDescripcion(),
                        sucursalMapper.entidadAResponse(venta.getSucursal()),
                        venta.getDetalleVentas().stream()
                                .map(detalleVentaMapper::entidadResponse)
                                .toList(),
                        calcularTotal(venta));
    }

    private BigDecimal calcularTotal (Venta venta){
        return venta.getDetalleVentas().stream()
                .map(d -> BigDecimal.valueOf(d.getCantidadProducto())
                        .multiply(d.getPrecioProducto()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
