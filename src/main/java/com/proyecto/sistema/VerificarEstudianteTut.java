package com.proyecto.sistema;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class VerificarEstudianteTut implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Integer estudiante = (Integer) delegateExecution.getVariable("estudiante");;



    }
}
