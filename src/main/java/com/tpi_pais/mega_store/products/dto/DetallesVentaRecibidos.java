package com.tpi_pais.mega_store.products.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class DetallesVentaRecibidos {
    private ArrayList<DetalleVentaDTO> detalles;
}
