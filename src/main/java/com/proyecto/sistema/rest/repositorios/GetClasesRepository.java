package com.proyecto.sistema.rest.repositorios;

import com.proyecto.sistema.clases.sistema.Clases;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GetClasesRepository extends JpaRepository<Clases, Long> {
    // No necesitas agregar un método adicional si solo quieres obtener todas las unidades curriculares.
}