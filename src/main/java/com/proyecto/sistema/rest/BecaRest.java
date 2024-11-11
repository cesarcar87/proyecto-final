package com.proyecto.sistema.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.sistema.clases.DTO.BecaDTO;
import com.proyecto.sistema.clases.sistema.Becas;
import com.proyecto.sistema.clases.sistema.Documento;
import com.proyecto.sistema.exceptions.ResourceNotFoundException;
import com.proyecto.sistema.rest.repositorios.GetDocRepository;
import com.proyecto.sistema.rest.servicios.BecaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/beca")
public class BecaRest {

    @Autowired
    private BecaService becaService;

    @Autowired
    private GetDocRepository getDocRespository;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/becas")
    public List<BecaDTO> listBecas() {
        List<Becas> becas = becaService.obtenerTodasLasBecas();
        List<BecaDTO> becaDTOs = new ArrayList<>();

        for (Becas beca : becas) {
            BecaDTO dto = new BecaDTO();
            dto.setIdBeca(beca.getIdBeca());
            dto.setEstudiante(beca.getEstudiante());
            dto.setReqLlamado(beca.getReqLlamado());
            dto.setEstadoBeca(beca.getEstadoBeca());
            // No incluir documentosPDF en el DTO
            becaDTOs.add(dto);
        }

        return becaDTOs;
    }


    @GetMapping("/becasPorEstudiante")
    public List<BecaDTO> listBecasPorEstudiante(@RequestParam Long estudiante) {
        // Obtener lista de becas del servicio
        List<Becas> becas = becaService.obtenerBecaPorEstudiante(estudiante);

        List<BecaDTO> becaDTOs = new ArrayList<>();

        for (Becas beca : becas) {
            BecaDTO dto = new BecaDTO();
            dto.setIdBeca(beca.getIdBeca());
            dto.setEstudiante(beca.getEstudiante());
            dto.setOtrasBecas(beca.getOtrasBecas());
            dto.setReqLlamado(beca.getReqLlamado());
            dto.setEstadoBeca(beca.getEstadoBeca());
            // No incluir documentosPDF en el DTO
            becaDTOs.add(dto);
        }

        return becaDTOs;
    }

    @GetMapping("/listarBecas")
    public List<Becas> listarTodasLasBecas() {
        // Llamar al servicio de autenticación
        return becaService.obtenerTodasLasBecas();
    }

