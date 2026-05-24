package proyecto.usuario_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
@Data
public class UsuarioResponseDTO {

    private Integer idUsuario;
    private String nombre;
    private String email;
    private LocalDate fechaRegistro;
}
