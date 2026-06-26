package proyecto.auth_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import proyecto.auth_service.dto.UsuarioDTO;
import java.util.Map;

@FeignClient(name = "usuario-service")
public interface UsuarioClient {

    @GetMapping("/api/v1/usuarios/email/{email}")
    UsuarioDTO buscarUsuarioPorEmail(@PathVariable("email") String email);

    @PostMapping("/api/v1/usuarios/validar")
    Boolean validarCredenciales(@RequestBody Map<String, String> credenciales);
}