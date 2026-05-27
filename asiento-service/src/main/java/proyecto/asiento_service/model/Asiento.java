package proyecto.asiento_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "asientos")
@Data
public class Asiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asiento")
    private Integer idAsiento;

    @Column(name = "fila", nullable = false, length = 2)
    private String fila;

    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @Column(name = "id_sala", nullable = false)
    private Integer idSala;
}