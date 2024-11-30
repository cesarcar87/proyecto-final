package com.proyecto.sistema.rest.servicios;

import com.proyecto.sistema.clases.sistema.Becas;
import com.proyecto.sistema.clases.sistema.SolicitudEquipos;
import com.proyecto.sistema.rest.repositorios.GetSolEquiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SoliEquiposService {

    @Autowired
    private GetSolEquiRepository getSolEquiRepository;

    // Crear una solicitud
    @Transactional
    public SolicitudEquipos crearSolicitud(SolicitudEquipos solicitudEquipos) {
        return getSolEquiRepository.save(solicitudEquipos);
    }

    // Listar todas las solicitudes
    public List<SolicitudEquipos> listarSolicitudes() {
        return getSolEquiRepository.findAll();
    }

    // Listar solicitudes por estudiante
    public List<SolicitudEquipos> listarSolicitudesPorEstudiante(Long estudianteSol) {
        return getSolEquiRepository.findByEstudianteSol(estudianteSol);
    }

    // Borrar una solicitud por ID
    @Transactional
    public void borrarSolicitud(Long idSolicitud) {
        getSolEquiRepository.deleteById(idSolicitud);
    }

    // Obtener solicitud por ID
    public Optional<SolicitudEquipos> obtenerSolicitudPorId(Long idSolicitud) {
        return getSolEquiRepository.findById(idSolicitud);
    }

    // Descargar los documentos asociados a la solicitud
    // Este método debería ser parte de otro servicio de gestión de documentos,
    // ya que el archivo puede estar almacenado de manera específica en el sistema.
    // Asumimos que el objeto Documento tiene una referencia al archivo o ruta para descargarlo.
    public <Documento> List<Documento> descargarDocumentos(Long idSolicitud) {
        Optional<SolicitudEquipos> solicitudEquipos = getSolEquiRepository.findById(idSolicitud);
        if (solicitudEquipos.isPresent()) {
            SolicitudEquipos solicitud = solicitudEquipos.get();
            // Aquí se devuelven los documentos asociados
            return (List<Documento>) solicitud.getDocumentosPDFEst();  // O 'documentosPDFCor' dependiendo de los requerimientos
        }
        return null;
    }

    public SolicitudEquipos modificarSolicitud(Long idSolicitud, SolicitudEquipos nuevaSolicitud) {
        // Verificamos si la beca con el ID proporcionado existe
        return getSolEquiRepository.findById(idSolicitud).map(solicitudExistente -> {
            // Actualizamos los campos de la beca existente con los datos de nuevaBeca
            nuevaSolicitud.setEstadoSolicitud(nuevaSolicitud.getEstadoSolicitud());
            // Guardamos la beca actualizada
            return getSolEquiRepository.save(solicitudExistente);
        }).orElseThrow(() -> new RuntimeException("No se encontró la Solicitud con el ID: " + idSolicitud));
    }
}
