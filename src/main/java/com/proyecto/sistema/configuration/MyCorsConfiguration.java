package com.proyecto.sistema.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class MyCorsConfiguration {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("https://gestionestudiantilfms.com"); // Dominio del frontend en producción
        config.addAllowedOrigin("http://localhost:3000"); // Permitir localhost para desarrollo
        config.addAllowedHeader("*"); // Permite todos los headers
        config.addAllowedMethod("*"); // Permite todos los métodos HTTP (GET, POST, etc.)
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public FilterRegistrationBean<CoopFilter> coopFilter() {
        FilterRegistrationBean<CoopFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CoopFilter());
        registrationBean.addUrlPatterns("/*"); // Aplicar a todas las rutas
        return registrationBean;
    }
}
