package com.rodrigo.almacen.services.sucursales;

import com.rodrigo.almacen.dto.sucursales.SucursalRequest;
import com.rodrigo.almacen.dto.sucursales.SucursalResponse;
import com.rodrigo.almacen.entities.Sucursal;

import java.util.List;

public interface SucursalService {

    List<SucursalResponse> listar();

    SucursalResponse obtenerPorId(Long id);

    SucursalResponse registrar(SucursalRequest request);

    SucursalResponse actualizar(SucursalRequest request, Long id);

    void eliminar(Long id);
}
