package com.tpi_pais.mega_store.products.controller.categoriaController;

import com.tpi_pais.mega_store.configs.SessionRequired;
import com.tpi_pais.mega_store.exception.BadRequestException;
import com.tpi_pais.mega_store.exception.ResponseService;
import com.tpi_pais.mega_store.products.model.Categoria;
import com.tpi_pais.mega_store.products.service.ICategoriaService;
import com.tpi_pais.mega_store.products.service.IProductoService;
import com.tpi_pais.mega_store.products.service.ProductoService;
import com.tpi_pais.mega_store.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class DeleteCategoriaController {

    private final ICategoriaService modelService;

    private final ResponseService responseService;

    private final IProductoService productoService;

    public DeleteCategoriaController(ICategoriaService modelService, ResponseService responseService, IProductoService productoService) {
        this.modelService = modelService;
        this.responseService = responseService;
        this.productoService = productoService;
    }

    @SessionRequired
    @DeleteMapping("/categoria/{id}")
    public ResponseEntity<ApiResponse<Object>>  eliminar(@PathVariable Integer id) {
        Categoria model = modelService.buscarPorId(id);
        Boolean valor = productoService.tieneProductoAsociado(model.getId(), "categoria");
        if (valor) {
            throw new BadRequestException("La categoria tiene productos asociados, no se puede eliminar");
        }
        modelService.eliminar(model);
        return responseService.successResponse(model, "Objeto eliminado");
    }
}
