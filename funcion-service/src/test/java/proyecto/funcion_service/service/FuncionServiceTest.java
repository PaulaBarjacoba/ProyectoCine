package proyecto.funcion_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.funcion_service.client.PeliculaClient;
import proyecto.funcion_service.dto.FuncionRequestDTO;
import proyecto.funcion_service.dto.FuncionResponseDTO;
import proyecto.funcion_service.dto.PeliculaDTO;
import proyecto.funcion_service.model.Funcion;
import proyecto.funcion_service.repository.FuncionRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FuncionServiceTest {

    @Mock
    private FuncionRepository funcionRepository;

    @Mock
    private PeliculaClient peliculaClient;

    @InjectMocks
    private FuncionService funcionService;

    private Funcion funcionPrueba;
    private FuncionRequestDTO requestDTO;
    private PeliculaDTO peliculaDTO;

    @BeforeEach
    void setUp() {
        funcionPrueba = new Funcion();
        funcionPrueba.setIdFuncion(1);
        funcionPrueba.setIdPelicula(10);
        funcionPrueba.setIdSala(2);
        funcionPrueba.setFechaHora(LocalDateTime.now());
        funcionPrueba.setPrecioBase(java.math.BigDecimal.valueOf(5000.0));

        requestDTO = new FuncionRequestDTO();
        requestDTO.setIdPelicula(10);
        requestDTO.setIdSala(2);
        requestDTO.setFechaHora(LocalDateTime.now());
        requestDTO.setPrecioBase(java.math.BigDecimal.valueOf(5000.0));

        peliculaDTO = new PeliculaDTO();
        peliculaDTO.setIdPelicula(10);
        peliculaDTO.setTitulo("Pelicula de Prueba");
    }

    @Test
    void crearFuncion_Exito() {
        when(peliculaClient.getPeliculaById(10L)).thenReturn(peliculaDTO);
        when(funcionRepository.save(any(Funcion.class))).thenReturn(funcionPrueba);

        FuncionResponseDTO resultado = funcionService.crearFuncion(requestDTO);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdFuncion());
        assertEquals(10, resultado.getIdPelicula());
        verify(peliculaClient, times(1)).getPeliculaById(10L);
        verify(funcionRepository, times(1)).save(any(Funcion.class));
    }

    @Test
    void crearFuncion_PeliculaNoExiste() {
        when(peliculaClient.getPeliculaById(10L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            funcionService.crearFuncion(requestDTO);
        });

        assertEquals("No se pudo validar la película: servicio no disponible.", exception.getMessage());
        verify(peliculaClient, times(1)).getPeliculaById(10L);
        verify(funcionRepository, never()).save(any(Funcion.class));
    }

    @Test
    void obtenerPorId_NoExiste() {
        when(funcionRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            funcionService.obtenerPorId(99);
        });

        assertEquals("Función no encontrada con ID: 99", exception.getMessage());
        verify(funcionRepository, times(1)).findById(99);
    }
}
