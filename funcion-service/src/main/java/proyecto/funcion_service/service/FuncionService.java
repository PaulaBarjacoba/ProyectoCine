package proyecto.funcion_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.funcion_service.client.PeliculaClient;
import proyecto.funcion_service.dto.FuncionRequestDTO;
import proyecto.funcion_service.dto.FuncionResponseDTO;
import proyecto.funcion_service.dto.PeliculaDTO;
import proyecto.funcion_service.model.Funcion;
import proyecto.funcion_service.repository.FuncionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FuncionService {

    private static final Logger log = LoggerFactory.getLogger(FuncionService.class);

    @Autowired
    private FuncionRepository funcionRepository;

    @Autowired
    private PeliculaClient peliculaClient;

    public FuncionResponseDTO crearFuncion(FuncionRequestDTO request) {
        log.info("Iniciando creación de función para película ID: {}", request.getIdPelicula());

        try {
            PeliculaDTO pelicula = peliculaClient.getPeliculaById(request.getIdPelicula().longValue());
            if (pelicula == null) {
                throw new RuntimeException("La película con ID " + request.getIdPelicula() + " no existe.");
            }
        } catch (Exception e) {
            log.error("Error al validar película: {}", e.getMessage());
            throw new RuntimeException("No se pudo validar la película: servicio no disponible.");
        }

        Funcion funcion = new Funcion();
        funcion.setIdPelicula(request.getIdPelicula());
        funcion.setIdSala(request.getIdSala());
        funcion.setFechaHora(request.getFechaHora());
        funcion.setPrecioBase(request.getPrecioBase());

        Funcion saved = funcionRepository.save(funcion);
        log.info("Función creada exitosamente con ID: {}", saved.getIdFuncion());
        return mapToResponse(saved);
    }

    public List<FuncionResponseDTO> listarTodas() {
        return funcionRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public FuncionResponseDTO obtenerPorId(Long id) {
        return funcionRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Función no encontrada con ID: " + id));
    }

    public FuncionResponseDTO actualizarFuncion(Long id, FuncionRequestDTO request) {
        Funcion funcionExistente = funcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Función no encontrada con ID: " + id));

        funcionExistente.setIdPelicula(request.getIdPelicula());
        funcionExistente.setIdSala(request.getIdSala());
        funcionExistente.setFechaHora(request.getFechaHora());
        funcionExistente.setPrecioBase(request.getPrecioBase());

        return mapToResponse(funcionRepository.save(funcionExistente));
    }

    public void eliminarFuncion(Long id) {
        if (!funcionRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar, ID no encontrado: " + id);
        }
        funcionRepository.deleteById(id);
    }

    private FuncionResponseDTO mapToResponse(Funcion funcion) {
        FuncionResponseDTO response = new FuncionResponseDTO();
        response.setIdFuncion(funcion.getIdFuncion());
        response.setIdPelicula(funcion.getIdPelicula());
        response.setIdSala(funcion.getIdSala());
        response.setFechaHora(funcion.getFechaHora());
        response.setPrecioBase(funcion.getPrecioBase());
        return response;
    }
}