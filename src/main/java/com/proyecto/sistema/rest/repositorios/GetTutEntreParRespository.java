package com.proyecto.sistema.rest.repositorios;

import com.proyecto.sistema.clases.sistema.TutEntrePar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GetTutEntreParRespository extends JpaRepository<TutEntrePar, Long> {
    // No es necesario declarar el método save aquí
    @Query(value = "SELECT COUNT(*) FROM tut_entre_par y WHERE y.estudiante_tutor = :estudianteTutId", nativeQuery = true)
    long countByEstudianteTutor(@Param("estudianteTutId") Long estudianteId);


}