package com.tpi_pais.mega_store.products.controller.GenerarDatos;

import com.tpi_pais.mega_store.configs.SessionRequired;
import com.tpi_pais.mega_store.exception.ResponseService;
import com.tpi_pais.mega_store.products.dto.*;
import com.tpi_pais.mega_store.products.model.Categoria;
import com.tpi_pais.mega_store.products.service.*;
import com.tpi_pais.mega_store.utils.ApiResponse;
import com.tpi_pais.mega_store.utils.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Random;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/generador_datos") // Define la ruta base para los productos
public class GenDatosProductos {

    private IProductoService productoService;
    private ICategoriaService categoriaService;
    private ResponseService responseService;
    private IMarcaService marcaService;
    private IColorService colorService;
    private ITalleService talleService;
    private IMovimientoStockService movimientoStockService;
    private StringUtils stringUtils;

    public GenDatosProductos(IProductoService productoService,
                             ICategoriaService categoriaService,
                             ResponseService responseService,
                             IMarcaService marcaService,
                             IColorService colorService,
                             ITalleService talleService,
                             IMovimientoStockService movimientoStockService,
                             StringUtils stringUtils) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.responseService = responseService;
        this.marcaService = marcaService;
        this.colorService = colorService;
        this.talleService = talleService;
        this.movimientoStockService = movimientoStockService;
        this.stringUtils = stringUtils;
    }



    @SessionRequired
    @PostMapping("/productos")
    public ResponseEntity<ApiResponse<Object>> guardar(@RequestHeader("Authorization") String token) throws ParseException {
        Map<String, List<String>> productosPorCategoria = new HashMap<>();

        productosPorCategoria.put("Camisetas deportivas", Arrays.asList(
                "Camiseta Dry-Fit negra", "Camiseta de compresión azul", "Camiseta térmica manga larga",
                "Camiseta sin mangas para running", "Camiseta de fútbol oficial", "Camiseta con capucha ligera",
                "Camiseta de entrenamiento básica", "Camiseta transpirable de poliéster", "Camiseta con tecnología antiolor",
                "Camiseta deportiva con estampado"
        ));

        productosPorCategoria.put("Pantalones deportivos", Arrays.asList(
                "Pantalón de jogging con cierre", "Leggings de compresión", "Pantalón corto de running",
                "Joggers de algodón", "Pantalón deportivo con bolsillos", "Pantalón de yoga elástico",
                "Pantalón cargo deportivo", "Pantalón térmico para invierno", "Pantalón de entrenamiento ligero",
                "Pantalón de ciclismo con acolchado"
        ));

        productosPorCategoria.put("Shorts y bermudas", Arrays.asList(
                "Short de entrenamiento ajustado", "Bermuda de básquetbol", "Short para correr con malla interior",
                "Short de secado rápido", "Bermuda de ciclismo", "Short para gimnasio con cordón ajustable",
                "Bermuda de surf resistente al agua", "Short holgado para yoga", "Short ligero con bolsillos",
                "Bermuda de entrenamiento con diseño moderno"
        ));

        productosPorCategoria.put("Calzado deportivo", Arrays.asList(
                "Zapatillas de running con amortiguación", "Botines de fútbol con tacos", "Zapatillas para entrenamiento cruzado",
                "Calzado de senderismo resistente al agua", "Tenis para tenis de suela antideslizante", "Zapatos de levantamiento de pesas",
                "Zapatillas ligeras para caminar", "Calzado de ciclismo con ajuste ergonómico", "Zapatillas de baloncesto de alto rendimiento",
                "Sandalias deportivas para descanso"
        ));

        productosPorCategoria.put("Sudaderas y buzos", Arrays.asList(
                "Sudadera con capucha y cierre", "Buzo térmico para invierno", "Sudadera sin mangas para entrenamiento",
                "Buzo con tecnología antiolor", "Sudadera con bolsillos laterales", "Buzo con diseño ajustado",
                "Sudadera ligera de algodón", "Buzo oversized de moda", "Sudadera con interior afelpado",
                "Buzo de compresión para entrenamiento"
        ));

        productosPorCategoria.put("Ropa térmica y compresiva", Arrays.asList(
                "Mallas térmicas para invierno", "Camiseta térmica de manga larga", "Pantalón de compresión para running",
                "Mangas compresivas para brazos", "Ropa térmica de dos piezas", "Calcetines de compresión deportivos",
                "Guantes térmicos para ciclismo", "Chaleco térmico ajustado", "Pantalón térmico para montaña",
                "Malla compresiva para recuperación muscular"
        ));

        productosPorCategoria.put("Tops y sujetadores deportivos", Arrays.asList(
                "Top deportivo de alto impacto", "Sujetador deportivo con soporte medio", "Top sin costuras para yoga",
                "Sujetador con tecnología de absorción de humedad", "Top de tirantes cruzados", "Sujetador con ajuste ergonómico",
                "Top con panel de malla transpirable", "Sujetador con relleno removible", "Top ajustado para entrenamiento intenso",
                "Sujetador sin costuras para mayor comodidad"
        ));

        productosPorCategoria.put("Chaquetas y cortavientos", Arrays.asList(
                "Chaqueta cortavientos para running", "Chaqueta térmica con capucha", "Rompevientos ultraligero",
                "Chaqueta impermeable para ciclismo", "Chaqueta acolchada para invierno", "Cortavientos ajustado con reflectantes",
                "Chaqueta para senderismo de secado rápido", "Rompevientos con diseño aerodinámico", "Chaqueta ligera para primavera",
                "Cortavientos con ventilación trasera"
        ));

        productosPorCategoria.put("Trajes de baño deportivos", Arrays.asList(
                "Bañador de natación de una pieza", "Traje de baño de competición", "Short de natación ajustado",
                "Bikini deportivo con soporte", "Traje de baño con protección UV", "Bañador de neopreno para triatlón",
                "Bikini con sujetador deportivo", "Short de natación con cintura ajustable", "Traje de baño con tejido hidrodinámico",
                "Bañador de manga larga para surf"
        ));

        productosPorCategoria.put("Accesorios deportivos", Arrays.asList(
                "Mochila deportiva impermeable", "Guantes de entrenamiento con agarre", "Gorra transpirable para running",
                "Calcetines deportivos de compresión", "Cinturón para llevar celular", "Cuerda para saltar ajustable",
                "Muñequeras absorbentes", "Gafas de natación con protección UV", "Rodilleras deportivas para entrenamiento",
                "Cinturón lumbar para levantamiento de pesas"
        ));

        productosPorCategoria.put("Ropa para entrenamiento en casa", Arrays.asList(
                "Conjunto de entrenamiento cómodo", "Pantalón de yoga elástico", "Camiseta holgada para mayor movilidad",
                "Leggings de alta elasticidad", "Short transpirable para entrenamiento", "Sudadera ligera para casa",
                "Sujetador deportivo de soporte medio", "Buzo amplio para relajación", "Calcetines antideslizantes para yoga",
                "Conjunto térmico para entrenamiento en climas fríos"
        ));

        // Imprimir la lista de productos por categoría
        productosPorCategoria.forEach((categoria, productos) -> {
            System.out.println("Categoría: " + categoria);
            productos.forEach(producto -> System.out.println("  - " + producto));
        });
        token = stringUtils.limpiarToken(token);

        String imagen = "https://i.ibb.co/8D4yWCsv/4458c3dc292d.png";
        int min = 1000;
        int max = 20000;
        int paso = 50; // Cambia a 100 si quieres números como 1000, 1100, 1200, etc.

        // Calcula la cantidad de valores posibles
        int cantidadValores = ((max - min) / paso) + 1;

        Random random = new Random();

        List<CategoriaDTO> categorias = categoriaService.listar();
        List<MarcaDTO> marcas = marcaService.listar();
        List<ColorDTO> colores = colorService.listar();
        List<TalleDTO> tallas = talleService.listar();

        for (Map.Entry<String, List<String>> entry : productosPorCategoria.entrySet()) {
            String categoriaNombre = entry.getKey();
            List<String> productos = entry.getValue();

            for (String nombreProducto : productos) {  // Recorremos cada producto dentro de la categoría
                ProductoDTO productoDTO = new ProductoDTO();
                productoDTO.setNombre(nombreProducto); // Ahora asignamos el nombre correcto del producto
                productoDTO.setDescripcion("Descripcion comun");

                int indiceAleatorio = random.nextInt(cantidadValores);
                int numeroGenerado = min + (indiceAleatorio * paso);
                productoDTO.setPrecio(new BigDecimal(numeroGenerado));
                productoDTO.setPeso(new BigDecimal(10));
                productoDTO.setStockMedio(50);
                productoDTO.setStockMinimo(10);
                productoDTO.setFoto(imagen);
                productoDTO.setSucursales(Arrays.asList(11, 12, 13, 14, 15, 16, 17, 19, 20, 21).toArray(new Integer[0]));

                Optional<CategoriaDTO> categoriaBuscada = categorias.stream()
                        .filter(categoria -> categoria.getNombre().equalsIgnoreCase(categoriaNombre))
                        .findFirst();

                if (categoriaBuscada.isPresent()) {
                    productoDTO.setCategoriaId(categoriaBuscada.get().getId());
                } else {
                    continue;
                }

                productoDTO.setMarcaId(marcas.get(random.nextInt(marcas.size())).getId());
                productoDTO.setColorId(colores.get(random.nextInt(colores.size())).getId());
                productoDTO.setTalleId(tallas.get(random.nextInt(tallas.size())).getId());

                ProductoDTO producto = productoService.crear(productoDTO, token);

                MovimientosDTO movimientosDTO = new MovimientosDTO();
                movimientosDTO.setIdProducto(producto.getId());
                List<Integer> sucursales = Arrays.asList(11, 12, 13, 14, 15, 16, 17, 19, 20, 21);
                List<MovimientoDTO> movimientos = new ArrayList<>();
                for (Integer sucursal : sucursales) {
                    MovimientoDTO movimiento = new MovimientoDTO();
                    movimiento.setIdSucursal(sucursal);
                    movimiento.setCantidad(300);
                    movimientos.add(movimiento);
                }
                movimientosDTO.setListaMovimientos(movimientos);
                movimientoStockService.guardar(movimientosDTO);
            }
        }


        return responseService.successResponse(null, "OK");
    }
}
