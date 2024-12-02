package com.proyecto.sistema.clases.sistema;

import jakarta.persistence.*;
import spinjar.com.fasterxml.jackson.annotation.JsonIgnore;
import spinjar.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UnidadCurricular {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUnidadCurricular;

    private String carrera;
    private String nombreUC;
    private Long semestreUC;

    @JsonIgnore
    private transient Object hibernateLazyInitializer;

    public Long getIdUnidadCurricular() {
        return idUnidadCurricular;
    }

    public void setIdUnidadCurricular(Long idUnidadCurricular) {
        this.idUnidadCurricular = idUnidadCurricular;
    }

    public String getNombreUC() {
        return nombreUC;
    }

    public void setNombreUC(String nombreUC) {
        this.nombreUC = nombreUC;
    }

    public Long getSemestreUC() {
        return semestreUC;
    }

    public void setSemestreUC(Long semestreUC) {
        this.semestreUC = semestreUC;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }
}
