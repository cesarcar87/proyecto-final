package com.proyecto.sistema;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.proyecto.servicios.GoogleMailApi;
import jakarta.inject.Named;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.ConferenceData;
import com.google.api.services.calendar.model.ConferenceSolutionKey;
import com.google.api.services.calendar.model.CreateConferenceRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.glassfish.jersey.server.ServerProperties.APPLICATION_NAME;

@Named
public class ServicioMeetBecas implements JavaDelegate {

    @Autowired
    GoogleMailApi googleMailApi;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("Se crean salas meet");

        String fechaEntrevista = (String) delegateExecution.getVariable("fechaEntrevista");

        // Parsear la fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        ZonedDateTime fechaFinal = ZonedDateTime.parse(fechaEntrevista, formatter);

        // Sumar 1 hora
        ZonedDateTime fechaFin = fechaFinal.plusHours(1);

        // Convertir de nuevo a String si es necesario
        String fechaFinString = fechaFin.format(formatter);

        String estadoBeca = "esperandoResultado";
        delegateExecution.setVariable("estadoBeca", estadoBeca);

        String DescripcionMeet = "Entrevista Coordinacion Estudiantil";
        String description = "Entrevista";

        // Llamar al método con las fechas correctas
        String googleMeetEntrevista = crearEventoConGoogleMeet(DescripcionMeet, description, fechaEntrevista, fechaFinString);

        String CorreoEstudiante = (String) delegateExecution.getVariable("correoEstudiante");
        String asunto = "Entrevista Google Meet";
        String cuerpoMensaje = "Se creo la cita en Google Calendar,%s\n enlaces para la meet de google: " + googleMeetEntrevista;

        googleMailApi.enviarCorreo(CorreoEstudiante,asunto,cuerpoMensaje);

    }


    // Método para crear una reunión en Google Calendar con Google Meet
    public String crearEventoConGoogleMeet(String summary, String description, String fechaInicio, String fechaFin) throws Exception {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

        Calendar calendarService = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, googleMailApi.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        Event event = new Event()
                .setSummary(summary)
                .setDescription(description);

        // Establecer fecha de inicio y fin
        EventDateTime start = new EventDateTime()
                .setDateTime(new DateTime(fechaInicio))
                .setTimeZone("America/Los_Angeles");  // Ajusta la zona horaria según tus necesidades
        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(new DateTime(fechaFin))
                .setTimeZone("America/Los_Angeles");
        event.setEnd(end);

        // Configurar conferencia de Google Meet
        ConferenceData conferenceData = new ConferenceData();
        ConferenceSolutionKey conferenceSolutionKey = new ConferenceSolutionKey();
        conferenceSolutionKey.setType("hangoutsMeet");
        CreateConferenceRequest createConferenceRequest = new CreateConferenceRequest();
        createConferenceRequest.setRequestId("meet-" + UUID.randomUUID().toString());
        createConferenceRequest.setConferenceSolutionKey(conferenceSolutionKey);
        conferenceData.setCreateRequest(createConferenceRequest);
        event.setConferenceData(conferenceData);

        // Crear el evento en el calendario
        event = calendarService.events().insert("primary", event)
                .setConferenceDataVersion(1)
                .execute();

        System.out.printf("Evento creado: %s\n", event.getHtmlLink());
        System.out.printf("Enlace de Google Meet: %s\n", event.getConferenceData().getEntryPoints().get(0).getUri());

        return event.getHtmlLink();  // Retorna el enlace del evento en Google Calendar
    }

}
