package proyecto.reserva_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservaResponseDTO {

    private Integer idReserva;
    private Integer idUsuario;
    private Integer idPelicula;
    private Integer idSala;
    private Integer idFuncion;
    private LocalDateTime fechaReserva;
    private Integer cantidadAsientos;
    private Double totalPago;
    private String estadoReserva;

}
