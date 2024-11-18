package com.proyecto.sistema.rest.repositorios;

import com.proyecto.sistema.clases.sistema.TutEntrePar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GetTutEntreParRespository extends JpaRepository<TutEntrePar, Long> {
    // No es necesario declarar el método save aquí
    @Query(value = "SELECT COUNT(*) FROM tut_entre_par_estudiantes_tutorados t WHERE t.estudiantes_tutorados = :estudianteId", nativeQuery = true)
    long countByEstudianteTutorados(@Param("estudianteId") Long estudianteId);

    @Query(value = "SELECT COUNT(*) FROM tut_entre_par y WHERE y.estudiante_tutor = :estudianteTutId", nativeQuery = true)
    long countByEstudianteTutor(@Param("estudianteTutId") Long estudianteId);


}