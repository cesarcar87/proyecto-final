package com.proyecto.sistema.clases.sistema;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TutEntrePar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTutoria;
    private Long[] estudiantesTutorados;
    private Long estudianteTutor;
    private Sting unidadCurricularTutoria;




}