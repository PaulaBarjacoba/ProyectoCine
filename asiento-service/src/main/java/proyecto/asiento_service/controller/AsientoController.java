package proyecto.asiento_service.controller;

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
@RequestMapping("/api/asientos")
public class AsientoController {

    @Autowired
    private AsientoService asientoService;

    @PostMapping
    public ResponseEntity<AsientoResponseDTO> crearAsiento(@Valid @RequestBody AsientoRequestDTO requestDTO) {
        AsientoResponseDTO nuevoAsiento = asientoService.crearAsiento(requestDTO);

        return new ResponseEntity<>(nuevoAsiento, HttpStatus.CREATED);
    }

    @GetMapping("/sala/{idSala}")
    public ResponseEntity<List<AsientoResponseDTO>> obtenerAsientosPorSala(@PathVariable Integer idSala) {
        List<AsientoResponseDTO> asientos = asientoService.obtenerAsientosPorSala(idSala);

        return new ResponseEntity<>(asientos, HttpStatus.OK);
    }
}