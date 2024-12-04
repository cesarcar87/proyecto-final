package com.proyecto.sistema.clases.DTO;

public class UnidadCurricularDTO {
    private Long idUnidadCurricular;
    private String carrera;
    private String nombreUC;
    private Long semestreUC;

    public UnidadCurricularDTO(Long idUnidadCurricular, String carrera, String nombreUC, Long semestreUC) {
        this.idUnidadCurricular = idUnidadCurricular;
        this.carrera = carrera;
        this.nombreUC = nombreUC;
        this.semestreUC = semestreUC;
    }

    public Long getIdUnidadCurricular() {
        return idUnidadCurricular;
    }

    public void setIdUnidadCurricular(Long idUnidadCurricular) {
        this.idUnidadCurricular = idUnidadCurricular;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
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
}
