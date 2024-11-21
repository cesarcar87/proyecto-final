package com.proyecto.sistema.rest.repositorios;

import com.proyecto.sistema.clases.sistema.UnidadesCurriculares;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GetUCRepository  extends JpaRepository<UnidadesCurriculares, Long> {
    // Método para encontrar un usuario por nombre de usuario y contraseña
}

