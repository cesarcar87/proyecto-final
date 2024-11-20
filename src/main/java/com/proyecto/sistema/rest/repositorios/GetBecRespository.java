    package com.proyecto.sistema.rest.repositorios;

    import com.proyecto.sistema.clases.sistema.Becas;
    import com.proyecto.sistema.clases.sistema.Documento;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;

    import java.util.List;

    public interface GetBecRespository extends JpaRepository<Becas, Long> {
        List<Becas> findByEstudiante(Long estudiante); // Método para buscar todas las becas por estudiante
        Becas findByEstudianteAndTipoBeca(Long estudiante, String tipoBeca); // Método para buscar una beca específica por estudiante y tipo de beca

        @Query("SELECT b.documentosPDF FROM Becas b WHERE b.idBeca = :idBeca")
        List<Documento> findDocumentosByBecaId(@Param("idBeca") Long idBeca);

    }

