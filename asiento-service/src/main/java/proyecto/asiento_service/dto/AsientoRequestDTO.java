package proyecto.asiento_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AsientoRequestDTO {

    @NotBlank(message = "La fila no puede estar vacía")
    private String fila;

    @NotNull(message = "El número de asiento es obligatorio")
    @Positive(message = "El número de asiento debe ser mayor a cero")
    private Integer numero;

    @NotNull(message = "El ID de la sala es obligatorio")
    @Positive(message = "El ID de la sala debe ser un número válido")
    private Integer idSala;
}