package com.proyecto.sistema;

import com.proyecto.sistema.clases.sistema.TutEntrePar;
import com.proyecto.sistema.rest.servicios.TutEntreParesService;
import jakarta.inject.Named;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Named
public class AgendaDatosYEstado implements JavaDelegate {

    @Autowired
    TutEntreParesService tutEntreParesService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        // Obtener el ID de la instancia del proceso
        String processInstanceId = delegateExecution.getProcessInstanceId();
        System.out.println("Process Instance ID: " + processInstanceId);

        // Obtener las variables de estudiante individuales
        List<Long> estudiantes = new java.util.ArrayList<>(List.of());

        // Convertir explícitamente las variables a Long
        Long estudiante1 = (Long) delegateExecution.getVariable("estudiante1");
        Long estudiante2 = (Long) delegateExecution.getVariable("estudiante2");
        Long estudiante3 = (Long) delegateExecution.getVariable("estudiante3");
        Long estudiante4 = (Long) delegateExecution.getVariable("estudiante4");

        Long UnidadCurricular = Long.valueOf((String) delegateExecution.getVariable("unidadCurricular"));
        String descripcionTutoria = (String) delegateExecution.getVariable("descripcionTutoria");

        estudiantes.add(estudiante1);
        estudiantes.add(estudiante2);
        estudiantes.add(estudiante3);
        estudiantes.add(estudiante4);

        TutEntrePar tutEntreParnew = new TutEntrePar();
        tutEntreParnew.setEstudiantesTutorados(estudiantes);
        tutEntreParnew.setDescripcionTutoria(descripcionTutoria);
        tutEntreParnew.setUnidadCurricularTutoria(UnidadCurricular);

        // Asociar el ID del proceso con el objeto si es necesario
        tutEntreParnew.setProcesoCamunda(processInstanceId);

        // Guardar la tutoría y obtener el ID autogenerado
        TutEntrePar tutEntreParSaved = tutEntreParesService.crearTutEntrePares(tutEntreParnew);

        // Obtener el ID generado
        Long idGenerado = tutEntreParSaved.getIdTutoria();

        System.out.println("ID generado para la tutoría: " + idGenerado);

        // Si necesitas asociarlo con alguna variable del proceso
        delegateExecution.setVariable("idTutorEntrePar", idGenerado);
    }
}
