package proyecto.snack_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.snack_service.model.Snack;
import proyecto.snack_service.repository.SnackRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SnackServiceTest {

    @Mock
    private SnackRepository snackRepository;

    @InjectMocks
    private SnackService snackService;

    private Snack snackPrueba;

    @BeforeEach
    void setUp() {
        snackPrueba = new Snack();
        snackPrueba.setIdProducto(1);
        snackPrueba.setNombreProducto("Combo Tradicional");
        snackPrueba.setDescripcion("Palomitas grandes + 2 bebidas medianas");
        snackPrueba.setPrecioUnitario(20000.0);
        snackPrueba.setCategoria("Combos");
        snackPrueba.setStockDisponible(50);
    }

    @Test
    void obtenerTodos_TestListaSnacks() {
        when(snackRepository.findAll()).thenReturn(Arrays.asList(snackPrueba));

        List<Snack> resultado = snackService.obtenerTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Combo Tradicional", resultado.get(0).getNombreProducto());
        verify(snackRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_TestSnackExiste() {
        when(snackRepository.findById(1)).thenReturn(Optional.of(snackPrueba));

        Snack resultado = snackService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals("Combo Tradicional", resultado.getNombreProducto());
        assertEquals(20000.0, resultado.getPrecioUnitario());
        verify(snackRepository, times(1)).findById(1);
    }

    @Test
    void buscarPorId_TestExceptionNoExiste() {
        when(snackRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            snackService.buscarPorId(99);
        });

        assertEquals("No se encontro el snack con el ID: 99", exception.getMessage());
    }

    @Test
    void registrar_TestPrecioValido() {

        when(snackRepository.save(any(Snack.class))).thenReturn(snackPrueba);

        Snack resultado = snackService.registrar(snackPrueba);

        assertNotNull(resultado);
        assertEquals(20000.0, resultado.getPrecioUnitario());
        verify(snackRepository, times(1)).save(snackPrueba);
    }

    @Test
    void registrar_TestExceptionPrecioInvalido() {
        snackPrueba.setPrecioUnitario(0.0); // Fuerza el error

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            snackService.registrar(snackPrueba);
        });

        assertEquals("El precio del snack debe ser mayor a cero", exception.getMessage());
        verify(snackRepository, never()).save(any(Snack.class));
    }

    @Test
    void actualizar_TestActualizarSnack() {
        Snack snackActualizado = new Snack();
        snackActualizado.setNombreProducto("Palomitas Grandes");
        snackActualizado.setDescripcion("Balde de palomitas dulces");
        snackActualizado.setPrecioUnitario(11000.0);
        snackActualizado.setCategoria("Popcorn");
        snackActualizado.setStockDisponible(120);

        when(snackRepository.findById(1)).thenReturn(Optional.of(snackPrueba));
        when(snackRepository.save(any(Snack.class))).thenReturn(snackPrueba);

        Snack resultado = snackService.actualizar(1, snackActualizado);

        assertNotNull(resultado);
        assertEquals("Palomitas Grandes", resultado.getNombreProducto());
        assertEquals(11000.0, resultado.getPrecioUnitario());
        verify(snackRepository, times(1)).findById(1);
        verify(snackRepository, times(1)).save(any(Snack.class));
    }

    @Test
    void borrar_TestEliminarSnackSiExiste() {
        when(snackRepository.findById(1)).thenReturn(Optional.of(snackPrueba));

        snackService.borrar(1);

        verify(snackRepository, times(1)).findById(1);
        verify(snackRepository, times(1)).delete(snackPrueba);
    }
}