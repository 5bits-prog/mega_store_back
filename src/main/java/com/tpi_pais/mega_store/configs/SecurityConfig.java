package com.tpi_pais.mega_store.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuración de seguridad de la aplicación.
 */
@Configuration
@EnableAspectJAutoProxy
public class SecurityConfig {

    /**
     * Configura el filtro de seguridad, incluyendo reglas de autorización y CORS.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Desactiva CSRF (útil en desarrollo)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/products/**").permitAll() // Acceso público a productos
                        .requestMatchers("/auth/**").permitAll() // Acceso público a autenticación
                        .anyRequest().permitAll() // Acceso público a cualquier otra ruta
                )
                .cors(Customizer.withDefaults()); // Habilita CORS con la configuración definida

        return http.build();
    }

    /**
     * Configura CORS para permitir solicitudes desde cualquier origen.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));  // Permite cualquier origen
        configuration.setAllowedMethods(List.of("*"));  // Permite todos los métodos HTTP
        configuration.setAllowedHeaders(List.of("*"));  // Permite cualquier header
        configuration.setAllowCredentials(false);        // No se permiten credenciales

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // Aplica la configuración a todas las rutas
        return source;
    }

    /**
     * Configura el codificador de contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


