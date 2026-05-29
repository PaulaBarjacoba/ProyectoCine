package proyecto.auth_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import proyecto.auth_service.dto.UsuarioDTO;

@FeignClient(name = "usuario-service")
public interface UsuarioClient {

    @GetMapping("/api/v1/usuarios/email/{email}")
    UsuarioDTO buscarUsuarioPorEmail(@PathVariable("email") String email);
}