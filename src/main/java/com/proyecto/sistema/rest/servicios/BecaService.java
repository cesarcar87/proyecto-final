package com.proyecto.sistema.rest.servicios;

import com.proyecto.sistema.clases.sistema.Becas;
import com.proyecto.sistema.clases.usuarios.Usuario;
import com.proyecto.sistema.rest.repositorios.GetBecRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class BecaService {

    @Autowired
    private GetBecRespository getBecRespository;

    // Obtener todas las becas
    @GetMapping
    public List<Becas> obtenerTodasLasBecas() {
        return getBecRespository.findAll();
    }


    public List<Becas> obtenerBecaPorEstudiante(Long estudianteId) {
        // Deberías retornar una lista de becas
        return getBecRespository.findByEstudiante(estudianteId);
    }

    public Becas findByEstudianteAndTipoBeca(Long estudianteId,String tipoBeca) {
        // Deberías retornar una lista de becas
        return getBecRespository.findByEstudianteAndTipoBeca(estudianteId,tipoBeca);
    }

    // Método para modificar una beca existente
    public Becas modificarBeca(Long idBeca, Becas nuevaBeca) {
        // Verificamos si la beca con el ID proporcionado existe
        return getBecRespository.findById(idBeca).map(becaExistente -> {
            // Actualizamos los campos de la beca existente con los datos de nuevaBeca
            becaExistente.setEstudiante(nuevaBeca.getEstudiante());
            becaExistente.setOtrasBecas(nuevaBeca.getOtrasBecas());
            becaExistente.setReqLlamado(nuevaBeca.getReqLlamado());
            becaExistente.setEstadoBeca(nuevaBeca.getEstadoBeca());
            becaExistente.setTipoBeca(nuevaBeca.getTipoBeca());
            becaExistente.setPrcesoCamunda(nuevaBeca.getPrcesoCamunda());
            becaExistente.setDocumentosPDF(nuevaBeca.getDocumentosPDF());

            // Guardamos la beca actualizada
            return getBecRespository.save(becaExistente);
        }).orElseThrow(() -> new RuntimeException("No se encontró la beca con el ID: " + idBeca));
    }

    // Crear una nueva Beca
    @PostMapping
    public Becas crearBeca(@RequestBody Becas beca) {
        return getBecRespository.save(beca);
    }



}
