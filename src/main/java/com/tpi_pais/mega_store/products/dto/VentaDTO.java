package com.tpi_pais.mega_store.products.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
public class VentaDTO {
    private Integer id;
    private LocalDateTime fechaVenta;
    private Integer usuarioId;
    private String  usuario;
    private BigDecimal totalVenta;
    private ArrayList<DetalleVentaDTO> detalles;
}
