package proyecto.notificacion_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificacionResponseDTO {
    private Integer idNotificacion;
    private Integer idUsuario;
    private String tipoNotificacion;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private String estado;
}
