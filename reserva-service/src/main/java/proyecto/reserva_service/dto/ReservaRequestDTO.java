package proyecto.reserva_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservaRequestDTO {

    @NotNull(message = "El ID del usuario no puede estar vacio")
    private Integer idUsuario;

    @NotNull(message = "El ID de la película no puede estar vacio")
    private Integer idPelicula;

    @NotNull(message = "El ID de la sala  no puede estar vacio")
    private Integer idSala;

    @NotNull(message = "La cantidad de asientos no puede estar vacia")
    @Min(value = 1, message = "Debe reservar al menos 1 asiento")
    private Integer cantidadAsientos;

    @NotNull(message = "El ID de la función es obligatorio")
    private Integer idFuncion;

}
