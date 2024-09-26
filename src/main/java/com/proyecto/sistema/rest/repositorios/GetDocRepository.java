package com.proyecto.sistema.rest.repositorios;
import com.proyecto.sistema.clases.sistema.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GetDocRepository extends JpaRepository<Documento, Long> {
}
