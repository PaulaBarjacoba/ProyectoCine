package proyecto.orden_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import proyecto.orden_service.client.dto.SnackClientDTO;

@FeignClient(name = "snack-service")
public interface SnackClient {

    @GetMapping("/api/v1/snacks/{id}")
    SnackClientDTO buscarSnack(@PathVariable("id") Integer id);

    @PutMapping("/api/v1/snacks/{id}/reducir-stock")
    void reducirStock(@PathVariable("id") Integer id, @RequestParam("cantidad") Integer cantidad);
}
