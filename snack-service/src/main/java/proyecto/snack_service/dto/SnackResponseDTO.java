package proyecto.snack_service.dto;

import lombok.Data;

@Data
public class SnackResponseDTO {

    private Integer idSnack;
    private String nombreProducto;
    private String descripcion;
    private Double precioUnitario;
    private String categoria;
    private Integer stockDisponible;

}
