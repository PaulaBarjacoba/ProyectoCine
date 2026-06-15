package proyecto.orden_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto.orden_service.model.DetalleOrden;

@Repository
public interface DetalleOrdenRepository extends JpaRepository<DetalleOrden, Integer> {
}
