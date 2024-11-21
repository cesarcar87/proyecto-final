package com.proyecto.sistema.clases.sistema;

import jakarta.persistence.*;

@Entity
public class UnidadesCurriculares {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUnidadCurricular;
    private String Nombre;
    private Long Semestre;

    public Long getIdUnidadCurricular() {
        return idUnidadCurricular;
    }

    public void setIdUnidadCurricular(Long idUnidadCurricular) {
        this.idUnidadCurricular = idUnidadCurricular;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public Long getSemestre() {
        return Semestre;
    }

    public void setSemestre(Long semestre) {
        Semestre = semestre;
    }
}
