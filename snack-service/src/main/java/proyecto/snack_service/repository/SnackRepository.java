package proyecto.snack_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto.snack_service.model.Snack;

@Repository
public interface SnackRepository extends JpaRepository<Snack, Integer> {

}
