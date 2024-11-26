package com.proyecto.sistema.rest.servicios;

import com.proyecto.sistema.clases.sistema.UnidadCurricular;
import com.proyecto.sistema.rest.repositorios.GetUcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnidadCurricularService {

    @Autowired
    private GetUcRepository getUcRepository;

    // Método para obtener todas las unidades curriculares
    public List<UnidadCurricular> obtenerTodasLasUnidadesCurriculares() {
        return getUcRepository.findAll(); // Devuelve todas las unidades curriculares
    }
}
