package com.tpi_pais.mega_store.products.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "frecuencia_ventas")
@Data
public class FrecuenciaVentas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "dias", nullable = false)
    private Integer dias;

}
