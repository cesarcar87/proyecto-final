package com.proyecto.sistema;

import com.proyecto.servicios.GoogleMailApi;
import jakarta.inject.Named;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;


@Named
public class MandarMailBecas implements JavaDelegate {

    @Autowired
    private GoogleMailApi googleMailApi;
    
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String estado = "";
        String newEstadoBeca = "";
        String asunto = "Beca";
        String cuerpoMensaje = "";

        String estudiante = (String) delegateExecution.getVariable("correoEstudiante");
        estado = (String) delegateExecution.getVariable("estadoBeca");

        String mensajeCoordinador = (String) delegateExecution.getVariable("mensajeCoordinador");

        System.out.println("estado en mandar mail becas a" + estado);

        if(estado.equals("enviarMail")){
            cuerpoMensaje = "Se ah aceptado su solicitud de Beca, se le informara cuando el proceso de validacion por el director quede completo. Mensaje de Coordinador: " + mensajeCoordinador;
            newEstadoBeca = "NotificadoAceptado";
        }

        if(estado.equals("NoNotificadoAceptado")){
            cuerpoMensaje = "Su Solicitud de Beca ha sido aceptada exitosamente, por favor, contactarse con coordinacion estudiantil. Mensaje de Coordinador: " + mensajeCoordinador;
            newEstadoBeca = "NotificadoAceptado";
        }

        if(estado.equals("Rechazado")){
            cuerpoMensaje = "Se ah Rechazado su solictud de Beca, razon: " + mensajeCoordinador;
            newEstadoBeca = "NotificadoRechazo";
        }

        String correoCoordinador = (String) delegateExecution.getVariable("correoCoordinador");
        googleMailApi.enviarCorreo(correoCoordinador,estudiante,asunto,cuerpoMensaje);
        delegateExecution.setVariable("estadoBeca",newEstadoBeca);
    }
}
