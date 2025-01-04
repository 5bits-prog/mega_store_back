package com.tpi_pais.mega_store.products.dto;

import lombok.Data;

@Data
public class StockSucursalDTO {

    private Integer id;
    private Integer idSucursal;
    private String nombreSucursal;
    private Integer idProducto;
    private Integer cantidad;
}
