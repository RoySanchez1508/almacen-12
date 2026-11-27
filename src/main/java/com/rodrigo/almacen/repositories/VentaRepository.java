package com.rodrigo.almacen.repositories;

import com.rodrigo.almacen.dto.reportes.ReporteVentasSucursalResponse;
import com.rodrigo.almacen.entities.Venta;
import com.rodrigo.almacen.enums.EstadoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query("SELECT v FROM Venta v JOIN FETCH v.detalleVentas d JOIN FETCH d.productos WHERE v.estadoVenta = :estado")
    List<Venta> findAllConDetallesPorEstado(@Param("estado")EstadoVenta estado);

    @Query("SELECT DISTINCT v FROM Venta v JOIN FETCH v.detalleVentas d WHERE v.id = :id")
    Optional<Venta> findConDetallesById(@Param("id")Long id);

    @Query("""
            SELECT new com.rodrigo.almacen.dto.reportes.ReporteVentasSucursalResponse(
                s.id,
                s.nombre,
                SUM(d.cantidadProducto * d.precioProducto),
                SUM(d.cantidadProducto)
           )
           FROM Venta v
           JOIN v.sucursal s
           JOIN v.detalleVentas d
           WHERE v.estadoVenta = com.rodrigo.almacen.enums.EstadoVenta.REGISTRADA
           GROUP BY s.id, s.nombre
           ORDER BY SUM(d.cantidadProducto * d.precioProducto) DESC
        """)
    List<ReporteVentasSucursalResponse> reporteVentasSucursal();

}
