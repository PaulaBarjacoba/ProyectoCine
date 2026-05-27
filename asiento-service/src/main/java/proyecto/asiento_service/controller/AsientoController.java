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
@RequestMapping("/api/v1/asientos")
public class AsientoController {

    @Autowired
    private AsientoService asientoService;

    // POST
    @PostMapping
    public ResponseEntity<AsientoResponseDTO> crearAsiento(@Valid @RequestBody AsientoRequestDTO requestDTO) {
        AsientoResponseDTO nuevoAsiento = asientoService.crearAsiento(requestDTO);
        return new ResponseEntity<>(nuevoAsiento, HttpStatus.CREATED);
    }

    // GET (Por Sala)
    @GetMapping("/sala/{idSala}")
    public ResponseEntity<List<AsientoResponseDTO>> obtenerAsientosPorSala(@PathVariable Integer idSala) {
        List<AsientoResponseDTO> asientos = asientoService.obtenerAsientosPorSala(idSala);

        if (asientos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(asientos, HttpStatus.OK);
    }

    // BUSCAR POR ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<AsientoResponseDTO> buscarPorId(@PathVariable Integer id) {
        AsientoResponseDTO asiento = asientoService.buscarPorId(id);
        if (asiento != null) {
            return new ResponseEntity<>(asiento, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // ACTUALIZAR (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<AsientoResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody AsientoRequestDTO requestDTO) {
        AsientoResponseDTO actualizado = asientoService.actualizar(id, requestDTO);

        if (actualizado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(actualizado, HttpStatus.OK);
    }

    // ELIMINAR (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        if (asientoService.eliminar(id)) {
            return new ResponseEntity<>("Asiento eliminado con exito", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se encontro el asiento", HttpStatus.BAD_REQUEST);
        }
    }
}