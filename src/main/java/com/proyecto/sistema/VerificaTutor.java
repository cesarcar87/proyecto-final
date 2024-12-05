package com.proyecto.sistema;

import com.proyecto.sistema.clases.sistema.TutEntrePar;
import com.proyecto.sistema.clases.usuarios.Estudiante;
import com.proyecto.sistema.rest.repositorios.GetEstRepository;
import com.proyecto.sistema.rest.repositorios.GetTutEntreParRespository;
import jakarta.inject.Named;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

@Named
public class VerificaTutor  implements JavaDelegate {

    @Autowired
    private GetTutEntreParRespository getTutEntreParRespository;

    @Autowired
    private GetEstRepository getEstRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        //El estudiante tutor debe ser avanzado en la carrera, mínimo segundo año (3er semestre)
        //Poner un límite de 4 tutorados por tutor a lo largo detodo el programa, es decir, un tutor no puede tener más de 4 estudiantes a cargo.

        Estudiante estTutor = getEstRepository.getReferenceById((Long) delegateExecution.getVariable("idEstudianteTutor"));
        TutEntrePar tutEntrePar =  getTutEntreParRespository.getReferenceById((Long) delegateExecution.getVariable("idTutoriaEntrePar"));

        Long semestreTutor = estTutor.getSemestre();

        Long cantidadTutorias = getTutEntreParRespository.countByEstudianteTutor(estTutor.getId());

        Boolean error = false;

        if (semestreTutor < 3) {
            delegateExecution.setVariable("estadoTutoria","TutorNoHabilitado");
            error = true;
            throw new IllegalArgumentException("El estudiante tutor debe estar al menos en el 3er semestre.");
        }

        if (cantidadTutorias >= 3) {
            delegateExecution.setVariable("estadoTutoria","TutorNoHabilitado");
            error = true;
            throw new IllegalArgumentException("El estudiante tutor no puede tener más de 4 tutorados a lo largo del programa.");
        }

        if (!error){
            tutEntrePar.setEstudianteTutor(estTutor.getId());
            tutEntrePar.setEstadoTutoria("tutorHabilitado");

            getTutEntreParRespository.save(tutEntrePar);

            delegateExecution.setVariable("estadoTutoria","tutorHabilitado");
        }

    }
}
