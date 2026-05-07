package proyecto.sala_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "salas")
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sala")
    private Integer idSala;

    @Column(name = "nombre_sala", length = 50, nullable = false)
    private String nombreSala;

    @Column(name = "capacidad_total", nullable = false)
    private Integer capacidadTotal;

    @Column(name = "tipo_sala", length = 50)
    private String tipoSala;

}
