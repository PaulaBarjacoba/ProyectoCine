package proyecto.sala_service.dto;


import lombok.Data;

@Data
public class SalaResponseDTO {
    private Integer idSala;
    private String nombreSala;
    private Integer capacidadTotal;
    private String tipoSala;
}
