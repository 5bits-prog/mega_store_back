package com.tpi_pais.mega_store.products.repository;

import com.tpi_pais.mega_store.products.dto.EstadisticaMontoPromedioVentaDTO;
import com.tpi_pais.mega_store.products.model.MontoPromedioVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MontoPromedioVentaRepository extends JpaRepository<MontoPromedioVenta, Integer> {
    @Query("SELECT new com.tpi_pais.mega_store.products.dto.EstadisticaMontoPromedioVentaDTO(v.usuario.id, AVG(v.totalVenta)) " +
            "FROM DetalleVenta dv " +
            "JOIN dv.venta v " +
            "GROUP BY v.usuario.id " +
            "ORDER BY v.usuario.id ASC")
    List<EstadisticaMontoPromedioVentaDTO> obtenerMontoPromedioPorUsuario();
}
