package proyecto.reserva_service.controller;

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
public class ReservaController {

    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> listarTodas() {
        log.info("Recibida petición REST para listar todas las reservas");
        List<ReservaResponseDTO> reservas = reservaService.listarTodas();
        return new ResponseEntity<>(reservas, HttpStatus.OK);
    }


    private final ReservaService reservaService;
    //POST
    @PostMapping
    public ResponseEntity<ReservaResponseDTO> registrarReserva(@Valid @RequestBody ReservaRequestDTO dto) {
        log.info("Recibida petición POST para crear nueva reserva");
        ReservaResponseDTO respuesta = reservaService.registrarReserva(dto);

        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
    }
    //GET
    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> buscarPorId(@PathVariable Integer id) {
        log.info("Recibida petición GET para buscar reserva por la ID: {}", id);
        ReservaResponseDTO respuesta = reservaService.buscarPorId(id);

        if (respuesta != null) {
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //PUT
    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody ReservaRequestDTO dto) {
        log.info("Petición PUT para actualizar reserva ID: {}", id);
        ReservaResponseDTO actualizada = reservaService.actualizar(id, dto);
        return new ResponseEntity<>(actualizada, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.info("Petición DELETE para eliminar reserva ID: {}", id);
        reservaService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
