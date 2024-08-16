package com.proyecto.clases.sistema;

import com.proyecto.clases.usuarios.Estudiante;
import jakarta.persistence.*;

/*
Nombre completo, documento, correo electrónico, teléfono contacto,
recibe algún otro tipo de beca/ayuda, carrera, ITR, requisitos del llamado (si/no).
Lo demás lo sacamos del formulario y de las bases.
*/



@Entity
public class Becas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBeca;
 /*
    private Estudiante estudiante;
    @ManyToOne
    private String[] otrasBecas;
    private Boolean reqLlamado;
    private String estadoBeca;

    // Getters y setters
    public Long getIdBeca() {
        return idBeca;
    }

    public void setIdBeca(Long idBeca) {
        this.idBeca = idBeca;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public String[] getOtrasBecas() {
        return otrasBecas;
    }

    public void setOtrasBecas(String[] otrasBecas) {
        this.otrasBecas = otrasBecas;
    }

    public Boolean getReqLlamado() {
        return reqLlamado;
    }

    public void setReqLlamado(Boolean reqLlamado) {
        this.reqLlamado = reqLlamado;
    }

    public String getEstadoBeca() {
        return estadoBeca;
    }

    public void setEstadoBeca(String estadoBeca) {
        this.estadoBeca = estadoBeca;
    }

  */
}