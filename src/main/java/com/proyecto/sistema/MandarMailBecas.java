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
        String estado = "";
        String newEstadoBeca = "";
        String asunto = "Beca";
        String cuerpoMensaje = "";



        String estudiante = (String) delegateExecution.getVariable("correoEstudiante");
        estado = (String) delegateExecution.getVariable("estadoBeca");

        System.out.println("estado en mandar mail becas" + estado);

        if(estado.equals("enviarMail")){
            cuerpoMensaje = "Se ah aceptado su solicitud de Beca, se le informara cuando el proceso de validacion por el director quede completo";
            newEstadoBeca = "NotificadoAceptado";
        }

        if(estado.equals("NoNotificadoAceptado")){
            cuerpoMensaje = "Su Solicitud de Beca ha sido aceptada exitosamente, por favor, contactarse con coordinacion estudiantil.";
            newEstadoBeca = "NotificadoAceptado";
        }

        if(estado.equals("Rechazado")){
            cuerpoMensaje = "Se ah Rechazado su solictud de Beca, razon: ";
            newEstadoBeca = "NotificadoRechazo";
        }

        apiDeMails.enviarCorreo(estudiante,asunto,cuerpoMensaje);
        delegateExecution.setVariable("estadoBeca",newEstadoBeca);
    }
}
