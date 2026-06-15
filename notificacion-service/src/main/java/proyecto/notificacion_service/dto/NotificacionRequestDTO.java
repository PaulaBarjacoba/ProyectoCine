package proyecto.notificacion_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificacionRequestDTO {

    @NotNull(message = "El ID de usuario no puede ser nulo")
    private Integer idUsuario;

    @NotBlank(message = "El tipo de notificación no puede estar vacío")
    private String tipoNotificacion;

    @NotBlank(message = "El mensaje no puede estar vacío")
    private String mensaje;
}
