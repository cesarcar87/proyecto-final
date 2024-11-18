package com.proyecto.sistema.clases.sistema;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class TutEntrePar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTutoria;

    @ElementCollection
    private List<Long> estudiantesTutorados;

    private Long estudianteTutor;
    private Long unidadCurricularTutoria;
    private String descripcionTutoria;
    private String estadoTutoria;
    private String procesoCamunda;

    public Long getIdTutoria() {
        return idTutoria;
    }

    public void setIdTutoria(Long idTutoria) {
        this.idTutoria = idTutoria;
    }

    public List<Long> getEstudiantesTutorados() {
        return estudiantesTutorados;
    }

    public void setEstudiantesTutorados(List<Long> estudiantesTutorados) {
        this.estudiantesTutorados = estudiantesTutorados;
    }

    public Long getEstudianteTutor() {
        return estudianteTutor;
    }

    public void setEstudianteTutor(Long estudianteTutor) {
        this.estudianteTutor = estudianteTutor;
    }

    public Long getUnidadCurricularTutoria() {
        return unidadCurricularTutoria;
    }

    public void setUnidadCurricularTutoria(Long unidadCurricularTutoria) {
        this.unidadCurricularTutoria = unidadCurricularTutoria;
    }

    public String getDescripcionTutoria() {
        return descripcionTutoria;
    }

    public void setDescripcionTutoria(String descripcionTutoria) {
        this.descripcionTutoria = descripcionTutoria;
    }

    public String getProcesoCamunda() {
        return procesoCamunda;
    }

    public void setProcesoCamunda(String procesoCamunda) {
        this.procesoCamunda = procesoCamunda;
    }

    public String getEstadoTutoria() {
        return estadoTutoria;
    }

    public void setEstadoTutoria(String estadoTutoria) {
        this.estadoTutoria = estadoTutoria;
    }
}