package proyecto.reserva_service.service;

import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.reserva_service.client.PeliculaClient;
import proyecto.reserva_service.client.SalaClient;
import proyecto.reserva_service.client.UsuarioClient;
import proyecto.reserva_service.dto.ReservaRequestDTO;
import proyecto.reserva_service.dto.ReservaResponseDTO;
import proyecto.reserva_service.model.Reserva;
import proyecto.reserva_service.repository.ReservaRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private UsuarioClient usuarioClient;
    @Mock
    private PeliculaClient peliculaClient;
    @Mock
    private SalaClient salaClient;

    @InjectMocks
    private ReservaService reservaService;

    private Reserva reservaPrueba;
    private ReservaRequestDTO requestDTO;

    @BeforeEach
    void setUp() {


        reservaPrueba = new Reserva();
        reservaPrueba.setIdReserva(1);
        reservaPrueba.setIdUsuario(2);
        reservaPrueba.setIdPelicula(10);
        reservaPrueba.setIdSala(3); // Sala IMAX
        reservaPrueba.setIdFuncion(1);
        reservaPrueba.setCantidadAsientos(2);
        reservaPrueba.setTotalPago(13000.0); // 2 asientos * 6500.0
        reservaPrueba.setEstadoReserva("CONFIRMADA");
        reservaPrueba.setFechaReserva(LocalDateTime.now());

        requestDTO = new ReservaRequestDTO();
        requestDTO.setIdUsuario(2);
        requestDTO.setIdPelicula(10);
        requestDTO.setIdSala(3);
        requestDTO.setIdFuncion(1);
        requestDTO.setCantidadAsientos(2);
    }

    @Test
    void registrarReserva_TestCalculoTotalGuardadoValido() {
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaPrueba);

        ReservaResponseDTO resultado = reservaService.registrarReserva(requestDTO);


        assertNotNull(resultado);
        //Verifica el calculo de precio
        assertEquals(13000.0, resultado.getTotalPago());
        assertEquals(2, resultado.getCantidadAsientos());

        //Verifica que se haya consultado los 3 microservicios
        verify(usuarioClient, times(1)).buscarPorId(2);
        verify(peliculaClient, times(1)).buscarPorId(10);
        verify(salaClient, times(1)).buscarPorId(3);
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }

    @Test
    void registrarReserva_TestExceptionSiUsuarioNoExiste() {

        // Simula que Usuario responde con error 404 NOT FOUND
        FeignException.NotFound notFoundMock = mock(FeignException.NotFound.class);
        when(usuarioClient.buscarPorId(2)).thenThrow(notFoundMock);


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservaService.registrarReserva(requestDTO);
        });

        assertEquals("El usuario ingresado no existe.", exception.getMessage());

        //Como el usuario falla, no debe consultar la película ni guardarla en BD
        verify(peliculaClient, never()).buscarPorId(anyInt());
        verify(salaClient, never()).buscarPorId(anyInt());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    void listarTodas_TestListaDeReservas() {
        when(reservaRepository.findAll()).thenReturn(Arrays.asList(reservaPrueba));


        List<ReservaResponseDTO> resultado = reservaService.listarTodas();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(13000.0, resultado.get(0).getTotalPago());
        verify(reservaRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_TestRetornarDTOCuandoExiste() {

        when(reservaRepository.findById(1)).thenReturn(Optional.of(reservaPrueba));

        ReservaResponseDTO resultado = reservaService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(2, resultado.getIdUsuario());
        verify(reservaRepository, times(1)).findById(1);
    }

    @Test
    void actualizar_TestRecalcularTotalCuandoSeCambianAsientos() {
        ReservaRequestDTO updateDTO = new ReservaRequestDTO();
        updateDTO.setCantidadAsientos(3);

        when(reservaRepository.findById(1)).thenReturn(Optional.of(reservaPrueba));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaPrueba);

        ReservaResponseDTO resultado = reservaService.actualizar(1, updateDTO);

        assertNotNull(resultado);
        verify(reservaRepository, times(1)).findById(1);
        verify(reservaRepository, times(1)).save(any(Reserva.class));
        //verifica que el objeto haya sido modificado
        assertEquals(3, reservaPrueba.getCantidadAsientos());
        // * 3
        assertEquals(19500.0, reservaPrueba.getTotalPago());
    }

    @Test
    void eliminar_TestExcepctionSiReservaNoExiste() {
        when(reservaRepository.existsById(99)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservaService.eliminar(99);
        });

        assertEquals("No se encontro la reserva para eliminar", exception.getMessage());
        verify(reservaRepository, never()).deleteById(anyInt());
    }
}