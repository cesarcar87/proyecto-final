package com.proyecto.servicios;

import com.google.api.client.auth.oauth2.Credential;
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
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class GoogleMailApi {

    private static final String APPLICATION_NAME = "Coordinacion Estudiantes";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Arrays.asList(
            GmailScopes.GMAIL_SEND,
            "https://www.googleapis.com/auth/calendar",
            "https://www.googleapis.com/auth/calendar.events"
    );
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "src/main/resources/client_secret_768873216358-7cdlvnvoc4rfc8vesjkp78d7j3g4ejdc.apps.googleusercontent.com.json";

    // Cargar las credenciales del archivo credentials.json
    public static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws Exception {
        // Verificar si el archivo de credenciales existe
        File credentialsFile = new File(CREDENTIALS_FILE_PATH);
        if (!credentialsFile.exists()) {
            throw new Exception("El archivo credentials.json no se encontró en " + CREDENTIALS_FILE_PATH);
        }

        // Cargar el archivo credentials.json
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(new FileInputStream(credentialsFile)));

        // Configurar el flujo de autenticación
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH))) // Almacena tokens
                .setAccessType("offline")
                .build();

        // Si las credenciales están almacenadas, se cargan automáticamente
        return flow.loadCredential("user");
    }

    // Enviar un correo utilizando Gmail API
    public static void enviarCorreo(String destinatario, String asunto, String cuerpoMensaje) throws Exception {
        // Construir el objeto de transporte HTTP
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        // Obtener credenciales
        Credential credential = getCredentials(HTTP_TRANSPORT);
        if (credential == null) {
            throw new Exception("Credenciales no disponibles. Autentique manualmente una vez para inicializar los tokens.");
        }

        // Crear el servicio Gmail
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
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
