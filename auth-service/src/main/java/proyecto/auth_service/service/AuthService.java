package proyecto.auth_service.service;

import org.springframework.stereotype.Service;
import proyecto.auth_service.client.UsuarioClient;
import proyecto.auth_service.dto.UsuarioDTO;

@Service
public class AuthService {

    private final UsuarioClient usuarioClient;

    public AuthService(UsuarioClient usuarioClient) {
        this.usuarioClient = usuarioClient;
    }

    public boolean validarLogin(String email, String password) {
        try {
            UsuarioDTO usuario = usuarioClient.buscarUsuarioPorEmail(email);

            // Validación lógica
            if (usuario != null && usuario.getPassword().equals(password)) {
                System.out.println("Login exitoso para: " + usuario.getEmail());
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error al conectar con usuario-service: " + e.getMessage());
        }
        return false;
    }
}