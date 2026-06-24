package proyecto.orden_service.service;

import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.orden_service.client.NotificacionClient;
import proyecto.orden_service.client.ReservaClient;
import proyecto.orden_service.client.SnackClient;
import proyecto.orden_service.client.UsuarioClient;
import proyecto.orden_service.client.dto.SnackClientDTO;
import proyecto.orden_service.dto.*;
import proyecto.orden_service.model.Orden;
import proyecto.orden_service.repository.OrdenRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdenServiceTest {

    @Mock
    private OrdenRepository ordenRepository;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private ReservaClient reservaClient;

    @Mock
    private SnackClient snackClient;

    @Mock
    private NotificacionClient notificacionClient;

    @InjectMocks
    private OrdenService ordenService;

    private OrdenRequestDTO requestDTO;
    private SnackClientDTO snackDTO;
    private Orden ordenPrueba;

    @BeforeEach
    void setUp() {
        DetalleOrdenRequestDTO detRequest = new DetalleOrdenRequestDTO();
        detRequest.setIdProducto(1);
        detRequest.setCantidad(2);

        requestDTO = new OrdenRequestDTO();
        requestDTO.setIdUsuario(10);
        requestDTO.setIdReserva(5);
        requestDTO.setDetalles(Collections.singletonList(detRequest));

        snackDTO = new SnackClientDTO();
        snackDTO.setIdSnack(1);
        snackDTO.setNombreProducto("Popcorn Grande");
        snackDTO.setPrecioUnitario(3000.0);
        snackDTO.setStockDisponible(10);

        ordenPrueba = new Orden();
        ordenPrueba.setIdOrden(1);
        ordenPrueba.setIdUsuario(10);
        ordenPrueba.setIdReserva(5);
        ordenPrueba.setTotalOrden(6000.0);
        ordenPrueba.setEstadoPreparacion("RECIBIDA");
        ordenPrueba.setDetalles(new ArrayList<>());
    }

    @Test
    void crearOrden_Exito() {
        // Mocks
        when(usuarioClient.buscarPorId(10)).thenReturn(null); // Retorna DTO dummy, void o null
        when(reservaClient.buscarPorId(5)).thenReturn(null);
        when(snackClient.buscarSnack(1)).thenReturn(snackDTO);
        doNothing().when(snackClient).reducirStock(1, 2);
        when(ordenRepository.save(any(Orden.class))).thenReturn(ordenPrueba);
        when(notificacionClient.enviarNotificacion(any())).thenReturn(null);

        OrdenResponseDTO resultado = ordenService.crearOrden(requestDTO);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdOrden());
        assertEquals(6000.0, resultado.getTotalOrden());
        assertEquals("RECIBIDA", resultado.getEstadoPreparacion());
        verify(usuarioClient, times(1)).buscarPorId(10);
        verify(snackClient, times(1)).buscarSnack(1);
        verify(snackClient, times(1)).reducirStock(1, 2);
        verify(ordenRepository, times(1)).save(any(Orden.class));
    }

    @Test
    void crearOrden_UsuarioNoExiste() {
        // Feign NotFound exception helper
        Request request = Request.create(Request.HttpMethod.GET, "/url", new HashMap<>(), null, new RequestTemplate());
        FeignException.NotFound mockException = new FeignException.NotFound("not found", request, null, new HashMap<>());

        when(usuarioClient.buscarPorId(10)).thenThrow(mockException);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ordenService.crearOrden(requestDTO);
        });

        assertEquals("El usuario ingresado no existe.", exception.getMessage());
        verify(usuarioClient, times(1)).buscarPorId(10);
        verify(ordenRepository, never()).save(any(Orden.class));
    }

    @Test
    void crearOrden_StockInsuficiente() {
        when(usuarioClient.buscarPorId(10)).thenReturn(null);
        when(reservaClient.buscarPorId(5)).thenReturn(null);

        snackDTO.setStockDisponible(1); // Solo hay 1 disponible, pero se piden 2
        when(snackClient.buscarSnack(1)).thenReturn(snackDTO);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ordenService.crearOrden(requestDTO);
        });

        assertTrue(exception.getMessage().contains("Stock insuficiente"));
        verify(snackClient, times(1)).buscarSnack(1);
        verify(snackClient, never()).reducirStock(anyInt(), anyInt());
        verify(ordenRepository, never()).save(any(Orden.class));
    }
}
