package proyecto.funcion_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "API Funciones", description = "API para la programación de funciones de películas en salas físicas del cine")
public class FuncionController {

    @Autowired
    private FuncionService funcionService;

    @PostMapping
    @Operation(summary = "Crear una nueva función", description = "Programa la exhibición de una película en una sala y horario específicos")
    @ApiResponse(responseCode = "200", description = "Función programada y guardada con éxito")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o inconsistencia en película/sala")
    public ResponseEntity<FuncionResponseDTO> crear(
            @Parameter(description = "DTO con los detalles de la función a programar") @Valid @RequestBody FuncionRequestDTO request) {
        return ResponseEntity.ok(funcionService.crearFuncion(request));
    }

    @GetMapping
    @Operation(summary = "Obtener todas las funciones", description = "Retorna el listado completo de todas las funciones programadas")
    @ApiResponse(responseCode = "200", description = "Listado de funciones recuperado con éxito")
    @ApiResponse(responseCode = "204", description = "No hay funciones programadas en el sistema")
    public ResponseEntity<List<FuncionResponseDTO>> listar() {
        List<FuncionResponseDTO> funciones = funcionService.listarTodas();
        if (funciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(funciones);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de una función", description = "Permite modificar la película, la sala, el horario o el precio de una función existente")
    @ApiResponse(responseCode = "200", description = "Función actualizada correctamente")
    @ApiResponse(responseCode = "404", description = "La función a actualizar no fue encontrada")
    @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos")
    public ResponseEntity<FuncionResponseDTO> actualizar(
            @Parameter(description = "ID único de la función a modificar") @PathVariable Long id,
            @Parameter(description = "DTO con los datos actualizados de la función") @Valid @RequestBody FuncionRequestDTO request) {
        return ResponseEntity.ok(funcionService.actualizarFuncion(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar función por ID", description = "Recupera la información detallada de una función según su ID único")
    @ApiResponse(responseCode = "200", description = "Función encontrada exitosamente")
    @ApiResponse(responseCode = "404", description = "No existe ninguna función registrada con ese ID")
    public ResponseEntity<FuncionResponseDTO> obtenerPorId(
            @Parameter(description = "ID único de la función a buscar") @PathVariable Long id) {
        return ResponseEntity.ok(funcionService.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una función", description = "Elimina permanentemente una función programada mediante su ID")
    @ApiResponse(responseCode = "204", description = "Función eliminada correctamente")
    @ApiResponse(responseCode = "404", description = "No se pudo eliminar: la función no existe")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID único de la función a eliminar") @PathVariable Long id) {
        funcionService.eliminarFuncion(id);
        return ResponseEntity.noContent().build();
    }
}