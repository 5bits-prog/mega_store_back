package com.tpi_pais.mega_store.configs;

import com.tpi_pais.mega_store.exception.BadRequestException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
@Service
public class ImageBBService {

    private static final String API_URL = "https://api.imgbb.com/1/upload";
    private static final String API_KEY = "c01631214212f30966500c874875dcc8";

    public String subirImagen(MultipartFile archivo) {
        validarArchivo(archivo);

        String base64Imagen = convertirImagenABase64(archivo);
        String urlConClave = construirUrlConClave();

        return enviarImagenAImgBB(base64Imagen, urlConClave);
    }

    private void validarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new BadRequestException("El archivo está vacío o es nulo.");
        }
    }

    private String convertirImagenABase64(MultipartFile archivo) {
        try {
            return java.util.Base64.getEncoder().encodeToString(archivo.getBytes());
        } catch (Exception e) {
            throw new BadRequestException("Error al convertir la imagen a Base64: " + e.getMessage());
        }
    }

    private String construirUrlConClave() {
        return API_URL + "?key=" + API_KEY;
    }

    private String enviarImagenAImgBB(String base64Imagen, String urlConClave) {
        RestTemplate restTemplate = new RestTemplate();

        // Crear el cuerpo de la solicitud
        MultiValueMap<String, String> cuerpoSolicitud = new LinkedMultiValueMap<>();
        cuerpoSolicitud.add("image", base64Imagen);

        // Configurar los encabezados
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Crear la entidad HTTP
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(cuerpoSolicitud, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    urlConClave,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            // Extraer la URL de la imagen desde la respuesta
            Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
            return data.get("display_url").toString();
        } catch (Exception e) {
            throw new BadRequestException("Error al subir la imagen a ImgBB: " + e.getMessage());
        }
    }
}

