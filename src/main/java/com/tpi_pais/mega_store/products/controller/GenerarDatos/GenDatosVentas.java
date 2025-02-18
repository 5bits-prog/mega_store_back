package com.tpi_pais.mega_store.products.controller.GenerarDatos;

import com.tpi_pais.mega_store.auth.dto.UsuarioDTO;
import com.tpi_pais.mega_store.auth.model.Sesion;
import com.tpi_pais.mega_store.auth.model.Usuario;
import com.tpi_pais.mega_store.auth.service.ISesionService;
import com.tpi_pais.mega_store.auth.service.IUsuarioService;
import com.tpi_pais.mega_store.configs.SessionRequired;
import com.tpi_pais.mega_store.exception.BadRequestException;
import com.tpi_pais.mega_store.exception.ResponseService;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.tpi_pais.mega_store.products.dto.DetalleVentaDTO;
import com.tpi_pais.mega_store.products.dto.ProductoDTO;
import com.tpi_pais.mega_store.products.dto.UsuarioGenDatosDTO;
import com.tpi_pais.mega_store.products.dto.VentaDTO;
import com.tpi_pais.mega_store.products.model.Producto;
import com.tpi_pais.mega_store.products.model.Venta;
import com.tpi_pais.mega_store.products.service.*;
import com.tpi_pais.mega_store.utils.ApiResponse;
import com.github.javafaker.Faker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/generador_datos") // Define la ruta base para los productos
public class GenDatosVentas {
    private IVentaService ventaService;
    private ResponseService responseService;
    private IProductoService productoService;
    private IUsuarioService usuarioService;
    private ISesionService sesionService;

    public GenDatosVentas (IVentaService ventaService,
                           ResponseService responseService,
                           IProductoService productoService,
                           IUsuarioService usuarioService,
                           ISesionService sesionService) {
        this.ventaService = ventaService;
        this.responseService = responseService;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
        this.sesionService = sesionService;
    }

    @SessionRequired
    @PostMapping("/ventas")
    public ResponseEntity<ApiResponse<Object>> guardar (@RequestHeader("Authorization") String token) throws ParseException {
        Faker faker = new Faker();
        // Generar una fecha aleatoria entre fechaInicio y fechaFin
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaDesde = dateFormat.parse("2020-01-01");
        Date fechaHasta = dateFormat.parse("2024-12-31");
        int contador = 0;

        List<ProductoDTO> productos = productoService.listar();

        List<UsuarioDTO> usuarios = usuarioService.listar();

        List<UsuarioGenDatosDTO> clientes = new ArrayList<UsuarioGenDatosDTO>();

        for (UsuarioDTO usuario : usuarios) {
            if (usuario.getRolId() == 4) {
                UsuarioGenDatosDTO cliente = new UsuarioGenDatosDTO();
                cliente.setId(usuario.getId());
                cliente.setEmail(usuario.getEmail());
                cliente.setNombre(usuario.getNombre());
                Sesion sesion = sesionService.obtenerSesionActual(usuarioService.buscarPorId(usuario.getId()));
                cliente.setToken(sesion.getToken());
                clientes.add(cliente);
            }
        }


        while (contador < 500) {
            Date fechaRandom = faker.date().between(fechaDesde, fechaHasta); // Puedes ajustar el rango de fechas, fechaFin
            LocalDateTime fechaLocalDateTime = fechaRandom.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();



            Random random = new Random();
            int cantidadProductos = random.nextInt(6) + 1; // Genera un número aleatorio entre 1 y 5q

            ArrayList<DetalleVentaDTO> detalles = new ArrayList<DetalleVentaDTO>();

            for (int i = 0; i < cantidadProductos; i++) {
                while (true) {
                    ProductoDTO producto = productos.get(random.nextInt(productos.size()));

                    boolean existeEnDetalles = detalles.stream()
                            .anyMatch(detalle -> detalle.getIdProducto().equals(producto.getId()));
                    if (existeEnDetalles) {
                        continue; // Si ya existe, se selecciona otro producto
                    }

                    int cantidad = random.nextInt(8) + 1; // Genera un número aleatorio entre 1 y 5
                    if (producto.getStockActual() >= cantidad) {
                        DetalleVentaDTO detalle = new DetalleVentaDTO();
                        detalle.setCantidad(cantidad);
                        detalle.setIdProducto(producto.getId());
                        detalles.add(detalle);
                        break;
                    }else {
                        return responseService.successResponse(null, "OK");
                    }
                }
            }

            VentaDTO venta = ventaService.guardar(clientes.get(random.nextInt(clientes.size())).getToken(), detalles);

            Venta ventaModel = ventaService.buscarPorId(venta.getId());

            ventaModel.setFechaCreacion(fechaLocalDateTime);

            contador += 1;
        }

        return responseService.successResponse(null, "OK");
    }
}
