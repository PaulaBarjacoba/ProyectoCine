package proyecto.reserva_service.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservaResponseDTO {

    private Integer idReserva;
    private Integer idUsuario;
    private Integer idPelicula;
    private Integer idSala;
    private Integer idFuncion;
    private LocalDate fechaReserva;
    private Integer cantidadAsientos;
    private Double totalPago;
    private String estadoReserva;

}
