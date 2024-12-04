package com.proyecto.sistema.rest.servicios;

import com.proyecto.sistema.clases.sistema.Clases;
import com.proyecto.sistema.rest.repositorios.GetClasesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClasesService {

    @Autowired
    private GetClasesRepository getClasesRepository;

    /**
     * Método para guardar una lista de clases.
     *
     * @param horarios Lista de horarios.
     * @param dias Lista de días.
     * @param materias Lista de materias (pueden ser null).
     * @param meses Lista de meses (pueden ser null).
     * @param cursos Lista de cursos (pueden ser null).
     * @param semestres Lista de semestres (pueden ser null).
     * @param enlacesGoogleMeet Lista de enlaces de Google Meet (pueden ser null).
     */
    public void guardarClases(
            List<String> horarios,
            List<String> dias,
            List<String> materias,
            List<String> meses,
            List<String> cursos,
            List<String> semestres,
            List<String> enlacesGoogleMeet) {

        // Validar que las listas de horarios y días no sean null o vacías
        if (horarios == null || dias == null || horarios.isEmpty() || dias.isEmpty()) {
            throw new IllegalArgumentException("Los campos horarios y días son obligatorios y no pueden estar vacíos.");
        }

        // Validar que las listas tengan la misma cantidad de elementos
        int totalRegistros = horarios.size();
        if (dias.size() != totalRegistros) {
            throw new IllegalArgumentException("Las listas de horarios y días deben tener el mismo tamaño.");
        }

        // Iterar sobre las listas y crear objetos Clase
        for (int i = 0; i < totalRegistros; i++) {
            Clases clase = new Clases();
            clase.setHorario(horarios.get(i));
            clase.setDia(dias.get(i));
            clase.setMateria(materias != null && i < materias.size() ? materias.get(i) : null);
            clase.setMes(meses != null && i < meses.size() ? meses.get(i) : null);
            clase.setCurso(cursos != null && i < cursos.size() ? cursos.get(i) : null);
            clase.setSemestre(semestres != null && i < semestres.size() ? semestres.get(i) : null);
            clase.setEnlacesGoogleMeet(enlacesGoogleMeet != null && i < enlacesGoogleMeet.size() ? enlacesGoogleMeet.get(i) : null);

            // Guardar cada clase en la base de datos
            getClasesRepository.save(clase);
        }
    }

    /**
     * Método para listar todas las clases.
     *
     * @return Lista de todas las clases.
     */
    public List<Clases> listarTodasLasClases() {
        return getClasesRepository.findAll();
    }
}
