package com.rodrigo.almacen.utils;

import com.rodrigo.almacen.entities.Producto;
import com.rodrigo.almacen.entities.Sucursal;
import com.rodrigo.almacen.enums.Categoria;
import com.rodrigo.almacen.repositories.ProductoRepository;
import com.rodrigo.almacen.repositories.SucursalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DatosIniciales implements CommandLineRunner {

    private final ProductoRepository productoRepository;

    private final SucursalRepository sucursalRepository;

    @Override
    public void run(String... args) throws Exception{
        if (productoRepository.count() == 0){
            productoRepository.saveAll(List.of(

                    new Producto(
                            null,
                            "Laptop gamer",
                            Categoria.ELECTRONICA,
                            BigDecimal.valueOf(15000),
                            10),

                    new Producto(
                            null,
                            "Mouse inalambrico",
                            Categoria.ELECTRONICA,
                            BigDecimal.valueOf(25),
                            50),

                    new Producto(
                            null,
                            "Camiseta deportiva",
                            Categoria.ELECTRONICA,
                            BigDecimal.valueOf(20),
                            100)
            ));
            log.info("Productos de prueba cargados correctamente");
        }
        if (sucursalRepository.count() == 0){
            sucursalRepository.saveAll(List.of(
                    new Sucursal(
                            null,
                            "Sucursal central",
                            "Av. principal 123"
                    ),
                    new Sucursal(
                            null,
                            "Sucursal norte",
                            "calle norte 456"
                    ),
                    new Sucursal(
                            null,
                            "Sucursal sur",
                            "Calle sur 789"
                    )
            ));
        }
    }
}
