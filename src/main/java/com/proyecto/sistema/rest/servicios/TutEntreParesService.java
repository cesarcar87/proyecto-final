package com.proyecto.sistema.rest.servicios;


import com.proyecto.sistema.clases.sistema.TutEntrePar;
import com.proyecto.sistema.rest.repositorios.GetTutEntreParRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TutEntreParesService {

    @Autowired
    GetTutEntreParRespository getTutEntreParRespository;

    public TutEntrePar buscarTutEntrePares(Long idTutoria) {
        // Deberías retornar una lista de becas
        return getTutEntreParRespository.getById(idTutoria);
    }

    public List<TutEntrePar> buscarTodasLasTutorias() {
        // Deberías retornar una lista de becas
        return getTutEntreParRespository.findAll();
    }

}
