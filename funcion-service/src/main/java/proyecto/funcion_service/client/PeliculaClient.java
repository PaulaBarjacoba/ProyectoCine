package proyecto.funcion_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import proyecto.funcion_service.dto.PeliculaDTO;

@FeignClient(name = "pelicula-service")
public interface PeliculaClient {

    @GetMapping("/api/v1/peliculas/{id}")
    PeliculaDTO getPeliculaById(@PathVariable("id") Long id);
}