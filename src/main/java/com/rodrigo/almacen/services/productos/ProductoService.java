package com.rodrigo.almacen.services.productos;

import com.rodrigo.almacen.dto.productos.ProductoRequest;
import com.rodrigo.almacen.dto.productos.ProductoResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoService {

    List<ProductoResponse>listar(
            String nombre, String categoria,
            BigDecimal precioMin, BigDecimal precioMax);

    ProductoResponse obtenerPorId(Long id);

    ProductoResponse registrar(ProductoRequest request);

    ProductoResponse actualizar(ProductoRequest request, long id);

    void eliminar(Long id);

}
