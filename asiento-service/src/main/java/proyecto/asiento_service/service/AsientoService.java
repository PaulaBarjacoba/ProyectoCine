package proyecto.asiento_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.asiento_service.dto.AsientoRequestDTO;
import proyecto.asiento_service.dto.AsientoResponseDTO;
import proyecto.asiento_service.model.Asiento;
import proyecto.asiento_service.repository.AsientoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsientoService {

    @Autowired
    private AsientoRepository asientoRepository;

    // 1. Método para crear un asiento nuevo
    public AsientoResponseDTO crearAsiento(AsientoRequestDTO requestDTO) {
        try {
            Asiento nuevoAsiento = new Asiento();
            nuevoAsiento.setFila(requestDTO.getFila());
            nuevoAsiento.setNumero(requestDTO.getNumero());
            nuevoAsiento.setIdSala(requestDTO.getIdSala());
            nuevoAsiento.setEstado("DISPONIBLE");

            Asiento asientoGuardado = asientoRepository.save(nuevoAsiento);

            return mapearAResponseDTO(asientoGuardado);

        } catch (Exception e) {
            throw new RuntimeException("Ocurrió un error al guardar el asiento en la base de datos: " + e.getMessage());
        }
    }

    public List<AsientoResponseDTO> obtenerAsientosPorSala(Integer idSala) {
        List<Asiento> asientos = asientoRepository.findByIdSala(idSala);

        return asientos.stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    private AsientoResponseDTO mapearAResponseDTO(Asiento asiento) {
        AsientoResponseDTO dto = new AsientoResponseDTO();
        dto.setId(asiento.getId());
        dto.setFila(asiento.getFila());
        dto.setNumero(asiento.getNumero());
        dto.setEstado(asiento.getEstado());
        dto.setIdSala(asiento.getIdSala());
        return dto;
    }
}