package com.proyecto.sistema.rest.repositorios;

import com.proyecto.sistema.clases.sistema.SolicitudEquipos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GetSolEquiRepository extends JpaRepository<SolicitudEquipos, Long> {
    // Métodos personalizados para búsquedas específicas
    List<SolicitudEquipos> findByEstudianteSol(Long estudianteSol);


}
