package com.rodrigo.almacen.services.ventas;

import com.rodrigo.almacen.dto.ventas.VentaRequest;
import com.rodrigo.almacen.dto.ventas.VentaResponse;

import java.util.List;

public interface VentasService {

    List<VentaResponse> listar();

    VentaResponse obtenerPorIdActiva(Long id);

    VentaResponse registrar(VentaRequest request);

    List<VentaResponse> listarCanceladas();

    void cancelar (Long id);
}
