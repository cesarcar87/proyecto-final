package com.proyecto.clases.repositorios;
import com.proyecto.clases.usuarios.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GestEstRepository extends JpaRepository<Estudiante, Long> {
    // Métodos de consulta personalizados si es necesario

}
