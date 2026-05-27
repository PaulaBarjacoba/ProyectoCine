package proyecto.asiento_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto.asiento_service.model.Asiento;

import java.util.List;

@Repository
public interface AsientoRepository extends JpaRepository<Asiento, Integer> {

    // Metodo para listar todos los asientos que pertenecen a una sala específica
    List<Asiento> findByIdSala(Integer idSala);

    // Metodo de validación: Verifica que no se repita el mismo asiento en la misma sala
    boolean existsByFilaAndNumeroAndIdSala(String fila, Integer numero, Integer idSala);
}