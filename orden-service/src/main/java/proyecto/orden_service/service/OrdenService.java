package proyecto.orden_service.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proyecto.orden_service.client.NotificacionClient;
import proyecto.orden_service.client.ReservaClient;
import proyecto.orden_service.client.SnackClient;
import proyecto.orden_service.client.UsuarioClient;
import proyecto.orden_service.client.dto.NotificacionClientRequestDTO;
import proyecto.orden_service.client.dto.SnackClientDTO;
import proyecto.orden_service.dto.*;
import proyecto.orden_service.model.DetalleOrden;
import proyecto.orden_service.model.Orden;
import proyecto.orden_service.repository.OrdenRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final UsuarioClient usuarioClient;
    private final ReservaClient reservaClient;
    private final SnackClient snackClient;
    private final NotificacionClient notificacionClient;

    @Transactional
    public OrdenResponseDTO crearOrden(OrdenRequestDTO requestDTO) {
        log.info("Iniciando creación de orden para usuario ID: {}", requestDTO.getIdUsuario());

        // 1. Validar Usuario
        try {
            usuarioClient.buscarPorId(requestDTO.getIdUsuario());
        } catch (FeignException.NotFound e) {
            log.error("Error: Usuario ID {} no existe.", requestDTO.getIdUsuario());
            throw new RuntimeException("El usuario ingresado no existe.");
        }

        // 2. Validar Reserva (si se indica)
        if (requestDTO.getIdReserva() != null) {
            try {
                reservaClient.buscarPorId(requestDTO.getIdReserva());
            } catch (FeignException.NotFound e) {
                log.error("Error: Reserva ID {} no existe.", requestDTO.getIdReserva());
                throw new RuntimeException("La reserva ingresada no existe.");
            }
        }

        // 3. Procesar snacks y calcular total
        Orden orden = new Orden();
        orden.setIdUsuario(requestDTO.getIdUsuario());
        orden.setIdReserva(requestDTO.getIdReserva());
        orden.setEstadoPreparacion("RECIBIDA");

        double total = 0.0;
        List<DetalleOrden> detalles = new ArrayList<>();

        for (DetalleOrdenRequestDTO detDTO : requestDTO.getDetalles()) {
            SnackClientDTO snack;
            try {
                snack = snackClient.buscarSnack(detDTO.getIdProducto());
            } catch (FeignException.NotFound e) {
                log.error("Error: Producto ID {} no existe en el catálogo de snacks.", detDTO.getIdProducto());
                throw new RuntimeException("El producto ID " + detDTO.getIdProducto() + " no existe.");
            }

            if (snack.getStockDisponible() < detDTO.getCantidad()) {
                log.error("Error: Stock insuficiente para el producto ID {}. Disponible: {}, Solicitado: {}",
                        detDTO.getIdProducto(), snack.getStockDisponible(), detDTO.getCantidad());
                throw new RuntimeException("Stock insuficiente para el producto: " + snack.getNombreProducto());
            }

            // Reducir stock del snack
            try {
                snackClient.reducirStock(detDTO.getIdProducto(), detDTO.getCantidad());
            } catch (Exception e) {
                log.error("Error al reducir stock del producto ID: {}", detDTO.getIdProducto());
                throw new RuntimeException("Error al procesar el stock del snack: " + snack.getNombreProducto());
            }

            double subtotal = snack.getPrecioUnitario() * detDTO.getCantidad();
            total += subtotal;

            DetalleOrden detalle = new DetalleOrden();
            detalle.setOrden(orden);
            detalle.setIdProducto(detDTO.getIdProducto());
            detalle.setCantidad(detDTO.getCantidad());
            detalle.setSubtotal(subtotal);
            detalles.add(detalle);
        }

        orden.setTotalOrden(total);
        orden.setDetalles(detalles);

        Orden guardada = ordenRepository.save(orden);
        log.info("Orden creada exitosamente con ID: {} y total: ${}", guardada.getIdOrden(), guardada.getTotalOrden());

        // 4. Enviar notificación
        try {
            String mensaje = "Tu orden de confitería ha sido registrada con éxito. Código de orden: " + guardada.getIdOrden() + ". Total: $" + guardada.getTotalOrden() + ".";
            notificacionClient.enviarNotificacion(new NotificacionClientRequestDTO(
                    guardada.getIdUsuario(),
                    "ORDEN_SNACK",
                    mensaje
            ));
            log.info("Notificación enviada correctamente para la orden ID: {}", guardada.getIdOrden());
        } catch (Exception e) {
            log.error("No se pudo enviar la notificación para la orden ID: {}. Error: {}", guardada.getIdOrden(), e.getMessage());
            // No lanzamos excepción para evitar hacer rollback si la notificación falla, ya que la orden se creó con éxito.
        }

        return convertirADTO(guardada);
    }

    public List<OrdenResponseDTO> listarTodas() {
        return ordenRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public OrdenResponseDTO obtenerPorId(Integer id) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la orden con ID: " + id));
        return convertirADTO(orden);
    }

    public List<OrdenResponseDTO> listarPorUsuario(Integer idUsuario) {
        return ordenRepository.findByIdUsuario(idUsuario).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrdenResponseDTO actualizarEstado(Integer id, String nuevoEstado) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la orden con ID: " + id));

        orden.setEstadoPreparacion(nuevoEstado);
        Orden actualizada = ordenRepository.save(orden);

        // Si el estado cambia a listo, enviar notificación
        if ("LISTO_PARA_RETIRO".equalsIgnoreCase(nuevoEstado)) {
            try {
                String mensaje = "Tu orden de confitería está lista para ser retirada en la caja.";
                notificacionClient.enviarNotificacion(new NotificacionClientRequestDTO(
                        actualizada.getIdUsuario(),
                        "ORDEN_SNACK",
                        mensaje
                ));
            } catch (Exception e) {
                log.error("Error al enviar notificación de orden lista: {}", e.getMessage());
            }
        }

        return convertirADTO(actualizada);
    }

    private OrdenResponseDTO convertirADTO(Orden orden) {
        OrdenResponseDTO dto = new OrdenResponseDTO();
        dto.setIdOrden(orden.getIdOrden());
        dto.setIdUsuario(orden.getIdUsuario());
        dto.setIdReserva(orden.getIdReserva());
        dto.setFechaOrden(orden.getFechaOrden());
        dto.setEstadoPreparacion(orden.getEstadoPreparacion());
        dto.setTotalOrden(orden.getTotalOrden());
        dto.setDetalles(orden.getDetalles().stream()
                .map(d -> {
                    DetalleOrdenResponseDTO detDto = new DetalleOrdenResponseDTO();
                    detDto.setIdDetalleOrden(d.getIdDetalleOrden());
                    detDto.setIdProducto(d.getIdProducto());
                    detDto.setCantidad(d.getCantidad());
                    detDto.setSubtotal(d.getSubtotal());
                    return detDto;
                }).collect(Collectors.toList()));
        return dto;
    }
}
