package proyecto.asiento_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "asientos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2)
    private String fila;

    @Column(nullable = false)
    private Integer numero;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "id_sala", nullable = false)
    private Integer idSala;
}