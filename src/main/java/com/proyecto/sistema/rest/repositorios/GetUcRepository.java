package com.proyecto.sistema.rest.repositorios;

import com.proyecto.sistema.clases.sistema.UnidadCurricular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GetUcRepository extends JpaRepository<UnidadCurricular, Long> {
    // No necesitas agregar un método adicional si solo quieres obtener todas las unidades curriculares.
}
