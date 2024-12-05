package com.proyecto.sistema.rest;

import com.proyecto.sistema.clases.sistema.Clases;
import com.proyecto.sistema.rest.servicios.ClasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/clases")
public class ClasesRest {

    @Autowired
    private ClasesService clasesService;

    @PostMapping("/crearClases")
    public ResponseEntity<String> guardarClases(
            @RequestParam List<String> horarios,
            @RequestParam List<String> dias,
            @RequestParam(required = false) List<String> materias,
            @RequestParam(required = false) List<String> meses,
            @RequestParam(required = false) List<String> cursos,
            @RequestParam(required = false) List<String> semestres,
            @RequestParam(required = false) List<String> enlacesGoogleMeet) {

        try {
            clasesService.guardarClases(horarios, dias, materias, meses, cursos, semestres, enlacesGoogleMeet);
            return ResponseEntity.ok("Clases guardadas exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al guardar las clases: " + e.getMessage());
        }
    }

    @GetMapping("/listarClases")
    public ResponseEntity<List<Clases>> listarTodasLasClases() {
        try {
            List<Clases> clases = clasesService.listarTodasLasClases();
            return ResponseEntity.ok(clases);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/cargarClasesExcel")
    public ResponseEntity<String> cargarClasesDesdeExcel(@RequestParam("file") MultipartFile file) {
        System.out.println("Llamado a Clases desde excel");
        System.out.println("Obtengo el archivo" + file);
        try {
            // Validar que el archivo no esté vacío
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("El archivo está vacío.");
            }

            // Procesar el archivo Excel y guardar los datos en la base de datos
            clasesService.cargarClasesDesdeExcel(file);

            return ResponseEntity.ok("Clases cargadas exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al cargar las clases: " + e.getMessage());
        }
    }

    @PutMapping("/{idClase}/actualizarEnlace")
    public ResponseEntity<String> actualizarEnlaceGoogleMeet(
            @PathVariable Long idClase,
            @RequestBody String nuevoEnlace) {
        try {
            clasesService.actualizarEnlaceGoogleMeet(idClase, nuevoEnlace);
            return ResponseEntity.ok("Enlace de Google Meet actualizado correctamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar el enlace de Google Meet.");
        }
    }

}
