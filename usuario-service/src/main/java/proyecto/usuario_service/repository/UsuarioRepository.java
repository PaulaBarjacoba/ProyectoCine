package proyecto.usuario_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto.usuario_service.model.Usuario;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // revisa si el mail ya existe (unique )
    boolean existsByEmail(String email);

    // buscar x mail
    Optional<Usuario> findByEmail(String email);
}
