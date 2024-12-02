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

        tutEntreParnew.setEstudiantesTutorados(estudiante1);
        tutEntreParnew.setUnidadCurricularTutoria(unidadCurricular);
        tutEntreParnew.setEstadoTutoria("Esperando Tutor");
        tutEntreParnew.setDescripcionTutoria(descripcionTutoria);
        tutEntreParnew.setProcesoCamunda(delegateExecution.getProcessInstanceId());

        TutEntrePar tutEntreParCreada = getTutEntreParRespository.save(tutEntreParnew);

        Long tutEntreParId = tutEntreParCreada.getIdTutoria();

        delegateExecution.setVariable("idTutoriaEntrePar",tutEntreParId);
    }
}
