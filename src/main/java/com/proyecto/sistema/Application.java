package com.proyecto.sistema;
import com.proyecto.servicios.GoogleMailApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import com.proyecto.sistema.clases.usuarios.Estudiante;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@ComponentScan(basePackages = "com.proyecto.sistema")
public class Application {

  public static void main(String... args) {
    //SpringApplication.run(Application.class, args);
    // Inicia la aplicación Spring Boot y obtiene el contexto de la aplicación
    ApplicationContext context = SpringApplication.run(Application.class, args);
  }

}