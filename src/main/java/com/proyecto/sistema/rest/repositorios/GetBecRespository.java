package com.proyecto.sistema.rest.repositorios;

import com.proyecto.sistema.clases.sistema.Becas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GetBecRespository extends JpaRepository<Becas, Long> {
    Becas findByEstudiante(Long estudiante);
}
