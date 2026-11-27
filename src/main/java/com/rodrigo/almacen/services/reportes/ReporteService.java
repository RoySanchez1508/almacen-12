package com.rodrigo.almacen.services.reportes;

import com.rodrigo.almacen.dto.reportes.ReporteVentasSucursalResponse;

import java.util.List;


public interface ReporteService {

    List<ReporteVentasSucursalResponse> reporteVentasPorSucursal();
}
