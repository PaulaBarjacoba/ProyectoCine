package proyecto.reserva_service.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReserva;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "id_pelicula")
    private Integer idPelicula;

    @Column(name = "id_sala")
    private Integer idSala;

    @Column(name = "id_funcion")
    private Integer idFuncion;

    @Column(name = "cantidad_asientos")
    private Integer cantidadAsientos;

    @Column(name = "total", columnDefinition = "DECIMAL(10,2)")
    private Double totalPago;

    @Column(name = "estado")
    private String estadoReserva = "CONFIRMADA";

    @Column(name = "fecha_reserva")
    private LocalDateTime fechaReserva = LocalDateTime.now();

}
