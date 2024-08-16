package com.proyecto;

import com.proyecto.clases.usuarios.Estudiante;
import com.proyecto.sistema.rest.EstudiantesRest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

  public static void main(String... args) {
    //SpringApplication.run(Application.class, args);
    // Inicia la aplicación Spring Boot y obtiene el contexto de la aplicación
    ApplicationContext context = SpringApplication.run(Application.class, args);

    // Obtiene el bean del controlador REST desde el contexto
    EstudiantesRest estudiantesRest = context.getBean(EstudiantesRest.class);

    // Crea un estudiante de prueba
    Estudiante estPrueba = new Estudiante();
    estPrueba.setUsuario("jdoe123");
    estPrueba.setNombre("John");
    estPrueba.setApellido("Doe");
    estPrueba.setTipoDocumento("DNI");
    estPrueba.setDocumento("12345678");
    estPrueba.setCorreo("johndoe@example.com");
    estPrueba.setPais("Argentina");
    estPrueba.setLocalidad("Buenos Aires");
    estPrueba.setCarrera("Ingeniería en Sistemas");
    estPrueba.setItr("Central");

    // Llama al método REST para crear el estudiante
    Estudiante estudianteGuardado = estudiantesRest.crearEstudiante(estPrueba);

    // Imprime el resultado (opcional)
    System.out.println("Estudiante creado con ID: " + estudianteGuardado.getIdEstudiante());
  }



}