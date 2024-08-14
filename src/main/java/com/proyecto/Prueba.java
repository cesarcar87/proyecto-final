package com.proyecto;
import com.fabdelgado.ciuy.*;
import static com.proyecto.estudiante.ValidarDatos.validarDatos;

import com.proyecto.sistema.MandarMail;
import com.proyecto.sistema.MandarMail.*;


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


        estadoBeca = validarDatos(nombre,documento,correo,telefono,otrasBecas,carrera,itr);

        System.out.println(estadoBeca);
        Validator validator = new Validator();
        System.out.println(validator.validateCi("12345678"));

        String mailDestino = "fede.lopez04pers@gmail.com"; String mailAsunto = "Prueba1"; String mailMensaje = "Hola mundo!";

        MandarMail mandarMail = new MandarMail();
        mandarMail.envioDeMail(mailDestino,mailAsunto,mailMensaje);

    }
}
