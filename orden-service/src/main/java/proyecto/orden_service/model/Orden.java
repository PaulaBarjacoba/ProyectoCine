package proyecto.orden_service.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordenes_comida")
@Data
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden")
    private Integer idOrden;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "id_reserva")
    private Integer idReserva;

    @Column(name = "fecha_orden", nullable = false)
    private LocalDateTime fechaOrden;

    @Column(name = "estado_preparacion", length = 50)
    private String estadoPreparacion = "RECIBIDA";

    @Column(name = "total_orden", nullable = false)
    private Double totalOrden;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleOrden> detalles = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (fechaOrden == null) {
            fechaOrden = LocalDateTime.now();
        }
    }
}
