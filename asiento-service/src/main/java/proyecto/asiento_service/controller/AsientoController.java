package proyecto.asiento_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.asiento_service.dto.AsientoRequestDTO;
import proyecto.asiento_service.dto.AsientoResponseDTO;
import proyecto.asiento_service.service.AsientoService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/asientos")
@Tag(name = "API Asientos", description = "API para la gestión y disponibilidad de las butacas de las salas")
public class AsientoController {

    @Autowired
    private AsientoService asientoService;

    // POST
    @PostMapping
    @Operation(summary = "Crear una nueva butaca / asiento", description = "Registra un asiento físico asignándole una sala, fila, número y estado")
    @ApiResponse(responseCode = "201", description = "Asiento registrado con éxito")
    @ApiResponse(responseCode = "400", description = "Los datos ingresados no son válidos")
    public ResponseEntity<AsientoResponseDTO> crearAsiento(@Valid @RequestBody AsientoRequestDTO requestDTO) {
        AsientoResponseDTO nuevoAsiento = asientoService.crearAsiento(requestDTO);
        return new ResponseEntity<>(nuevoAsiento, HttpStatus.CREATED);
    }

    // GET (Por Sala)
    @GetMapping("/sala/{idSala}")
    @Operation(summary = "Obtener asientos de una sala", description = "Retorna la lista de todas las butacas asociadas a una sala física")
    @ApiResponse(responseCode = "200", description = "Consulta exitosa, entrega lista de asientos")
    @ApiResponse(responseCode = "204", description = "No se encontraron asientos registrados en esta sala")
    public ResponseEntity<List<AsientoResponseDTO>> obtenerAsientosPorSala(
            @Parameter(description = "ID de la sala a consultar") @PathVariable Integer idSala) {
        List<AsientoResponseDTO> asientos = asientoService.obtenerAsientosPorSala(idSala);

        if (asientos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(asientos, HttpStatus.OK);
    }

    // BUSCAR POR ID (GET)
    @GetMapping("/{id}")
    @Operation(summary = "Buscar asiento por ID", description = "Obtiene los detalles de un asiento específico mediante su ID único")
    @ApiResponse(responseCode = "200", description = "Asiento encontrado con éxito")
    @ApiResponse(responseCode = "404", description = "El asiento con el ID especificado no existe")
    public ResponseEntity<AsientoResponseDTO> buscarPorId(
            @Parameter(description = "ID único del asiento") @PathVariable Integer id) {
        AsientoResponseDTO asiento = asientoService.buscarPorId(id);
        if (asiento != null) {
            return new ResponseEntity<>(asiento, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // ACTUALIZAR (PUT)
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de un asiento", description = "Permite modificar la fila, número, sala o estado de un asiento específico")
    @ApiResponse(responseCode = "200", description = "Asiento actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "El asiento a actualizar no existe")
    public ResponseEntity<AsientoResponseDTO> actualizar(
            @Parameter(description = "ID del asiento a actualizar") @PathVariable Integer id,
            @Valid @RequestBody AsientoRequestDTO requestDTO) {
        AsientoResponseDTO actualizado = asientoService.actualizar(id, requestDTO);

        if (actualizado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(actualizado, HttpStatus.OK);
    }

    // ELIMINAR (DELETE)
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un asiento", description = "Borra permanentemente un asiento del mapa de la sala")
    @ApiResponse(responseCode = "200", description = "Asiento eliminado exitosamente")
    @ApiResponse(responseCode = "400", description = "No se pudo eliminar el asiento (ej. no existe)")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID del asiento a eliminar") @PathVariable Integer id) {
        if (asientoService.eliminar(id)) {
            return new ResponseEntity<>("Asiento eliminado con exito", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se encontro el asiento", HttpStatus.BAD_REQUEST);
        }
    }
}