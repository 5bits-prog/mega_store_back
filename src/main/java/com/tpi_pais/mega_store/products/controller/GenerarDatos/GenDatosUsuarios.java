package com.tpi_pais.mega_store.products.controller.GenerarDatos;

import com.github.javafaker.Faker;
import com.tpi_pais.mega_store.auth.dto.UsuarioDTO;
import com.tpi_pais.mega_store.auth.model.Usuario;
import com.tpi_pais.mega_store.auth.service.IUsuarioService;
import com.tpi_pais.mega_store.configs.SessionRequired;
import com.tpi_pais.mega_store.exception.ResponseService;
import com.tpi_pais.mega_store.utils.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/generador_datos")
public class GenDatosUsuarios {

    private IUsuarioService usuarioService;
    private ResponseService responseService;

    public GenDatosUsuarios(IUsuarioService usuarioService, ResponseService response) {
        this.usuarioService = usuarioService;
        this.responseService = response;
    }


    @SessionRequired
    @PostMapping("/usuarios")
    public ResponseEntity<ApiResponse<Object>> guardar () throws ParseException {
        Faker faker = new Faker();

        while (this.usuarioService.listar().size() < 500) {
            UsuarioDTO usuarioDTO = new UsuarioDTO();

            usuarioDTO.setNombre(faker.name().fullName());
            usuarioDTO.setEmail(faker.internet().emailAddress());
            usuarioDTO.setTelefono(faker.phoneNumber().cellPhone());
            usuarioDTO.setDireccionEnvio(faker.address().streetAddress());
            usuarioDTO.setPassword(faker.internet().password());
            usuarioDTO.setRolId(4);

            Usuario usuario = this.usuarioService.crearUsuario(usuarioDTO);

            usuarioService.verificarCodigoVerificacion(usuario.getEmail(), usuario.getCodigoVerificacion());

            this.usuarioService.guardar(usuario);
        }

        return responseService.successResponse(null, "Usuarios creados");
    }

}
