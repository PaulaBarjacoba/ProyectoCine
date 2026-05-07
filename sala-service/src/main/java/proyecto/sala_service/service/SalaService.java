package proyecto.sala_service.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.sala_service.dto.SalaRequestDTO;
import proyecto.sala_service.dto.SalaResponseDTO;
import proyecto.sala_service.model.Sala;
import proyecto.sala_service.repository.SalaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j //para log en consola
@Service
public class SalaService {

    @Autowired
    private SalaRepository salaRepository;

    //GET
    public List<SalaResponseDTO> listarTodas() {
        log.info("Consultando la lista de salas");
        return salaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    //POST
    public SalaResponseDTO guardar(SalaRequestDTO dto) {
        log.info("Creando la sala: {}", dto.getNombreSala());

        if (salaRepository.existsByNombreSala(dto.getNombreSala())) {
            log.error("Fallo la creacion de sala: La sala '{}' ya existe", dto.getNombreSala());
            throw new RuntimeException("Ya existe una sala con ese nombre");
        }

        Sala sala = new Sala();
        sala.setNombreSala(dto.getNombreSala());
        sala.setCapacidadTotal(dto.getCapacidadTotal());
        sala.setTipoSala(dto.getTipoSala());

        Sala guardada = salaRepository.save(sala);
        log.info("Sala creada exitosamente con ID: {}", guardada.getIdSala());

        return convertirADTO(guardada);
    }
    //Buscar x id
    public SalaResponseDTO buscarPorId(Integer id) {
        log.info("Buscando sala con ID: {}", id);
        return salaRepository.findById(id)
                .map(this::convertirADTO)
                .orElseGet(() -> {
                    log.warn("Sala con ID {} no encontrada", id);
                    return null;
                });
    }

    //PUT

    public SalaResponseDTO actualizar(int id, SalaRequestDTO dto) {
        log.info("Buscando sala con ID {} para actualizar", id);
        java.util.Optional<Sala> optional = salaRepository.findById(id);

        if (optional.isPresent()) {
            Sala salaExistente = optional.get();
            salaExistente.setNombreSala(dto.getNombreSala());
            salaExistente.setCapacidadTotal(dto.getCapacidadTotal());
            salaExistente.setTipoSala(dto.getTipoSala());

            Sala actualizada = salaRepository.save(salaExistente);
            log.info("Sala ID {} actualizada exitosamente", id);
            return convertirADTO(actualizada);
        }

        log.warn("No se pudo actualizar: Sala con ID {} no encontrada", id);
        return null;
    }

    // DELTE
    public boolean eliminar(int id) {
        log.info("Eliminando sala con ID {}", id);
        if (salaRepository.existsById(id)) {
            salaRepository.deleteById(id);
            log.info("Sala ID {} eliminada correctamente", id);
            return true;
        }
        log.error("Fallo al eliminar: La sala con ID {} no existe", id);
        return false;
    }

    private SalaResponseDTO convertirADTO(Sala sala) {
        SalaResponseDTO dto = new SalaResponseDTO();
        dto.setIdSala(sala.getIdSala());
        dto.setNombreSala(sala.getNombreSala());
        dto.setCapacidadTotal(sala.getCapacidadTotal());
        dto.setTipoSala(sala.getTipoSala());
        return dto;
    }

}
