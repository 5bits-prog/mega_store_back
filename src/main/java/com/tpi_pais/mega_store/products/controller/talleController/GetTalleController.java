package com.tpi_pais.mega_store.products.controller.talleController;

import com.tpi_pais.mega_store.exception.BadRequestException;
import com.tpi_pais.mega_store.exception.ResponseService;
import com.tpi_pais.mega_store.products.dto.TalleDTO;
import com.tpi_pais.mega_store.products.mapper.TalleMapper;
import com.tpi_pais.mega_store.products.model.Talle;
import com.tpi_pais.mega_store.products.service.ITalleService;
import com.tpi_pais.mega_store.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class GetTalleController {

    private final ITalleService modelService;

    private final ResponseService responseService;

    public GetTalleController(ITalleService modelService, ResponseService responseService) {
        this.modelService = modelService;
        this.responseService = responseService;
    }

    @GetMapping({"/talles"})
    public ResponseEntity<ApiResponse<Object>>  getAll() {
        List<TalleDTO> talles = modelService.listar();
        if (talles.isEmpty()) {
            throw new BadRequestException("No hay talles creados");
        }
        return responseService.successResponse(talles, "OK");
    }

    @GetMapping("/talle/{id}")
    public ResponseEntity<ApiResponse<Object>>  getPorId(@PathVariable Integer id){
        Talle model = modelService.buscarPorId(id);
        TalleDTO modelDTO = TalleMapper.toDTO(model);
        return responseService.successResponse(modelDTO, "OK");
    }
}
