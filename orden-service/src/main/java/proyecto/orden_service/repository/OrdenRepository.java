package proyecto.orden_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto.orden_service.model.Orden;

import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Integer> {
    List<Orden> findByIdUsuario(Integer idUsuario);
}
