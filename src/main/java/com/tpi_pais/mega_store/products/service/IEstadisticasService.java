package com.tpi_pais.mega_store.products.service;

import com.tpi_pais.mega_store.products.dto.EstadisticaVentasDTO;
import com.tpi_pais.mega_store.products.dto.EstadisticasProductosMasVendidosDTO;
import java.util.List;

public interface IEstadisticasService {

    public List<EstadisticasProductosMasVendidosDTO> obtenerProductosMasVendidos(String fechaDesde, String fechaHasta, Integer limite);

    public void verificarFechas (String fechaDesde, String fechaHasta);

    public List<EstadisticaVentasDTO> obtenerVentas(String fechaDesde, String fechaHasta, String frecuencia);
}
