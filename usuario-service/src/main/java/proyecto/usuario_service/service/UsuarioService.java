package proyecto.usuario_service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.usuario_service.dto.UsuarioRequestDTO;
import proyecto.usuario_service.dto.UsuarioResponseDTO;
import proyecto.usuario_service.model.Usuario;
import proyecto.usuario_service.repository.UsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Listar usuarios y convierte a DTO
    public List<UsuarioResponseDTO> listarTodos() {
        log.info("Consultando la lista de usuarios");
        return usuarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());

    }

    // Nuevo metodo para que Auth-Service pueda consultar por email
    public UsuarioResponseDTO buscarPorEmail(String email) {
        log.info("Buscando usuario por email: {}", email);
        return usuarioRepository.findByEmail(email)
                .map(usuario -> {
                    UsuarioResponseDTO dto = convertirADTO(usuario);
                    return dto;
                })
                .orElseGet(() -> {
                    log.warn("Usuario con email {} no encontrado", email);
                    return null;
                });
    }

    public boolean verificarPassword(String email, String password) {
        var optionalUsuario = usuarioRepository.findByEmail(email);
        if (optionalUsuario.isPresent()) {
            return optionalUsuario.get().getPassword().equals(password);
        }
        return false;
    }

    // Guardar usuario (de request a response)
    public UsuarioResponseDTO guardar(UsuarioRequestDTO dto) {
        log.info("Registrando usuario con email: {}", dto.getEmail());
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            log.error("Fallo al registrar: El correo '{}' ya existe", dto.getEmail());
            throw new RuntimeException("El correo ya esta registrado");
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword()); //aqui falta encriptar contraseña creo

        Usuario guardado = usuarioRepository.save(usuario);
        log.info("Usuario creado exitosamente con ID: {}", guardado.getIdUsuario());
        return convertirADTO(guardado);
    }


    private UsuarioResponseDTO convertirADTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        if (usuario.getFechaRegistro() != null) {
            dto.setFechaRegistro(usuario.getFechaRegistro().toLocalDate());
        }
        return dto;
    }

    //buscar x id
    public UsuarioResponseDTO buscarPorId(Integer id) {
        log.info("Buscando usuario con ID: {}", id);
        return usuarioRepository.findById(id)
                .map(this::convertirADTO)
                .orElseGet(() -> {
                    log.warn("Usuario con ID {} no encontrado", id);
                    return null;
                });
    }


    //PUT (actualizar)
    public UsuarioResponseDTO actualizar(int id, UsuarioRequestDTO dto) {
        log.info("Buscando usuario con ID {} para actualizar", id);
        java.util.Optional<Usuario> optional = usuarioRepository.findById(id);

        if (optional.isPresent()) {
            Usuario usuarioExistente = optional.get();
            usuarioExistente.setNombre(dto.getNombre());
            usuarioExistente.setEmail(dto.getEmail());
            usuarioExistente.setPassword(dto.getPassword());

            Usuario actualizado = usuarioRepository.save(usuarioExistente);
            log.info("Usuario ID {} actualizado exitosamente", id);
            return convertirADTO(actualizado);
        }

        log.warn("No se pudo actualizar el usuario: Usuario con ID {} no encontrado", id);
        return null;
    }

    // DELETE
    public boolean eliminar(int id) {
        log.info("Eliminando usuario con ID {}", id);
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            log.info("Usuario ID {} eliminado correctamente", id);
            return true;
        }
        log.error("Fallo al eliminar: El usuario con ID {} no existe", id);
        return false;
    }

}
