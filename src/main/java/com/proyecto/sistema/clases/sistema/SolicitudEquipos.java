package com.proyecto.sistema.clases.sistema;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class SolicitudEquipos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSolicitud;

    private Long estudianteSol;
    private Long fechaSolicitud;
    private String equipoSol;
    private String descripcionSol;
    private String procesoCamundaSol;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "idSolicitud")  // Define la clave foránea explícitamente si es necesario
    private List<Documento> documentosPDFEst;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "idSolicitud")
    private List<Documento> documentosPDFCor;

    public Long getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(Long idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public Long getEstudianteSol() {
        return estudianteSol;
    }

    public void setEstudianteSol(Long estudianteSol) {
        this.estudianteSol = estudianteSol;
    }

    public Long getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Long fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getEquipoSol() {
        return equipoSol;
    }

    public void setEquipoSol(String equipoSol) {
        this.equipoSol = equipoSol;
    }

    public String getDescripcionSol() {
        return descripcionSol;
    }

    public void setDescripcionSol(String descripcionSol) {
        this.descripcionSol = descripcionSol;
    }

    public List<Documento> getDocumentosPDFEst() {
        return documentosPDFEst;
    }

    public void setDocumentosPDFEst(List<Documento> documentosPDFEst) {
        this.documentosPDFEst = documentosPDFEst;
    }

    public List<Documento> getDocumentosPDFCor() {
        return documentosPDFCor;
    }

    public void setDocumentosPDFCor(List<Documento> documentosPDFCor) {
        this.documentosPDFCor = documentosPDFCor;
    }

    public String getProcesoCamundaSol() {
        return procesoCamundaSol;
    }

    public void setProcesoCamundaSol(String procesoCamundaSol) {
        this.procesoCamundaSol = procesoCamundaSol;
    }
}

