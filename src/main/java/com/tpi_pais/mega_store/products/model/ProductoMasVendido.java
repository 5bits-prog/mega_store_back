package com.tpi_pais.mega_store.products.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "productos_mas_vendidos")
@Data
public class ProductoMasVendido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_producto", nullable = false)
    private Integer idProducto;

    @Column(name = "producto", nullable = false)
    private String producto;

    @Column(name = "cantidad_vendida", nullable = false)
    private Integer cantidadVendida;

    @Column(name = "ventas", nullable = false)
    private Integer ventas;
}
