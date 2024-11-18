package com.proyecto.sistema;

import com.proyecto.sistema.rest.repositorios.GetTutEntreParRespository;
import com.proyecto.sistema.rest.repositorios.GetUCRepository;
import jakarta.inject.Named;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;

@Named
public class VerificarEstudianteTut implements JavaDelegate {

    @Autowired
    private GetTutEntreParRespository getTutEntreParRespository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        // Obtener las variables de estudiante individuales
        Long estudiante1 = (Long) delegateExecution.getVariable("estudiante1");
        Long estudiante2 = (Long) delegateExecution.getVariable("estudiante2");
        Long estudiante3 = (Long) delegateExecution.getVariable("estudiante3");
        Long estudiante4 = (Long) delegateExecution.getVariable("estudiante4");

        try {
            // Verificar el número de tutorías para cada estudiante
            verificarTutorias(estudiante1);
            verificarTutorias(estudiante2);
            verificarTutorias(estudiante3);
            verificarTutorias(estudiante4);

            // Si no hay error en la verificación, establecer "Acepta"
            delegateExecution.setVariable("estadoTutoria", "Acepta");
        } catch (Exception e) {
            // Si ocurre un error (más de 4 tutorías), establecer "NoPermite"
            delegateExecution.setVariable("estadoTutoria", "NoPermite");
            throw e; // Re-lanzar la excepción para que el flujo de Camunda se detenga si es necesario
        }
    }

    // Método para verificar el número de tutorías de un estudiante
    private void verificarTutorias(Long estudianteId) throws Exception {
        System.out.println("ID del estudiante: " + estudianteId);

        // Verificar el número de tutorías
        Long cantidadTutorias = getTutEntreParRespository.countByEstudianteTutorados(estudianteId);

        System.out.println("Cantidad de tutorias: " + cantidadTutorias);
        // Lanza una excepción si el estudiante tiene más de 4 tutorías
        if (cantidadTutorias > 3) {
            throw new Exception("El estudiante con ID " + estudianteId + " tiene más de 4 tutorías asignadas.");
        }
    }
}
