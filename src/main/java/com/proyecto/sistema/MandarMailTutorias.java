package com.proyecto.sistema;

import com.proyecto.servicios.GoogleMailApi;
import com.proyecto.sistema.clases.usuarios.Usuario;
import com.proyecto.sistema.rest.repositorios.GetUsuRepository;
import jakarta.inject.Named;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;


@Named
public class MandarMailTutorias implements JavaDelegate {

    @Autowired
    GoogleMailApi apiDeMails;

    @Autowired
    GetUsuRepository getUsuRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String estado = "";
        String newEstadoBeca = "";
        String asunto = "Tutorias Entre Pares";
        String cuerpoMensaje = "La tutoría entre Par fue aceptada por el equipo de coordinacion estudiantil";

        System.out.println("Se mandan mails");

        Usuario estudianteTutor = getUsuRepository.getReferenceById((Long) delegateExecution.getVariable("idEstudianteTutor"));

        //String estudiante = (String) delegateExecution.getVariable("correoEstudiante");
        String estudiante = estudianteTutor.getCorreo();

        apiDeMails.enviarCorreo(estudiante,asunto,cuerpoMensaje);
        delegateExecution.setVariable("estadoBeca",newEstadoBeca);
    }
}
