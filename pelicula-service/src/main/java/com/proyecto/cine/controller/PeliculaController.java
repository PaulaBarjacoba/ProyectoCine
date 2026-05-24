package com.proyecto.cine.controller;

import com.proyecto.cine.dto.PeliculaRequestDTO;
import com.proyecto.cine.dto.PeliculaResponseDTO;
import com.proyecto.cine.service.PeliculaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/peliculas")
public class PeliculaController {

    @Autowired
    private PeliculaService peliculaService;

    //GET
    @GetMapping
    public ResponseEntity<List<PeliculaResponseDTO>> listar() {
        List<PeliculaResponseDTO> peliculas = peliculaService.listarTodas();
        if (peliculas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(peliculas, HttpStatus.OK);
    }
    //buscar por ID

    @GetMapping("/{id}")
    public ResponseEntity<PeliculaResponseDTO> buscarPorId(@PathVariable Integer id) {
        PeliculaResponseDTO pelicula = peliculaService.buscarPorId(id);
        if (pelicula != null) {
            return new ResponseEntity<>(pelicula, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    //POST
    @PostMapping
    public ResponseEntity<PeliculaResponseDTO> guardar(@Valid @RequestBody PeliculaRequestDTO dto) {
        PeliculaResponseDTO nueva = peliculaService.guardar(dto);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }
    //PUT actualizar
    @PutMapping("/{id}")
    public ResponseEntity<PeliculaResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody PeliculaRequestDTO dto) {
        PeliculaResponseDTO actualizada = peliculaService.actualizar(id, dto);
        if (actualizada != null) {
            return new ResponseEntity<>(actualizada, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        if (peliculaService.eliminar(id)) {
            return new ResponseEntity<>("Pelicula eliminada con exito", HttpStatus.OK);
        }
        return new ResponseEntity<>("No se encontro la pelicula", HttpStatus.BAD_REQUEST);
    }

}
