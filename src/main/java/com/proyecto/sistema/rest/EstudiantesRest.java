package com.proyecto.sistema.rest;

import com.proyecto.clases.repositorios.GestEstRepository;
import com.proyecto.clases.usuarios.Estudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/estudiantes")
public class EstudiantesRest {

    @Autowired
    private GestEstRepository estudianteRepository;

    // Obtener todos los estudiantes
    @GetMapping
    public List<Estudiante> obtenerTodosLosEstudiantes() {
        return estudianteRepository.findAll();
    }

    // Crear un nuevo estudiante
    @PostMapping
    public Estudiante crearEstudiante(@RequestBody Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }

}
