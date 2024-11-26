package com.proyecto.sistema;

import com.proyecto.servicios.GoogleMailApi;
import com.proyecto.sistema.clases.usuarios.Usuario;
import com.proyecto.sistema.rest.repositorios.GetUsuRepository;
import jakarta.inject.Named;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;


@Named
public class MandarMailSolEquipos implements JavaDelegate {

    @Autowired
    private GoogleMailApi apiDeMails;

    @Autowired
    GetUsuRepository getUsuRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String newEstado= "";
        String asunto = "Solicitud de Equipos";
        String cuerpoMensaje = "Mensaje por defecto";

        System.out.println("Se mandan mails");

        Usuario estudianteTutor = getUsuRepository.getReferenceById((Long) delegateExecution.getVariable("estudianteSol"));

        //String estudiante = (String) delegateExecution.getVariable("correoEstudiante");
        String estudiante = estudianteTutor.getCorreo();

        String estado = (String) delegateExecution.getVariable("estado");

        if(estado.equals("Aceptado")){
            cuerpoMensaje = "Se ah aceptado su solicitud de Equipos Informaticos, favor contactarse con Coordinacion Estudiantil.";
            newEstado = "NotificadoAceptado";
        }

        if(estado.equals("Rechazado")){
            cuerpoMensaje = "Se ah Rechazado su solictud de Solicitud de Equipos Informatico, razon: ";
            newEstado = "NotificadoRechazo";
        }

        GoogleMailApi.enviarCorreo(estudiante,asunto,cuerpoMensaje);
        delegateExecution.setVariable("estado",newEstado);
    }
}
