package com.proyecto.sistema.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.sistema.clases.sistema.SolicitudEquipos;
import com.proyecto.sistema.rest.servicios.SoliEquiposService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/solicitudesEquip")
public class SoliEquiposRestController {

    @Autowired
    public SoliEquiposService soliEquiposService;

    @Autowired
    private RestTemplate restTemplate;

    // Crear una solicitud
    @PostMapping("/crearSolicitud")
    public ResponseEntity<Map<String, String>> crearSolicitud(@RequestBody SolicitudEquipos request) {
        System.out.println("Equipo Solicitado: " + request.getEquipoSol());
        System.out.println("Descripción: " + request.getDescripcionSol());
        System.out.println("Estudiante Solicitante: " + request.getEstudianteSol());

        // Preparar las variables para Camunda
        Map<String, Object> variables = new HashMap<>();
        variables.put("equipoSol", Map.of("value", request.getEquipoSol(), "type", "String"));
        variables.put("descripcionSol", Map.of("value", request.getDescripcionSol(), "type", "String"));
        variables.put("estudianteSol", Map.of("value", request.getEstudianteSol(), "type", "Long"));

        // Asignar la fecha actual como variable
        LocalDate today = LocalDate.now();
        Long fechaComoLong = today.getYear() * 10000L + today.getMonthValue() * 100 + today.getDayOfMonth();
        variables.put("fechaSolicitud", Map.of("value", fechaComoLong, "type", "Long"));

        request.setFechaSolicitud(fechaComoLong);

        // Preparar el cuerpo de la solicitud a Camunda
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("variables", variables);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        String camundaUrl = "http://localhost:8080/engine-rest/process-definition/key/SolicitudEquiposInf/start";

        try {
            // Iniciar el proceso en Camunda
            ResponseEntity<String> response = restTemplate.postForEntity(camundaUrl, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Proceso iniciado en Camunda correctamente.");

                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(response.getBody());
                String processInstanceId = rootNode.path("id").asText();  // Obtener el processInstanceId
                request.setProcesoCamundaSol(processInstanceId);

                // Devolver un JSON en lugar de texto plano
                Map<String, String> respuesta = new HashMap<>();
                respuesta.put("mensaje", "Proceso iniciado en Camunda correctamente");
                respuesta.put("camundaResponse", response.getBody());

                soliEquiposService.crearSolicitud(request);
                return ResponseEntity.ok(respuesta);
            } else {
                System.err.println("Error al iniciar el proceso en Camunda: " + response.getStatusCode());
                return ResponseEntity.status(response.getStatusCode())
                        .body(Map.of("error", "Error al iniciar el proceso en Camunda", "detalle", response.getBody()));
            }
        } catch (Exception e) {
            System.err.println("Excepción al intentar iniciar el proceso en Camunda: " + e.getMessage());
            String errorMessage = "No se pudo iniciar el proceso de solicitud de equipos. Detalles del error: " + e.getMessage();
            return ResponseEntity.status(500).body(Map.of("error", errorMessage));
        }
    }

