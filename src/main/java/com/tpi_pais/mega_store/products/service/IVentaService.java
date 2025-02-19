package com.tpi_pais.mega_store.products.service;

import com.tpi_pais.mega_store.products.dto.DetalleVentaDTO;
import com.tpi_pais.mega_store.products.dto.VentaDTO;
import com.tpi_pais.mega_store.products.dto.VentaUsuarioDTO;
import com.tpi_pais.mega_store.products.model.Venta;

import java.util.ArrayList;
import java.util.List;

public interface IVentaService {

    public VentaDTO guardar (String token, ArrayList<DetalleVentaDTO> model);

    public Venta buscarPorId(Integer id);

    public List<VentaUsuarioDTO> listarPorUsuario(Integer id);
}
