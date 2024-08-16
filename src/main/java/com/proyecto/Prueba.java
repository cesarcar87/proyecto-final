package com.proyecto;

import com.proyecto.PruebaService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Prueba {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Prueba.class, args);
        PruebaService pruebaService = context.getBean(PruebaService.class);
        pruebaService.runPrueba();
    }
}


/*
public class Prueba {
    public static void main(String... args) {
        String nombre = "Juan Pérez";
        String documento = "49237262"; // Documento de identidad válido en algunos países.
        String correo = "juan.perez@example.com";
        int telefono = 55512342; // Un número de teléfono válido con 10 dígitos.
        String otrasBecas = "Beca Académica";
        String carrera = "Ingeniería en Sistemas";
        String itr = "Centro Universitario"; // Nombre de un instituto o centro.
        String estadoBeca = "ingresada";
        String error = null; // Inicialmente, sin errores.

        GestEstRepository gestEstRepository;
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

        gestEstRepository.save(estPrueba);


        estadoBeca = validarDatos(nombre,documento,correo,telefono,otrasBecas,carrera,itr);

        System.out.println(estadoBeca);
        Validator validator = new Validator();
        System.out.println(validator.validateCi("12345678"));

        String mailDestino = "fede.lopez04pers@gmail.com"; String mailAsunto = "Prueba1"; String mailMensaje = "Hola mundo!";

        MandarMail mandarMail = new MandarMail();
        mandarMail.envioDeMail(mailDestino,mailAsunto,mailMensaje);

    }
}*/
