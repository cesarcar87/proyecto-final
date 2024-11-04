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
    public Estudiante register(@RequestBody Estudiante newEstudiante) {
        // Llamar al servicio de autenticación
        return (Estudiante) usuarioService.crearEstudiante(newEstudiante);
    }


    /*
    @PostMapping("/register")
    public Estudiante register(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String correo,
            @RequestParam String tipoDocumento,
            @RequestParam String documento,
            @RequestParam String domicilio,
            @RequestParam String localidad,
            @RequestParam String pais,
            @RequestParam String fechaNacimiento,
            @RequestParam String edad,
            @RequestParam String telefono,
            @RequestParam String carrera,
            @RequestParam String itr,
            @RequestParam String semestre,
            @RequestParam String password) {
        Estudiante newEstudiante = new Estudiante();
        newEstudiante.setNombre(nombre);
        newEstudiante.setApellido(apellido);
        newEstudiante.setCorreo(correo);
        newEstudiante.setTipoDocumento(tipoDocumento);
        newEstudiante.setDocumento(documento);
        newEstudiante.setDomicilio(domicilio);
        newEstudiante.setLocalidad(localidad);
        newEstudiante.setPais(pais);
        newEstudiante.setFechaNacimiento(fechaNacimiento);
        newEstudiante.setEdad(edad);
        newEstudiante.setTelefono(telefono);
        newEstudiante.setCarrera(carrera);
        newEstudiante.setItr(itr);
        newEstudiante.setSemestre(semestre);
        newEstudiante.setPassword(password);

        // Llamar al servicio de autenticación
        return (Estudiante) usuarioService.crearEstudiante(newEstudiante);
    }
*/

    @PostMapping("/login")
    public Usuario login(@RequestParam String correo, @RequestParam String password) {
        // Llamar al servicio de autenticación
        return usuarioService.login(correo, password);
    }
}
