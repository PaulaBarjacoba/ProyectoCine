package proyecto.sala_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto.sala_service.model.Sala;

import java.util.List;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Integer> {

    //no pueden existir dos salas con el mismo numero
    boolean existsByNombreSala(String nombreSala);


}