    @PostMapping("/avanzarSolicitud")
    public ResponseEntity<Map<String, String>> avanzarSolicitud(
            @RequestParam("idSolicitud") Long idSolicitud,
            @RequestParam("estadoSolicitud") String estadoSolicitud) {

        // Validar entrada
        if (!estadoSolicitud.equalsIgnoreCase("Aceptada") && !estadoSolicitud.equalsIgnoreCase("Rechazada")) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "El estado debe ser 'Aceptada' o 'Rechazada'"
            ));
        }
        // Preparar las variables para completar la tarea en Camunda
        Map<String, Object> variables = new HashMap<>();
        variables.put("estado", Map.of("value", estadoSolicitud, "type", "String"));

        // Buscar la instancia del proceso asociado a esta solicitud (simulado aquí)
        String instanceId = buscarInstanceIdPorIdSolicitud(idSolicitud);
        if (instanceId == null) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "No se encontró una instancia del proceso asociada a esta solicitud"
            ));
        }

        // Preparar el cuerpo de la solicitud a Camunda
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("variables", variables);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        String completeTaskUrl = "http://localhost:8080/engine-rest/task/" + instanceId + "/complete";

        try {
            // Completar la tarea en Camunda
            ResponseEntity<String> response = restTemplate.postForEntity(completeTaskUrl, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Tarea completada en Camunda correctamente.");

                // Devolver un JSON indicando el éxito
                return ResponseEntity.ok(Map.of(
                        "mensaje", "Tarea completada en Camunda correctamente",
                        "estadoSolicitud", estadoSolicitud
                ));
            } else {
                System.err.println("Error al completar la tarea en Camunda: " + response.getStatusCode());
                return ResponseEntity.status(response.getStatusCode())
                        .body(Map.of("error", "Error al completar la tarea en Camunda", "detalle", response.getBody()));
            }
        } catch (Exception e) {
            System.err.println("Excepción al intentar completar la tarea en Camunda: " + e.getMessage());
            String errorMessage = "No se pudo completar la tarea en Camunda. Detalles del error: " + e.getMessage();
            return ResponseEntity.status(500).body(Map.of("error", errorMessage));
        }
    }

    // Listar todas las solicitudes
    @GetMapping("/listarSolicitudes")
    public ResponseEntity<List<SolicitudEquipos>> obtenerTodasLasSolicitudes() {
        try {
            // Llamar al servicio para obtener todas las solicitudes
            List<SolicitudEquipos> solicitudes = soliEquiposService.listarSolicitudes();

            return ResponseEntity.ok(solicitudes); // Devuelve 200 con la lista de solicitudes
        } catch (Exception e) {
            System.err.println("Error al obtener las solicitudes: " + e.getMessage());
            return ResponseEntity.status(500).body(null); // Devuelve 500 en caso de error
        }
    }

    // Listar todas las solicitudes
    @GetMapping("/listarSolPorIdEst")
    public ResponseEntity<List<SolicitudEquipos>> obtenerSolicitudesEstudiante(@RequestParam("idEstudiante") Long idEstudiante) {
        try {
            // Llamar al servicio para obtener todas las solicitudes
            List<SolicitudEquipos> solicitudes = soliEquiposService.listarSolicitudesPorEstudiante(idEstudiante);

            return ResponseEntity.ok(solicitudes); // Devuelve 200 con la lista de solicitudes
        } catch (Exception e) {
            System.err.println("Error al obtener las solicitudes: " + e.getMessage());
            return ResponseEntity.status(500).body(null); // Devuelve 500 en caso de error
        }
    }



    // Simular método para obtener la instancia del proceso asociada a una solicitud
    private String buscarInstanceIdPorIdSolicitud(Long idSolicitud) {
        //Consulta a la base por el estado actual de la beca
        System.out.println("id de la solicitud: " + idSolicitud);

        Optional<SolicitudEquipos> soliEquipo = soliEquiposService.obtenerSolicitudPorId(idSolicitud);

            // Verificar si el Optional contiene un valor
        if (soliEquipo.isPresent()) {
            // Obtener el valor de Optional
            SolicitudEquipos solicitudEquipos = soliEquipo.get();

            String ProcessIdCamunda = solicitudEquipos.getProcesoCamundaSol();
            // Usar la solicitud como necesites
            // Obtener el taskId utilizando el processInstanceId
            String taskUrl = "http://localhost:8080/engine-rest/task?processInstanceId=" + ProcessIdCamunda;
            ResponseEntity<String> taskResponse = restTemplate.getForEntity(taskUrl, String.class);

            // Parsear la respuesta para obtener el taskId
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = null;
            try {
                rootNode = mapper.readTree(taskResponse.getBody());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            String mensaje;
            if (rootNode.isArray() && rootNode.size() > 0) {
                String taskId = rootNode.get(0).path("id").asText();  // Obtener el primer taskId
                return taskId;
            } else {
                mensaje = "No se encontraron tareas activas para el processInstanceId: " + ProcessIdCamunda;
                return mensaje;
            }
        } else {
            // Manejar el caso en que no se encontró la solicitud
            System.err.println("No se encontró la solicitud con ID: " + idSolicitud);
        }


        return "";
    }





}

