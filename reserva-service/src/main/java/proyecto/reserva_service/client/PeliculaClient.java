package proyecto.reserva_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pelicula-service",path = "/api/v1/peliculas")
public interface PeliculaClient {

    @GetMapping("/{id}")
    Object buscarPorId(@PathVariable("id") Integer id);

}