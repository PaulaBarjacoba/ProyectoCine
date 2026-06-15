package proyecto.sala_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.sala_service.dto.SalaRequestDTO;
import proyecto.sala_service.dto.SalaResponseDTO;
import proyecto.sala_service.model.Sala;
import proyecto.sala_service.repository.SalaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalaServiceTest {

    @Mock
    private SalaRepository salaRepository;

    @InjectMocks
    private SalaService salaService;

    // Variables globales
    private Sala salaPrueba;
    private SalaRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // Entidad falsa
        salaPrueba = new Sala();
        salaPrueba.setIdSala(1);
        salaPrueba.setNombreSala("Sala 1 - Principal");
        salaPrueba.setCapacidadTotal(150);
        salaPrueba.setTipoSala("2D");

        // DTO falso
        requestDTO = new SalaRequestDTO();
        requestDTO.setNombreSala("Sala 1 - Principal");
        requestDTO.setCapacidadTotal(150);
        requestDTO.setTipoSala("2D");
    }

    @Test
    void listarTodas_TestListaSalasDTO() {
        when(salaRepository.findAll()).thenReturn(Arrays.asList(salaPrueba));

        List<SalaResponseDTO> resultado = salaService.listarTodas();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Sala 1 - Principal", resultado.get(0).getNombreSala());
        verify(salaRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_TestSiExiste() {
        when(salaRepository.findById(1)).thenReturn(Optional.of(salaPrueba));

        SalaResponseDTO resultado = salaService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals("Sala 1 - Principal", resultado.getNombreSala());
        assertEquals(150, resultado.getCapacidadTotal());
        verify(salaRepository, times(1)).findById(1);
    }

    @Test
    void buscarPorId_TestNullNoExiste() {
        when(salaRepository.findById(99)).thenReturn(Optional.empty());

        SalaResponseDTO resultado = salaService.buscarPorId(99);

        assertNull(resultado); // null
        verify(salaRepository, times(1)).findById(99);
    }

    @Test
    void guardar_TestDTONombreNoExiste() {
        when(salaRepository.existsByNombreSala("Sala 1 - Principal")).thenReturn(false);
        when(salaRepository.save(any(Sala.class))).thenReturn(salaPrueba);

        SalaResponseDTO resultado = salaService.guardar(requestDTO);

        assertNotNull(resultado);
        assertEquals("2D", resultado.getTipoSala());
        assertEquals(150, resultado.getCapacidadTotal());
        verify(salaRepository, times(1)).save(any(Sala.class));
    }

    @Test
    void guardar_TestExceptionNombreYaExiste() {

        when(salaRepository.existsByNombreSala("Sala 1 - Principal")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            salaService.guardar(requestDTO);
        });

        assertEquals("Ya existe una sala con ese nombre", exception.getMessage());
        // Confirma que no intento guardar
        verify(salaRepository, never()).save(any(Sala.class));
    }

    @Test
    void actualizar_TestActualizarDTOSiSalaExiste() {
        SalaRequestDTO salaActualizada = new SalaRequestDTO();
        salaActualizada.setNombreSala("Sala 1 - Renovada");
        salaActualizada.setCapacidadTotal(120);
        salaActualizada.setTipoSala("3D");

        when(salaRepository.findById(1)).thenReturn(Optional.of(salaPrueba));
        when(salaRepository.save(any(Sala.class))).thenReturn(salaPrueba);

        SalaResponseDTO resultado = salaService.actualizar(1, salaActualizada);

        assertNotNull(resultado);
        verify(salaRepository, times(1)).findById(1);
        verify(salaRepository, times(1)).save(any(Sala.class));
    }

    @Test
    void actualizar_TestNullSiSalaNoExiste() {
        when(salaRepository.findById(99)).thenReturn(Optional.empty());

        SalaResponseDTO resultado = salaService.actualizar(99, requestDTO);

        assertNull(resultado); // null
        verify(salaRepository, times(1)).findById(99);
        verify(salaRepository, never()).save(any(Sala.class));
    }

    @Test
    void eliminar_DeberiaRetornarTrue_CuandoExiste() {

        when(salaRepository.existsById(1)).thenReturn(true);
        doNothing().when(salaRepository).deleteById(1);

        boolean resultado = salaService.eliminar(1);

        assertTrue(resultado);
        verify(salaRepository, times(1)).existsById(1);
        verify(salaRepository, times(1)).deleteById(1);
    }

    @Test
    void eliminar_DeberiaRetornarFalse_CuandoNoExiste() {
        when(salaRepository.existsById(99)).thenReturn(false);

        boolean resultado = salaService.eliminar(99);

        assertFalse(resultado);
        verify(salaRepository, times(1)).existsById(99);
        verify(salaRepository, never()).deleteById(anyInt());
    }
}