package com.proyecto.sistema.rest;

import com.proyecto.sistema.clases.DTO.BecaDTO;
import com.proyecto.sistema.clases.sistema.Becas;
import com.proyecto.sistema.clases.sistema.Documento;
import com.proyecto.sistema.clases.usuarios.Estudiante;
import com.proyecto.sistema.clases.usuarios.Usuario;
import com.proyecto.sistema.rest.servicios.BecaService;
import com.proyecto.sistema.rest.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/beca")
public class BecaRest {

    @Autowired
    private BecaService becaService;

    @GetMapping("/becas")
    public List<BecaDTO> listBecas() {
        List<Becas> becas = becaService.obtenerTodasLasBecas();
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
            @RequestParam Long[] otraBecas,
            @RequestParam Boolean reqLLamado,
            @RequestParam("archivosPdf") MultipartFile[] archivosPdf // Recibir múltiples archivos
    ) throws IOException {

        // Crear nueva beca
        Becas newBeca = new Becas();
        newBeca.setEstudiante(estudiante);
        newBeca.setOtrasBecas(otraBecas);
        newBeca.setReqLlamado(reqLLamado);
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

        // Asignar la lista de documentos a la beca
        newBeca.setDocumentosPDF(documentos);

        // Llamar al servicio para crear la beca
        return becaService.crearBeca(newBeca);
    }




}
