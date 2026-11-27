package com.rodrigo.almacen.services.ventas;

import com.rodrigo.almacen.dto.ventas.DetalleVentaRequest;
import com.rodrigo.almacen.dto.ventas.VentaRequest;
import com.rodrigo.almacen.dto.ventas.VentaResponse;
import com.rodrigo.almacen.entities.DetalleVenta;
import com.rodrigo.almacen.entities.Producto;
import com.rodrigo.almacen.entities.Sucursal;
import com.rodrigo.almacen.entities.Venta;
import com.rodrigo.almacen.enums.EstadoVenta;
import com.rodrigo.almacen.exceptions.DatoInvalidException;
import com.rodrigo.almacen.exceptions.RecursoNoEncontradoException;
import com.rodrigo.almacen.mappers.VentaMapper;
import com.rodrigo.almacen.repositories.ProductoRepository;
import com.rodrigo.almacen.repositories.SucursalRepository;
import com.rodrigo.almacen.repositories.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VentasServiceImpl implements VentasService{

    private final VentaRepository ventaRepository;

    private final ProductoRepository productoRepository;

    private final SucursalRepository sucursalRepository;

    private final VentaMapper ventaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listar() {
        log.info("Listando las ventas activas... ");
        List<VentaResponse> ventas = ventaRepository
                .findAllConDetallesPorEstado(EstadoVenta.REGISTRADA).stream()
                .map(ventaMapper::ventaResponse).toList();

        log.info("Ventas activas listadas: {}", ventas.size());
        return ventas;
    }

    @Override
    @Transactional(readOnly = true)
    public VentaResponse obtenerPorIdActiva(Long id) {
        log.info("Obteniendo una venta activa por el id {}", id);
        Venta venta = ventaRepository.findConDetallesById(id)
                .filter(v -> v.getEstadoVenta() == EstadoVenta.REGISTRADA)
                .orElseThrow(() ->  new RecursoNoEncontradoException("No existe venta activa con el id: " + id));

        return ventaMapper.ventaResponse(venta);

    }

    @Override
    public VentaResponse registrar(VentaRequest request) {
        log.info("Registrando nueva venta... ");

        Sucursal sucursal = sucursalRepository.findById(request.idSucursal())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Sucursal no encontrada con id: " + request.idSucursal()));

        long distintos = request.productos().stream()
                .map(DetalleVentaRequest::idProducto).distinct().count();

        if (distintos != request.productos().size()){
            throw new DatoInvalidException("Hay productos duplicados en la venta");
        }

        Venta venta = Venta.builder()
                .estadoVenta(EstadoVenta.REGISTRADA)
                .fecha(LocalDate.now())
                .sucursal(sucursal)
                .build();

        for (DetalleVentaRequest d : request.productos()) {

            Producto producto = productoRepository.findById(d.idProducto())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Producto no encontrado con id: " + d.idProducto()));

            producto.descontarCantidad(d.cantidadProducto());

            DetalleVenta detalle = DetalleVenta.builder()
                    .productos(producto)
                    .cantidadProducto(d.cantidadProducto())
                    .precioProducto(producto.getPrecio())
                    .build();

            detalle.asignarVenta(venta);
            venta.agregarDetalle(detalle);

        }

        ventaRepository.saveAndFlush(venta);

        log.info("Venta registrada con id: {}", venta.getId());

        return ventaMapper.ventaResponse(venta);
    }

    @Override
    public void cancelar(Long id) {
        log.info("Cancelando venta... ");

        Venta venta = ventaRepository.findConDetallesById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con el id: " +id));

        venta.cancelar();

        for (DetalleVenta detalle : venta.getDetalleVentas()){
            detalle.getProductos().aumentarCantidad(detalle.getCantidadProducto());
        }

        ventaRepository.flush();

        log.info("Venta {} cancelada, stock devuelto de {} productos", id , venta.getDetalleVentas().size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listarCanceladas() {
        log.info("Listado de las ventas canceladas... ");
        List<VentaResponse> ventasCanceladas = ventaRepository
                .findAllConDetallesPorEstado(EstadoVenta.CANCELADA).stream()
                .map(ventaMapper::ventaResponse).toList();

        log.info("Ventas canceladas listadas: {}", ventasCanceladas.size());
        return ventasCanceladas;
    }
}
