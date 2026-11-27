package com.rodrigo.almacen.mappers;

import com.rodrigo.almacen.dto.productos.ProductoRequest;
import com.rodrigo.almacen.dto.productos.ProductoResponse;
import com.rodrigo.almacen.entities.Producto;
import com.rodrigo.almacen.enums.Categoria;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto requestAEntidad (ProductoRequest request, Categoria categoria){

        return request == null
                ? null
                : Producto.crear(
                        request.nombre(),
                        categoria,
                        request.precio(),
                        request.cantidad());
    }

    public ProductoResponse entidadResponse (Producto producto){
        return producto == null
                ? null
                : new ProductoResponse(
                        producto.getId(),
                        producto.getNombre(),
                        producto.getCategoria().getDescripcion(),
                        producto.getPrecio(),
                        producto.getCantidad());
    }
}
