package com.rodrigo.almacen.repositories.specs;

import com.rodrigo.almacen.entities.Producto;
import com.rodrigo.almacen.enums.Categoria;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductoEspecificaciones {

    private ProductoEspecificaciones(){}

    public static Specification<Producto> nombreContiene(String nombre) {
        return (root, query, cb) -> {
            if (nombre == null || nombre.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(
                    cb.lower(root.get("nombre")),
                    "%" + nombre.trim().toLowerCase() + "%");
        };
    }

    public static Specification<Producto> categoriaIgual(Categoria categoria){
        return ((root, query, cb) -> categoria == null
                ? cb.conjunction()
                : cb.equal(root.get("categoria"), categoria));
    }

    public static Specification<Producto> precioMayorIgual(BigDecimal precioMin){
        return ((root, query, cb) -> precioMin == null
                ? cb.conjunction()
                : cb.greaterThanOrEqualTo(root.get("precio"), precioMin));
    }

    public static Specification<Producto> precioMenorIgual(BigDecimal precioMax){
        return ((root, query, cb) -> precioMax == null
                ? cb.conjunction()
                : cb.lessThanOrEqualTo(root.get("precio"), precioMax));
    }
}
