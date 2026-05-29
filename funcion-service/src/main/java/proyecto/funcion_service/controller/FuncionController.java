package proyecto.funcion_service.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.funcion_service.dto.FuncionRequestDTO;
import proyecto.funcion_service.dto.FuncionResponseDTO;
import proyecto.funcion_service.service.FuncionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/funciones")
public class FuncionController {

    @Autowired
    private FuncionService funcionService;

    @PostMapping
    public ResponseEntity<FuncionResponseDTO> crear(@Valid @RequestBody FuncionRequestDTO request) {
        return ResponseEntity.ok(funcionService.crearFuncion(request));
    }

    @GetMapping
    public ResponseEntity<List<FuncionResponseDTO>> listar() {
        return ResponseEntity.ok(funcionService.listarTodas());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody FuncionRequestDTO request) {
        return ResponseEntity.ok(funcionService.actualizarFuncion(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(funcionService.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        funcionService.eliminarFuncion(id);
        return ResponseEntity.noContent().build();
    }
}