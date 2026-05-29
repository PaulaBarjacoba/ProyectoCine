package proyecto.funcion_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FuncionRequestDTO {
    @NotNull(message = "El ID de la película es obligatorio")
    private Integer idPelicula;

    @NotNull(message = "El ID de la sala es obligatorio")
    private Integer idSala;

    @NotNull(message = "La fecha y hora son obligatorias")
    @Future(message = "La fecha debe ser futura")
    private LocalDateTime fechaHora;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precioBase;
}