    @PostMapping("/nuevaBeca")
    public Becas register(
            @RequestParam Long estudiante,
            @RequestParam String tipoBeca,
            @RequestParam("archivosPdf") MultipartFile[] archivosPdf // Recibir múltiples archivos
    ) throws IOException {

        if(tipoBeca.equals("Alimentacion")){
            tipoBeca = "alimentacion";
        }

        if(tipoBeca.equals("Transporte")){
            tipoBeca = "transporte";
        }

        // Crear nueva beca
        Becas newBeca = new Becas();
        newBeca.setEstudiante(estudiante);
        newBeca.setTipoBeca(tipoBeca);
        newBeca.setEstadoBeca("Iniciado");

        // Procesar los archivos PDF y crear la lista de Documentos
        List<Documento> documentos = new ArrayList<>();
        for (MultipartFile archivoPdf : archivosPdf) {
            if (!archivoPdf.isEmpty()) {
                Documento documento = new Documento();
                documento.setContenidoPDF(archivoPdf.getBytes()); // Guardar el archivo como byte[]
                documentos.add(documento); // Agregar el documento a la lista
            }
        }

        // Invocar el proceso de Camunda
        try {
            // Crear el payload para Camunda
            Map<String, Object> variables = new HashMap<>();

            // Formatear variables para el proceso de Camunda
            Map<String, Object> estudianteVar = new HashMap<>();
            estudianteVar.put("value", estudiante);

            Map<String, Object> tipoBecaVar = new HashMap<>();
            tipoBecaVar.put("value", newBeca.getTipoBeca());

            Map<String, Object> estadoBecaVar = new HashMap<>();
            estadoBecaVar.put("value", newBeca.getEstadoBeca());

            // Agregar las variables al cuerpo
            Map<String, Object> processVariables = new HashMap<>();
            processVariables.put("estudiante", estudianteVar);
            processVariables.put("tipoBeca", tipoBecaVar);
            processVariables.put("estadoBeca", estadoBecaVar);

            // Crear el cuerpo de la solicitud
            Map<String, Object> body = new HashMap<>();
            body.put("variables", processVariables);

            System.out.println(new ObjectMapper().writeValueAsString(body)); // Serializa el mapa a JSON

            // URL del proceso en Camunda
            String camundaUrl = "http://localhost:8080/engine-rest/process-definition/key/gestionBeca/start";

            // Enviar la solicitud a Camunda
            ResponseEntity<String> response = restTemplate.postForEntity(camundaUrl, body, String.class);

            // Verificar si la respuesta es satisfactoria
            if (response.getStatusCode().is2xxSuccessful()) {
                // Parsear la respuesta para obtener el processInstanceId
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(response.getBody());
                String processInstanceId = rootNode.path("id").asText();  // Obtener el processInstanceId
                newBeca.setPrcesoCamunda(processInstanceId);

                // Obtener el taskId utilizando el processInstanceId
                String taskUrl = "http://localhost:8080/engine-rest/task?processInstanceId=" + processInstanceId;
                ResponseEntity<String> taskResponse = restTemplate.getForEntity(taskUrl, String.class);

                // Parsear la respuesta para obtener el taskId
                JsonNode taskNode = mapper.readTree(taskResponse.getBody());
                if (taskNode.isArray() && taskNode.size() > 0) {
                    String taskId = taskNode.get(0).path("id").asText();  // Obtener el primer taskId

                    // Preparar la URL para completar la tarea
                    String completeTaskUrl = "http://localhost:8080/engine-rest/task/" + taskId + "/complete";

                    // Crear el cuerpo de la solicitud para completar la tarea
                    Map<String, Object> completeTaskBody = new HashMap<>();
                    completeTaskBody.put("variables", new HashMap<>());  // Aquí puedes agregar las variables necesarias para completar la tarea

                    // Enviar la solicitud POST para completar la tarea
                    ResponseEntity<String> taskCompleteResponse = restTemplate.postForEntity(completeTaskUrl, completeTaskBody, String.class);

                    // Verificar si la respuesta fue exitosa
                    if (taskCompleteResponse.getStatusCode().is2xxSuccessful()) {
                        System.out.println("Tarea completada exitosamente.");
                    } else {
                        System.out.println("Error al completar la tarea: " + taskCompleteResponse.getBody());
                    }
                } else {
                    System.out.println("No se encontraron tareas activas para el processInstanceId: " + processInstanceId);
                }

            } else {
                System.out.println("Error al iniciar el proceso en Camunda: " + response.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al invocar el proceso de Camunda: " + e.getMessage());
        }

        // Asignar la lista de documentos a la beca
        newBeca.setDocumentosPDF(documentos);

        // Llamar al servicio para crear la beca
        return becaService.crearBeca(newBeca);
    }



    @PostMapping("/avanzarBeca")
    public void AvanzarBeca(
            @RequestParam Long idEstudiante,
            @RequestParam String estadoBeca,
            @RequestParam String tipoBeca) throws JsonProcessingException {
        // Llamar al servicio para continuar con la beca
        System.out.println("Parametros de avanzarBeca");
        System.out.println(idEstudiante);
        System.out.println(estadoBeca);
        System.out.println(tipoBeca);


        Becas BecaUpd = becaService.findByEstudianteAndTipoBeca(idEstudiante,tipoBeca);
        String InstanceId = BuscarIntanceId(idEstudiante,tipoBeca);

        System.out.println("Estao de BecaUpd" + BecaUpd.getEstadoBeca());
        String estadoAnt = BecaUpd.getEstadoBeca();
        String nuevoEst = "";
        //Si pasa o si Rechaza cambia el estado

        System.out.println("Estao de Beca antes del switch" +estadoBeca);
        if ("avanza".equals(estadoBeca)){
            switch (estadoAnt){
                case "Iniciado":
                    nuevoEst = "definirFechas";
                    BecaUpd.setEstadoBeca("definirFechas");
                    break;
                case "definirFechas":
                    nuevoEst = "esperandoReunion";
                    BecaUpd.setEstadoBeca("esperandoReunion");
                    break;
                case "esperandoReunion":
                    nuevoEst = "enviarMail";
                    BecaUpd.setEstadoBeca("enviarMail");
                    break;
                case "esperandoResultado":
                    nuevoEst = "enviarMail";
                    BecaUpd.setEstadoBeca("enviarMail");
                    break;
                case "mailEnviado":
                    nuevoEst = "NoNotificadoAceptado";
                    BecaUpd.setEstadoBeca("NoNotificadoAceptado");
                    break;
                case "enviarMail":
                    nuevoEst = "NoNotificadoAceptado";
                    BecaUpd.setEstadoBeca("NoNotificadoAceptado");
                    break;
            }
        } else if (estadoBeca.equals("rechazado")) {
            BecaUpd.setEstadoBeca("Rechazado");
            switch (estadoAnt){
                case "Iniciado":
                    nuevoEst = "Rechazado";
                    BecaUpd.setEstadoBeca("Rechazado");
                    break;
                case "definirFechas":
                    nuevoEst = "Rechazado";
                    BecaUpd.setEstadoBeca("Rechazado");
                    break;
                case "esperandoReunion":
                    nuevoEst = "Rechazado";
                    BecaUpd.setEstadoBeca("Rechazado");
                    break;
                case "esperandoResultado":
                    nuevoEst = "Rechazado";
                    BecaUpd.setEstadoBeca("Rechazado");
                    break;
                case "mailEnviado":
                    nuevoEst = "Rechazado";
                    BecaUpd.setEstadoBeca("Rechazado");
                    break;
                case "enviarMail":
                    nuevoEst = "Rechazado";
                    BecaUpd.setEstadoBeca("Rechazado");
                    break;
            }
        }
//mensaje para commit
        System.out.println("estadoBeca nuevo: "+nuevoEst);

        // Crear el payload para Camunda
        Map<String, Object> estadoBecaVar = new HashMap<>();
        estadoBecaVar.put("value", nuevoEst);
        estadoBecaVar.put("type", "String"); // Especifica el tipo de dato

        // Agregar las variables al cuerpo
        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put("estadoBeca", estadoBecaVar);

        // Crear el cuerpo de la solicitud
        Map<String, Object> body = new HashMap<>();
        body.put("variables", processVariables);

        System.out.println(new ObjectMapper().writeValueAsString(body)); // Serializa el mapa a JSON

        // Preparar la URL para completar la tarea
        String completeTaskUrl = "http://localhost:8080/engine-rest/task/" + InstanceId + "/complete";

        // Enviar la solicitud POST para completar la tarea
        ResponseEntity<String> taskCompleteResponse = restTemplate.postForEntity(completeTaskUrl, body, String.class);

        // Verificar si la respuesta fue exitosa
        if (taskCompleteResponse.getStatusCode().is2xxSuccessful()) {
            System.out.println("Tarea completada exitosamente.");

            becaService.modificarBeca(BecaUpd.getIdBeca(),BecaUpd);
        } else {
            System.out.println("Error al completar la tarea: " + taskCompleteResponse.getBody());
        }
    }

    public String BuscarIntanceId(Long idEstudiante,String tipoBeca) throws JsonProcessingException {
        //Consulta a la base por el estado actual de la beca
        System.out.println("id del estudiante: " + idEstudiante);
        System.out.println("tipo de Beca: " + tipoBeca);

        Becas becaEst = becaService.findByEstudianteAndTipoBeca(idEstudiante,tipoBeca);

        String ProcessIdCamunda = becaEst.getPrcesoCamunda();

        // Obtener el taskId utilizando el processInstanceId
        String taskUrl = "http://localhost:8080/engine-rest/task?processInstanceId=" + ProcessIdCamunda;
        ResponseEntity<String> taskResponse = restTemplate.getForEntity(taskUrl, String.class);

        // Parsear la respuesta para obtener el taskId
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(taskResponse.getBody());

        String mensaje;
        if (rootNode.isArray() && rootNode.size() > 0) {
                String taskId = rootNode.get(0).path("id").asText();  // Obtener el primer taskId
                return taskId;
        } else {
            mensaje = "No se encontraron tareas activas para el processInstanceId: " + ProcessIdCamunda;
            return mensaje;
        }
    }

    //@Transactional(readOnly = true)
    @GetMapping("/documento/{id}")
    public ResponseEntity<byte[]> descargarDocumento(@PathVariable Long id) {
        Documento documento = getDocRespository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Documento no encontrado"));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"documento_" + id + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(documento.getContenidoPDF());
    }



}



