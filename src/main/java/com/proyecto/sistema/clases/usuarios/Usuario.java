package com.proyecto.sistema.clases.usuarios;

import jakarta.persistence.*;


@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String nombre;
    private String apellido;
    private String edad;
    private String telefono;
    private String correo;
    private String tipoDocumento;
    private String documento;
    private String password;
    private String datoGoogle;
    private String datoGoogle2;
    private Boolean esCoordinador;

    // Getters y setters

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatoGoogle() {
        return datoGoogle;
    }

    public void setDatoGoogle(String datosGoogle) {
        this.datoGoogle = datosGoogle;
    }

    public String getDatoGoogle2() {
        return datoGoogle2;
    }

    public void setDatoGoogle2(String datosGoogle2) {
        this.datoGoogle2 = datosGoogle2;
    }


    public Boolean getEsCoordinador() {
        return esCoordinador;
    }

    public void setEsCoordinador(Boolean esCoordinador) {
        this.esCoordinador = esCoordinador;
    }
}