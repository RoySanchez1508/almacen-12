package com.rodrigo.almacen.entities;

import com.rodrigo.almacen.enums.Categoria;
import com.rodrigo.almacen.enums.EstadoVenta;
import com.rodrigo.almacen.exceptions.ConflictoException;
import com.rodrigo.almacen.exceptions.DatoInvalidException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VENTAS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VENTA")
    private Long id;

    @Column(name = "ESTADO", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoVenta estadoVenta;

    @Column(name = "FEHCA", nullable = false)
    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SUCURSAL", nullable = false)
    private Sucursal sucursal;

    @Builder.Default
    @OneToMany(mappedBy = "venta", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DetalleVenta> detalleVentas = new ArrayList<>();

    public void  agregarDetalle(DetalleVenta detalleVenta){
        if (detalleVenta == null)
            throw new DatoInvalidException("El detalle de venta es requerido");

        this.detalleVentas.add(detalleVenta);
    }

    public void cancelar(){

        if (this.estadoVenta == EstadoVenta.CANCELADA)
            throw new ConflictoException("La venta ya esta cancelada");

        this.estadoVenta = EstadoVenta.CANCELADA;
    }
}
