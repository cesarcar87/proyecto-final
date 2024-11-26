package com.proyecto.sistema.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.sistema.clases.sistema.TutEntrePar;
import com.proyecto.sistema.rest.servicios.TutEntreParesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tutentpar")
public class TutEntreParesRest {

    @Autowired
    private TutEntreParesService tutEntreParesService;

    @Autowired
    private ObjectMapper objectMapper;  // Para convertir la lista a JSON

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/listarTutoriasCreadas")
    public List<TutEntrePar> listarTodasLasTutorias() {
        // Llamar al servicio de autenticación
        System.out.println("ingresa a llamado");
        return tutEntreParesService.buscarTodasLasTutorias();
    }

    @PostMapping("/nuevaTutEntrePares")
    public ResponseEntity<String> nuevaTutoria(@RequestBody TutEntrePar request) throws IOException {
        System.out.println("Estudiantes: " + request.getEstudiantesTutorados());
        System.out.println("Unidad Curricular: " + request.getUnidadCurricularTutoria());
        System.out.println("Descripcion: " + request.getDescripcionTutoria());

        // Preparar las variables para Camunda
        Map<String, Object> variables = new HashMap<>();
        List<Long> estudiantesTutorados = request.getEstudiantesTutorados();

        if (estudiantesTutorados != null && estudiantesTutorados.size() >= 4) {
            variables.put("estudiante1", Map.of("value", estudiantesTutorados.get(0), "type", "Long"));
            variables.put("estudiante2", Map.of("value", estudiantesTutorados.get(1), "type", "Long"));
            variables.put("estudiante3", Map.of("value", estudiantesTutorados.get(2), "type", "Long"));
            variables.put("estudiante4", Map.of("value", estudiantesTutorados.get(3), "type", "Long"));
        } else {
            return ResponseEntity.badRequest().body("Se necesitan al menos 4 estudiantes.");
        }

        if (request.getUnidadCurricularTutoria() != null) {
            variables.put("unidadCurricular", Map.of("value", request.getUnidadCurricularTutoria(), "type", "String"));
        }
        if (request.getDescripcionTutoria() != null) {
            variables.put("descripcionTutoria", Map.of("value", request.getDescripcionTutoria(), "type", "String"));
        }

        // Preparar el cuerpo de la solicitud a Camunda
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("variables", variables);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        String camundaUrl = "http://localhost:8080/engine-rest/process-definition/key/TutoriaEntrePares/start";

        try {
            // Iniciar el proceso en Camunda
            ResponseEntity<String> response = restTemplate.exchange(camundaUrl, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Proceso iniciado en Camunda correctamente.");
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(response.getBody());
                String ProcessId = rootNode.path("id").asText(); // Extraer el instanceId

                System.out.println("Process Instance ID: " + ProcessId);

                String InstanceId = BuscarIntanceId(ProcessId);


                // Preparar las variables para completar la tarea
                Map<String, Object> estadoBecaVar = Map.of(
                        "value", "Solicitado",
                        "type", "String"
                );

                Map<String, Object> processVariables = Map.of(
                        "estadoBeca", estadoBecaVar
                );

                Map<String, Object> body = Map.of(
                        "variables", processVariables
                );

                String completeTaskUrl = "http://localhost:8080/engine-rest/task/" + InstanceId + "/complete";

                // Completar la tarea en Camunda
                ResponseEntity<String> taskCompleteResponse = restTemplate.postForEntity(completeTaskUrl, body, String.class);

                if (taskCompleteResponse.getStatusCode().is2xxSuccessful()) {
                    System.out.println("Tarea completada exitosamente.");
                    return ResponseEntity.ok("Tarea completada y proceso iniciado: " + response.getBody());
                } else {
                    System.err.println("Error al completar la tarea: " + taskCompleteResponse.getBody());
                    return ResponseEntity.status(taskCompleteResponse.getStatusCode())
                            .body("Error al completar la tarea: " + taskCompleteResponse.getBody());
                }
            } else {
                System.err.println("Error al iniciar el proceso en Camunda: " + response.getStatusCode());
                return ResponseEntity.status(response.getStatusCode()).body("Error al iniciar el proceso en Camunda: " + response.getBody());
            }
        } catch (Exception e) {
            System.err.println("Excepción al intentar iniciar el proceso en Camunda: " + e.getMessage());
            String errorMessage = "No se pudo iniciar el proceso de tutoría entre pares. Detalles del error: " + e.getMessage();
            return ResponseEntity.status(500).body(errorMessage);
        }
    }

