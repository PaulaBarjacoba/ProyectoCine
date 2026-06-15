package proyecto.notificacion_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto.notificacion_service.model.Notificacion;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    List<Notificacion> findByIdUsuario(Integer idUsuario);
}
