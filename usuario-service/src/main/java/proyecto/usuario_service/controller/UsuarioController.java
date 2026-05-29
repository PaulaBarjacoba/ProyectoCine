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

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // GET
    @GetMapping
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
    public ResponseEntity<UsuarioResponseDTO> guardar(@Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO nuevoUsuario = usuarioService.guardar(dto);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    //BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Integer id) {
        UsuarioResponseDTO usuario = usuarioService.buscarPorId(id);
        if (usuario != null) {
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // BUSCAR POR EMAIL (Para que OpenFeign lo consuma)
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorEmail(@PathVariable String email) {
        UsuarioResponseDTO usuario = usuarioService.buscarPorEmail(email);

        if (usuario != null) {
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // ENDPOINT QUE RECIVE MAIL Y PASSWD
    @PostMapping("/validar")
    public ResponseEntity<Boolean> validarCredenciales(@RequestBody Map<String, String> credenciales) {
        String email = credenciales.get("email");
        String pass = credenciales.get("password");

        boolean esValido = usuarioService.verificarPassword(email, pass);
        return ResponseEntity.ok(esValido);
    }

    // ACTUALIZAR PUT
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(@Valid @PathVariable int id, @Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO actualizado = usuarioService.actualizar(id, dto);

        if (actualizado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@Valid @PathVariable int id) {
        if (usuarioService.eliminar(id)) {
            return new ResponseEntity<>("Usuario eliminado con exito", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se encontro el usuario", HttpStatus.BAD_REQUEST);
        }
    }

}
