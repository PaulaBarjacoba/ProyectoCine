package proyecto.sala_service.controller;

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
public class SalaController {

    @Autowired
    private SalaService salaService;

    //GET
    @GetMapping
    public ResponseEntity<List<SalaResponseDTO>> listar() {
        List<SalaResponseDTO> salas = salaService.listarTodas();
        if (salas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(salas, HttpStatus.OK);
    }

    //POST
    @PostMapping
    public ResponseEntity<SalaResponseDTO> guardar(@Valid @RequestBody SalaRequestDTO dto) {
        SalaResponseDTO nuevaSala = salaService.guardar(dto);
        return new ResponseEntity<>(nuevaSala, HttpStatus.CREATED);
    }

    //BUSCAR POR ID

    @GetMapping("/{id}")
    public ResponseEntity<SalaResponseDTO> buscarPorId(@PathVariable Integer id) {
        SalaResponseDTO sala = salaService.buscarPorId(id);
        if (sala != null) {
            return new ResponseEntity<>(sala, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    //PUT
    @PutMapping("/{id}")
    public ResponseEntity<SalaResponseDTO> actualizar(@PathVariable int id, @Valid @RequestBody SalaRequestDTO dto) {
        SalaResponseDTO actualizada = salaService.actualizar(id, dto);

        if (actualizada == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(actualizada, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable int id) {
        if (salaService.eliminar(id)) {
            return new ResponseEntity<>("Sala eliminada con exito", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se encontro la sala", HttpStatus.BAD_REQUEST);
        }

    }
}
