package com.proyecto.sistema.rest.servicios;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.proyecto.sistema.clases.usuarios.Estudiante;
import com.proyecto.sistema.clases.usuarios.Usuario;
import com.proyecto.sistema.rest.repositorios.GetEstRepository;
import com.proyecto.sistema.rest.repositorios.GetUsuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private GetUsuRepository getUsuRepository;

    // Constantes para Google OAuth
    private static final String CLIENT_ID = "768873216358-7cdlvnvoc4rfc8vesjkp78d7j3g4ejdc.apps.googleusercontent.com";

    @Autowired
    private GetEstRepository getEstRepository;

    // Obtener todos los usuario
    @GetMapping
    public List<Usuario> obtenerTodosLosUsuarios() {
        return getUsuRepository.findAll();
    }

    public Optional<Estudiante> obtenerInfoEstudiante(Long estudianteId) {
        // Deberías retornar una lista de becas
        return getEstRepository.findById(estudianteId);
    }

    public Estudiante modificarEstudiante(Estudiante estudianteModificado) {
        Optional<Estudiante> estudianteExistente = getEstRepository.findById(estudianteModificado.getId());
        if (estudianteExistente.isPresent()) {
            Estudiante estudiante = estudianteExistente.get();
            estudiante.setDomicilio(estudianteModificado.getDomicilio());
            estudiante.setLocalidad(estudianteModificado.getLocalidad());
            estudiante.setPais(estudianteModificado.getPais());
            estudiante.setTelefono(estudianteModificado.getTelefono());
            estudiante.setItr(estudianteModificado.getItr());
            estudiante.setSemestre(estudianteModificado.getSemestre());
            estudiante.setCarrera(estudianteModificado.getCarrera());

            // Otros campos a actualizar
            return getEstRepository.save(estudiante);
        } else {
            return null;  // Si no existe el estudiante, retorna null
        }
    }

    // Crear un nuevo estudiante
    @PostMapping
    public Estudiante crearEstudiante(@RequestBody Estudiante estudiante) {
        return getEstRepository.save(estudiante);
    }

    // Método de login con Google OAuth
    public ResponseEntity<Map<String, String>> googleAuth(String accessToken) throws GeneralSecurityException, IOException {
        // Verificar el token de acceso usando la API de Google
        GoogleIdToken idToken = verifyAccessToken(accessToken);

        if (idToken == null) {
            return ResponseEntity.status(401).body(Map.of("error", "El access_token no es válido"));
        }

        // Obtener la información del usuario a partir del idToken
        String userId = idToken.getPayload().getSubject();  // ID del usuario
        String email = idToken.getPayload().getEmail(); // Email del usuario
        String fullName = (String) idToken.getPayload().get("name");  // Nombre completo
        String[] nameParts = fullName.split(" ");  // Dividir el nombre completo en partes (nombre y apellido)
        String firstName = nameParts[0];  // Asumimos que el primer nombre es el "firstName"
        String lastName = nameParts.length > 1 ? nameParts[1] : "";  // El apellido será el segundo elemento, si existe

        // Devolver la respuesta con el token, nombre, apellido y demás datos
        return ResponseEntity.ok(Map.of(
                "access_token", accessToken,
                "userId", userId,
                "email", email,
                "firstName", firstName,  // Nombre
                "lastName", lastName     // Apellido
        ));
    }

    // Método para verificar el access_token
    private GoogleIdToken verifyAccessToken(String accessToken) throws GeneralSecurityException, IOException {
        HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        return verifier.verify(accessToken);
    }

    public Usuario login(String correo) {
        // Buscar el usuario en la base de datos usando el repositorio
        Usuario usuario = getUsuRepository.findByCorreo(correo);
        // Validar el usuario encontrado
        if (usuario != null) {
            System.out.print("correo" + correo);
            System.out.print("Se logueo correctamente");
            return usuario; // Inicio de sesión exitoso
        } else {
            return null; // Credenciales inválidas
        }
    }


}
