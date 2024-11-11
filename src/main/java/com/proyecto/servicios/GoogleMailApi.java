package com.proyecto.servicios;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class GoogleMailApi {

    private static final String APPLICATION_NAME = "Coordinacion Estudiantes";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Arrays.asList(
            GmailScopes.GMAIL_SEND,
            "https://www.googleapis.com/auth/calendar",
            "https://www.googleapis.com/auth/calendar.events"
    );
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    // Cargar las credenciales del archivo credentials.json
    public static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        File credentialsFile = new File("src/main/resources/client_secret_768873216358-7cdlvnvoc4rfc8vesjkp78d7j3g4ejdc.apps.googleusercontent.com.json"); // Coloca aquí la ruta de tu archivo credentials.json
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(new FileInputStream(credentialsFile)));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    // Enviar un correo utilizando Gmail API
    public static void enviarCorreo(String destinatario, String asunto, String cuerpoMensaje) throws Exception {
        // Construir el objeto de transporte HTTP
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Crear el mensaje MIME
        MimeMessage mimeMessage = crearEmail(destinatario, "me", asunto, cuerpoMensaje);

        // Convertir el mensaje a formato base64
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        mimeMessage.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.getUrlEncoder().encodeToString(rawMessageBytes);

        // Enviar el mensaje
        Message message = new Message();
        message.setRaw(encodedEmail);
        service.users().messages().send("me", message).execute();
        System.out.println("Correo enviado exitosamente");
    }

    // Método para crear el mensaje MIME
    public static MimeMessage crearEmail(String destinatario, String remitente, String asunto, String cuerpoTexto) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(remitente));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(destinatario));
        email.setSubject(asunto);
        email.setText(cuerpoTexto);
        return email;
    }
}
