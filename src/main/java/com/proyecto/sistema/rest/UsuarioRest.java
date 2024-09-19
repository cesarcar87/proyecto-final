package com.proyecto.sistema.rest;

import com.proyecto.sistema.clases.usuarios.Estudiante;
import com.proyecto.sistema.clases.usuarios.Usuario;
import com.proyecto.sistema.rest.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioRest{

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping("/listar")
    public List<Usuario> listarTodos() {
        // Llamar al servicio de autenticación
        return usuarioService.obtenerTodosLosUsuarios();
    }

    @PostMapping("/register")
    public Estudiante register(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String edad,
            @RequestParam String telefono,
            @RequestParam String correo,
            @RequestParam String password,
            //@RequestParam String datosGoogle,
            @RequestParam String semestre) {
        Estudiante newEstudiante = new Estudiante();
        newEstudiante.setNombre(nombre);
        newEstudiante.setApellido(apellido);
        newEstudiante.setEdad(edad);
        newEstudiante.setTelefono(telefono);
        newEstudiante.setCorreo(correo);
        newEstudiante.setPassword(password);
        newEstudiante.setSemestre(semestre);
        //newEstudiante.setDatosGoogle(datosGoogle);

        // Llamar al servicio de autenticación
        return (Estudiante) usuarioService.crearEstudiante(newEstudiante);
    }

    @PostMapping("/login")
    public Usuario login(@RequestParam String correo, @RequestParam String password) {
        // Llamar al servicio de autenticación
        return usuarioService.login(correo, password);
    }
}
