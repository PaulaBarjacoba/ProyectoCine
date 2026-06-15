package proyecto.usuario_service.controller;

import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.usuario_service.dto.UsuarioRequestDTO;
import proyecto.usuario_service.dto.UsuarioResponseDTO;
import proyecto.usuario_service.service.UsuarioService;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // GET
    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Retorna una lista completa de todos los usuarios registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Consulta exitosa, se entrega la lista de usuarios")
    @ApiResponse(responseCode = "204", description = "Consulta exitosa, pero no hay usuarios registrados")
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodos();

        if (usuarios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }
    }

    // CREAR POST
    @PostMapping
    @Operation(summary = "Registrar un nuevo usuario", description = "Permite agregar un usuario nuevo enviando sus datos en el body")
    @ApiResponse(responseCode = "201", description = "Usuario creado y guardado con éxito")
    public ResponseEntity<UsuarioResponseDTO> guardar(@Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO nuevoUsuario = usuarioService.guardar(dto);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID", description = "Obtiene los detalles de un usuario específico según su identificador único")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado con éxito")
    @ApiResponse(responseCode = "404", description = "El usuario no existe en la base de datos")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(
            @Parameter(description = "ID numérico del usuario a consultar") @PathVariable Integer id) {

        UsuarioResponseDTO usuario = usuarioService.buscarPorId(id);
        if (usuario != null) {
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // BUSCAR POR EMAIL (Para que OpenFeign lo consuma)
    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar usuario por Email (Uso interno)", description = "Endpoint utilizado principalmente por OpenFeign para validar la existencia de usuarios mediante su correo")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente")
    @ApiResponse(responseCode = "404", description = "No se encontró ningún usuario con ese correo")
    public ResponseEntity<UsuarioResponseDTO> buscarPorEmail(
            @Parameter(description = "Correo electrónico exacto del usuario") @PathVariable String email) {

        UsuarioResponseDTO usuario = usuarioService.buscarPorEmail(email);
        if (usuario != null) {
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // ENDPOINT QUE RECIBE MAIL Y PASSWD
    @PostMapping("/validar")
    @Operation(summary = "Validar credenciales de acceso", description = "Verifica si el email y la contraseña coinciden para permitir la autenticación")
    @ApiResponse(responseCode = "200", description = "Retorna true si las credenciales son válidas, o false si son incorrectas")
    public ResponseEntity<Boolean> validarCredenciales(
            @Parameter(description = "Mapa JSON conteniendo las claves 'email' y 'password'") @RequestBody Map<String, String> credenciales) {

        String email = credenciales.get("email");
        String pass = credenciales.get("password");

        boolean esValido = usuarioService.verificarPassword(email, pass);
        return ResponseEntity.ok(esValido);
    }

    // ACTUALIZAR PUT
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos del usuario", description = "Modifica la información de un usuario existente en la base de datos")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "El usuario que se intenta actualizar no existe")
    public ResponseEntity<UsuarioResponseDTO> actualizar(
            @Parameter(description = "ID del usuario a modificar") @PathVariable int id,
            @Valid @RequestBody UsuarioRequestDTO dto) {

        UsuarioResponseDTO actualizado = usuarioService.actualizar(id, dto);
        if (actualizado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario", description = "Borra físicamente a un usuario del sistema mediante su ID")
    @ApiResponse(responseCode = "200", description = "Mensaje confirmando la eliminación exitosa")
    @ApiResponse(responseCode = "400", description = "El usuario no pudo ser eliminado porque no fue encontrado")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID del usuario a eliminar") @PathVariable int id) {

        if (usuarioService.eliminar(id)) {
            return new ResponseEntity<>("Usuario eliminado con exito", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se encontro el usuario", HttpStatus.BAD_REQUEST);
        }
    }
}