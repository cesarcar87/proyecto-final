    package com.proyecto.sistema.clases.sistema;

    import jakarta.persistence.*;

    @Entity
    public class Documento {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long idDocumento;

        @Lob
        private byte[] contenidoPDF; // Almacena los bytes del archivo PDF

    // Getters y setters

    public Long getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(Long idDocumento) {
        this.idDocumento = idDocumento;
    }

    public byte[] getContenidoPDF() {
        return contenidoPDF;
    }

    public void setContenidoPDF(byte[] contenidoPDF) {
        this.contenidoPDF = contenidoPDF;
    }
}
