package com.tpi_pais.mega_store.products.service;

import com.tpi_pais.mega_store.auth.model.Sesion;
import com.tpi_pais.mega_store.auth.model.Usuario;
import com.tpi_pais.mega_store.auth.service.ISesionService;
import com.tpi_pais.mega_store.auth.service.SesionService;
import com.tpi_pais.mega_store.products.dto.DetalleVentaDTO;
import com.tpi_pais.mega_store.products.dto.VentaDTO;
import com.tpi_pais.mega_store.products.dto.VentaUsuarioDTO;
import com.tpi_pais.mega_store.products.mapper.DetalleVentaMapper;
import com.tpi_pais.mega_store.products.mapper.VentaMapper;
import com.tpi_pais.mega_store.products.model.DetalleVenta;
import com.tpi_pais.mega_store.products.model.Producto;
import com.tpi_pais.mega_store.products.model.Venta;
import com.tpi_pais.mega_store.products.repository.DetalleVentaRepository;
import com.tpi_pais.mega_store.products.repository.VentaRepository;
import com.tpi_pais.mega_store.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.*;

@Service
public class VentaService implements IVentaService {
    private final IDetalleVentaService detalleVentaService;
    private final ISesionService sesionService;
    private final StringUtils stringUtils;
    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final IMovimientoStockService movimientoStockService;
    private final IProductoService productoService;

    public VentaService(
            DetalleVentaService detalleVentaService,
            SesionService sesionService,
            StringUtils stringUtils,
            VentaRepository ventaRepository,
            DetalleVentaRepository detalleVentaRepository,
            MovimientoStockService movimientoStockService,
            ProductoService productoService) {
        this.detalleVentaService = detalleVentaService;
        this.sesionService = sesionService;
        this.stringUtils = stringUtils;
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.movimientoStockService = movimientoStockService;
        this.productoService = productoService;
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public VentaDTO guardar(String token, ArrayList<DetalleVentaDTO> model) {
        Sesion sesion = sesionService.obtenerSesionPorToken(stringUtils.limpiarToken(token));
        Usuario usuario = sesion.getUsuario();

        Venta venta = new Venta();
        venta.setUsuario(usuario);

        ArrayList<DetalleVenta> detalles = new ArrayList<>();
        BigDecimal totalVenta = new BigDecimal(0);
        for (DetalleVentaDTO detalleVentaDTO : model) {
            //Validar
            detalleVentaService.verificarDetalle(detalleVentaDTO);
            //Guardar
            Producto producto = productoService.buscarPorId(detalleVentaDTO.getIdProducto());

            DetalleVenta detalleVenta = new DetalleVenta();
            detalleVenta.setProducto(producto);
            detalleVenta.setCantidad(detalleVentaDTO.getCantidad());
            detalleVenta.setVenta(venta);
            detalleVenta.setPrecioUnitario(producto.getPrecio());
            detalleVenta.setSubtotal(BigDecimal.valueOf(detalleVentaDTO.getCantidad()).multiply(producto.getPrecio()));
            detalleVenta.setVenta(venta);
            detalles.add(detalleVenta);
            //Calcular
            totalVenta = totalVenta.add(detalleVenta.getSubtotal());
        }
        venta.setTotalVenta(totalVenta);
        Venta ventaGuardada = ventaRepository.save(venta);
        ArrayList<DetalleVentaDTO> detallesGuardados = new ArrayList<>();

        for (DetalleVenta detalleVenta : detalles) {
            detalleVenta.setVenta(ventaGuardada);
            DetalleVenta detalleVentaGuardada = detalleVentaRepository.save(detalleVenta);
            detallesGuardados.add(DetalleVentaMapper.toDTO(detalleVentaGuardada));
            movimientoStockService.egresar(detalleVenta);
        }
        return VentaMapper.toDTO(ventaGuardada, detallesGuardados);
    }

    @Override
    public Venta buscarPorId(Integer id) {
        return ventaRepository.findById(id).orElse(null);
    }

    @Override
    public List<VentaUsuarioDTO> listarPorUsuario(Integer id) {
        List<Venta> ventas = ventaRepository.findByFechaEliminacionIsNullAndUsuarioId(id);
        return ventas.stream().map(VentaMapper::toDTOUsuario).toList();
    }
}

