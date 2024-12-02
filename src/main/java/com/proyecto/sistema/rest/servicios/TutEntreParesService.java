package com.proyecto.sistema.rest.servicios;


import com.proyecto.sistema.clases.sistema.TutEntrePar;
import com.proyecto.sistema.clases.sistema.UnidadCurricular;
import com.proyecto.sistema.rest.repositorios.GetTutEntreParRespository;
import com.proyecto.sistema.rest.repositorios.GetUcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TutEntreParesService {

    @Autowired
    GetTutEntreParRespository getTutEntreParRespository;

    @Autowired
    GetUcRepository getUcRepository;


    public TutEntrePar buscarTutEntrePares(Long idTutoria) {
        // Deberías retornar una lista de becas
        return getTutEntreParRespository.getById(idTutoria);
    }

    public List<TutEntrePar> buscarTodasLasTutorias() {
        // Deberías retornar una lista de becas
        return getTutEntreParRespository.findAll();
    }

    public UnidadCurricular obtgenerUC(Long idUc){
        return getUcRepository.getReferenceById(idUc);
    }
}
