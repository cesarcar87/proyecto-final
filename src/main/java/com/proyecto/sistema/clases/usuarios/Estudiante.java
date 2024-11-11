package com.proyecto.sistema.clases.usuarios;

import jakarta.persistence.Entity;

@Entity
public class Estudiante extends Usuario{

    private String semestre;
    private String fechaNacimiento;
    private String carrera;
    private String itr;
    private String domicilio;
    private String localidad;
    private String pais;
    
    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getItr() {
        return itr;
    }

    public void setItr(String itr) {
        this.itr = itr;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}
