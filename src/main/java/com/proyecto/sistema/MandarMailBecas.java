package com.proyecto.sistema;

import com.proyecto.servicios.GoogleMailApi;
import jakarta.inject.Named;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;


@Named
public class MandarMailBecas implements JavaDelegate {

    GoogleMailApi apiDeMails;
    
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Integer estudiante = 4;
        String estado = "";
        String newEstadoBeca = "";
        String asunto = "Prueba";
        String cuerpoMensaje = "Mensaje enviado mediante el proceso de camunda para asegurarnos que se ejecuta";

        estudiante = (Integer) delegateExecution.getVariable("estudiante");
        estado = (String) delegateExecution.getVariable("estadoBeca");
        asunto = "Respuesta Beca";

        if(estado.equals("enviarMail")){
            cuerpoMensaje = "Mensaje A";
            newEstadoBeca = "MailDirector";
        }

        if(estado.equals("NoNotificadoAceptado")){
            cuerpoMensaje = "Mensaje B";
            newEstadoBeca = "NotificadoAceptado";
        }

        if(estado.equals("Rechazado")){
            cuerpoMensaje = "Mensaje C";
            newEstadoBeca = "NotificadoRechazo";
        }

        //apiDeMails.enviarCorreo(estudiante,asunto,cuerpoMensaje);
        delegateExecution.setVariable("estadoBeca",newEstadoBeca);
    }
}
