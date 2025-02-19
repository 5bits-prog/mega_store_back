package com.tpi_pais.mega_store.products.controller.marcaController;

import com.tpi_pais.mega_store.configs.SessionRequired;
import com.tpi_pais.mega_store.exception.BadRequestException;
import com.tpi_pais.mega_store.exception.ResponseService;
import com.tpi_pais.mega_store.products.model.Marca;
import com.tpi_pais.mega_store.products.service.IMarcaService;
import com.tpi_pais.mega_store.products.service.IProductoService;
import com.tpi_pais.mega_store.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class DeleteMarcaController {

    private final IMarcaService modelService;

    private final ResponseService responseService;

    private final IProductoService productoService;

    public DeleteMarcaController(IMarcaService modelService, ResponseService responseService, IProductoService productoService) {
        this.modelService = modelService;
        this.responseService = responseService;
        this.productoService = productoService;
    }

    @SessionRequired
    @DeleteMapping("/marca/{id}")
    public ResponseEntity<ApiResponse<Object>>  eliminar(@PathVariable Integer id) {
        Marca model = modelService.buscarPorId(id);
        if (productoService.tieneProductoAsociado(model.getId(), "marca")) {
            throw new BadRequestException("La marca tiene productos asociados, no se puede eliminar");
        }
        modelService.eliminar(model);
        return responseService.successResponse(model, "Objeto eliminado");
    }
}
