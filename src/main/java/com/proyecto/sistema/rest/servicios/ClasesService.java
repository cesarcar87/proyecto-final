package com.proyecto.sistema.rest.servicios;

import com.proyecto.sistema.clases.sistema.Clases;
import com.proyecto.sistema.rest.repositorios.GetClasesRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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

    public void cargarClasesDesdeExcel(MultipartFile file) throws Exception {
        System.out.println("Llega hasta aqui");

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            System.out.println("Ingresa al try");
            Sheet sheet = workbook.getSheetAt(0); // Primera hoja del Excel
            if (sheet == null) {
                throw new Exception("La hoja del archivo está vacía o no existe.");
            }

            System.out.println("Pasa el tema de Sheets");
            List<Clases> clases = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Saltar la cabecera
                System.out.println("Procesando fila: " + row.getRowNum());
                Clases clase = new Clases();

                try {
                    clase.setHorario(getCellValue(row.getCell(0)));
                    clase.setDia(getCellValue(row.getCell(1)));
                    clase.setMateria(getCellValue(row.getCell(2)));
                    clase.setMes(getCellValue(row.getCell(3)));
                    clase.setCurso(getCellValue(row.getCell(4)));
                    clase.setSemestre(getCellValue(row.getCell(5)));
                    clase.setEnlacesGoogleMeet(getCellValue(row.getCell(6)));
                    clases.add(clase);
                    System.out.println("Clase agregada: " + clase);
                } catch (Exception e) {
                    System.err.println("Error al procesar la fila " + row.getRowNum() + ": " + e.getMessage());
                }
            }

            System.out.println("pasa por aca");
            if (clases.isEmpty()) {
                throw new Exception("No se pudieron procesar las clases. Verifica el formato del archivo.");
            }

            getClasesRepository.saveAll(clases);
            System.out.println("Clases guardadas con éxito.");
        } catch (IOException e) {
            System.err.println("Error al leer el archivo Excel: " + e.getMessage());
            throw new Exception("Error al procesar el archivo Excel.");
        }
    }

    // Método para manejar celdas nulas o con diferentes tipos de datos
    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:  return cell.getStringCellValue();
            case NUMERIC: return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
        }
    }

    public void actualizarEnlaceGoogleMeet(Long idClase, String nuevoEnlace) {
        Clases clase = getClasesRepository.findById(idClase)
                .orElseThrow(() -> new IllegalArgumentException("La clase con ID " + idClase + " no existe."));
        clase.setEnlacesGoogleMeet(nuevoEnlace);
        getClasesRepository.save(clase);
    }


}
