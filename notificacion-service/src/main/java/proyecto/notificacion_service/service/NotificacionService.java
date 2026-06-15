package proyecto.notificacion_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.notificacion_service.dto.NotificacionRequestDTO;
import proyecto.notificacion_service.dto.NotificacionResponseDTO;
import proyecto.notificacion_service.model.Notificacion;
import proyecto.notificacion_service.repository.NotificacionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    public NotificacionResponseDTO registrar(NotificacionRequestDTO requestDTO) {
        Notificacion notificacion = new Notificacion();
        notificacion.setIdUsuario(requestDTO.getIdUsuario());
        notificacion.setTipoNotificacion(requestDTO.getTipoNotificacion());
        notificacion.setMensaje(requestDTO.getMensaje());
        notificacion.setEstado("ENVIADO");

        Notificacion guardada = notificacionRepository.save(notificacion);
        return convertirADTO(guardada);
    }

    public List<NotificacionResponseDTO> obtenerTodas() {
        return notificacionRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<NotificacionResponseDTO> obtenerPorUsuario(Integer idUsuario) {
        return notificacionRepository.findByIdUsuario(idUsuario).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public NotificacionResponseDTO obtenerPorId(Integer id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la notificación con ID: " + id));
        return convertirADTO(notificacion);
    }

    private NotificacionResponseDTO convertirADTO(Notificacion notificacion) {
        NotificacionResponseDTO dto = new NotificacionResponseDTO();
        dto.setIdNotificacion(notificacion.getIdNotificacion());
        dto.setIdUsuario(notificacion.getIdUsuario());
        dto.setTipoNotificacion(notificacion.getTipoNotificacion());
        dto.setMensaje(notificacion.getMensaje());
        dto.setFechaEnvio(notificacion.getFechaEnvio());
        dto.setEstado(notificacion.getEstado());
        return dto;
    }
}
