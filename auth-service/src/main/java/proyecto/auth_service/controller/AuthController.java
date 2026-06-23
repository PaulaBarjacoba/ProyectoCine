package proyecto.auth_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.auth_service.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "API Autenticación", description = "API para validación de acceso y asignación de roles de seguridad")
public class AuthController {

    private final AuthService authService;

    // Inyección por constructor
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/test-conexion/{email}")
    @Operation(summary = "Probar conexión con usuario-service", description = "Verifica si el microservicio de usuarios está respondiendo y accesible desde auth-service")
    @ApiResponse(responseCode = "200", description = "Conexión a usuario-service establecida correctamente y usuario encontrado")
    @ApiResponse(responseCode = "404", description = "El usuario no existe o se perdió la conexión con el microservicio de usuarios")
    public ResponseEntity<String> testConexion(
            @Parameter(description = "Correo electrónico del usuario para verificar la conectividad") @PathVariable String email) {
        // Para el test, enviamos un password dummy, ya que solo queremos probar la conexión
        boolean existe = authService.validarLogin(email, "password123");

        return existe ? ResponseEntity.ok("Conexión a usuario-service OK")
                : ResponseEntity.status(404).body("No se encontró al usuario o falló la conexión");
    }
}