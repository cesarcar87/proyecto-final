package com.proyecto.sistema;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.proyecto.servicios.GoogleMailApi;
import com.proyecto.sistema.clases.usuarios.Usuario;
import com.proyecto.sistema.rest.repositorios.GetUsuRepository;
import jakarta.inject.Named;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;


import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.glassfish.jersey.server.ServerProperties.APPLICATION_NAME;

@Named
public class ServicioMeetTutorias implements JavaDelegate {

    @Autowired
    private GoogleMailApi googleMailApi;

    @Autowired
    GetUsuRepository getUsuRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("Se crean salas meet Tutorias");

        // Validar fecha de entrevista
        //String fechaEntrevista = (String) delegateExecution.getVariable("fechaEntrevista");
        // Parsear la fecha usando la clase DateTime de Google
        String fechaEntrevista = "2024-11-21T15:45:30.123-03:00";

        // Utiliza la clase DateTime de Google para parsear directamente
        DateTime fechaGoogle = new DateTime(fechaEntrevista);

        // Sumar 1 hora
        ZonedDateTime fechaFinal = ZonedDateTime.parse(fechaEntrevista, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        ZonedDateTime fechaFin = fechaFinal.plusHours(1);

        // Convertir de nuevo a String si es necesario
        String fechaFinString = fechaFin.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        // Validar correo del estudiante
        Usuario tutor = getUsuRepository.getReferenceById((Long) delegateExecution.getVariable("idEstudianteTutor"));

        String CorreoEstudiante = tutor.getCorreo();

        if (CorreoEstudiante == null || CorreoEstudiante.isEmpty()) {
            throw new NullPointerException("La variable 'correoEstudiante' no está configurada o está vacía.");
        }

        System.out.println("Se crean salas meet C");

        String DescripcionMeet = "Entrevista Coordinacion Estudiantil";
        String description = "Entrevista";

        // Crear evento de Google Meet
        String googleMeetEntrevista = crearEventoConGoogleMeet(DescripcionMeet, description, fechaEntrevista, fechaFinString);

        System.out.println("Se crean salas meet D");

        // Enviar correo
        String asunto = "Entrevista Google Meet";
        String cuerpoMensaje = "Se creó la cita en Google Calendar. Enlace para la reunión: " + googleMeetEntrevista;
        googleMailApi.enviarCorreo(CorreoEstudiante, asunto, cuerpoMensaje);


        System.out.println("Se crean salas meet E");
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
