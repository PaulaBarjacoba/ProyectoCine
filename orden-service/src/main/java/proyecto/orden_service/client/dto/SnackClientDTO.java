package proyecto.orden_service.client.dto;

import lombok.Data;

@Data
public class SnackClientDTO {
    private Integer idSnack;
    private String nombreProducto;
    private Double precioUnitario;
    private Integer stockDisponible;
}
