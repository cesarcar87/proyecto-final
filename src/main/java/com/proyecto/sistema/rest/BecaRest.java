package com.proyecto.sistema.rest;

import com.proyecto.sistema.clases.sistema.Becas;
import com.proyecto.sistema.clases.usuarios.Estudiante;
import com.proyecto.sistema.clases.usuarios.Usuario;
import com.proyecto.sistema.rest.servicios.BecaService;
import com.proyecto.sistema.rest.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/beca")
public class BecaRest {

    @Autowired
    private BecaService becaService;

    @GetMapping("/listar")
    public List<Becas> listarTodos() {
        // Llamar al servicio de autenticación
        return becaService.obtenerTodasLasBecas();
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
            @RequestParam("archivoPdf") MultipartFile archivoPdf // Recibir archivo
    ) throws IOException {

        // Crear nueva beca
        Becas newBeca = new Becas();
        newBeca.setEstudiante(estudiante);
        newBeca.setOtrasBecas(otraBecas);
        newBeca.setReqLlamado(reqLLamado);
        newBeca.setEstadoBeca("Iniciado");

        // Procesar el archivo PDF
        if (!archivoPdf.isEmpty()) {
            byte[] contenidoPdf = archivoPdf.getBytes(); // Convertir archivo a bytes
            newBeca.setDocumentoPDF(contenidoPdf); // Guardar el archivo en la entidad
        }

        // Llamar al servicio para crear la beca
        return becaService.crearBeca(newBeca);
    }


}
