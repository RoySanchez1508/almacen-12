package com.rodrigo.almacen.services.reportes;


import com.rodrigo.almacen.dto.reportes.ReporteVentasSucursalResponse;
import com.rodrigo.almacen.repositories.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ReporteServiceImpl implements ReporteService{

    private final VentaRepository ventaRepository;


    @Override
    public List<ReporteVentasSucursalResponse> reporteVentasPorSucursal() {
        log.info("Generando reporte de ventas por sucursal...");
        List<ReporteVentasSucursalResponse> reporte =
                ventaRepository.reporteVentasSucursal();
        log.info("Reporte generado: {} sucursales con actividad comercial", reporte.size());
        return reporte;
    }
}

