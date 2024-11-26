package com.proyecto.sistema;

import com.proyecto.sistema.clases.sistema.TutEntrePar;
import com.proyecto.sistema.rest.repositorios.GetTutEntreParRespository;
import jakarta.inject.Named;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Named
public class AgendaDatosYEstado  implements JavaDelegate {

    @Autowired
    private GetTutEntreParRespository getTutEntreParRespository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        TutEntrePar tutEntreParnew = new TutEntrePar();

        Long unidadCurricular = Long.valueOf((String) delegateExecution.getVariable("unidadCurricular"));

        String descripcionTutoria = (String) delegateExecution.getVariable("descripcionTutoria");

        // Obtener las variables de estudiante individuales
        Long estudiante1 = (Long) delegateExecution.getVariable("estudiante1");
        Long estudiante2 = (Long) delegateExecution.getVariable("estudiante2");
        Long estudiante3 = (Long) delegateExecution.getVariable("estudiante3");
        Long estudiante4 = (Long) delegateExecution.getVariable("estudiante4");

        List<Long> estudiantesTutorados = new ArrayList<>();

        estudiantesTutorados.add(estudiante1);
        estudiantesTutorados.add(estudiante2);
        estudiantesTutorados.add(estudiante3);
        estudiantesTutorados.add(estudiante4);

        tutEntreParnew.setEstudiantesTutorados(estudiantesTutorados);
        tutEntreParnew.setUnidadCurricularTutoria(unidadCurricular);
        tutEntreParnew.setEstadoTutoria("Esperando Tutor");
        tutEntreParnew.setDescripcionTutoria(descripcionTutoria);
        tutEntreParnew.setProcesoCamunda(delegateExecution.getProcessInstanceId());

        TutEntrePar tutEntreParCreada = getTutEntreParRespository.save(tutEntreParnew);

        Long tutEntreParId = tutEntreParCreada.getIdTutoria();

        delegateExecution.setVariable("idTutoriaEntrePar",tutEntreParId);
    }
}
