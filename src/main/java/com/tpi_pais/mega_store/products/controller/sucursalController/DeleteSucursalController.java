package com.tpi_pais.mega_store.products.controller.sucursalController;

import com.tpi_pais.mega_store.exception.ResponseService;
import com.tpi_pais.mega_store.products.model.Sucursal;
import com.tpi_pais.mega_store.products.service.ISucursalService;
import com.tpi_pais.mega_store.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class DeleteSucursalController {
    @Autowired
    private ISucursalService modelService;
    @Autowired
    private ResponseService responseService;

    @DeleteMapping("/sucursal/{id}")
    public ResponseEntity<ApiResponse<Object>>  eliminar(@PathVariable Integer id) {
            Sucursal model = modelService.buscarPorId(id);
            modelService.eliminar(model);
            return responseService.successResponse(model, "Objeto eliminado");
    }
}