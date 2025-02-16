package com.tpi_pais.mega_store.products.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "ventas_por_periodo")
@Data
public class VentasPorPeriodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha", nullable = false)
    private String fecha;

    @Column(name = "total_ventas", nullable = false)
    private Integer total_ventas;

    @Column(name = "total_monto", nullable = false)
    private BigDecimal total_monto;
}
