package com.rodrigo.almacen.services.sucursales;


import com.rodrigo.almacen.dto.sucursales.SucursalRequest;
import com.rodrigo.almacen.dto.sucursales.SucursalResponse;
import com.rodrigo.almacen.entities.Producto;
import com.rodrigo.almacen.entities.Sucursal;
import com.rodrigo.almacen.enums.Categoria;
import com.rodrigo.almacen.exceptions.ConflictoException;
import com.rodrigo.almacen.exceptions.RecursoNoEncontradoException;
import com.rodrigo.almacen.mappers.SucursalMapper;
import com.rodrigo.almacen.repositories.SucursalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SucursalServiceImpl implements SucursalService{

    private final SucursalRepository sucursalRepository;

    private final SucursalMapper sucursalMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SucursalResponse> listar() {

        log.info("Listando las sucursales");

        return sucursalRepository.findAll().stream()
                .map(sucursalMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SucursalResponse obtenerPorId(Long id) {
        return sucursalMapper.entidadAResponse(obtenerSucursalException(id));
    }

    @Override
    public SucursalResponse registrar(SucursalRequest request) {
        log.info("Registrando nueva sucursal... ");

        validarDatosUnicos(request);

        Sucursal sucursal = sucursalMapper.requestAEntidad(request);

        log.info("Nueva sucursal {} registrado ", sucursal.getNombre());

        return sucursalMapper.entidadAResponse(sucursal);
    }

    @Override
    public SucursalResponse actualizar(SucursalRequest request, Long id) {

        Sucursal sucursal = obtenerSucursalException(id);

        validarCambiosUnicos(request, id);

        log.info("Actualizar sucusal con id: {}", id);

        sucursal.actualizar(
                request.nombre(),
                request.direccion());

        sucursalRepository.saveAndFlush(sucursal);

        log.info("Producto con id: {} actualizado", id);

        return sucursalMapper.entidadAResponse(sucursal);
    }

    @Override
    public void eliminar(Long id) {
        Sucursal sucursal = obtenerSucursalException(id);

        log.info("Eliminando sucursal con id: {}", id);

        sucursalRepository.delete(sucursal);
        sucursalRepository.flush();

        log.info("Sucursal con id {} eliminado", id);
    }

    private Sucursal obtenerSucursalException(Long id){
        log.info("Buscando producto con id: {}", id);

        return sucursalRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException(
                        "Sucursal no encontrada con id: " +id));
    }

    private void validarDatosUnicos (SucursalRequest request){

        log.info("Validar nombre unico.. ");
        if (sucursalRepository.existsByNombreIgnoreCase(request.nombre().trim()))
            throw new ConflictoException("Ya existe una sucursal con el nombre: " +request.nombre());
    }

    private void validarCambiosUnicos (SucursalRequest request, Long id){

        log.info("Validar cambio de nombre unico.. ");
        if (sucursalRepository.existsByNombreIgnoreCaseAndIdNot(request.nombre().trim(),id))
            throw new ConflictoException("Ya existe una sucursal con el nombre: " +request.nombre());
    }
}
