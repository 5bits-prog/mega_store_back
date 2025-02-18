package com.tpi_pais.mega_store.products.controller.productoController;

import com.tpi_pais.mega_store.exception.BadRequestException;
import com.tpi_pais.mega_store.exception.ResponseService;
import com.tpi_pais.mega_store.products.dto.ProductoDTO; // DTO de Producto
import com.tpi_pais.mega_store.products.dto.ProductoRespuestaDTO;
import com.tpi_pais.mega_store.products.dto.StockSucursalDTO;
import com.tpi_pais.mega_store.products.mapper.ProductoMapper; // Mapper para convertir a DTO
import com.tpi_pais.mega_store.products.model.Producto; // Modelo Producto
import com.tpi_pais.mega_store.products.service.*;
import com.tpi_pais.mega_store.utils.ApiResponse; // Respuesta API común
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products") // Ruta base para los productos
public class GetProductoController {

    private final IProductoService productoService; // Servicio para manejar productos

    private final ResponseService responseService; // Servicio para manejar respuestas
    private final IMarcaService marcaService;
    private final ICategoriaService categoriaService;
    private final IColorService colorService;
    private final ITalleService talleService;

    // Constructor con inyección de dependencias
    public GetProductoController(
            IProductoService productoService,
            ResponseService responseService,
            IMarcaService marcaService,
            ICategoriaService categoriaService,
            IColorService colorService,
            ITalleService talleService) {
        this.productoService = productoService;
        this.responseService = responseService;
        this.marcaService = marcaService;
        this.categoriaService = categoriaService;
        this.colorService = colorService;
        this.talleService = talleService;
    }

    // Endpoint para obtener todos los productos
    @GetMapping("/productosAll")
    public ResponseEntity<ApiResponse<Object>> getAll() {
        // Llamar al servicio para listar todos los productos
        List<ProductoDTO> productos = productoService.listar();
        // Si no hay productos, lanzar una excepción con mensaje personalizado
        if (productos.isEmpty()) {
            throw new BadRequestException("No hay productos creados");
        }
        ArrayList<ProductoRespuestaDTO> productosRespuesta = new ArrayList<>();
        for (ProductoDTO productoDTO : productos) {
            ArrayList<StockSucursalDTO> sucursalesDTO = productoService.obtenerSucursales(productoDTO.getId());
            productosRespuesta.add(new ProductoRespuestaDTO(productoDTO, sucursalesDTO,marcaService.buscarPorId(productoDTO.getMarcaId()).getNombre(),categoriaService.buscarPorId(productoDTO.getCategoriaId()).getNombre(),colorService.buscarPorId(productoDTO.getColorId()).getNombre(),talleService.buscarPorId(productoDTO.getTalleId()).getNombre()));
        }
        // Retornar respuesta con los productos en formato DTO y mensaje "OK"
        return responseService.successResponse(productosRespuesta, "OK");
    }

    @GetMapping("/productos")
    public ResponseEntity<ApiResponse<Object>> getAll(@PageableDefault(size = 10, page = 0, sort = "id") Pageable pageable) {
        // Llamar al servicio para listar los productos paginados
        Page<ProductoDTO> productos = productoService.listar(pageable);

        // Si no hay productos, lanzar una excepción con mensaje personalizado
        if (productos.isEmpty()) {
            throw new BadRequestException("No hay productos creados");
        }

        // Convertir los productos a ProductoRespuestaDTO
        Page<ProductoRespuestaDTO> productosRespuesta = productos.map(productoDTO ->
                new ProductoRespuestaDTO(
                        productoDTO,
                        productoService.obtenerSucursales(productoDTO.getId()),
                        marcaService.buscarPorId(productoDTO.getMarcaId()).getNombre(),
                        categoriaService.buscarPorId(productoDTO.getCategoriaId()).getNombre(),
                        colorService.buscarPorId(productoDTO.getColorId()).getNombre(),
                        talleService.buscarPorId(productoDTO.getTalleId()).getNombre()
                )
        );

        // Retornar respuesta con los productos en formato paginado
        return responseService.successResponse(productosRespuesta, "OK");
    }

    // Endpoint para obtener un producto por su ID
    @GetMapping("/producto/{id}")
    public ResponseEntity<ApiResponse<Object>> getPorId(@PathVariable Integer id) {
        // Buscar el producto por su ID en el servicio
        Producto producto = productoService.buscarPorId(id);
        // Si no se encuentra el producto, lanzar una excepción con mensaje personalizado
        if (producto == null) {
            throw new BadRequestException("Producto no encontrado");
        }
        // Convertir el modelo Producto a DTO utilizando el mapper
        ProductoDTO productoDTO = ProductoMapper.toDTO(producto);
        ArrayList<StockSucursalDTO> sucursalesDTO = productoService.obtenerSucursales(productoDTO.getId());

        ProductoRespuestaDTO productoRespuestaDTO = new ProductoRespuestaDTO(productoDTO, sucursalesDTO,producto.getMarca().getNombre(),producto.getCategoria().getNombre(),producto.getColor().getNombre(),producto.getTalle().getNombre());
        // Retornar respuesta con el producto en formato DTO y mensaje "OK"
        return responseService.successResponse(productoRespuestaDTO, "OK");
    }
}
