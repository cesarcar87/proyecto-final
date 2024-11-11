package com.proyecto.sistema;

import com.proyecto.servicios.GoogleMailApi;

public class Prueba {

    public static void main(String[] args) {
        try {
            // Crear instancia de GoogleMailApi (asegúrate de que esté correctamente configurada)
            GoogleMailApi googleMailApi = new GoogleMailApi();
            ServicioMeet.googleMailApi = googleMailApi;

            // Llamar al método crearEventoConGoogleMeet para crear un evento de prueba en Google Calendar
            String enlaceEvento = ServicioMeet.crearEventoConGoogleMeet(
                    "Reunión de Prueba",
                    "Descripción de la reunión de prueba",
                    "2024-11-10T10:00:00-07:00",  // Ajusta la fecha de inicio
                    "2024-11-10T11:00:00-07:00"   // Ajusta la fecha de fin
            );

            System.out.println("Enlace del evento en Google Calendar: " + enlaceEvento);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