    @PostMapping("/solicitarTutor")
    public ResponseEntity<String> solicitarTutor(@RequestParam Long idTutEntrePar, @RequestParam Long idEstudianteTutor) throws JsonProcessingException {
        try {
            // Buscar la tutoría entre pares por ID
            TutEntrePar tutEntreParPen = tutEntreParesService.buscarTutEntrePares(idTutEntrePar);
            if (tutEntreParPen == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la tutoría con ID: " + idTutEntrePar);
            }

            // Obtener el ID del proceso Camunda asociado a la tutoría
            String processId = tutEntreParPen.getProcesoCamunda();
            if (processId == null || processId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El proceso Camunda no está asociado a esta tutoría.");
            }

            // Obtener el instanceId del proceso
            String processInstanceId = BuscarIntanceId(processId);
            if (processInstanceId == null || processInstanceId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna instancia del proceso con ID: " + processId);
            }

            // URL para completar la tarea en Camunda
            String completeTaskUrl = "http://localhost:8080/engine-rest/task/" + processInstanceId + "/complete";

            // Crear el cuerpo de la solicitud con el formato correcto
            Map<String, Object> variableValue = Map.of(
                    "value", idEstudianteTutor,
                    "type", "Long"
            );
            Map<String, Object> variables = Map.of("idEstudianteTutor", variableValue);
            Map<String, Object> body = Map.of("variables", variables);

            // Completar la tarea en Camunda
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> taskCompleteResponse = restTemplate.postForEntity(completeTaskUrl, body, String.class);

            // Validar la respuesta de Camunda
            if (taskCompleteResponse.getStatusCode().is2xxSuccessful()) {
                System.out.println("Tarea completada exitosamente.");
                return ResponseEntity.ok("Tarea completada exitosamente. Proceso asociado: " + processId);
            } else {
                System.err.println("Error al completar la tarea: " + taskCompleteResponse.getBody());
                return ResponseEntity.status(taskCompleteResponse.getStatusCode())
                        .body("Error al completar la tarea: " + taskCompleteResponse.getBody());
            }

        } catch (Exception e) {
            // Manejo de errores generales
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un error inesperado: " + e.getMessage());
        }
    }

    @PostMapping("/confirmarTutoria")
    public ResponseEntity<String> confirmarTutoria(@RequestParam Long idTutEntrePar, @RequestParam String estadoTutoria) {
        System.out.println("idTutEntrePar recibido: " + idTutEntrePar);
        System.out.println("estadoTutoria recibido: " + estadoTutoria);
        try {
            // Validar el parámetro estadoTutoria
            if (!estadoTutoria.equalsIgnoreCase("Aceptado") && !estadoTutoria.equalsIgnoreCase("Rechazado")) {
                return ResponseEntity.badRequest().body("El estadoTutoria debe ser 'Aceptado' o 'Rechazado'.");
            }

            // Buscar la tutoría entre pares
            TutEntrePar tutEntrePar = tutEntreParesService.buscarTutEntrePares(idTutEntrePar);
            if (tutEntrePar == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró la tutoría con ID: " + idTutEntrePar);
            }

            // Obtener el ID del proceso Camunda asociado
            String processId = tutEntrePar.getProcesoCamunda();
            if (processId == null || processId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El proceso Camunda no está asociado a esta tutoría.");
            }

            // Buscar el instanceId asociado al proceso Camunda
            String processInstanceId = BuscarIntanceId(processId);
            if (processInstanceId == null || processInstanceId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró ninguna instancia del proceso con ID: " + processId);
            }

            // Definir la variable estadoTutoria en función del valor recibido
            String estadoCamunda = estadoTutoria.equalsIgnoreCase("Aceptado") ? "Aceptada" : "Rechazada";

            // Crear el cuerpo de la solicitud para Camunda
            Map<String, Object> estadoTutoriaVar = Map.of(
                    "value", estadoCamunda,
                    "type", "String"
            );

            Map<String, Object> variables = Map.of("estadoTutoria", estadoTutoriaVar);
            Map<String, Object> body = Map.of("variables", variables);

            // URL para completar la tarea en Camunda
            String completeTaskUrl = "http://localhost:8080/engine-rest/task/" + processInstanceId + "/complete";

            // Completar la tarea en Camunda
            ResponseEntity<String> taskCompleteResponse = restTemplate.postForEntity(completeTaskUrl, body, String.class);

            if (taskCompleteResponse.getStatusCode().is2xxSuccessful()) {
                System.out.println("Tarea completada exitosamente.");
                return ResponseEntity.ok("La tarea fue completada exitosamente con estado: " + estadoCamunda);
            } else {
                System.err.println("Error al completar la tarea: " + taskCompleteResponse.getBody());
                return ResponseEntity.status(taskCompleteResponse.getStatusCode())
                        .body("Error al completar la tarea: " + taskCompleteResponse.getBody());
            }

        } catch (Exception e) {
            // Manejo de errores
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un error inesperado: " + e.getMessage());
        }
    }


    public String BuscarIntanceId(String idCamunda) throws JsonProcessingException {
        //Consulta a la base por el estado actual de la beca

        // Obtener el taskId utilizando el processInstanceId
        String taskUrl = "http://localhost:8080/engine-rest/task?processInstanceId=" + idCamunda;
        ResponseEntity<String> taskResponse = restTemplate.getForEntity(taskUrl, String.class);

        // Parsear la respuesta para obtener el taskId
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(taskResponse.getBody());

        String mensaje;
        if (rootNode.isArray() && rootNode.size() > 0) {
            String taskId = rootNode.get(0).path("id").asText();  // Obtener el primer taskId
            return taskId;
        } else {
            mensaje = "No se encontraron tareas activas para el processInstanceId: " + idCamunda;
            return mensaje;
        }
    }



}
