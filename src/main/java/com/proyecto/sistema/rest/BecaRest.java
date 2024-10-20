package com.proyecto.sistema.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.sistema.configuration.MyCorsConfiguration;
import com.proyecto.sistema.clases.DTO.BecaDTO;
import com.proyecto.sistema.clases.sistema.Becas;
import com.proyecto.sistema.clases.sistema.Documento;
import com.proyecto.sistema.clases.usuarios.Estudiante;
import com.proyecto.sistema.clases.usuarios.Usuario;
import com.proyecto.sistema.rest.servicios.BecaService;
import com.proyecto.sistema.rest.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

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


    @GetMapping("/listar/")
    public List<Becas> listarBecaPorEstudiante() {
        // Llamar al servicio de autenticación
        return becaService.obtenerTodasLasBecas();
    }


    @PostMapping("/nuevaBeca")
    public Becas register(
            @RequestParam Long estudiante,
            @RequestParam String tipoBeca,
            @RequestParam("archivosPdf") MultipartFile[] archivosPdf // Recibir múltiples archivos
    ) throws IOException {

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
            processVariables.put("tipoBecaVar", estadoBecaVar);
            processVariables.put("estadoBeca", estadoBecaVar);

            // Crear el cuerpo de la solicitud
            Map<String, Object> body = new HashMap<>();
            body.put("variables", processVariables);

            System.out.println(new ObjectMapper().writeValueAsString(body)); // Serializa el mapa a JSON

            // URL del proceso en Camunda
            String camundaUrl = "http://localhost:8080/engine-rest/process-definition/key/gestionBecaAlimentacion/start";

            System.out.println("Antes");

            // Enviar la solicitud a Camunda
            ResponseEntity<String> response = restTemplate.postForEntity(camundaUrl, body, String.class);

            System.out.println("Despues");

            // Verificar si la respuesta es satisfactoria
            if (response.getStatusCode().is2xxSuccessful()) {

                System.out.println("Principio");

                // Parsear la respuesta para obtener el processInstanceId
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(response.getBody());
                String processInstanceId = rootNode.path("id").asText();  // Obtener el processInstanceId

                System.out.println("Aqui?");

                // Usar el processInstanceId para obtener el executionId
                String executionUrl = "http://localhost:8080/engine-rest/execution?processInstanceId=" + processInstanceId;
                ResponseEntity<String> executionResponse = restTemplate.getForEntity(executionUrl, String.class);

                System.out.println("ACa?");

                // Parsear la respuesta para obtener el executionId
                JsonNode executionNode = mapper.readTree(executionResponse.getBody());
                String executionId = executionNode.get(0).path("id").asText(); // Obtener el primer executionId

                System.out.println("Esto?");

                //Con el ExecutionId ya puedo completar la primera Task, esto ademas me va a servir para los proximos pasos del proceso
                String completeTaskUrl = "http://localhost:8080/engine-rest/task/" + executionId + "/complete";
                ResponseEntity<String> executionResponseTskCmplt = restTemplate.getForEntity(completeTaskUrl, String.class);


                System.out.println("Proceso de beca de alimentación iniciado en Camunda. ExecutionId: " + executionResponseTskCmplt);

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

    @GetMapping("/avanzarBeca")
    public void AvanzarBeca() {
        // Llamar al servicio para continuar con la beca

    }

}

