package proyecto.reserva_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "sala-service", url = "http://localhost:8084/api/v1/salas")
public interface SalaClient {

    @GetMapping("/{id}")
    Object buscarPorId(@PathVariable("id") Integer id);

}
