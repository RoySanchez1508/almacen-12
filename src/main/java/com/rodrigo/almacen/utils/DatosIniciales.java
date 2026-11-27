package com.rodrigo.almacen.utils;

import com.rodrigo.almacen.entities.DetalleVenta;
import com.rodrigo.almacen.entities.Producto;
import com.rodrigo.almacen.entities.Sucursal;
import com.rodrigo.almacen.entities.Venta;
import com.rodrigo.almacen.enums.Categoria;
import com.rodrigo.almacen.enums.EstadoVenta;
import com.rodrigo.almacen.repositories.ProductoRepository;
import com.rodrigo.almacen.repositories.SucursalRepository;
import com.rodrigo.almacen.repositories.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DatosIniciales implements CommandLineRunner {

    private final ProductoRepository productoRepository;

    private final SucursalRepository sucursalRepository;

    private final VentaRepository ventaRepository;

    @Override
    public void run(String... args) throws Exception{
        if (productoRepository.count() == 0){
            productoRepository.saveAll(List.of(

                    Producto.crear(
                            "Laptop gamer",
                            Categoria.ELECTRONICA,
                            BigDecimal.valueOf(15000),
                            9),

                    Producto.crear(
                            "Mouse inalambrico",
                            Categoria.ELECTRONICA,
                            BigDecimal.valueOf(25),
                            45),

                    Producto.crear(
                            "Camiseta deportiva",
                            Categoria.ROPA,
                            BigDecimal.valueOf(20),
                            90)
            ));
            log.info("Productos de prueba cargados correctamente");
        }
        if (sucursalRepository.count() == 0){
            sucursalRepository.saveAll(List.of(
                    Sucursal.crear(
                            "Sucursal central",
                            "Av. principal 123"
                    ),
                    Sucursal.crear(
                            "Sucursal norte",
                            "Calle norte 456"
                    ),
                    Sucursal.crear(
                            "Sucursal sur",
                            "Calle sur 789"
                    )
            ));
            log.info("Sucursales de prueba cargadas correctamente");
        }

        List<Producto> productos = productoRepository.findAll();
        List<Sucursal> sucursales = sucursalRepository.findAll();

        Producto laptop = buscarPorNombre(productos, "Laptop gamer");
        Producto mouse = buscarPorNombre(productos, "Mouse inalambrico");
        Producto camisa = buscarPorNombre(productos, "Camiseta deportiva");

        Sucursal central = buscarSucursal(sucursales, "Sucursal central");
        Sucursal norte = buscarSucursal(sucursales, "Sucursal norte");

        if (ventaRepository.count() == 0){
            Venta v1 = crearVenta(central, EstadoVenta.REGISTRADA, LocalDate.of(2026, 2, 12),
                    List.<Object[]>of(new Object[]{laptop, 1}, new Object[]{mouse, 2}));

            Venta v2 = crearVenta(norte, EstadoVenta.REGISTRADA, LocalDate.of(2026, 3, 5),
                    List.<Object[]>of(new Object[]{mouse, 3}, new Object[]{camisa, 10}));

            Venta v3 = crearVenta(central, EstadoVenta.CANCELADA, LocalDate.of(2026, 4, 1),
                    List.<Object[]>of(new Object[]{laptop, 2}));

            ventaRepository.saveAll(List.of(v1, v2, v3));
            log.info("Ventas de prueba cargadas correctamente");
        }
    }

    private Producto buscarPorNombre(List<Producto> productos, String nombre) {
        return productos.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Falta producto seed: " + nombre));
    }

    private Sucursal buscarSucursal(List<Sucursal> sucursales, String nombre) {
        return sucursales.stream()
                .filter(s -> s.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Falta sucursal seed: " + nombre));
    }

    private Venta crearVenta(Sucursal sucursal, EstadoVenta estado,
                             LocalDate fecha, List<Object[]> items) {

        Venta venta = Venta.builder()
                .sucursal(sucursal)
                .estadoVenta(estado)
                .fecha(fecha)
                .build();

        for (Object[] item : items) {
            Producto prod = (Producto) item[0];
            Integer cantidad = (Integer) item[1];

            DetalleVenta detalle = DetalleVenta.builder()
                    .venta(venta)
                    .productos(prod)
                    .cantidadProducto(cantidad)
                    .precioProducto(prod.getPrecio())
                    .build();

            venta.agregarDetalle(detalle);
        }
        return venta;
    }
}
