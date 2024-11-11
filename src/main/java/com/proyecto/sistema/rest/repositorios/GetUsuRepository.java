package com.proyecto.sistema.rest.repositorios;
import com.proyecto.sistema.clases.usuarios.Estudiante;
import com.proyecto.sistema.clases.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GetUsuRepository  extends JpaRepository<Usuario, Long>{
    // Método para encontrar un usuario por nombre de usuario y contraseña
    Usuario findByCorreo(String correo);
}

