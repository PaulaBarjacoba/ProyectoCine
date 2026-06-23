package proyecto.notificacion_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.notificacion_service.dto.NotificacionRequestDTO;
import proyecto.notificacion_service.dto.NotificacionResponseDTO;
import proyecto.notificacion_service.service.NotificacionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notificaciones")
@Tag(name = "API Notificaciones", description = "API para el envío, registro y consulta de notificaciones de confirmación a los usuarios")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @PostMapping
    @Operation(summary = "Crear una nueva notificación", description = "Registra y despacha una nueva notificación para un usuario, asignándole un asunto y mensaje")
    @ApiResponse(responseCode = "201", description = "Notificación creada y guardada con éxito")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o incompletos")
    public ResponseEntity<NotificacionResponseDTO> crearNotificacion(
            @Parameter(description = "DTO con los datos de la notificación a crear") @Valid @RequestBody NotificacionRequestDTO requestDTO) {
        NotificacionResponseDTO nuevaNotificacion = notificacionService.registrar(requestDTO);
        return new ResponseEntity<>(nuevaNotificacion, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obtener todas las notificaciones", description = "Retorna el historial completo de todas las notificaciones enviadas")
    @ApiResponse(responseCode = "200", description = "Consulta exitosa, entrega la lista de notificaciones")
    @ApiResponse(responseCode = "204", description = "Consulta exitosa, pero no hay notificaciones registradas")
    public ResponseEntity<List<NotificacionResponseDTO>> listarTodas() {
        List<NotificacionResponseDTO> notificaciones = notificacionService.obtenerTodas();
        if (notificaciones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(notificaciones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar notificación por ID", description = "Obtiene los detalles de una notificación específica según su ID único")
    @ApiResponse(responseCode = "200", description = "Notificación encontrada con éxito")
    @ApiResponse(responseCode = "404", description = "La notificación con el ID especificado no existe")
    public ResponseEntity<NotificacionResponseDTO> buscarPorId(
            @Parameter(description = "ID único de la notificación a buscar") @PathVariable Integer id) {
        NotificacionResponseDTO notificacion = notificacionService.obtenerPorId(id);
        return new ResponseEntity<>(notificacion, HttpStatus.OK);
    }

    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Listar notificaciones por Usuario", description = "Retorna todas las notificaciones asociadas a un usuario específico mediante su ID")
    @ApiResponse(responseCode = "200", description = "Consulta exitosa, entrega la lista de notificaciones del usuario")
    @ApiResponse(responseCode = "204", description = "Consulta exitosa, pero el usuario no tiene notificaciones")
    public ResponseEntity<List<NotificacionResponseDTO>> listarPorUsuario(
            @Parameter(description = "ID único del usuario para consultar sus notificaciones") @PathVariable Integer idUsuario) {
        List<NotificacionResponseDTO> notificaciones = notificacionService.obtenerPorUsuario(idUsuario);
        if (notificaciones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(notificaciones, HttpStatus.OK);
    }
}
