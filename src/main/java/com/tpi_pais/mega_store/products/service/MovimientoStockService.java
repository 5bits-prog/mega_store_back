package com.tpi_pais.mega_store.products.service;

import com.tpi_pais.mega_store.auth.model.Usuario;
import com.tpi_pais.mega_store.auth.repository.UsuarioRepository;
import com.tpi_pais.mega_store.auth.service.UsuarioService;
import com.tpi_pais.mega_store.exception.BadRequestException;
import com.tpi_pais.mega_store.exception.MessagesException;
import com.tpi_pais.mega_store.exception.NotFoundException;
import com.tpi_pais.mega_store.products.dto.MovimientoDTO;
import com.tpi_pais.mega_store.products.dto.MovimientoStockDTO;
import com.tpi_pais.mega_store.products.dto.MovimientosDTO;
import com.tpi_pais.mega_store.products.mapper.MovimientoStockMapper;
import com.tpi_pais.mega_store.products.model.*;
import com.tpi_pais.mega_store.products.repository.MovimientoStockRepository;
import com.tpi_pais.mega_store.products.repository.ProductoRepository;
import com.tpi_pais.mega_store.products.repository.StockSucursalRepository;
import com.tpi_pais.mega_store.products.repository.SucursalRepository;
import com.tpi_pais.mega_store.utils.EmailCodigoValidacion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.Integer.parseInt;

@Service
public class MovimientoStockService implements IMovimientoStockService {

    private final MovimientoStockRepository repository;
    private final IProductoService productoService;
    private final MovimientoStockMapper movimientoStockMapper;
    private final SucursalRepository sucursalRepository;
    private final StockSucursalRepository stockSucursalRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final WebClient webClient;

    public MovimientoStockService(
            MovimientoStockRepository repository,
            IProductoService productoService,
            MovimientoStockMapper movimientoStockMapper,
            SucursalRepository sucursalRepository,
            StockSucursalRepository stockSucursalRepository,
            ProductoRepository productoRepository, UsuarioRepository usuarioRepository, WebClient webClient) {
        this.repository = repository;
        this.productoService = productoService;
        this.movimientoStockMapper = movimientoStockMapper;
        this.sucursalRepository = sucursalRepository;
        this.productoRepository = productoRepository;
        this.stockSucursalRepository = stockSucursalRepository;
        this.usuarioRepository = usuarioRepository;
        this.webClient = webClient;
    }

    /*
    * Este guardar se utiliza cuando se hace un movimiento de stock.
    * Si es un egreso se hace un movimiento de stock de egreso,
    * si es un ingreso se hace un movimiento de stock de ingreso
    * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArrayList<MovimientoStockDTO> guardar(MovimientoStockDTO model){
        Producto producto = productoService.buscarPorId(model.getIdProducto());

        Optional<Sucursal> sucursal = sucursalRepository.findByIdAndFechaEliminacionIsNull(model.getIdSucursal());
        ArrayList<MovimientoStockDTO> movimientoStock;
        if (model.getEsEgreso()){
            movimientoStock = this.egreso(producto, model.getCantidad());
        }else{
            movimientoStock = this.ingreso(producto, model.getCantidad(), sucursal.get());
        }

        return movimientoStock;
    };

    @Override
    @Transactional (rollbackFor = Exception.class)
    public ArrayList<MovimientoStockDTO> guardar(MovimientosDTO model){
        /*
        * Verificar que el producto exista
        * Ir mov por mov verificando que la sucursal exista
        * Si existe la sucursal
        * Verificar que exista un stock por sucursal correspondiente
        * Si no existe lo creo
        * Genero el mov, y actualizo el stock de la sucursal y del producto.
        * ----------------------------------------------------
        * */
        Producto producto = productoService.buscarPorId(model.getIdProducto());

