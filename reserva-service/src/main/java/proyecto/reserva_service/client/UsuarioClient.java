package proyecto.reserva_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuario-service", path = "/api/v1/usuarios")
public interface UsuarioClient {

    @GetMapping("/{id}")
    Object buscarPorId(@PathVariable("id") Integer id);
}
