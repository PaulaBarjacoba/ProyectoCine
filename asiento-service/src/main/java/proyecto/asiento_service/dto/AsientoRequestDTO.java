package proyecto.asiento_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AsientoRequestDTO {

    @NotBlank(message = "La fila no puede estar vacia")
    private String fila;

    @NotNull(message = "El numero de asiento es obligatorio")
    @Min(value = 1, message = "El numero de asiento debe ser al menos 1")
    private Integer numero;

    @NotBlank(message = "El estado es obligatorio (DISPONIBLE, OCUPADO, etc.)")
    private String estado;

    @NotNull(message = "El ID de la sala es obligatorio")
    @Min(value = 1, message = "El ID de la sala no es valido")
    private Integer idSala;
}