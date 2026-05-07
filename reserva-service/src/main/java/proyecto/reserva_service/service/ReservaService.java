package proyecto.reserva_service.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import proyecto.reserva_service.client.PeliculaClient;
import proyecto.reserva_service.client.SalaClient;
import proyecto.reserva_service.client.UsuarioClient;
import proyecto.reserva_service.dto.ReservaRequestDTO;
import proyecto.reserva_service.dto.ReservaResponseDTO;
import proyecto.reserva_service.model.Reserva;
import proyecto.reserva_service.repository.ReservaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioClient usuarioClient;
    private final PeliculaClient peliculaClient;
    private final SalaClient salaClient;


    public ReservaResponseDTO registrarReserva(ReservaRequestDTO dto) {
        log.info("Iniciando reserva para el usuario ID: {}", dto.getIdUsuario());

        //checar que exista en el otro microservicio

        try {
            usuarioClient.buscarPorId(dto.getIdUsuario());
        } catch (FeignException.NotFound e) {
            log.error("Error: Usuario ID {} no existe.", dto.getIdUsuario());
            throw new RuntimeException("El usuario ingresado no existe.");
        }

        try {
            peliculaClient.buscarPorId(dto.getIdPelicula());
        } catch (FeignException.NotFound e) {
            log.error("Error: Película ID {} no existe.", dto.getIdPelicula());
            throw new RuntimeException("La película ingresada no existe en cartelera.");
        }

        try {
            salaClient.buscarPorId(dto.getIdSala());
        } catch (FeignException.NotFound e) {
            log.error("Error: Sala ID {} no existe.", dto.getIdSala());
            throw new RuntimeException("La sala ingresada no existe.");
        }

        // tan caras las entradas
        double precioPorEntrada = 6700.0;
        double totalAPagar = dto.getCantidadAsientos() * precioPorEntrada;

        // guardar en bd
        Reserva reserva = new Reserva();
        reserva.setIdUsuario(dto.getIdUsuario());
        reserva.setIdPelicula(dto.getIdPelicula());
        reserva.setIdSala(dto.getIdSala());
        reserva.setCantidadAsientos(dto.getCantidadAsientos());
        reserva.setTotalPago(totalAPagar);

        Reserva guardada = reservaRepository.save(reserva);
        log.info("Reserva exitosa. ID Reserva: {}, Total: ${}", guardada.getIdReserva(), guardada.getTotalPago());

        return convertirADTO(guardada);
    }


    private ReservaResponseDTO convertirADTO(Reserva reserva) {
        ReservaResponseDTO dto = new ReservaResponseDTO();
        dto.setIdReserva(reserva.getIdReserva());
        dto.setIdUsuario(reserva.getIdUsuario());
        dto.setIdPelicula(reserva.getIdPelicula());
        dto.setIdSala(reserva.getIdSala());
        dto.setIdFuncion(reserva.getIdFuncion());
        dto.setFechaReserva(reserva.getFechaReserva());
        dto.setCantidadAsientos(reserva.getCantidadAsientos());
        dto.setTotalPago(reserva.getTotalPago());
        dto.setEstadoReserva(reserva.getEstadoReserva());
        return dto;
    }

    //GET
    public List<ReservaResponseDTO> listarTodas() {
        log.info("Listado de reservas");
        return reservaRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    //buscar por id
    public ReservaResponseDTO buscarPorId(Integer id) {
        log.info("Buscando la reserva ID: {}", id);
        return reservaRepository.findById(id)
                .map(this::convertirADTO)
                .orElseGet(() -> {
                    log.warn("No se encontró la reserva con ID: {}", id);
                    return null;
                });
    }

    public ReservaResponseDTO actualizar(Integer id, ReservaRequestDTO dto) {
        log.info("Actualizando la reserva ID: {}", id);

        return reservaRepository.findById(id).map(reserva -> {
            reserva.setCantidadAsientos(dto.getCantidadAsientos());
            //calcula el total
            reserva.setTotalPago(dto.getCantidadAsientos() * 6700.0);

            Reserva actualizada = reservaRepository.save(reserva);
            log.info("Reserva ID {} actualizada con éxito", id);
            return convertirADTO(actualizada);
        }).orElseThrow(() -> {
            log.error("Error al actualizar: La reserva ID {} no existe", id);
            return new RuntimeException("No se puede actualizar una reserva inexistente");
        });
    }

    public void eliminar(Integer id) {
        log.info("Solicitud para eliminar reserva ID: {}", id);
        if (reservaRepository.existsById(id)) {
            reservaRepository.deleteById(id);
            log.info("Reserva ID {} eliminada correctamente", id);
        } else {
            log.error("Fallo al eliminar: ID {} no encontrado", id);
            throw new RuntimeException("No se encontro la reserva para eliminar");
        }

    }



}