package com.tpi_pais.mega_store.products.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "monto_promedio_ventas")
@Data
public class MontoPromedioVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "monto", nullable = false)
    private Double monto;
}
