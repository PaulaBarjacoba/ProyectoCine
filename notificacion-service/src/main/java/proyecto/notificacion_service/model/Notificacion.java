package proyecto.notificacion_service.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_notificaciones")
@Data
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Integer idNotificacion;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "tipo_notificacion", nullable = false, length = 50)
    private String tipoNotificacion;

    @Column(name = "mensaje", nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    @Column(name = "estado", length = 20)
    private String estado = "ENVIADO";

    @PrePersist
    protected void onCreate() {
        if (fechaEnvio == null) {
            fechaEnvio = LocalDateTime.now();
        }
    }
}
