package proyecto.orden_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class OrdenRequestDTO {

    @NotNull(message = "El ID de usuario no puede ser nulo")
    private Integer idUsuario;

    private Integer idReserva;

    @NotEmpty(message = "La orden debe contener al menos un snack/producto")
    @Valid
    private List<DetalleOrdenRequestDTO> detalles;
}
