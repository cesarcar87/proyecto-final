package com.proyecto;

import com.proyecto.clases.usuarios.Estudiante;
import com.proyecto.sistema.rest.EstudiantesRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PruebaService {

    @Autowired
    private EstudiantesRest estudiantesRest;

    public void runPrueba() {
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

        estudiantesRest.crearEstudiante(estPrueba);
    }
}
