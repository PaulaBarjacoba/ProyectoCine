package proyecto.sala_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.sala_service.dto.SalaRequestDTO;
import proyecto.sala_service.dto.SalaResponseDTO;
import proyecto.sala_service.service.SalaService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/salas")
@Tag(name = "API Salas", description = "API para la gestión de salas, formatos y capacidad de asientos del cine")
public class SalaController {

    @Autowired
    private SalaService salaService;

    // GET
    @GetMapping
    @Operation(summary = "Obtener todas las salas", description = "Retorna una lista completa de todas las salas operativas en el cine")
    @ApiResponse(responseCode = "200", description = "Consulta exitosa, se entrega la lista de salas")
    @ApiResponse(responseCode = "204", description = "Consulta exitosa, pero no hay salas registradas")
    public ResponseEntity<List<SalaResponseDTO>> listar() {
        List<SalaResponseDTO> salas = salaService.listarTodas();
        if (salas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(salas, HttpStatus.OK);
    }

    // POST
    @PostMapping
    @Operation(summary = "Registrar una nueva sala", description = "Permite agregar una nueva sala al sistema definiendo su capacidad y formato")
    @ApiResponse(responseCode = "201", description = "Sala creada y guardada con éxito")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o incompletos")
    public ResponseEntity<SalaResponseDTO> guardar(@Valid @RequestBody SalaRequestDTO dto) {
        SalaResponseDTO nuevaSala = salaService.guardar(dto);
        return new ResponseEntity<>(nuevaSala, HttpStatus.CREATED);
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar sala por ID", description = "Obtiene los detalles de una sala específica según su ID")
    @ApiResponse(responseCode = "200", description = "Sala encontrada con éxito")
    @ApiResponse(responseCode = "404", description = "La sala no existe en la base de datos")
    public ResponseEntity<SalaResponseDTO> buscarPorId(
            @Parameter(description = "ID numérico de la sala a consultar") @PathVariable Integer id) {
        SalaResponseDTO sala = salaService.buscarPorId(id);
        if (sala != null) {
            return new ResponseEntity<>(sala, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // PUT
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de la sala", description = "Modifica la información de una sala existente")
    @ApiResponse(responseCode = "200", description = "Sala actualizada correctamente")
    @ApiResponse(responseCode = "404", description = "La sala que se intenta actualizar no existe")
    public ResponseEntity<SalaResponseDTO> actualizar(
            @Parameter(description = "ID de la sala a modificar") @PathVariable int id,
            @Valid @RequestBody SalaRequestDTO dto) {
        SalaResponseDTO actualizada = salaService.actualizar(id, dto);

        if (actualizada == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(actualizada, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una sala", description = "Borra una sala del sistema mediante su ID")
    @ApiResponse(responseCode = "200", description = "Mensaje confirmando la eliminación exitosa")
    @ApiResponse(responseCode = "400", description = "La sala no pudo ser eliminada porque no fue encontrada")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID de la sala a eliminar") @PathVariable int id) {
        if (salaService.eliminar(id)) {
            return new ResponseEntity<>("Sala eliminada con exito", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se encontro la sala", HttpStatus.BAD_REQUEST);
        }
    }
}