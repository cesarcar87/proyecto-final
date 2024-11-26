package com.proyecto.sistema.clases.sistema;

import jakarta.persistence.*;

@Entity
public class UnidadCurricular {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUnidadCurricular;

    private String carrera;
    private String nombreUC;
    private Long semestreUC;

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
