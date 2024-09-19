package com.proyecto.sistema.clases.sistema;

import com.proyecto.sistema.clases.usuarios.Estudiante;
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
    private Long estudiante;
    private Long[] otrasBecas;
    private Boolean reqLlamado;
    private String estadoBeca;

    @Lob
    private byte[] documentoPDF;

    public Long getIdBeca() {
        return idBeca;
    }

    public void setIdBeca(Long idBeca) {

        this.idBeca = idBeca;
    }

    public Long getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Long estudiante) {
        this.estudiante = estudiante;
    }

    public Long[] getOtrasBecas() {
        return otrasBecas;
    }

    public void setOtrasBecas(Long[] otrasBecas) {
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

    public byte[] getDocumentoPDF() {
        return documentoPDF;
    }

    public void setDocumentoPDF(byte[] documentoPDF) {
        this.documentoPDF = documentoPDF;
    }
}