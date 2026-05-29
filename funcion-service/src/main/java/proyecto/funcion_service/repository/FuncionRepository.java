package proyecto.funcion_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto.funcion_service.model.Funcion;

@Repository
public interface FuncionRepository extends JpaRepository<Funcion, Long> {
    // Aquí puedes agregar métodos personalizados después,
    // como buscar funciones por ID de película.
}