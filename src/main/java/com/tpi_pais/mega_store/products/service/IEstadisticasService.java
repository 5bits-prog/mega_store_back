package com.tpi_pais.mega_store.products.service;

import com.tpi_pais.mega_store.products.dto.EstadisticaVentasDTO;
import com.tpi_pais.mega_store.products.dto.EstadisticasProductosMasVendidosDTO;
import java.util.List;
import java.util.Map;

public interface IEstadisticasService {

    List<EstadisticasProductosMasVendidosDTO> obtenerProductosMasVendidos(String fechaDesde, String fechaHasta, Integer limite);

    void verificarFechas (String fechaDesde, String fechaHasta);

    List<EstadisticaVentasDTO> obtenerVentas(String fechaDesde, String fechaHasta, String frecuencia);

    Map<String, Integer> obtenerFrecuenciaVentas ();

    Map<String, Integer> obtenerMontoPromedioVentas();
}