        ArrayList<MovimientoStockDTO> movimientosDTO = new ArrayList<>();
        for (MovimientoDTO movimiento : model.getListaMovimientos()){
            Sucursal sucursal = this.verificarSucursalYTraer(movimiento.getIdSucursal());
            StockSucursal stock = this.obtenerStockPorSucursal(producto, sucursal);

            MovimientoStock movimientoStock = new MovimientoStock();
            movimientoStock.setProducto(producto);
            movimientoStock.setCantidad(movimiento.getCantidad());
            movimientoStock.setEsEgreso(false);
            movimientoStock.setSucursal(sucursal);
            MovimientoStock movimientoGuardado = repository.save(movimientoStock);

            stock.setStock(stock.getStock() + movimiento.getCantidad());
            stockSucursalRepository.save(stock);

            movimientosDTO.add(movimientoStockMapper.toDTO(movimientoGuardado));

            producto.setStockActual(producto.getStockActual() + movimiento.getCantidad());
            productoRepository.save(producto);
        }
        return movimientosDTO;
    }

    @Override
    @Transactional (rollbackFor = Exception.class)
    public ArrayList<MovimientoStockDTO> ingreso (Producto producto, Integer cantidad, Sucursal sucursal){
        this.verificarCantidad(cantidad, false, producto);
        this.verificarSucursal(sucursal.getId());

        ArrayList<MovimientoStockDTO> movimientosDTO = new ArrayList<>();

        MovimientoStock movimientoStock = new MovimientoStock();
        movimientoStock.setProducto(producto);
        movimientoStock.setCantidad(cantidad);
        movimientoStock.setEsEgreso(false);
        movimientoStock.setSucursal(sucursal);
        MovimientoStock movimientoGuardado = repository.save(movimientoStock);

        producto.setStockActual(producto.getStockActual() + cantidad);
        productoRepository.save(producto);

        movimientosDTO.add(movimientoStockMapper.toDTO(movimientoGuardado));

        return movimientosDTO;
    }

    @Override
    @Transactional (rollbackFor = Exception.class)
    public ArrayList<MovimientoStockDTO> egreso (Producto producto, Integer cantidad){
        //Falta la logica de descontar el stock
        this.verificarCantidad(cantidad, false, producto);

        ArrayList<StockSucursal> stockSucursales = stockSucursalRepository.findByProductoIdOrderByStockDesc(producto.getId());
        ArrayList<MovimientoStockDTO> movimientosDTO = new ArrayList<>();

        for (StockSucursal stockSucursal : stockSucursales){
            if (cantidad <= 0){
                break;
            }
            if (stockSucursal.getStock() >= cantidad){

                MovimientoStock movimientoStock = new MovimientoStock();
                movimientoStock.setProducto(producto);
                movimientoStock.setCantidad(cantidad);
                movimientoStock.setEsEgreso(true);
                movimientoStock.setSucursal(stockSucursal.getSucursal());
                MovimientoStock movimientoGuardado = repository.save(movimientoStock);

                stockSucursal.setStock(stockSucursal.getStock() - cantidad);
                stockSucursalRepository.save(stockSucursal);

                producto.setStockActual(producto.getStockActual() - cantidad);
                System.out.println("Entro a nivel bajo");
                if (producto.getStockActual() < producto.getStockMinimo()){
                    System.out.println("Entro a nivel bajo");
                    this.mandarAlertaEmail("B",producto);
                } else if (producto.getStockActual() < producto.getStockMedio()){
                    System.out.println("Entro a nivel medio");
                    this.mandarAlertaEmail("M",producto);
                }
                productoRepository.save(producto);

                movimientosDTO.add(movimientoStockMapper.toDTO(movimientoGuardado));

                break;
            }else{

                MovimientoStock movimientoStock = new MovimientoStock();
                movimientoStock.setProducto(producto);
                movimientoStock.setCantidad(stockSucursal.getStock());
                movimientoStock.setEsEgreso(true);
                movimientoStock.setSucursal(stockSucursal.getSucursal());
                MovimientoStock movimientoGuardado = repository.save(movimientoStock);

                stockSucursal.setStock(0);
                stockSucursalRepository.save(stockSucursal);

                producto.setStockActual(producto.getStockActual() - stockSucursal.getStock());
                System.out.println("Entro a nivel bajo");
                if (producto.getStockActual() < producto.getStockMinimo()){
                    System.out.println("Entro a nivel bajo");
                    this.mandarAlertaEmail("B",producto);
                } else if (producto.getStockActual() < producto.getStockMedio()){
                    System.out.println("Entro a nivel medio");
                    this.mandarAlertaEmail("M",producto);
                }
                productoRepository.save(producto);

                cantidad = cantidad - stockSucursal.getStock();

                movimientosDTO.add(movimientoStockMapper.toDTO(movimientoGuardado));
            }
        }

        return movimientosDTO;
    }

    @Override
    @Transactional (rollbackFor = Exception.class)
    public void egresar (DetalleVenta detalleVenta){
        ArrayList<MovimientoStockDTO> movimientos = this.egreso(detalleVenta.getProducto(), detalleVenta.getCantidad());
    }

    public void verificarSucursal (Integer sucursalId){
        Optional<Sucursal> sucursal = sucursalRepository.findByIdAndFechaEliminacionIsNull(sucursalId);
        if (sucursal.isEmpty()){
            throw new BadRequestException(MessagesException.OBJECTO_INEXISTENTE);
        }
    }

    public Sucursal verificarSucursalYTraer (Integer sucursalId){
        Optional<Sucursal> sucursal = sucursalRepository.findByIdAndFechaEliminacionIsNull(sucursalId);
        if (sucursal.isEmpty()){
            throw new BadRequestException(MessagesException.OBJECTO_INEXISTENTE);
        }
        return sucursal.get();
    }

    public StockSucursal obtenerStockPorSucursal (Producto producto, Sucursal sucursal){
        Optional<StockSucursal> stock = stockSucursalRepository.findByProductoIdAndSucursalId (producto.getId(), sucursal.getId());
        if (stock.isEmpty()){
            StockSucursal stockSucursal = new StockSucursal();
            stockSucursal.setStock(0);
            stockSucursal.setSucursal(sucursal);
            stockSucursal.setProducto(producto);
            stockSucursalRepository.save(stockSucursal);
            return stockSucursal;
        }else{
            return stock.get();
        }
    }

    @Override
    @Transactional (rollbackFor = Exception.class)
    public MovimientoStockDTO guardar (Integer productoId, Integer cantidad, Boolean esEgreso){
        Producto producto = productoService.buscarPorId(productoId);
        this.verificarCantidad(cantidad, esEgreso, producto);
        MovimientoStock movimientoStock = new MovimientoStock();
        movimientoStock.setProducto(producto);
        movimientoStock.setCantidad(cantidad);
        movimientoStock.setEsEgreso(esEgreso);

        repository.save(movimientoStock);
        return this.movimientoStockMapper.toDTO(movimientoStock);
    };

    /*
    * Este guardar se usa unicamente cuando se crea un producto.
    * Dado un array de sucursales, se crean movimientos de stock para cada una de ellas
    * con un stock inicial de 0.
    * */
    @Override
    @Transactional (rollbackFor = Exception.class)
    public void guardar (Producto producto, Integer[] sucursales){
        for (Integer sucursalId : sucursales) {
            MovimientoStock movimientoStock = new MovimientoStock();
            movimientoStock.setProducto(producto);
            movimientoStock.setCantidad(0);
            movimientoStock.setEsEgreso(false);
            Optional<Sucursal> sucursal = sucursalRepository.findByIdAndFechaEliminacionIsNull(sucursalId);
            if (sucursal.isEmpty()){
                throw new BadRequestException(MessagesException.OBJECTO_INEXISTENTE);
            }
            movimientoStock.setSucursal(sucursal.get());
            repository.save(movimientoStock);

            StockSucursal stockSucursal = new StockSucursal();
            stockSucursal.setStock(0);
            stockSucursal.setSucursal(sucursal.get());
            stockSucursal.setProducto(producto);
            stockSucursalRepository.save(stockSucursal);
        }
    }

    @Override
    public List<MovimientoStockDTO> listarPorProducto(Integer productoId){
        Producto producto = productoService.buscarPorId(productoId);
        List<MovimientoStock> models = repository.findByProductoIdOrderByFechaCreacionDesc(producto.getId());
        if (models.isEmpty()){
            throw new NotFoundException(MessagesException.OBJECTO_NO_ENCONTRADO);
        }
        return models.stream().map(this.movimientoStockMapper::toDTO).toList();
    };

    @Override
    public Integer obtenerStockActual(Integer productoId){
        Producto producto = productoService.buscarPorId(productoId);

        List<MovimientoStock> models = repository.findByProductoIdOrderByFechaCreacionDesc(producto.getId());
        Integer stockActual = 0;
        for (MovimientoStock model : models){
            if (!model.getEsEgreso()){
                stockActual += model.getCantidad();
            } else {
                stockActual -= model.getCantidad();
            }
        }
        return stockActual;
    };

    @Override
    public Integer obtenerStockActual(Producto producto){
        List<MovimientoStock> models = repository.findByProductoIdOrderByFechaCreacionDesc(producto.getId());
        Integer stockActual = 0;
        for (MovimientoStock model : models){
            if (!model.getEsEgreso()){
                stockActual += model.getCantidad();
            } else {
                stockActual -= model.getCantidad();
            }
        }
        return stockActual;
    };

    @Override
    public void verificarCantidad(Integer cantidad, Boolean esEgreso, Producto producto){
        if (esEgreso != null){
            if (esEgreso){
                if (cantidad > producto.getStockActual()){
                    throw new BadRequestException(MessagesException.STOCK_INSUFICIENTE);
                }
            }
        }else {
            throw new BadRequestException("El campo esEgreso es obligatorio.");
        }
    };

    public void mandarAlertaEmail (String alerta, Producto producto){
        List<Usuario> usuarios = usuarioRepository.findByRolId(5);
        for (Usuario usuario: usuarios){
            this.enviarEmailAlerta(usuario.getEmail(),alerta, producto.getNombre(), producto.getStockActual());
        }
    }

    public void enviarEmailAlerta  (String email, String alerta, String producto, Integer stock) {
        try {
            String requestBody = getStringR(email, alerta , producto, stock);

            // Token de autorización (reemplaza "your-token" por el token real)
            String token = "48cd4db4cf2acbb1a532528b71fadb202efe8af8";

            // Realizar la solicitud POST para enviar el correo
            String response = String.valueOf(webClient.post()
                    .uri("/emailSender/enviar/")  // Especifica el endpoint correcto
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Token " + token)  // Agrega el token al encabezado
                    .bodyValue(requestBody)  // Cuerpo de la solicitud con los datos de email y código
                    .retrieve()
                    .toEntity(String.class)
                    .doOnTerminate(() -> {
                    })
                    .doOnError(error -> {
                    })
                    .block());  // Espera la respuesta sin bloquear el hilo principal

        } catch (Exception e) {
            throw new BadRequestException("Error al enviar el email: " + e.getMessage());
        }
    }
    private static String getStringR (String email, String alerta, String producto, Integer stock) {
        String emailHtmlTemplate = null;
        if (alerta.equals("B")){
            emailHtmlTemplate = EmailCodigoValidacion.stockCriticalAlertEmailTemplate;
        } else {
            emailHtmlTemplate = EmailCodigoValidacion.stockModerateAlertEmailTemplate;
        }

        // Reemplaza las variables en la plantilla HTML
        String emailContent = emailHtmlTemplate
                .replace("{LOGO_URL}", EmailCodigoValidacion.logoUrl)
                .replace("{NOMBRE_PRODUCTO}", producto)
                .replace("STOCK_ACTUAL",String.valueOf(stock));

        // Aquí escapamos las comillas dobles dentro del contenido HTML
        String escapedEmailContent = emailContent.replace("\"", "\\\"");

        // Aquí formateamos el JSON correctamente
        return String.format("""
        {
            "destinatario": "%s",
            "asunto": "Alerta de Stock",
            "cuerpo": "%s"
        }
        """, email, escapedEmailContent);
    }
}

