package com.tpi_pais.mega_store.products.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EstadisticaVentasDTO {
    public String fecha;
    public Integer total_ventas;
    public BigDecimal total_monto;
}
