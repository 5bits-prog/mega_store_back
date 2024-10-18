package com.tpi_pais.mega_store.auth.controller.RolController;

import com.tpi_pais.mega_store.auth.dto.RolDTO;
import com.tpi_pais.mega_store.auth.mapper.RolMapper;
import com.tpi_pais.mega_store.auth.service.IRolService;
import com.tpi_pais.mega_store.auth.model.Rol;

import com.tpi_pais.mega_store.exception.BadRequestException;
import com.tpi_pais.mega_store.exception.ResponseService;
import com.tpi_pais.mega_store.utils.ApiResponse;
import com.tpi_pais.mega_store.utils.ExpresionesRegulares;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
public class PutRolController {
    @Autowired
    private IRolService modelService;

    @Autowired
    private ResponseService responseService;

    @PutMapping("/rol")
    public ResponseEntity<?> actualizar(@RequestBody RolDTO model){
        /*
         * Validaciones:
         * 1) Que se haya enviado un RolDTO
         *   En caso que falle se ejecuta el @ExceptionHandler
         * 2) Que el dto enviado tenga un id distinto de null o 0
         *   En caso que falle se retorna una badrequest
         * 3) Que el id enviado corresponda a un objeto Rol y que el mismo no este eliminado
         *   En caso que falle se retorna una badrequest
         * 4) Que el dto enviado tenga un nombre distinto de null o ""
         *   En caso que falle se retorna una badrequest
         * 5) En caso de que contenga un nombre verifico si coincide con la expresion regular determinada.
         *   Las condiciones son:
         *   - Debe estar formado solo por letras y/o espacios.
         *   - Puede contener espacios, pero solo entre las palabras, no al principio ni al final.
         *   - Puede contener 1 y solo 1 espacio entre 2 palabras.
         * Una vez pasado esto se debe capitalizar el nombre para estandarizar todas las rols.
         * 6) Que el nuevo nombre no este registrado en otro objeto Rol
         *   En caso que falle se retorna una badrequest
         * */
        Rol rolModificar = modelService.buscarPorId(model.getId());
        RolDTO modelDTO = modelService.verificarAtributos(model);
        if (modelService.rolExistente(modelDTO.getNombre())){
            throw new BadRequestException("Ya existe un rol con ese nombre");
        } else {
            RolDTO modelGuardado = modelService.guardar(model);
            return responseService.successResponse(modelGuardado, "Rol actualiazado");
        }
    }
    @PutMapping("/rol/recuperar/{id}")
    public ResponseEntity<?> recuperar(@PathVariable Integer id) {
        /*
         * Validaciones:
         * 1) Que el id se haya enviado.
         *   En caso que falle se ejecuta el @ExceptionHandler
         * 2) Que el id sea un entero.
         *   En caso que falle se ejecuta el @ExceptionHandler
         * 3) Que exista una rol con dicho id.
         *   Se realiza la busqueda del obj y si el mismo retorna null se devuelve el badrequest
         * 4) Que la rol encontrada este eliminada.
         *   Si se encuentra la rol, y la misma no esta elimianda se retorna un badrequest.
         * En caso de que pase todas las verificacioens se cambia el la fechaEliminacion por el valor null.
         * */
        Rol model = modelService.buscarEliminadoPorId(id);
        modelService.recuperar(model);
        return responseService.successResponse(model, "Rol recuperado");
    }
}
