package proyecto.notificacion_service.controller;

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
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @PostMapping
    public ResponseEntity<NotificacionResponseDTO> crearNotificacion(@Valid @RequestBody NotificacionRequestDTO requestDTO) {
        NotificacionResponseDTO nuevaNotificacion = notificacionService.registrar(requestDTO);
        return new ResponseEntity<>(nuevaNotificacion, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<NotificacionResponseDTO>> listarTodas() {
        List<NotificacionResponseDTO> notificaciones = notificacionService.obtenerTodas();
        if (notificaciones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(notificaciones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacionResponseDTO> buscarPorId(@PathVariable Integer id) {
        NotificacionResponseDTO notificacion = notificacionService.obtenerPorId(id);
        return new ResponseEntity<>(notificacion, HttpStatus.OK);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<NotificacionResponseDTO>> listarPorUsuario(@PathVariable Integer idUsuario) {
        List<NotificacionResponseDTO> notificaciones = notificacionService.obtenerPorUsuario(idUsuario);
        if (notificaciones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(notificaciones, HttpStatus.OK);
    }
}
