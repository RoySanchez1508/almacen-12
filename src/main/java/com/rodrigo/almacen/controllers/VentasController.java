package com.rodrigo.almacen.controllers;

import com.rodrigo.almacen.docs.ProblemaDoc;
import com.rodrigo.almacen.dto.productos.ProductoResponse;
import com.rodrigo.almacen.dto.sucursales.SucursalRequest;
import com.rodrigo.almacen.dto.sucursales.SucursalResponse;
import com.rodrigo.almacen.dto.ventas.VentaRequest;
import com.rodrigo.almacen.dto.ventas.VentaResponse;
import com.rodrigo.almacen.services.ventas.VentasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@Tag(name = "API Ventas", description = "Gestion de inventario")
// Errores que pueden ocurrir en cualquier endpoint:
@ApiResponse(responseCode = "400", description = "Datos o parámetros inválidos",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
@ApiResponse(responseCode = "500", description = "Error interno del servidor",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
public class VentasController {

    private final VentasService ventasService;

    @GetMapping
    @Operation(summary = "Listar las ventas activas", description = "Todos los filtros son opcionales.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido")
    public ResponseEntity<List<VentaResponse>> listar(
    ){
        return ResponseEntity.ok(ventasService.listar());
    }


    @GetMapping("/canceladas")
    @Operation(summary = "Listar las ventas canceladas", description = "Todos los filtros son opcionales.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido")
    public ResponseEntity<List<VentaResponse>> listarCanceladas(
    ){
        return ResponseEntity.ok(ventasService.listarCanceladas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener venta por ID")
    @ApiResponse(responseCode = "200", description = "Venta encontrada")
    @ApiResponse(responseCode = "404", description = "La Venta no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<VentaResponse> obtenerPorId(
            @Parameter(description = "ID de la sucursal", example = "1")
            @PathVariable @Positive(message = "El ID debe de ser posiivo")Long id)
    {
        return ResponseEntity.ok(ventasService.obtenerPorIdActiva(id));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar una venta")
    @ApiResponse(responseCode = "204", description = "Venta cancelada")
    @ApiResponse(responseCode = "404", description = "La Sucursal no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<VentaResponse> cancelar(
            @Parameter(description = "ID de la venta", example = "1")
            @PathVariable @Positive(message = "El ID debe de ser posiivo")Long id
    ){
        ventasService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @Operation(summary = "Registrar un nueva venta")
    @ApiResponse(responseCode = "201", description = "Venta creada")
    @ApiResponse(responseCode = "409", description = "Conflicto con datos existentes",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<VentaResponse> registrar(
            @Valid @RequestBody VentaRequest request
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(ventasService.registrar(request));
    }
}
