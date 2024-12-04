package com.proyecto.sistema;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.proyecto.servicios.GoogleMailApi;
import jakarta.inject.Named;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.glassfish.jersey.server.ServerProperties.APPLICATION_NAME;

@Named
public class ServicioMeetBecas implements JavaDelegate {

    @Autowired
    private GoogleMailApi googleMailApi;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("Se crean salas meet para becas");

        // Obtener la fecha de entrevista desde las variables
        String fechaEntrevista = (String) delegateExecution.getVariable("fechaEntrevista");

        // Parsear la fecha y sumar 1 hora
        ZonedDateTime fechaFinal = ZonedDateTime.parse(fechaEntrevista, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        ZonedDateTime fechaFin = fechaFinal.plusHours(1);
        String fechaFinString = fechaFin.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        // Establecer estado de la beca
        String estadoBeca = "esperandoResultado";
        delegateExecution.setVariable("estadoBeca", estadoBeca);

        // Obtener los correos
        String correoEstudiante = (String) delegateExecution.getVariable("correoEstudiante");
        String correoCoordinador = (String) delegateExecution.getVariable("correoCoordinador");

        // Crear la sala de Google Meet
        String descripcionMeet = "Entrevista Coordinación Estudiantil";
        String description = "Entrevista";
        String googleMeetEntrevista = crearEventoConGoogleMeet(correoCoordinador, correoEstudiante, descripcionMeet, description, fechaEntrevista, fechaFinString);

        // Enviar correo de notificación
        String asunto = "Entrevista Google Meet";
        String cuerpoMensaje = "Se creó la cita en Google Calendar. Enlace para la reunión de Google Meet: " + googleMeetEntrevista;
        googleMailApi.enviarCorreo(correoCoordinador, correoEstudiante, asunto, cuerpoMensaje);

        System.out.println("Sala de Google Meet creada y correo enviado.");
    }

    // Método para crear una reunión en Google Calendar con Google Meet
    public String crearEventoConGoogleMeet(String correoCoordinador, String correoEstudiante, String summary, String description, String fechaInicio, String fechaFin) throws Exception {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

        // Crear el servicio de Calendar con las credenciales del coordinador
        Calendar calendarService = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, googleMailApi.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Crear el evento
        Event event = new Event()
                .setSummary(summary)
                .setDescription(description);

        // Configurar fecha de inicio y fin
        EventDateTime start = new EventDateTime()
                .setDateTime(new DateTime(fechaInicio))
                .setTimeZone("America/Los_Angeles");
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

        // Configurar los asistentes
        List<EventAttendee> attendees = new ArrayList<>();
        attendees.add(new EventAttendee().setEmail(correoEstudiante));
        event.setAttendees(attendees);

        // Crear el evento en el calendario
        event = calendarService.events().insert("primary", event)
                .setConferenceDataVersion(1)
                .execute();

        System.out.printf("Evento creado: %s\n", event.getHtmlLink());
        System.out.printf("Enlace de Google Meet: %s\n", event.getConferenceData().getEntryPoints().get(0).getUri());

        return event.getHtmlLink(); // Retorna el enlace del evento en Google Calendar
    }
}
