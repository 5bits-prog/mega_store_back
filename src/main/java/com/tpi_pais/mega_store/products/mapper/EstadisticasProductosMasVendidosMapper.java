package com.tpi_pais.mega_store.products.mapper;

import com.tpi_pais.mega_store.products.dto.EstadisticasProductosMasVendidosDTO;
import com.tpi_pais.mega_store.products.model.ProductoMasVendido;
import org.springframework.stereotype.Component;

@Component
public class EstadisticasProductosMasVendidosMapper {

    public EstadisticasProductosMasVendidosDTO toDTO(ProductoMasVendido interfaz) {
        EstadisticasProductosMasVendidosDTO dto = new EstadisticasProductosMasVendidosDTO();
        dto.setIdProducto(interfaz.getIdProducto());
        dto.setProducto(interfaz.getProducto());
        dto.setCantidad_vendida(interfaz.getCantidadVendida());
        dto.setVentas(interfaz.getVentas());
        return dto;
    }
}
