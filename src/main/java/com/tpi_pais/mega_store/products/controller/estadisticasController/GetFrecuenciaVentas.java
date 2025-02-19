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
@RequestMapping("/estadisticas-clientes")
public class GetFrecuenciaVentas {

    private EstadisticasService estadisticasService;
    private ResponseService responseService;

    public GetFrecuenciaVentas(EstadisticasService estadisticasService, ResponseService responseService) {
        this.estadisticasService = estadisticasService;
        this.responseService = responseService;
    }

    @SessionRequired
    @GetMapping({"/frecuencia-ventas"})
    public ResponseEntity<ApiResponse<Object>> obtenerProductosMasVendidos() {
        return responseService.successResponse(estadisticasService.obtenerFrecuenciaVentas(), "OK");
    }

    @SessionRequired
    @GetMapping({"/montos-ventas"})
    public ResponseEntity<ApiResponse<Object>> obtenerMontosPromediosVentas() {
        return responseService.successResponse(estadisticasService.obtenerMontoPromedioVentas(), "OK");
    }
}
