package com.proyecto.sistema.rest.servicios;

import com.proyecto.sistema.clases.sistema.Becas;
import com.proyecto.sistema.clases.sistema.TutEntrePar;
import com.proyecto.sistema.rest.repositorios.GetTutEntreParRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
public class TutEntreParesService {

    @Autowired
    private GetTutEntreParRespository getTutEntreParRespository;


    // Obtener todas las Tutorias
    public List<TutEntrePar> obtenerTodasLasTutorias() {
        return getTutEntreParRespository.findAll();
    }

//    // Obtener todas las Tutorias
//    public List<TutEntrePar> obtenerTutoriaPorEstado() {
//        return getTutEntreParRespository.findByestadoTutoria();
//    }

    // Crear una nueva Tutoria Entre Pares
    public TutEntrePar crearTutEntrePares(TutEntrePar tutEntrePar) {
        return getTutEntreParRespository.save(tutEntrePar);
    }

    // Modificar una tutoría existente entre pares
    public TutEntrePar modificarTutEntrePares(Long idTutoria, TutEntrePar tutEntrePar) {
        return getTutEntreParRespository.findById(idTutoria).map(tutExistente -> {
            tutExistente.setEstadoTutoria(tutEntrePar.getEstadoTutoria());
            tutExistente.setIdTutoria(tutEntrePar.getIdTutoria());
            tutExistente.setEstudiantesTutorados(tutEntrePar.getEstudiantesTutorados());
            tutExistente.setEstudianteTutor(tutEntrePar.getEstudianteTutor());
            tutExistente.setUnidadCurricularTutoria(tutEntrePar.getUnidadCurricularTutoria());
            tutExistente.setDescripcionTutoria(tutEntrePar.getDescripcionTutoria());
            tutExistente.setProcesoCamunda(tutEntrePar.getProcesoCamunda());
            return getTutEntreParRespository.save(tutExistente);
        }).orElseThrow(() -> new RuntimeException("No se encontró la tutoría con el ID: " + idTutoria));
    }

    // Buscar una tutoría entre pares por ID
    public TutEntrePar buscarTutEntrePares(Long tutEntreParId) {
        return getTutEntreParRespository.findById(tutEntreParId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la tutoría entre pares con el ID: " + tutEntreParId));
    }

}
