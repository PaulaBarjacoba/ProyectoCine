package proyecto.orden_service.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionClientRequestDTO {
    private Integer idUsuario;
    private String tipoNotificacion;
    private String mensaje;
}
