package com.proyecto.sistema.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
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
        config.addAllowedOrigin("http://localhost:3000"); // O especifica la URL de tu frontend
        config.addAllowedHeader("*"); // O especifica los headers permitidos
        config.addAllowedMethod("*"); // O especifica los métodos HTTP permitidos
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
