package proyecto.asiento_service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.asiento_service.dto.AsientoRequestDTO;
import proyecto.asiento_service.dto.AsientoResponseDTO;
import proyecto.asiento_service.model.Asiento;
import proyecto.asiento_service.repository.AsientoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AsientoService {

    @Autowired
    private AsientoRepository asientoRepository;

    // POST - Crear asiento
    public AsientoResponseDTO crearAsiento(AsientoRequestDTO dto) {
        log.info("Creando asiento en la fila {} número {} para la sala con ID: {}", dto.getFila(), dto.getNumero(), dto.getIdSala());

        Asiento asiento = new Asiento();
        asiento.setFila(dto.getFila());
        asiento.setNumero(dto.getNumero());
        asiento.setEstado(dto.getEstado());
        asiento.setIdSala(dto.getIdSala());

        Asiento guardado = asientoRepository.save(asiento);
        log.info("Asiento creado exitosamente con ID: {}", guardado.getIdAsiento());

        return convertirADTO(guardado);
    }

    // GET - Listar asientos por Sala
    public List<AsientoResponseDTO> obtenerAsientosPorSala(Integer idSala) {
        log.info("Consultando la lista de asientos de la sala con ID: {}", idSala);
        return asientoRepository.findByIdSala(idSala).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // GET - Buscar asiento por ID
    public AsientoResponseDTO buscarPorId(Integer id) {
        log.info("Buscando asiento con ID: {}", id);
        return asientoRepository.findById(id)
                .map(this::convertirADTO)
                .orElseGet(() -> {
                    log.warn("Asiento con ID {} no encontrado", id);
                    return null;
                });
    }

    // PUT - Actualizar asiento completo
    public AsientoResponseDTO actualizar(Integer id, AsientoRequestDTO dto) {
        log.info("Buscando asiento con ID {} para proceder a su actualización", id);
        java.util.Optional<Asiento> optional = asientoRepository.findById(id);

        if (optional.isPresent()) {
            Asiento asientoExistente = optional.get();
            asientoExistente.setFila(dto.getFila());
            asientoExistente.setNumero(dto.getNumero());
            asientoExistente.setEstado(dto.getEstado());
            asientoExistente.setIdSala(dto.getIdSala());

            Asiento actualizado = asientoRepository.save(asientoExistente);
            log.info("Asiento ID {} actualizado exitosamente", id);
            return convertirADTO(actualizado);
        }

        log.warn("No se pudo completar la actualización: Asiento con ID {} no encontrado", id);
        return null;
    }

    // DELETE - Eliminar asiento
    public boolean eliminar(Integer id) {
        log.info("Eliminando asiento con ID: {}", id);
        if (asientoRepository.existsById(id)) {
            asientoRepository.deleteById(id);
            log.info("Asiento ID {} eliminado correctamente", id);
            return true;
        }
        log.error("Fallo al eliminar: El asiento con ID {} no existe en los registros", id);
        return false;
    }

    // Metodo de mapeo manual interno de Entidad a DTO
    private AsientoResponseDTO convertirADTO(Asiento asiento) {
        AsientoResponseDTO dto = new AsientoResponseDTO();
        dto.setIdAsiento(asiento.getIdAsiento());
        dto.setFila(asiento.getFila());
        dto.setNumero(asiento.getNumero());
        dto.setEstado(asiento.getEstado());
        dto.setIdSala(asiento.getIdSala());
        return dto;
    }
}