package com.tpi_pais.mega_store.products.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EstadisticaMontoPromedioVentaDTO {

    private Integer usuarioId;  // Representa el usuario_id
    private Double monto;       // Representa el monto promedio de venta
}
