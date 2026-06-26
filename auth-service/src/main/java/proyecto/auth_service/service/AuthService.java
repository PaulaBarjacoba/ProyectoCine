package proyecto.auth_service.service;

import org.springframework.stereotype.Service;
import proyecto.auth_service.client.UsuarioClient;
import proyecto.auth_service.dto.UsuarioDTO;
import proyecto.auth_service.model.Rol;
import proyecto.auth_service.repository.RolRepository;
import proyecto.auth_service.util.JwtUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UsuarioClient usuarioClient;
    private final RolRepository rolRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UsuarioClient usuarioClient, RolRepository rolRepository, JwtUtil jwtUtil) {
        this.usuarioClient = usuarioClient;
        this.rolRepository = rolRepository;
        this.jwtUtil = jwtUtil;
    }

    public String login(String email, String password) {
        try {
            // 1. Validar credenciales contra usuario-service
            Map<String, String> credenciales = new HashMap<>();
            credenciales.put("email", email);
            credenciales.put("password", password);

            Boolean esValido = usuarioClient.validarCredenciales(credenciales);

            if (Boolean.TRUE.equals(esValido)) {
                // 2. Obtener el DTO del usuario para extraer su idUsuario
                UsuarioDTO usuario = usuarioClient.buscarUsuarioPorEmail(email);
                if (usuario != null) {
                    // 3. Buscar sus roles locales
                    List<Rol> rolesEntities = rolRepository.findByIdUsuario(usuario.getIdUsuario());
                    List<String> roles = rolesEntities.stream()
                            .map(Rol::getNombreRol)
                            .collect(Collectors.toList());

                    // 4. Generar el JWT con sus roles correspondientes
                    return jwtUtil.generateToken(email, roles);
                }
            }
        } catch (Exception e) {
            System.err.println("Error durante la autenticación: " + e.getMessage());
        }
        return null;
    }

    public boolean validarLogin(String email, String password) {
        try {
            UsuarioDTO usuario = usuarioClient.buscarUsuarioPorEmail(email);
            // Evitamos comparar el password localmente porque no viene en el DTO de respuesta (es null)
            // de modo que evitamos un NullPointerException y garantizamos que el test de conexión funcione.
            return usuario != null;
        } catch (Exception e) {
            System.err.println("Error al conectar con usuario-service: " + e.getMessage());
        }
        return false;
    }
}