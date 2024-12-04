package com.proyecto.sistema.rest.repositorios;

import com.proyecto.sistema.clases.sistema.Becas;
import com.proyecto.sistema.clases.usuarios.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GetEstRepository  extends JpaRepository<Estudiante, Long> {
    @Query("SELECT e FROM Estudiante e WHERE e.correo = :correo")
    Estudiante findEstudianteByCorreo(@Param("correo") String correo);

    Estudiante findByCorreo(String correo);
}
