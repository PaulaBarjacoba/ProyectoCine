package proyecto.orden_service.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrdenResponseDTO {
    private Integer idOrden;
    private Integer idUsuario;
    private Integer idReserva;
    private LocalDateTime fechaOrden;
    private String estadoPreparacion;
    private Double totalOrden;
    private List<DetalleOrdenResponseDTO> detalles;
}
