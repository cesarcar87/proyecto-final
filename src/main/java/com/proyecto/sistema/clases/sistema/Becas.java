package com.proyecto.sistema.clases.sistema;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
public class Becas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBeca;
    private Long estudiante;
    private Long[] otrasBecas;
    private Boolean reqLlamado;
    private String estadoBeca;
    private String tipoBeca;
    private String prcesoCamunda;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) // Relación OneToMany con los documentos
    @Fetch(FetchMode.JOIN)
    private List<Documento> documentosPDF;


    // Getters y setters
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

    public List<Documento> getDocumentosPDF() {
        return documentosPDF;
    }

    public void setDocumentosPDF(List<Documento> documentosPDF) {
        this.documentosPDF = documentosPDF;
    }

    public String getTipoBeca() {
        return tipoBeca;
    }

    public void setTipoBeca(String tipoBeca) {
        this.tipoBeca = tipoBeca;
    }

    public String getPrcesoCamunda() {
        return prcesoCamunda;
    }

    public void setPrcesoCamunda(String prcesoCamunda) {
        this.prcesoCamunda = prcesoCamunda;
    }
}
