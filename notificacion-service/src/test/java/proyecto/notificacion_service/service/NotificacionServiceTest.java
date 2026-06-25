package proyecto.notificacion_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.notificacion_service.dto.NotificacionRequestDTO;
import proyecto.notificacion_service.dto.NotificacionResponseDTO;
import proyecto.notificacion_service.model.Notificacion;
import proyecto.notificacion_service.repository.NotificacionRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @InjectMocks
    private NotificacionService notificacionService;

    private Notificacion notificacionPrueba;
    private NotificacionRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        notificacionPrueba = new Notificacion();
        notificacionPrueba.setIdNotificacion(1);
        notificacionPrueba.setIdUsuario(5);
        notificacionPrueba.setTipoNotificacion("CONFIRMACION");
        notificacionPrueba.setMensaje("Tu reserva ha sido confirmada.");
        notificacionPrueba.setEstado("ENVIADO");
        notificacionPrueba.setFechaEnvio(LocalDateTime.now());

        requestDTO = new NotificacionRequestDTO();
        requestDTO.setIdUsuario(5);
        requestDTO.setTipoNotificacion("CONFIRMACION");
        requestDTO.setMensaje("Tu reserva ha sido confirmada.");
    }

    @Test
    void registrar_Exito() {
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacionPrueba);

        NotificacionResponseDTO resultado = notificacionService.registrar(requestDTO);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdNotificacion());
        assertEquals("ENVIADO", resultado.getEstado());
        verify(notificacionRepository, times(1)).save(any(Notificacion.class));
    }

    @Test
    void obtenerPorId_Exito() {
        when(notificacionRepository.findById(1)).thenReturn(Optional.of(notificacionPrueba));

        NotificacionResponseDTO resultado = notificacionService.obtenerPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdNotificacion());
        assertEquals("Tu reserva ha sido confirmada.", resultado.getMensaje());
        verify(notificacionRepository, times(1)).findById(1);
    }

    @Test
    void obtenerPorId_NoExiste() {
        when(notificacionRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificacionService.obtenerPorId(99);
        });

        assertEquals("No se encontró la notificación con ID: 99", exception.getMessage());
        verify(notificacionRepository, times(1)).findById(99);
    }
}
