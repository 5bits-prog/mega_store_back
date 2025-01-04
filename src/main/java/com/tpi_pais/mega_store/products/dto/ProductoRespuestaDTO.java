package com.tpi_pais.mega_store.products.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tpi_pais.mega_store.products.service.SucursalService;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
public class ProductoRespuestaDTO {

    public ProductoRespuestaDTO (ProductoDTO productoDTO, ArrayList<StockSucursalDTO> sucursales) {

        this.id = productoDTO.getId();
        this.nombre = productoDTO.getNombre();
        this.fechaEliminacion = productoDTO.getFechaEliminacion();
        this.precio = productoDTO.getPrecio();
        this.peso = productoDTO.getPeso();
        this.stockMedio = productoDTO.getStockMedio();
        this.stockMinimo = productoDTO.getStockMinimo();
        this.stockActual = productoDTO.getStockActual();
        this.foto = productoDTO.getFoto();
        this.descripcion = productoDTO.getDescripcion();
        this.categoriaId = productoDTO.getCategoriaId();
        this.marcaId = productoDTO.getMarcaId();
        this.talleId = productoDTO.getTalleId();
        this.colorId = productoDTO.getColorId();
        this.sucursales = sucursales;


    }

    private Integer id;
    private String nombre;
    private LocalDateTime fechaEliminacion;
    private BigDecimal precio;
    private BigDecimal peso;
    private Integer stockMedio;
    private Integer stockMinimo;
    private Integer stockActual;
    private String foto;
    private String descripcion;
    private Integer categoriaId;
    private ArrayList<StockSucursalDTO> sucursales;
    private Integer marcaId;
    private Integer talleId;
    private Integer colorId;

}
