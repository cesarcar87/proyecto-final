package com.proyecto.sistema.rest.servicios;

import com.proyecto.sistema.clases.sistema.Becas;
import com.proyecto.sistema.clases.usuarios.Usuario;
import com.proyecto.sistema.rest.repositorios.GetBecRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class BecaService {

    @Autowired
    private GetBecRespository getBecRespository;

    // Obtener todas las becas
    @GetMapping
    public List<Becas> obtenerTodasLasBecas() {
        return getBecRespository.findAll();
    }

    @GetMapping
    public List<Becas> obtenerBecaPorEstudiante(Long estudiante) {
        return (List<Becas>) getBecRespository.findByEstudiante(estudiante);
    }

    // Crear una nueva Beca
    @PostMapping
    public Becas crearBeca(@RequestBody Becas beca) {
        return getBecRespository.save(beca);
    }

}
