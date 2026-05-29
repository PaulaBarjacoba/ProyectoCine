package proyecto.usuario_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 255)
    @Size(min = 8, max = 100)
    private String password;

    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;


    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
    }
}
