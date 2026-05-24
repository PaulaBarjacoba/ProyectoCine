package proyecto.sala_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SalaRequestDTO {

    @NotBlank(message = "El nombre de la sala no puede estar vacio")
    private String nombreSala;

    @NotNull(message = "La capacidad no puede estar vacia")
    @Min(value = 1, message = "La capacidad debe ser de al menos 1 asiento")
    private Integer capacidadTotal;

    @NotBlank(message = "El tipo de sala es obligatorio (2D, 3D, IMAX, etc.)")
    private String tipoSala;


}
