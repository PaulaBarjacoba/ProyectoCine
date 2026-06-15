package proyecto.orden_service.dto;

import lombok.Data;

@Data
public class DetalleOrdenResponseDTO {
    private Integer idDetalleOrden;
    private Integer idProducto;
    private Integer cantidad;
    private Double subtotal;
}
