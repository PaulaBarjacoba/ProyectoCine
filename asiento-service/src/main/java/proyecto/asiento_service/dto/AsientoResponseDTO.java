package proyecto.asiento_service.dto;

import lombok.Data;

@Data
public class AsientoResponseDTO {
    private Long id;
    private String fila;
    private Integer numero;
    private String estado;
    private Integer idSala;
}