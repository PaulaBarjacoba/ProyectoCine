package proyecto.reserva_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.reserva_service.dto.ReservaRequestDTO;
import proyecto.reserva_service.dto.ReservaResponseDTO;
import proyecto.reserva_service.service.ReservaService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor
@Tag(name = "API Reservas", description = "API para la gestión de reservas de clientes en el cine")
public class ReservaController {


    private final ReservaService reservaService;

    // GET
    @GetMapping
    @Operation(summary = "Obtener todas las reservas", description = "Retorna el historial completo de todas las reservas emitidas en el cine")
    @ApiResponse(responseCode = "200", description = "Historial de reservas obtenido exitosamente")
    public ResponseEntity<List<ReservaResponseDTO>> listarTodas() {
        log.info("Recibida petición REST para listar todas las reservas");
        List<ReservaResponseDTO> reservas = reservaService.listarTodas();
        return new ResponseEntity<>(reservas, HttpStatus.OK);
    }

    // POST
    @PostMapping
    @Operation(summary = "Crear una nueva reserva", description = "Procesa la compra o emisión de una nueva reserva para una función específica")
    @ApiResponse(responseCode = "201", description = "Reserva procesada con éxito")
    @ApiResponse(responseCode = "400", description = "Datos de la reserva inválidos o asientos no disponibles")
    public ResponseEntity<ReservaResponseDTO> registrarReserva(@Valid @RequestBody ReservaRequestDTO dto) {
        log.info("Recibida petición POST para crear nueva reserva");
        ReservaResponseDTO respuesta = reservaService.registrarReserva(dto);
        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
    }

    // GET POR ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar reserva por ID", description = "Busca los detalles de una reserva específica mediante su ID")
    @ApiResponse(responseCode = "200", description = "Reserva encontrada exitosamente")
    @ApiResponse(responseCode = "404", description = "La reserva indicada no existe en el sistema")
    public ResponseEntity<ReservaResponseDTO> buscarPorId(
            @Parameter(description = "ID único de la reserva a buscar") @PathVariable Integer id) {
        log.info("Recibida petición GET para buscar reserva por la ID: {}", id);
        ReservaResponseDTO respuesta = reservaService.buscarPorId(id);

        if (respuesta != null) {
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // PUT
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de una reserva", description = "Modifica los detalles de una reserva existente")
    @ApiResponse(responseCode = "200", description = "Reserva modificada correctamente")
    @ApiResponse(responseCode = "404", description = "La reserva que se intenta modificar no existe")
    public ResponseEntity<ReservaResponseDTO> actualizar(
            @Parameter(description = "ID de la reserva a modificar") @PathVariable Integer id,
            @Valid @RequestBody ReservaRequestDTO dto) {
        log.info("Petición PUT para actualizar reserva ID: {}", id);
        ReservaResponseDTO actualizada = reservaService.actualizar(id, dto);
        return new ResponseEntity<>(actualizada, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar una reserva", description = "Anula una reserva y libera los asientos asociados para que puedan ser comprados por otro cliente")
    @ApiResponse(responseCode = "204", description = "Reserva cancelada exitosamente")
    @ApiResponse(responseCode = "404", description = "La reserva no pudo ser cancelada porque no existe")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la reserva a cancelar") @PathVariable Integer id) {
        log.info("Petición DELETE para eliminar reserva ID: {}", id);
        reservaService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
