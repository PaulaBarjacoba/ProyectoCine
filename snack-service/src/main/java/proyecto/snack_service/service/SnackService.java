package proyecto.snack_service.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.snack_service.model.Snack;
import proyecto.snack_service.repository.SnackRepository;

import java.util.List;

@Service
public class SnackService {

    @Autowired
    private SnackRepository snackRepository;

    //GET
    public List<Snack> obtenerTodos() {
        return snackRepository.findAll();
    }

    // buscar por id
    public Snack buscarPorId(Integer id) {
        return snackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontro el snack con el ID: " + id));
    }

    // POST
    public Snack registrar(Snack snack) {
        if (snack.getPrecioUnitario() <= 0) {
            throw new IllegalArgumentException("El precio del snack debe ser mayor a cero");
        }
        return snackRepository.save(snack);
    }

    // PUT actualizar
    public Snack actualizar(Integer id, Snack snackActualizado) {
        Snack snackExistente = buscarPorId(id);

        snackExistente.setNombreProducto(snackActualizado.getNombreProducto());
        snackExistente.setDescripcion(snackActualizado.getDescripcion());
        snackExistente.setPrecioUnitario(snackActualizado.getPrecioUnitario());
        snackExistente.setCategoria(snackActualizado.getCategoria());
        snackExistente.setStockDisponible(snackActualizado.getStockDisponible());

        return snackRepository.save(snackExistente);
    }

    public void borrar(Integer id) {
        Snack snackExistente = buscarPorId(id);
        snackRepository.delete(snackExistente);
    }



}
