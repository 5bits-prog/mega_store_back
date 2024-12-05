package com.tpi_pais.mega_store.products.controller.productoController;

import com.tpi_pais.mega_store.exception.ResponseService;
import com.tpi_pais.mega_store.products.model.Producto;
import com.tpi_pais.mega_store.products.service.IProductoService;
import com.tpi_pais.mega_store.products.service.ProductoService;
import com.tpi_pais.mega_store.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class DeleteProductoController {

    private final IProductoService modelService; // No debe ser estático

    private final ResponseService responseService;

    public DeleteProductoController(IProductoService modelService, ResponseService responseService) {
        this.modelService = modelService;
        this.responseService = responseService;
    }

    @DeleteMapping("/producto/{id}")
    public ResponseEntity<ApiResponse<Object>>  eliminar(@PathVariable Integer id) {
        Producto model = modelService.buscarPorId(id);

        modelService.eliminar(model,"Usuario que lo realizo");
        return responseService.successResponse(model, "Producto eliminado");
    }
}