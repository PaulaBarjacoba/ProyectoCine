package proyecto.orden_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "API Órdenes", description = "API para la compra de confitería, snack bar y control de consumos de los clientes")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    @PostMapping
    @Operation(summary = "Crear una nueva orden de confitería", description = "Registra una compra de snacks o alimentos vinculándola a un usuario")
    @ApiResponse(responseCode = "201", description = "Orden de confitería creada con éxito")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos, stock insuficiente o usuario no existe")
    public ResponseEntity<OrdenResponseDTO> crearOrden(
            @Parameter(description = "DTO con los snacks a ordenar y cantidad") @Valid @RequestBody OrdenRequestDTO requestDTO) {
        OrdenResponseDTO nuevaOrden = ordenService.crearOrden(requestDTO);
        return new ResponseEntity<>(nuevaOrden, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obtener todas las órdenes", description = "Retorna el historial completo de todas las órdenes emitidas")
    @ApiResponse(responseCode = "200", description = "Consulta exitosa, entrega lista de órdenes")
    @ApiResponse(responseCode = "204", description = "Consulta exitosa, pero no hay órdenes registradas")
    public ResponseEntity<List<OrdenResponseDTO>> listarTodas() {
        List<OrdenResponseDTO> ordenes = ordenService.listarTodas();
        if (ordenes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ordenes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar orden por ID", description = "Obtiene los detalles de una orden de confitería mediante su ID único")
    @ApiResponse(responseCode = "200", description = "Orden encontrada exitosamente")
    @ApiResponse(responseCode = "404", description = "La orden con el ID indicado no existe")
    public ResponseEntity<OrdenResponseDTO> buscarPorId(
            @Parameter(description = "ID único de la orden a consultar") @PathVariable Integer id) {
        OrdenResponseDTO orden = ordenService.obtenerPorId(id);
        return new ResponseEntity<>(orden, HttpStatus.OK);
    }

    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Listar órdenes por Usuario", description = "Retorna el listado de todas las órdenes de confitería realizadas por un usuario específico")
    @ApiResponse(responseCode = "200", description = "Consulta exitosa, entrega las órdenes del usuario")
    @ApiResponse(responseCode = "204", description = "Consulta exitosa, pero el usuario no registra compras de confitería")
    public ResponseEntity<List<OrdenResponseDTO>> listarPorUsuario(
            @Parameter(description = "ID único del usuario") @PathVariable Integer idUsuario) {
        List<OrdenResponseDTO> ordenes = ordenService.listarPorUsuario(idUsuario);
        if (ordenes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ordenes, HttpStatus.OK);
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado de una orden", description = "Permite cambiar el estado de la compra de snacks (ej. PENDIENTE, ENTREGADO, CANCELADO)")
    @ApiResponse(responseCode = "200", description = "Estado de la orden actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "La orden a actualizar no fue encontrada")
    @ApiResponse(responseCode = "400", description = "El nuevo estado no es válido")
    public ResponseEntity<OrdenResponseDTO> actualizarEstado(
            @Parameter(description = "ID único de la orden") @PathVariable Integer id,
            @Parameter(description = "Nombre del nuevo estado a asignar") @RequestParam String nuevoEstado) {
        OrdenResponseDTO actualizada = ordenService.actualizarEstado(id, nuevoEstado);
        return new ResponseEntity<>(actualizada, HttpStatus.OK);
    }
}
