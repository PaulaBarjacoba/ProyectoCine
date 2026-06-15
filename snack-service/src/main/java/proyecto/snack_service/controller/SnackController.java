package proyecto.snack_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "API Confiteria", description = "API para la gestión del catálogo de productos de la confitería")
public class SnackController {

    @Autowired
    private SnackService snackService;

    // GET
    @GetMapping
    @Operation(summary = "Obtener todos los snacks", description = "Retorna el catálogo completo de productos disponibles en el sistema")
    @ApiResponse(responseCode = "200", description = "Catálogo obtenido exitosamente")
    public ResponseEntity<List<SnackResponseDTO>> listarSnacks() {
        List<Snack> snacks = snackService.obtenerTodos();
        List<SnackResponseDTO> responseDTOs = snacks.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    // POST
    @PostMapping
    @Operation(summary = "Registrar un nuevo snack", description = "Agrega un nuevo producto al catálogo de la confitería definiendo su precio, categoría y stock")
    @ApiResponse(responseCode = "201", description = "Snack registrado exitosamente en la base de datos")
    @ApiResponse(responseCode = "400", description = "Los datos enviados no son válidos o están incompletos")
    public ResponseEntity<SnackResponseDTO> crearSnack(@Valid @RequestBody SnackRequestDTO requestDTO) {
        Snack nuevoSnack = convertirAEntidad(requestDTO);
        Snack snackGuardado = snackService.registrar(nuevoSnack);
        return new ResponseEntity<>(convertirADTO(snackGuardado), HttpStatus.CREATED);
    }

    // BUSCAR X ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar snack por ID", description = "Obtiene los detalles exactos de un producto de la confitería mediante su ID")
    @ApiResponse(responseCode = "200", description = "Producto encontrado con éxito")
    @ApiResponse(responseCode = "404", description = "El producto no existe en el catálogo")
    public ResponseEntity<SnackResponseDTO> buscarSnack(
            @Parameter(description = "ID numérico del snack a consultar") @PathVariable Integer id) {
        Snack snack = snackService.buscarPorId(id);
        return new ResponseEntity<>(convertirADTO(snack), HttpStatus.OK);
    }

    // PUT
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de un snack", description = "Modifica la información de un producto existente")
    @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "El producto que se intenta actualizar no existe")
    public ResponseEntity<SnackResponseDTO> actualizarSnack(
            @Parameter(description = "ID del snack a modificar") @PathVariable Integer id,
            @Valid @RequestBody SnackRequestDTO requestDTO) {

        Snack snackActualizado = convertirAEntidad(requestDTO);
        Snack snackGuardado = snackService.actualizar(id, snackActualizado);
        return new ResponseEntity<>(convertirADTO(snackGuardado), HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un snack", description = "Elimina un producto del catálogo de forma permanente")
    @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "El producto no pudo ser eliminado porque no fue encontrado")
    public ResponseEntity<Void> borrarSnack(
            @Parameter(description = "ID del snack a eliminar") @PathVariable Integer id) {
        snackService.borrar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Devuelve 204 No Content
    }

    // PUT reducir-stock
    @PutMapping("/{id}/reducir-stock")
    @Operation(summary = "Reducir stock de un snack", description = "Reduce el stock disponible de un snack por la cantidad indicada")
    @ApiResponse(responseCode = "200", description = "Stock reducido exitosamente")
    @ApiResponse(responseCode = "400", description = "Stock insuficiente o datos incorrectos")
    @ApiResponse(responseCode = "404", description = "El snack no existe")
    public ResponseEntity<Void> reducirStock(
            @Parameter(description = "ID del snack a actualizar") @PathVariable Integer id,
            @Parameter(description = "Cantidad a restar del stock") @RequestParam Integer cantidad) {
        snackService.reducirStock(id, cantidad);
        return new ResponseEntity<>(HttpStatus.OK);
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
