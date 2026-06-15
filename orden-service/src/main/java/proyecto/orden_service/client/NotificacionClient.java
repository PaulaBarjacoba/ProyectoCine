package proyecto.orden_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import proyecto.orden_service.client.dto.NotificacionClientRequestDTO;

@FeignClient(name = "notificacion-service")
public interface NotificacionClient {

    @PostMapping("/api/v1/notificaciones")
    Object enviarNotificacion(@RequestBody NotificacionClientRequestDTO requestDTO);
}
