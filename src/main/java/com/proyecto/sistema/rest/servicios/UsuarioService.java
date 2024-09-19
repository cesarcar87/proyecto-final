package com.proyecto.sistema.rest.servicios;

import com.proyecto.sistema.clases.usuarios.Estudiante;
import com.proyecto.sistema.clases.usuarios.Usuario;
import com.proyecto.sistema.rest.repositorios.GetEstRepository;
import com.proyecto.sistema.rest.repositorios.GetUsuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private GetUsuRepository getUsuRepository;

    @Autowired
    private GetEstRepository getEstRepository;

    // Obtener todos los usuario
    @GetMapping
    public List<Usuario> obtenerTodosLosUsuarios() {
        return getUsuRepository.findAll();
    }

    // Crear un nuevo estudiante
    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return getUsuRepository.save(usuario);
    }

    // Crear un nuevo estudiante
    @PostMapping
    public Usuario crearEstudiante(@RequestBody Estudiante estudiante) {
        return getEstRepository.save(estudiante);
    }


    public Usuario login(String correo, String password) {
        // Buscar el usuario en la base de datos usando el repositorio
        Usuario usuario = getUsuRepository.findByCorreoAndPassword(correo, password);


        // Validar el usuario encontrado
        if (usuario != null) {
            System.out.print("correo" + correo);
            System.out.print("contraseña" + password);
            System.out.print("Se logueo correctamente");
            return usuario; // Inicio de sesión exitoso
        } else {
            return null; // Credenciales inválidas
        }
    }
}
