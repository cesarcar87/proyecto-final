package com.proyecto.sistema.rest.repositorios;

import com.proyecto.sistema.clases.usuarios.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GetEstRepository  extends JpaRepository<Estudiante, Long> {
    // Método para encontrar un usuario por nombre de usuario y contraseña
}
