package com.tpi_pais.mega_store.products.repository;

import com.tpi_pais.mega_store.products.dto.PromedioVentaDTO;
import com.tpi_pais.mega_store.products.model.FrecuenciaVentas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FrecuenciaVentasRepository extends JpaRepository<FrecuenciaVentas, Integer> {
    @Query(value = "SELECT " +
            "    cliente_id AS id, " +
            "    ROUND(AVG(intervalo_dias)) AS dias " +
            "FROM ( " +
            "    SELECT " +
            "        v.usuario_id AS cliente_id, " +
            "        EXTRACT(EPOCH FROM (v.fecha_venta - LAG(v.fecha_venta) OVER (PARTITION BY v.usuario_id ORDER BY v.fecha_venta))) / 86400 AS intervalo_dias " +
            "    FROM ventas v " +
            ") subquery " +
            "WHERE intervalo_dias IS NOT NULL " +
            "GROUP BY cliente_id",
            nativeQuery = true)
    List<FrecuenciaVentas> obtenerPromedioDiasEntreVentas();
}
