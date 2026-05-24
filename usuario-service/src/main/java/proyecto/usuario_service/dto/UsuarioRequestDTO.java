package proyecto.usuario_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data

public class UsuarioRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @NotBlank(message = "El email no puede estar vacio")
    @Email(message = "Debe tener formato de correo valido")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacia")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

}
