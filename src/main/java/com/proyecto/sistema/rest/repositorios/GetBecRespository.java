package com.proyecto.sistema.rest.repositorios;

import com.proyecto.sistema.clases.sistema.Becas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GetBecRespository extends JpaRepository<Becas, Long> {
    List<Becas> findByEstudiante(Long estudiante); // Método para buscar por estudiante
}

