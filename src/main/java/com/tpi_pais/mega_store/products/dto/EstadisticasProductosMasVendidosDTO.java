package com.tpi_pais.mega_store.products.dto;

import lombok.Data;

@Data
public class EstadisticasProductosMasVendidosDTO {
    private Integer idProducto;
    private String producto;
    private Integer cantidad_vendida;
    private Integer ventas;
}
