package proyecto.snack_service.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.snack_service.dto.SnackRequestDTO;
import proyecto.snack_service.dto.SnackResponseDTO;
import proyecto.snack_service.model.Snack;
import proyecto.snack_service.service.SnackService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/snacks")
public class SnackController {

    @Autowired
    private SnackService snackService;


    // GET
    @GetMapping
    public ResponseEntity<List<SnackResponseDTO>> listarSnacks() {
        List<Snack> snacks = snackService.obtenerTodos();
        List<SnackResponseDTO> responseDTOs = snacks.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    // POST
    @PostMapping
    public ResponseEntity<SnackResponseDTO> crearSnack(@Valid @RequestBody SnackRequestDTO requestDTO) {
        Snack nuevoSnack = convertirAEntidad(requestDTO);
        Snack snackGuardado = snackService.registrar(nuevoSnack);
        return new ResponseEntity<>(convertirADTO(snackGuardado), HttpStatus.CREATED);
    }

    // buscar x id
    @GetMapping("/{id}")
    public ResponseEntity<SnackResponseDTO> buscarSnack(@PathVariable Integer id) {
        Snack snack = snackService.buscarPorId(id);
        return new ResponseEntity<>(convertirADTO(snack), HttpStatus.OK);
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<SnackResponseDTO> actualizarSnack(
            @PathVariable Integer id,
            @Valid @RequestBody SnackRequestDTO requestDTO) {

        Snack snackActualizado = convertirAEntidad(requestDTO);
        Snack snackGuardado = snackService.actualizar(id, snackActualizado);
        return new ResponseEntity<>(convertirADTO(snackGuardado), HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarSnack(@PathVariable Integer id) {
        snackService.borrar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Devuelve 204 No Content
    }


    private Snack convertirAEntidad(SnackRequestDTO dto) {
        Snack snack = new Snack();
        snack.setNombreProducto(dto.getNombreProducto());
        snack.setDescripcion(dto.getDescripcion());
        snack.setPrecioUnitario(dto.getPrecioUnitario());
        snack.setCategoria(dto.getCategoria());
        snack.setStockDisponible(dto.getStockDisponible());
        return snack;
    }

    private SnackResponseDTO convertirADTO(Snack snack) {
        SnackResponseDTO dto = new SnackResponseDTO();
        dto.setIdSnack(snack.getIdProducto());
        dto.setNombreProducto(snack.getNombreProducto());
        dto.setDescripcion(snack.getDescripcion());
        dto.setPrecioUnitario(snack.getPrecioUnitario());
        dto.setCategoria(snack.getCategoria());
        dto.setStockDisponible(snack.getStockDisponible());
        return dto;
    }


}
