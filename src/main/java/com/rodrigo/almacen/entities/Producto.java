package com.rodrigo.almacen.entities;

import com.rodrigo.almacen.enums.Categoria;
import com.rodrigo.almacen.exceptions.DatoInvalidException;
import com.rodrigo.almacen.utils.StringCustomUtils;
import com.rodrigo.almacen.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "PRODUCTOS")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRODUCTO")
    private Long id;

    @Column(name = "NOMBRE", length = 30, nullable = false)
    private String nombre;


    @Column(name = "CATEGORIA", nullable = false)
    @Enumerated(EnumType.STRING)
    private Categoria categoria;


    @Column(name = "PRECIO", nullable = false)
    private BigDecimal precio;


    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    public static void validarDatos(
            String nombre, Categoria categoria,
            BigDecimal precio, Integer cantidad){
        StringCustomUtils.validarTamanio(nombre,5,30,"El nombre es requerido y debe tener entre 5 y 30 caracteres");

        if(categoria == null)
            throw new DatoInvalidException("La categoria es requerida");

        ValoresNumericosUtils.validarBigDecimalPositivo(precio, "El precio es requerido");
        ValoresNumericosUtils.validarEnteroPositivo(cantidad, "La cantidad debe de ser positiva");
    }

    public void actualizar(
            String nombre, Categoria categoria,
            BigDecimal precio, Integer cantidad){
        validarDatos(nombre,categoria,precio,cantidad);

        this.nombre=nombre.trim();
        this.categoria=categoria;
        this.precio=precio;
        this.cantidad=cantidad;
    }

    public void aumentarCantidad(int cantidad){
        ValoresNumericosUtils.validarEnteroPositivo(
                cantidad, "La cantidad debe de ser positiva");
        this.cantidad+= cantidad;
    }

    public void descontarCantidad(int cantidad){
        ValoresNumericosUtils.validarEnteroPositivo(
                cantidad, "La cantidad debe de ser positiva");

        if (cantidad > this.cantidad)
            throw new DatoInvalidException("La cantidad debe ser menor a la cantidad actual");

        this.cantidad -= cantidad;
    }

    public static Producto crear(
            String nombre, Categoria categoria,
            BigDecimal precio, Integer cantidad){

        validarDatos(nombre, categoria, precio, cantidad);

        return Producto.builder()
                .nombre(nombre)
                .categoria(categoria)
                .precio(precio)
                .cantidad(cantidad)
                .build();
    }

}
