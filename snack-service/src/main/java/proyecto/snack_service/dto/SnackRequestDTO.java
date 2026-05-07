package proyecto.snack_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SnackRequestDTO {

    @NotBlank(message = "El nombre del snack no puede estar vacio")
    private String nombreProducto;

    private String descripcion;

    @NotNull(message = "El precio no puede estar vacio")
    @Min(value = 1, message = "El precio debe ser mayor a cero")
    private Double precioUnitario;

    private String categoria;

    @NotNull(message = "El stock no puede estar vacio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stockDisponible;
}
