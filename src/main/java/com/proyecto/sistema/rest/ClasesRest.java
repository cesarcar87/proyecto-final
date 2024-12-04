package com.proyecto.sistema.rest;

import com.proyecto.sistema.clases.sistema.Clases;
import com.proyecto.sistema.rest.servicios.ClasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clases")
public class ClasesRest {

    @Autowired
    private ClasesService clasesService;

    /**
     * Endpoint para guardar una lista de clases.
     *
     * @param horarios Lista de horarios (obligatorio).
     * @param dias Lista de días (obligatorio).
     * @param materias Lista de materias (opcional).
     * @param meses Lista de meses (opcional).
     * @param cursos Lista de cursos (opcional).
     * @param semestres Lista de semestres (opcional).
     * @param enlacesGoogleMeet Lista de enlaces de Google Meet (opcional).
     * @return Respuesta con mensaje de éxito.
     */
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

    /**
     * Endpoint para listar todas las clases.
     *
     * @return Lista de todas las clases en la base de datos.
     */
    @GetMapping("/listarClases")
    public ResponseEntity<List<Clases>> listarTodasLasClases() {
        try {
            List<Clases> clases = clasesService.listarTodasLasClases();
            return ResponseEntity.ok(clases);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
