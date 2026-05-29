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

        // 1. Validamos la película antes de guardar
        PeliculaDTO pelicula = peliculaClient.getPeliculaById(request.getIdPelicula().longValue());

        // 2. Mapeo de DTO a Entidad
        Funcion funcion = new Funcion();
        funcion.setIdPelicula(request.getIdPelicula());
        funcion.setIdSala(request.getIdSala());
        funcion.setFechaHora(request.getFechaHora());
        funcion.setPrecioBase(request.getPrecioBase());

        // 3. Guardado
        Funcion saved = funcionRepository.save(funcion);

        log.info("Función creada exitosamente con ID: {}", saved.getIdFuncion());
        return mapToResponse(saved);
    }

    // 2. Listar todas las funciones
    public List<FuncionResponseDTO> listarTodas() {
        return funcionRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Metodo auxiliar para mapear de Entidad a ResponseDTO
    private FuncionResponseDTO mapToResponse(Funcion funcion) {
        FuncionResponseDTO response = new FuncionResponseDTO();
        response.setIdFuncion(funcion.getIdFuncion());
        response.setIdPelicula(funcion.getIdPelicula());
        response.setIdSala(funcion.getIdSala());
        response.setFechaHora(funcion.getFechaHora());
        response.setPrecioBase(funcion.getPrecioBase());
        return response;
    }


    public FuncionResponseDTO obtenerPorId(Long id) {
        Funcion funcion = funcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Función no encontrada con ID: " + id));
        return mapToResponse(funcion);
    }

    public FuncionResponseDTO actualizarFuncion(Long id, FuncionRequestDTO request) {
        Funcion funcionExistente = funcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Función no encontrada con ID: " + id));

        funcionExistente.setIdPelicula(request.getIdPelicula());
        funcionExistente.setIdSala(request.getIdSala());
        funcionExistente.setFechaHora(request.getFechaHora());
        funcionExistente.setPrecioBase(request.getPrecioBase());

        Funcion actualizada = funcionRepository.save(funcionExistente);
        return mapToResponse(actualizada);
    }

    public void eliminarFuncion(Long id) {
        if (!funcionRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar, ID no encontrado: " + id);
        }
        funcionRepository.deleteById(id);
    }
}