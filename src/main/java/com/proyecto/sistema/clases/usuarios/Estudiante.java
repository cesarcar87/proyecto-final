package com.proyecto.sistema.clases.usuarios;

import jakarta.persistence.Entity;

@Entity
public class Estudiante extends Usuario{

    private String semestre;

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }
}
