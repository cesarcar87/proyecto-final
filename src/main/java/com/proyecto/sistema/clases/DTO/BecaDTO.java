package com.proyecto.sistema.clases.DTO;

public class BecaDTO {
    private Long idBeca;
    private Long estudiante;
    private Long[] otrasBecas;
    private Boolean reqLlamado;
    private String estadoBeca;

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
}
