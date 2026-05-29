package proyecto.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.auth_service.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    // Inyección por constructor
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/test-conexion/{email}")
    public ResponseEntity<String> testConexion(@PathVariable String email) {
        // Para el test, enviamos un password dummy, ya que solo queremos probar la conexión
        boolean existe = authService.validarLogin(email, "password123");

        return existe ? ResponseEntity.ok("Conexión a usuario-service OK")
                : ResponseEntity.status(404).body("No se encontró al usuario o falló la conexión");
    }
}