package com.tpi_pais.mega_store.products.repository;

import com.tpi_pais.mega_store.products.model.VentasPorPeriodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface EstVentasRepository extends JpaRepository<VentasPorPeriodo, Integer> {
    @Query(value = "SELECT " +
            "ROW_NUMBER() OVER (ORDER BY MAX(v.fecha_venta))  AS id, " +
            "TO_CHAR(v.fecha_venta, 'YYYY') AS fecha, " +
            "COUNT(*) AS total_ventas, " +
            "SUM(v.total_venta) AS total_monto " +
            "FROM ventas v " +
            "WHERE v.fecha_venta >= :fechaDesde " +
            "  AND v.fecha_venta < :fechaHasta " +
            "GROUP BY fecha " +
            "ORDER BY fecha",
            nativeQuery = true)
    List<VentasPorPeriodo> findVentasPorAnios(
            @Param("fechaDesde") Timestamp fechaDesde,
            @Param("fechaHasta") Timestamp fechaHasta
    );

    @Query(value = "SELECT " +
            "ROW_NUMBER() OVER (ORDER BY MAX(v.fecha_venta))  AS id, " +
            "TO_CHAR(v.fecha_venta, 'YYYY-MM') AS fecha, " +
            "COUNT(*) AS total_ventas, " +
            "SUM(v.total_venta) AS total_monto " +
            "FROM ventas v " +
            "WHERE v.fecha_venta >= :fechaDesde " +
            "  AND v.fecha_venta < :fechaHasta " +
            "GROUP BY fecha " +
            "ORDER BY fecha ",
            nativeQuery = true)
    List<VentasPorPeriodo> findVentasPorMes(
            @Param("fechaDesde") Timestamp fechaDesde,
            @Param("fechaHasta") Timestamp fechaHasta
    );

    @Query(value = "SELECT " +
            "ROW_NUMBER() OVER (ORDER BY MAX(v.fecha_venta))  AS id, " +
            "TO_CHAR(v.fecha_venta, 'YYYY-MM-DD') AS fecha, " +
            "COUNT(*) AS total_ventas, " +
            "SUM(v.total_venta) AS total_monto " +
            "FROM ventas v " +
            "WHERE v.fecha_venta >= :fechaDesde " +
            "  AND v.fecha_venta < :fechaHasta " +
            "GROUP BY fecha " +
            "ORDER BY fecha ",
            nativeQuery = true)
    List<VentasPorPeriodo> findVentasPorDias(
            @Param("fechaDesde") Timestamp fechaDesde,
            @Param("fechaHasta") Timestamp fechaHasta
    );

}

