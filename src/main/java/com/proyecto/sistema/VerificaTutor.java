package com.proyecto.sistema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.sistema.clases.sistema.TutEntrePar;
import com.proyecto.sistema.clases.sistema.UnidadesCurriculares;
import com.proyecto.sistema.clases.usuarios.Estudiante;
import com.proyecto.sistema.rest.TutEntreParesRest;
import com.proyecto.sistema.rest.repositorios.GetEstRepository;
import com.proyecto.sistema.rest.repositorios.GetTutEntreParRespository;
import com.proyecto.sistema.rest.repositorios.GetUCRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import jakarta.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Named
public class VerificaTutor  implements JavaDelegate {

    @Autowired
    private GetTutEntreParRespository getTutEntreParRespository;

    @Autowired
    private GetEstRepository getEstRepository;

    @Autowired
    private GetUCRepository getUCRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        Long idTutEntrePar = (Long) delegateExecution.getVariable("idTutorEntrePar");
        Long idEstudianteTutor = (Long) delegateExecution.getVariable("idEstudianteTutor");

        System.out.println("idTutEntrePar "+ idTutEntrePar);
        System.out.println("idEstudianteTutor "+ idEstudianteTutor);

        Long cantidadTutorias = getTutEntreParRespository.countByEstudianteTutor(idEstudianteTutor);
        TutEntrePar tutEntrePar = getTutEntreParRespository.getReferenceById(idTutEntrePar);

        Optional<Estudiante> estudianteTutor = getEstRepository.findById(idEstudianteTutor);

        if (estudianteTutor.isPresent()) {
            Estudiante estudiante = estudianteTutor.get();

            Optional<UnidadesCurriculares> unidadCurricular = getUCRepository.findById(tutEntrePar.getUnidadCurricularTutoria());
            // Usa el objeto estudiante aquí

            System.out.println("Es tutor? " + estudiante.getEsTutor());

            if (Boolean.TRUE.equals(estudiante.getEsTutor())) {

                if (unidadCurricular.isPresent()) {

                    UnidadesCurriculares unidadesCurriculares = unidadCurricular.get();

                    if (cantidadTutorias > 4) {
                        System.out.println("Llega Aqui");

                        delegateExecution.setVariable("estadoTutoria", "TutorNoHabilitado");
                        throw new Exception("El estudiante con ID " + estudiante.getNombre() + " " + estudiante.getApellido() + " tiene más de 4 tutorías asignadas.");
                    } else {
                        System.out.println("Llega Aqui 2");
                        delegateExecution.setVariable("estadoTutoria", "tutorHabilitado");
                    }

                    if (estudiante.getSemestre() < unidadesCurriculares.getSemestre()) {
                        System.out.println("Llega Aqui3");
                        delegateExecution.setVariable("estadoTutoria", "TutorNoHabilitado");
                        throw new Exception("El estudiante debe ser de un semestre superior a la Unidad Curricular.");
                    } else {
                        System.out.println("Llega Aqui 4");
                        delegateExecution.setVariable("estadoTutoria", "tutorHabilitado");
                        tutEntrePar.setEstudianteTutor(idEstudianteTutor);
                    }
                }
            } else {
                // Maneja el caso donde no se encontró el estudiante
                //En este caso, como vamos a utilizar un estudiante que ya esta con sesion iniciada, no debería de ser un problema nunca.
            }
        } else {
            delegateExecution.setVariable("estadoTutoria", "TutorNoHabilitado");
            throw new Exception("El estudiante no esta habilitado para ser tutor.");
        }

    }
}
