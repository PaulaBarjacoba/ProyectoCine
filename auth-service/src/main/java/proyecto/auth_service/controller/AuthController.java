package proyecto.auth_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.auth_service.dto.LoginRequestDTO;
import proyecto.auth_service.dto.LoginResponseDTO;
import proyecto.auth_service.service.AuthService;
import proyecto.auth_service.util.JwtUtil;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "API Autenticación", description = "API para validación de acceso y asignación de roles de seguridad")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    // Inyección por constructor
    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Valida las credenciales con usuario-service, obtiene roles locales y retorna el JWT")
    @ApiResponse(responseCode = "200", description = "Autenticación exitosa, entrega del token JWT")
    @ApiResponse(responseCode = "401", description = "Credenciales de acceso incorrectas")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        String token = authService.login(loginRequest.getEmail(), loginRequest.getPassword());

        if (token != null) {
            List<String> roles = jwtUtil.extractRoles(token);
            return ResponseEntity.ok(new LoginResponseDTO(token, loginRequest.getEmail(), roles));
        }

        return ResponseEntity.status(401).body("Credenciales incorrectas");
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