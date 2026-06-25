package proyecto.asiento_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.asiento_service.dto.AsientoRequestDTO;
import proyecto.asiento_service.dto.AsientoResponseDTO;
import proyecto.asiento_service.model.Asiento;
import proyecto.asiento_service.repository.AsientoRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsientoServiceTest {

    @Mock
    private AsientoRepository asientoRepository;

    @InjectMocks
    private AsientoService asientoService;

    private Asiento asientoPrueba;
    private AsientoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        asientoPrueba = new Asiento();
        asientoPrueba.setIdAsiento(1);
        asientoPrueba.setFila("A");
        asientoPrueba.setNumero(5);
        asientoPrueba.setEstado("DISPONIBLE");
        asientoPrueba.setIdSala(10);

        requestDTO = new AsientoRequestDTO();
        requestDTO.setFila("A");
        requestDTO.setNumero(5);
        requestDTO.setEstado("DISPONIBLE");
        requestDTO.setIdSala(10);
    }

    @Test
    void crearAsiento_Exito() {
        when(asientoRepository.save(any(Asiento.class))).thenReturn(asientoPrueba);

        AsientoResponseDTO resultado = asientoService.crearAsiento(requestDTO);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdAsiento());
        assertEquals("A", resultado.getFila());
        assertEquals(5, resultado.getNumero());
        verify(asientoRepository, times(1)).save(any(Asiento.class));
    }

    @Test
    void buscarPorId_CuandoExiste() {
        when(asientoRepository.findById(1)).thenReturn(Optional.of(asientoPrueba));

        AsientoResponseDTO resultado = asientoService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdAsiento());
        assertEquals("DISPONIBLE", resultado.getEstado());
        verify(asientoRepository, times(1)).findById(1);
    }

    @Test
    void buscarPorId_CuandoNoExiste() {
        when(asientoRepository.findById(99)).thenReturn(Optional.empty());

        AsientoResponseDTO resultado = asientoService.buscarPorId(99);

        assertNull(resultado);
        verify(asientoRepository, times(1)).findById(99);
    }
}
