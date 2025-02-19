package com.tpi_pais.mega_store.auth.dto;

import lombok.Data;

@Data
public class RecuperacionContrasenaDTO {
    private String email;
    private String codigoRecuperacion;
    private String password;
}
