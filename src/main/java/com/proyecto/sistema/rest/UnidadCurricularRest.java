package com.proyecto.sistema.rest;

import com.proyecto.sistema.clases.sistema.UnidadCurricular;

import com.proyecto.sistema.rest.servicios.UnidadCurricularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/unidadesCurriculares")
public class UnidadCurricularRest {

    @Autowired
    private UnidadCurricularService unidadCurricularService;

    // Endpoint para obtener todas las unidades curriculares
    @GetMapping("/listarUnidadesCurriculares")
    public List<UnidadCurricular> listarUnidadesCurriculares() {
        return unidadCurricularService.obtenerTodasLasUnidadesCurriculares();
    }
}