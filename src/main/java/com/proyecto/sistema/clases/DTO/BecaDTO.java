package com.proyecto.sistema.clases.DTO;

import java.util.Date;

public class BecaDTO {
    private Long idBeca;
    private Long estudiante;
    private Long[] otrasBecas;
    private Boolean reqLlamado;
    private String tipoBeca;
    private String estadoBeca;
    private Date fechaDeSolicitud;

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

    public String getTipoBeca() {
        return tipoBeca;
    }

    public void setTipoBeca(String tipoBeca) {
        this.tipoBeca = tipoBeca;
    }

    public Date getFechaDeSolicitud() {
        return fechaDeSolicitud;
    }

    public void setFechaDeSolicitud(Date fechaDeSolicitud) {
        this.fechaDeSolicitud = fechaDeSolicitud;
    }
}
