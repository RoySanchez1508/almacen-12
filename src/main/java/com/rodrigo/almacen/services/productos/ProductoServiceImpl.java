package com.rodrigo.almacen.services.productos;

import com.rodrigo.almacen.dto.productos.ProductoRequest;
import com.rodrigo.almacen.dto.productos.ProductoResponse;
import com.rodrigo.almacen.entities.Producto;
import com.rodrigo.almacen.enums.Categoria;
import com.rodrigo.almacen.exceptions.DatoInvalidException;
import com.rodrigo.almacen.exceptions.RecursoNoEncontradoException;
import com.rodrigo.almacen.mappers.ProductoMapper;
import com.rodrigo.almacen.repositories.ProductoRepository;
import com.rodrigo.almacen.repositories.specs.ProductoEspecificaciones;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductoServiceImpl implements ProductoService{

    private final ProductoRepository productoRepository;

    private final ProductoMapper productoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar(
            String nombre, String categoria,
            BigDecimal precioMin, BigDecimal precioMax) {

        log.info("Listado de los productos... nombre: {}, categoria: {}, precioMin: {}, precioMax: {}",
                nombre, categoria, precioMin, precioMax);

        Categoria cat = null;
        if (categoria != null && !categoria.isBlank()){
            cat = Categoria.obtenerCategoriaPorDescripcion(categoria.trim());
        }
        if (precioMin != null && precioMin.signum()<0) {
            throw new DatoInvalidException("El precio no puede ser negativo");
        }
        if (precioMax != null && precioMax.signum()<0){
            throw new DatoInvalidException("El precio maximo no puede ser negativo");
        }
        if (precioMin != null && precioMax != null && precioMin.compareTo(precioMax) > 0) {
            throw new DatoInvalidException("El precio minimo no puede ser mayor al precio maximo");
        }

        Specification<Producto> spec = ProductoEspecificaciones.nombreContiene(nombre)
                .and(ProductoEspecificaciones.categoriaIgual(cat))
                .and(ProductoEspecificaciones.precioMayorIgual(precioMin))
                .and(ProductoEspecificaciones.precioMenorIgual(precioMax));

        List<ProductoResponse> resultado = productoRepository.findAll(spec).stream()
                .map(productoMapper::entidadResponse)
                .toList();

        log.info("Listado de productos finalizado: {} coincidencias", resultado.size());
        return resultado;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        return productoMapper.entidadResponse(obtenerProductoException(id));
    }

    @Override
    public ProductoResponse registrar(ProductoRequest request) {

        log.info("Registrando nuevo producto... ");

        Producto producto = productoMapper.requestAEntidad(
                request,
                Categoria.obtenerCategoriaPorDescripcion(request.categoria().trim()));

        productoRepository.saveAndFlush(producto);

        log.info("Nuevo producto {} registrado ", producto.getNombre());

        return productoMapper.entidadResponse(producto);
    }

    @Override
    public ProductoResponse actualizar(ProductoRequest request, long id) {

        Producto producto = obtenerProductoException(id);

        log.info("Actualizar producto con id: {}", id);

        producto.actualizar(
                request.nombre().trim(),
                Categoria.obtenerCategoriaPorDescripcion(request.categoria().trim()),
                request.precio(),
                request.cantidad());

        productoRepository.saveAndFlush(producto);

        log.info("Producto con id: {} actualizado", id);

        return productoMapper.entidadResponse(producto);
    }

    @Override
    public void eliminar(Long id) {

        Producto producto = obtenerProductoException(id);

        log.info("Eliminando producto con id: {}", id);

        productoRepository.delete(producto);
        productoRepository.flush();

        log.info("Producto con id {} eliminado", id);

    }

    private Producto obtenerProductoException(Long id){
        log.info("Buscando producto con id: {}", id);

        return productoRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException(
                "Producto no encontrado con id: " +id));
    }
}
