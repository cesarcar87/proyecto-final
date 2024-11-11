package com.proyecto.sistema.clases.sistema;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TutEntrePar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTutoria;
    private Long[] estudiantesTutorados;
    private Long estudianteTutor;
    private String unidadCurricularTutoria;

    public Long getIdTutoria() {
        return idTutoria;
    }

    public void setIdTutoria(Long idTutoria) {
        this.idTutoria = idTutoria;
    }

    public Long[] getEstudiantesTutorados() {
        return estudiantesTutorados;
    }

    public void setEstudiantesTutorados(Long[] estudiantesTutorados) {
        this.estudiantesTutorados = estudiantesTutorados;
    }

    public Long getEstudianteTutor() {
        return estudianteTutor;
    }

    public void setEstudianteTutor(Long estudianteTutor) {
        this.estudianteTutor = estudianteTutor;
    }

    public String getUnidadCurricularTutoria() {
        return unidadCurricularTutoria;
    }

    public void setUnidadCurricularTutoria(String unidadCurricularTutoria) {
        this.unidadCurricularTutoria = unidadCurricularTutoria;
    }
}