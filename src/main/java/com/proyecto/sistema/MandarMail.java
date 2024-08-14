package com.proyecto.sistema;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class MandarMail {

    private final String usuario = "xfliptherx@gmail.com"; // Reemplaza con tu correo
    private final String contraseña = "xanu cefk uhfu gadu"; // Reemplaza con tu contraseña

    public void envioDeMail(String mailDestino, String mailAsunto, String mailMensaje) {
        // Configuración de propiedades para la conexión SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Autenticación
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, contraseña);
            }
        });

        try {
            // Crear el mensaje de correo
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usuario));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailDestino));
            message.setSubject(mailAsunto);
            message.setText(mailMensaje);

            // Enviar el mensaje
            Transport.send(message);

            System.out.println("Correo enviado exitosamente");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}