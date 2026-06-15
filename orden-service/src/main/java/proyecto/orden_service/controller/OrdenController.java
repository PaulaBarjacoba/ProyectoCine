package proyecto.orden_service.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.orden_service.dto.OrdenRequestDTO;
import proyecto.orden_service.dto.OrdenResponseDTO;
import proyecto.orden_service.service.OrdenService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    @PostMapping
    public ResponseEntity<OrdenResponseDTO> crearOrden(@Valid @RequestBody OrdenRequestDTO requestDTO) {
        OrdenResponseDTO nuevaOrden = ordenService.crearOrden(requestDTO);
        return new ResponseEntity<>(nuevaOrden, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrdenResponseDTO>> listarTodas() {
        List<OrdenResponseDTO> ordenes = ordenService.listarTodas();
        if (ordenes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ordenes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenResponseDTO> buscarPorId(@PathVariable Integer id) {
        OrdenResponseDTO orden = ordenService.obtenerPorId(id);
        return new ResponseEntity<>(orden, HttpStatus.OK);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<OrdenResponseDTO>> listarPorUsuario(@PathVariable Integer idUsuario) {
        List<OrdenResponseDTO> ordenes = ordenService.listarPorUsuario(idUsuario);
        if (ordenes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ordenes, HttpStatus.OK);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<OrdenResponseDTO> actualizarEstado(
            @PathVariable Integer id,
            @RequestParam String nuevoEstado) {
        OrdenResponseDTO actualizada = ordenService.actualizarEstado(id, nuevoEstado);
        return new ResponseEntity<>(actualizada, HttpStatus.OK);
    }
}
