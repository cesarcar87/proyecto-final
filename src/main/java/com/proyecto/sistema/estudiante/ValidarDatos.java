package com.proyecto.sistema.estudiante;
import jakarta.inject.Named;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import com.fabdelgado.ciuy.*;

@Named
public class ValidarDatos implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String nombre = (String) delegateExecution.getVariable("nombre");
        System.out.println(nombre);
        //String tipoDocumento = (String) delegateExecution.getVariable("nombre");
        String documento = (String) delegateExecution.getVariable("documento");
        System.out.println(documento);
        String correo = (String) delegateExecution.getVariable("correo");
        System.out.println(correo);
        String telefono = (String) delegateExecution.getVariable("telefono");
        System.out.println(telefono);
        String otrasBecas = (String) delegateExecution.getVariable("otrasBecas");
        System.out.println(otrasBecas);
        String carrera = (String) delegateExecution.getVariable("carrera");
        System.out.println(carrera);
        String itr = (String) delegateExecution.getVariable("itr");
        System.out.println(itr);
        String estadoBeca = "ingresada";
        String error;


        int telefonoInt = Integer.valueOf(telefono);

        estadoBeca = validarDatos(nombre,documento,correo,telefonoInt,otrasBecas,carrera,itr);

        System.out.println(estadoBeca);

        String idBeca = "1";

        delegateExecution.setVariable("idBeca",idBeca);
        delegateExecution.setVariable("nombre",nombre);
        delegateExecution.setVariable("estadoBeca",estadoBeca);

    }

    public static String validarDatos(String nombre, String documento, String correo, int telefono, String otrasBecas, String carrera, String itr) {
        String estBec;

        String verifDoc = esDocumentoValido(documento);
        String verifCor = esCorreoValido(correo);
        String verifTel = esTelefonoValido(telefono);

        if (verifDoc == "valido"){
            if (verifCor == "valido") {
                if (verifTel == "valido") {
                    estBec = "pasaValidDocs";
                    return estBec;
                } else {
                    estBec = esTelefonoValido(telefono);
                    return estBec;
                }
            }
            estBec = (esCorreoValido(correo));
            return estBec;
        }else{
            estBec = (esDocumentoValido(documento));
            return estBec;
        }
    }

    // Valida si el documento uruguayo es válido
    public static String esDocumentoValido(String documento) {
        String error = "valido";
        Boolean verDoc;

        if (documento == "1") {
            //Validador de la cedula
            Validator validator = new Validator();
            verDoc = validator.validateCi(documento);
            //Si no tiene el guion antes, se lo agrego
            if (documento.substring(0, documento.length() - 1) != "-") {
                documento = documento.substring(0, documento.length() - 1) + "-" + documento.charAt(documento.length() - 1);
            }
            // Documento uruguayo formato, 7 o 8 dígitos seguido de un guion y un dígito (ej: 1234567-8)
            if (documento != null && documento.matches("\\d{7,8}-\\d") && verDoc) {
                error = "valido";
            } else {
                error = "1 - El documento ingresado es invalido.";
            }
        }

        return error;
    }

    // Valida si el correo es válido
    public static String esCorreoValido(String correo) {
        String error;
        String patronCorreo = "^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$";
        if(correo != null && correo.matches(patronCorreo)){
            error = "valido";
        } else {
            error = "2 - El correo ingresado es inválido.";
        }
        return error;
    }


    // Valida si el teléfono es válido (considerando que sea un número de 8 dígitos en Uruguay)
    public static String esTelefonoValido(int telefono) {
        String error;
        String telefonoStr = String.valueOf(telefono);
        if (telefonoStr.matches("\\d{8}")){
            error = "valido";

        }else{
            error = "3 - el telefono ingresado es invalido.";
        }
        return error;
    }
}
