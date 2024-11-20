package com.proyecto.sistema;

import com.proyecto.servicios.GoogleMailApi;

public class Prueba {

    public static void main(String[] args) {
        try {
            // Crear instancia de GoogleMailApi (asegúrate de que esté correctamente configurada)
            GoogleMailApi googleMailApi = new GoogleMailApi();
            ServicioMeet.googleMailApi = googleMailApi;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
