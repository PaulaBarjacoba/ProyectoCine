package com.proyecto.cine.service;

import com.proyecto.cine.dto.PeliculaRequestDTO;
import com.proyecto.cine.dto.PeliculaResponseDTO;
import com.proyecto.cine.model.Pelicula;
import com.proyecto.cine.repository.PeliculaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PeliculaService {
    //hola :)

    @Autowired
    private PeliculaRepository peliculaRepository;

    //get
    public List<PeliculaResponseDTO> listarTodas() {
        log.info("Consultando catalogo de peliculas");
        return peliculaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    //buscar x id
    public PeliculaResponseDTO buscarPorId(Integer id) {
        log.info("Buscando pelicula con ID: {}", id);
        java.util.Optional<Pelicula> optional = peliculaRepository.findById(id);

        if (optional.isPresent()) {
            return convertirADTO(optional.get());
        }
        log.warn("Pelicula con ID {} no encontrada", id);
        return null;
    }
    //POST
    public PeliculaResponseDTO guardar(PeliculaRequestDTO dto) {
        log.info("Registrando nueva pelicula: {}", dto.getTitulo());

        if (peliculaRepository.existsByTitulo(dto.getTitulo())) {
            log.error("Fallo al registrar: Ya existe una pelicula con el título '{}'", dto.getTitulo());
            throw new RuntimeException("La pelicula ya esta registrada");
        }

        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo(dto.getTitulo());
        pelicula.setSinopsis(dto.getSinopsis());
        pelicula.setDuracionMinutos(dto.getDuracionMinutos());
        pelicula.setClasificacionEdad(dto.getClasificacionEdad());
        pelicula.setUrlPoster(dto.getUrlPoster());
        pelicula.setEstadoCartelera(dto.getEstadoCartelera());

        Pelicula guardada = peliculaRepository.save(pelicula);
        log.info("Pelicula guardada exitosamente con ID: {}", guardada.getIdPelicula());
        return convertirADTO(guardada);
    }

    //PUT
    public PeliculaResponseDTO actualizar(Integer id, PeliculaRequestDTO dto) {
        log.info("Actualizando pelicula con ID: {}", id);
        java.util.Optional<Pelicula> optional = peliculaRepository.findById(id);

        if (optional.isPresent()) {
            Pelicula existente = optional.get();
            existente.setTitulo(dto.getTitulo());
            existente.setSinopsis(dto.getSinopsis());
            existente.setDuracionMinutos(dto.getDuracionMinutos());
            existente.setClasificacionEdad(dto.getClasificacionEdad());
            existente.setUrlPoster(dto.getUrlPoster());
            existente.setEstadoCartelera(dto.getEstadoCartelera());

            Pelicula actualizada = peliculaRepository.save(existente);
            log.info("Pelicula ID {} actualizada", id);
            return convertirADTO(actualizada);
        }
        log.error("Fallo al actualizar: La pelicula ID {} no existe", id);
        return null;
    }

    //DELETE
    public boolean eliminar(Integer id) {
        log.info("Eliminando pelicula con ID: {}", id);
        if (peliculaRepository.existsById(id)) {
            peliculaRepository.deleteById(id);
            log.info("Pelicula ID {} eliminada correctamente", id);
            return true;
        }
        log.error("Fallo al eliminar: Pelicula ID {} no encontrada", id);
        return false;
    }

    private PeliculaResponseDTO convertirADTO(Pelicula pelicula) {
        PeliculaResponseDTO dto = new PeliculaResponseDTO();
        dto.setIdPelicula(pelicula.getIdPelicula());
        dto.setTitulo(pelicula.getTitulo());
        dto.setSinopsis(pelicula.getSinopsis());
        dto.setDuracionMinutos(pelicula.getDuracionMinutos());
        dto.setClasificacionEdad(pelicula.getClasificacionEdad());
        dto.setUrlPoster(pelicula.getUrlPoster());
        dto.setEstadoCartelera(pelicula.getEstadoCartelera());
        return dto;
    }

}
