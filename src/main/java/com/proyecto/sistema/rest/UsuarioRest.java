package com.proyecto.sistema.rest;

import com.proyecto.sistema.clases.usuarios.Estudiante;
import com.proyecto.sistema.clases.usuarios.Usuario;
import com.proyecto.sistema.rest.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioRest {

    @Autowired
    private UsuarioService usuarioService;

    // Obtener todos los usuarios
    @GetMapping("/listar")
    public List<Usuario> listarTodos() {
        return usuarioService.obtenerTodosLosUsuarios();
    }

    @GetMapping("/obtenerInfo")
    public Optional<Estudiante> obtenerInfoEstudiante(@RequestParam Long estudianteId) {
        return usuarioService.obtenerInfoEstudiante(estudianteId);
    }

    // Registrar un nuevo estudiante
    @PostMapping("/register")
    public Estudiante register(@RequestBody Estudiante newEstudiante) {
        return usuarioService.crearEstudiante(newEstudiante);
    }

    // Login con Google OAuth
    @PostMapping("/google-auth")
    public ResponseEntity<Map<String, String>> googleAuth(@RequestBody Map<String, String> payload) throws Exception {
        String accessToken = payload.get("access_token");  // Recibir el access_token directamente
        return usuarioService.googleAuth(accessToken); // Pasar el access_token al servicio
    }

    @PostMapping("/login")
    public Usuario login(@RequestParam String correo) {
        // Llamar al servicio de autenticación
        return usuarioService.login(correo);
    }

    // Registrar un nuevo estudiante
    @PostMapping("/modificarEstudiante")
    public Estudiante modificarEstudiante(@RequestBody Estudiante EstudianteUPD) {
        return usuarioService.modificarEstudiante(EstudianteUPD);
    }


}
