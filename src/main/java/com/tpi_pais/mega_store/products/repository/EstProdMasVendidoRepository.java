package com.tpi_pais.mega_store.products.repository;

import com.tpi_pais.mega_store.products.model.ProductoMasVendido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface EstProdMasVendidoRepository extends JpaRepository<ProductoMasVendido, Integer> {
    @Query(value = "SELECT " +
            "ROW_NUMBER() OVER (ORDER BY MAX(v.fecha_venta)) AS id, " +
            "dv.producto_id AS id_producto, " +
            "p.nombre AS producto, " +
            "SUM(dv.cantidad) AS cantidad_vendida, " +
            "COUNT(dv.id) AS ventas " +
            "FROM detalles_ventas dv " +
            "INNER JOIN ventas v ON v.id = dv.venta_id " +
            "INNER JOIN productos p ON p.id = dv.producto_id " +
            "WHERE v.fecha_venta >= :fechaDesde " +
            "  AND v.fecha_venta < :fechaHasta " +
            "GROUP BY dv.producto_id, p.nombre " +
            "ORDER BY cantidad_vendida DESC " +
            "LIMIT :limite",
            nativeQuery = true)
    List<ProductoMasVendido> findProductosMasVendidos(
            @Param("fechaDesde") Timestamp fechaDesde,
            @Param("fechaHasta") Timestamp fechaHasta,
            @Param("limite") Integer limite
    );
}
