package com.proyecto.sistema.clases.sistema;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Date;
import java.util.List;

@Entity
public class SolicitudEquipos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSolicitud;

    private Long estudianteSol;
    private Date fechaDeSolicitud;
    private String equipoSol;
    private String descripcionSol;
    private String estadoSolicitud;
    private String procesoCamundaSol;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) // Relación OneToMany con los documentos
    @Fetch(FetchMode.JOIN)
    private List<Documento> documentosPDFEst;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) // Relación OneToMany con los documentos
    @Fetch(FetchMode.JOIN)
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

    public Date getFechaDeSolicitud() {
        return fechaDeSolicitud;
    }

    public void setFechaDeSolicitud(Date fechaDeSolicitud) {
        this.fechaDeSolicitud = fechaDeSolicitud;
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

    public String getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(String estadoSolicitud) {
        this.estadoSolicitud = estadoSolicitud;
    }
}

