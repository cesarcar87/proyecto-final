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

import java.util.UUID;

import static org.glassfish.jersey.server.ServerProperties.APPLICATION_NAME;

@Named
public class ServicioMeet implements JavaDelegate {

    static GoogleMailApi googleMailApi;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("Se crean salas meet");

        String estadoBeca = "esperandoResultado";
        delegateExecution.setVariable("estadoBeca",estadoBeca);

        String DescripcionMeet = "EntrevistaBeca";
        String description  = "Entrevista para la beca";
        // Fecha de inicio
        String fechaInicio = "2024-11-15T09:00:00-08:00"; // 15 de noviembre de 2024 a las 9:00 AM en la zona horaria América/Los Ángeles

        // Fecha de fin
        String fechaFin = "2024-11-15T10:00:00-08:00"; // 15 de noviembre de 2024 a las 10:00 AM en la misma zona horaria

        crearEventoConGoogleMeet(DescripcionMeet,description,fechaInicio,fechaFin);
    }

    // Método para crear una reunión en Google Calendar con Google Meet
    public static String crearEventoConGoogleMeet(String summary, String description, String fechaInicio, String fechaFin) throws Exception {
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
