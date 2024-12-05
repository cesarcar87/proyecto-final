package com.proyecto.sistema.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.sistema.clases.sistema.Documento;
import com.proyecto.sistema.clases.sistema.SolicitudEquipos;
import com.proyecto.sistema.exceptions.ResourceNotFoundException;
import com.proyecto.sistema.rest.servicios.SoliEquiposService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@RestController
@RequestMapping("/api/solicitudesEquip")
public class SoliEquiposRestController {

    @Autowired
    public SoliEquiposService soliEquiposService;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping(value = "/crearSolicitud", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> crearSolicitud(
            @RequestParam("equipoSol") String equipoSol,
            @RequestParam("descripcionSol") String descripcionSol,
            @RequestParam("estudianteSol") Long estudianteSol,
            @RequestParam(value = "documentosPDFEst", required = false) MultipartFile[] documentosPDFEst) throws IOException {

        System.out.println("Equipo Solicitado: " + equipoSol);
        System.out.println("Descripción: " + descripcionSol);
        System.out.println("Estudiante Solicitante: " + estudianteSol);


        // Procesar los archivos PDF y crear la lista de Documentos
        List<Documento> documentos = new ArrayList<>();
        for (MultipartFile archivoPdf : documentosPDFEst) {
            if (!archivoPdf.isEmpty()) {
                Documento documento = new Documento();
                documento.setContenidoPDF(archivoPdf.getBytes()); // Guardar el archivo como byte[]
                documentos.add(documento); // Agregar el documento a la lista
            }
        }

        // Validaciones de datos
        if (equipoSol == null || equipoSol.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El campo 'equipoSol' es obligatorio."));
        }
        if (descripcionSol == null || descripcionSol.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El campo 'descripcionSol' es obligatorio."));
        }
        if (estudianteSol == null || estudianteSol <= 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "El campo 'estudianteSol' debe ser un número positivo."));
        }

        // Preparar las variables para Camunda
        Map<String, Object> variables = new HashMap<>();
        variables.put("equipoSol", Map.of("value", equipoSol, "type", "String"));
        variables.put("descripcionSol", Map.of("value", descripcionSol, "type", "String"));
        variables.put("estudianteSol", Map.of("value", estudianteSol, "type", "Long"));

        // Asignar la fecha actual como variable
        LocalDate today = LocalDate.now();
        Date fechaDeHoy = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Crear una nueva instancia de solicitud
        SolicitudEquipos request = new SolicitudEquipos();
        request.setEquipoSol(equipoSol);
        request.setDescripcionSol(descripcionSol);
        request.setEstudianteSol(estudianteSol);
        request.setFechaDeSolicitud(fechaDeHoy);
        request.setDocumentosPDFEst(documentos); // Convertir lista de vuelta a arreglo si es necesario
        request.setEstadoSolicitud("Solicitado");

        // Preparar el cuerpo de la solicitud para Camunda
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
                String processInstanceId = rootNode.path("id").asText(); // Obtener el processInstanceId
                request.setProcesoCamundaSol(processInstanceId);

                // Guardar la solicitud en la base de datos
                soliEquiposService.crearSolicitud(request);

                // Devolver un JSON en lugar de texto plano
                Map<String, String> respuesta = new HashMap<>();
                respuesta.put("mensaje", "Proceso iniciado en Camunda correctamente");
                respuesta.put("camundaResponse", response.getBody());
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
            @RequestParam("estadoSolicitud") String estadoSolicitud,
            @RequestParam("correoCoordinador") String correoCoordinador) {

        // Validar entrada
        if (!estadoSolicitud.equalsIgnoreCase("Aceptada") && !estadoSolicitud.equalsIgnoreCase("Rechazada")) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "El estado debe ser 'Aceptada' o 'Rechazada'"
            ));
        }

        Optional<SolicitudEquipos> optionalSolicitudEquipos = soliEquiposService.obtenerSolicitudPorId(idSolicitud);

        if (optionalSolicitudEquipos.isPresent()) {
            SolicitudEquipos solicitudEquipos = optionalSolicitudEquipos.get();
            solicitudEquipos.setEstadoSolicitud(estadoSolicitud);
            soliEquiposService.modificarSolicitud(idSolicitud, solicitudEquipos);
        } else {
            throw new IllegalArgumentException("No se encontró una solicitud con el ID: " + idSolicitud);
        }

        // Preparar las variables para completar la tarea en Camunda
        Map<String, Object> variables = new HashMap<>();
        variables.put("estado", Map.of("value", estadoSolicitud, "type", "String"));
        variables.put("correoCoordinador", Map.of("value", correoCoordinador, "type", "String"));

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

    @GetMapping("/documentosEst/{solEquipoId}")
    public ResponseEntity<byte[]> descargarDocumentosEstudiante(@PathVariable Long solEquipoId) throws IOException {
        // Buscar los documentos asociados al ID de la beca
        List<Documento> documentos = soliEquiposService.descargarDocumentosEst(solEquipoId);

        if (documentos.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron documentos para la solicitud con ID: " + solEquipoId);
        }

        // Crear un archivo ZIP en memoria
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (Documento documento : documentos) {
                // Crear una nueva entrada en el archivo ZIP
                ZipEntry zipEntry = new ZipEntry("documento_" + documento.getIdDocumento() + ".pdf");
                zos.putNextEntry(zipEntry);
                // Agregar el contenido del documento al archivo ZIP
                zos.write(documento.getContenidoPDF());
                zos.closeEntry();
            }
        }

        // Configurar la respuesta con el archivo ZIP
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"documentos_solicitudEquipos_" + solEquipoId + ".zip\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(baos.toByteArray());
    }

    @GetMapping("/documentosCor/{solEquipoId}")
    public ResponseEntity<byte[]> descargarDocumentosCoordinador(@PathVariable Long solEquipoId) throws IOException {
        // Buscar los documentos asociados al ID de la beca
        List<Documento> documentos = soliEquiposService.descargarDocumentosCor(solEquipoId);

        if (documentos.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron documentos para la solicitud con ID: " + solEquipoId);
        }

        // Crear un archivo ZIP en memoria
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (Documento documento : documentos) {
                // Crear una nueva entrada en el archivo ZIP
                ZipEntry zipEntry = new ZipEntry("documento_" + documento.getIdDocumento() + ".pdf");
                zos.putNextEntry(zipEntry);
                // Agregar el contenido del documento al archivo ZIP
                zos.write(documento.getContenidoPDF());
                zos.closeEntry();
            }
        }

        // Configurar la respuesta con el archivo ZIP
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"documentos_solicitudEquipos_" + solEquipoId + ".zip\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(baos.toByteArray());
    }

}

