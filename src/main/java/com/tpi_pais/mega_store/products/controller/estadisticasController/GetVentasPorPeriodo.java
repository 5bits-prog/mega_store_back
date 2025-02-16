package com.tpi_pais.mega_store.products.controller.estadisticasController;

import com.tpi_pais.mega_store.configs.SessionRequired;
import com.tpi_pais.mega_store.exception.ResponseService;
import com.tpi_pais.mega_store.products.service.EstadisticasService;
import com.tpi_pais.mega_store.utils.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estadisticas-ventas")
public class GetVentasPorPeriodo {
    private EstadisticasService estadisticasService;
    private ResponseService responseService;

    public GetVentasPorPeriodo(EstadisticasService estadisticasService, ResponseService responseService) {
        this.estadisticasService = estadisticasService;
        this.responseService = responseService;
    }

    @SessionRequired
    @GetMapping({"/productos-mas-vendidos"})
    public ResponseEntity<ApiResponse<Object>> obtenerProductosMasVendidos(
            @RequestParam("fechaDesde") String fechaDesde,
            @RequestParam("fechaHasta") String fechaHasta,
            @RequestParam("limite") Integer limite) {
        return responseService.successResponse(estadisticasService.obtenerProductosMasVendidos(fechaDesde, fechaHasta, limite), "OK");
    }

    @SessionRequired
    @GetMapping({"/ventas-por-periodo"})
    public ResponseEntity<ApiResponse<Object>> ventasPorPeriodo(
            @RequestParam("fechaDesde") String fechaDesde,
            @RequestParam("fechaHasta") String fechaHasta,
            @RequestParam("frecuencia") String frecuencia) {
        return responseService.successResponse(estadisticasService.obtenerVentas(fechaDesde, fechaHasta, frecuencia), "OK");
    }
}
