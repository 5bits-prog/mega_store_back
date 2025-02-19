package com.tpi_pais.mega_store.auth.controller.AuthController;

import com.tpi_pais.mega_store.auth.dto.RecuperacionContrasenaDTO;
import com.tpi_pais.mega_store.auth.service.IUsuarioService;
import com.tpi_pais.mega_store.exception.BadRequestException;
import com.tpi_pais.mega_store.exception.ResponseService;
import com.tpi_pais.mega_store.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class PasswordRecoveryController {

    private final IUsuarioService modelService;

    private final ResponseService responseService;

    public PasswordRecoveryController(
            IUsuarioService modelService,
            ResponseService responseService) {
        this.modelService = modelService;
        this.responseService = responseService;
    }

    @PostMapping("/obtener-codigo-password")
    public ResponseEntity<ApiResponse<Object>> obtenerCodigoRecuperacion(
            @RequestBody RecuperacionContrasenaDTO recuperacionContrasenaDTO) {
        modelService.recuperarContrasena(recuperacionContrasenaDTO.getEmail());
        return responseService.successResponse(null, "Se ha enviado el codigo de recuperacion");
    }

    @PostMapping("/verificar-codigo-password")
    public ResponseEntity<ApiResponse<Object>> verificarCodigoRecuperacion(
            @RequestBody RecuperacionContrasenaDTO recuperacionContrasenaDTO) {
        if (modelService.validarCodigoRecuperacion(recuperacionContrasenaDTO.getEmail(), recuperacionContrasenaDTO.getCodigoRecuperacion())) {
            return responseService.successResponse(null, "Se ha verificado el codigo de recuperacion");
        }else {
            throw new BadRequestException("El codigo de recuperacion es incorrecto.");
        }
    }

    @PostMapping("/restablecer-password")
    public ResponseEntity<ApiResponse<Object>> restablecerPassword(
            @RequestBody RecuperacionContrasenaDTO recuperacionContrasenaDTO) {
        modelService.restablecerContrasena(recuperacionContrasenaDTO.getEmail(), recuperacionContrasenaDTO.getPassword(), recuperacionContrasenaDTO.getCodigoRecuperacion());
        return responseService.successResponse(null, "Se ha restablecido la contrasena");
    }
}
