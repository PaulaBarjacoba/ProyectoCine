package com.proyecto.cine.controller;

import com.proyecto.cine.model.Pelicula;
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

    //listar pelis
    @GetMapping
    public ResponseEntity<List<Pelicula>> listar() {
        List<Pelicula> peliculas = peliculaService.findAll();
        if (peliculas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(peliculas);
    }
    //crear nueva peli
    @PostMapping
    public ResponseEntity<Pelicula> guardar(@RequestBody Pelicula pelicula) {
        Pelicula peliculaNueva = peliculaService.save(pelicula);
        return ResponseEntity.status(HttpStatus.CREATED).body(peliculaNueva);
    }

    // buscar peli x id
    @GetMapping("/{id}")
    public ResponseEntity<Pelicula> buscar(@Valid @PathVariable int id) {
        Pelicula buscada = peliculaService.findById(id);

        if(buscada == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(buscada, HttpStatus.OK);
        }
    }

    // actualizar peli
    @PutMapping("/{id}")
    public ResponseEntity<Pelicula> actualizar(@Valid @PathVariable int id, @Valid @RequestBody Pelicula peliculaDetalles) {
        Pelicula peliculaExistente = peliculaService.findById(id);

        if(peliculaExistente == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            peliculaExistente.setTitulo(peliculaDetalles.getTitulo());
            peliculaExistente.setSinopsis(peliculaDetalles.getSinopsis());
            peliculaExistente.setDuracionMinutos(peliculaDetalles.getDuracionMinutos());
            peliculaExistente.setClasificacionEdad(peliculaDetalles.getClasificacionEdad());
            peliculaExistente.setUrlPoster(peliculaDetalles.getUrlPoster());
            peliculaExistente.setEstadoCartelera(peliculaDetalles.getEstadoCartelera());

            Pelicula peliculaGuardada = peliculaService.save(peliculaExistente);
            return new ResponseEntity<>(peliculaGuardada, HttpStatus.OK);
        }
    }

    // Eliminar peli
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@Valid @PathVariable int id) {
        Pelicula peliculaExistente = peliculaService.findById(id);

        if(peliculaExistente != null) {
            peliculaService.eliminar(id);
            return new ResponseEntity<>("Eliminado con exito", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se encontro la pelicula", HttpStatus.BAD_REQUEST);
        }
    }

}
