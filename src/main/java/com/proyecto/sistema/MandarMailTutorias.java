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
    private GoogleMailApi apiDeMails;

    @Autowired
    GetUsuRepository getUsuRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String newEstadoBeca = "";
        String asunto = "Tutorias Entre Pares";
        String cuerpoMensaje = "";


        System.out.println("Se mandan mails");

        Usuario estudianteTutor = getUsuRepository.getReferenceById((Long) delegateExecution.getVariable("idEstudianteTutor"));

        Usuario estudianteTutorado = getUsuRepository.getReferenceById((Long) delegateExecution.getVariable("estudiante1"));

        String estado = (String) delegateExecution.getVariable("estadoTutoria");
        //Mensaje a mandar

        if (estado.equals("Aceptado")) {
            cuerpoMensaje = "La tutoría entre el estudiante tutor:" + estudianteTutor + " y el tutorado:" + estudianteTutorado + "   fue aceptada, Favor de establecer contacto ";
            newEstadoBeca = "Aceptada";
        } else {
            cuerpoMensaje = "La tutoría entre el estudiante tutor:" + estudianteTutor + " y el tutorado:" + estudianteTutorado + "   fue Rechazada";
            newEstadoBeca = "Rechazada";
        }

        String estudiante = estudianteTutor.getCorreo();
        String estudanteTutorado = estudianteTutorado.getCorreo();

        String correoCoordinador = (String) delegateExecution.getVariable("correoCoordinador");
        apiDeMails.enviarCorreo(correoCoordinador,estudiante,asunto,cuerpoMensaje);
        apiDeMails.enviarCorreo(correoCoordinador,estudanteTutorado,asunto,cuerpoMensaje);
        delegateExecution.setVariable("estadoTutoria",newEstadoBeca);
    }
}
