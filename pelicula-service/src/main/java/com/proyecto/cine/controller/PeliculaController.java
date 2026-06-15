package com.proyecto.cine.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import com.proyecto.cine.dto.PeliculaRequestDTO;
import com.proyecto.cine.dto.PeliculaResponseDTO;
import com.proyecto.cine.service.PeliculaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/peliculas")
@Tag(name = "API Peliculas", description = "API para la gestión del catálogo de películas y cartelera del cine")
public class PeliculaController {

    @Autowired
    private PeliculaService peliculaService;

    // GET
    @GetMapping
    @Operation(summary = "Obtener todas las peliculas", description = "Retorna el listado completo de todas las peliculas registradas en el sistema")
    @ApiResponse(responseCode = "200", description = "Listado de peliculas obtenido exitosamente")
    @ApiResponse(responseCode = "204", description = "Petición exitosa, pero no hay peliculas registradas")
    public ResponseEntity<List<PeliculaResponseDTO>> listar() {
        log.info("Petición GET recibida para listar todas las peliculas");
        List<PeliculaResponseDTO> peliculas = peliculaService.listarTodas();
        if (peliculas.isEmpty()) {
            log.info("No se encontraron peliculas en la base de datos");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(peliculas, HttpStatus.OK);
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar pelicula por ID", description = "Busca los detalles de una pelicula específica en la cartelera mediante su ID")
    @ApiResponse(responseCode = "200", description = "Pelicula encontrada exitosamente")
    @ApiResponse(responseCode = "404", description = "La pelicula indicada no existe")
    public ResponseEntity<PeliculaResponseDTO> buscarPorId(
            @Parameter(description = "ID de la pelicula a buscar") @PathVariable Integer id) {
        log.info("Petición GET recibida para buscar la pelicula con ID: {}", id);
        PeliculaResponseDTO pelicula = peliculaService.buscarPorId(id);
        if (pelicula != null) {
            return new ResponseEntity<>(pelicula, HttpStatus.OK);
        }
        log.warn("Pelicula con ID: {} no encontrada", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // POST
    @PostMapping
    @Operation(summary = "Registrar una nueva pelicula", description = "Agrega una nueva pelicula a la base de datos para habilitarla en la cartelera")
    @ApiResponse(responseCode = "201", description = "Pelicula registrada con éxito")
    @ApiResponse(responseCode = "400", description = "Datos de la pelicula inválidos o incompletos")
    public ResponseEntity<PeliculaResponseDTO> guardar(@Valid @RequestBody PeliculaRequestDTO dto) {
        log.info("Petición POST recibida para registrar una nueva pelicula");
        PeliculaResponseDTO nueva = peliculaService.guardar(dto);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    // PUT ACTUALIZAR
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de una pelicula", description = "Modifica la información de una pelicula existente ")
    @ApiResponse(responseCode = "200", description = "Pelicula actualizada correctamente")
    @ApiResponse(responseCode = "404", description = "La pelicula que se intenta actualizar no existe")
    public ResponseEntity<PeliculaResponseDTO> actualizar(
            @Parameter(description = "ID de la pelicula a actualizar") @PathVariable Integer id,
            @Valid @RequestBody PeliculaRequestDTO dto) {
        log.info("Petición PUT recibida para actualizar la pelicula con ID: {}", id);
        PeliculaResponseDTO actualizada = peliculaService.actualizar(id, dto);
        if (actualizada != null) {
            return new ResponseEntity<>(actualizada, HttpStatus.OK);
        }
        log.warn("No se pudo actualizar. Pelicula con ID: {} no encontrada", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una pelicula", description = "Elimina físicamente una pelicula del registro del cine")
    @ApiResponse(responseCode = "200", description = "Pelicula eliminada con éxito")
    @ApiResponse(responseCode = "400", description = "No se encontró la pelicula a eliminar")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID de la pelicula a eliminar") @PathVariable Integer id) {
        log.info("Petición DELETE recibida para eliminar la pelicula con ID: {}", id);
        if (peliculaService.eliminar(id)) {
            log.info("Pelicula con ID: {} eliminada exitosamente", id);
            return new ResponseEntity<>("Pelicula eliminada con exito", HttpStatus.OK);
        }
        log.warn("Fallo al eliminar. Pelicula con ID: {} no encontrada", id);
        return new ResponseEntity<>("No se encontro la pelicula", HttpStatus.BAD_REQUEST);
    }
}