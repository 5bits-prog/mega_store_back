package com.tpi_pais.mega_store.products.mapper;

import com.tpi_pais.mega_store.products.dto.EstadisticaVentasDTO;
import com.tpi_pais.mega_store.products.model.VentasPorPeriodo;
import org.springframework.stereotype.Component;

@Component
public class EstadisticasVentasMapper {
    public EstadisticaVentasDTO diaToDTO (VentasPorPeriodo estadisticaVentasDias) {
        EstadisticaVentasDTO estadisticaVentasDTO = new EstadisticaVentasDTO();
        estadisticaVentasDTO.setFecha(estadisticaVentasDias.getFecha());
        estadisticaVentasDTO.setTotal_ventas(estadisticaVentasDias.getTotal_ventas());
        estadisticaVentasDTO.setTotal_monto(estadisticaVentasDias.getTotal_monto());
        return estadisticaVentasDTO;
    }

    public EstadisticaVentasDTO mesToDTO (VentasPorPeriodo estadisticaVentasMes) {
        EstadisticaVentasDTO estadisticaVentasDTO = new EstadisticaVentasDTO();
        estadisticaVentasDTO.setFecha(estadisticaVentasMes.getFecha());
        estadisticaVentasDTO.setTotal_ventas(estadisticaVentasMes.getTotal_ventas());
        estadisticaVentasDTO.setTotal_monto(estadisticaVentasMes.getTotal_monto());
        return estadisticaVentasDTO;
    }

    public EstadisticaVentasDTO anioToDTO (VentasPorPeriodo estadisticaVentasAnios) {
        EstadisticaVentasDTO estadisticaVentasDTO = new EstadisticaVentasDTO();
        estadisticaVentasDTO.setFecha(estadisticaVentasAnios.getFecha());
        estadisticaVentasDTO.setTotal_ventas(estadisticaVentasAnios.getTotal_ventas());
        estadisticaVentasDTO.setTotal_monto(estadisticaVentasAnios.getTotal_monto());
        return estadisticaVentasDTO;
    }
}
