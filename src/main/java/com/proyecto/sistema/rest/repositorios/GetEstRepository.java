package com.proyecto.sistema.rest.repositorios;

import com.proyecto.sistema.clases.sistema.Becas;
import com.proyecto.sistema.clases.usuarios.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GetEstRepository  extends JpaRepository<Estudiante, Long> {

}
