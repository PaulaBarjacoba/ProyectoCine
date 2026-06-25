package proyecto.funcion_service.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FuncionResponseDTO {
        private Integer idFuncion;
        private Integer idPelicula;
        private Integer idSala;
        private LocalDateTime fechaHora;
        private BigDecimal precioBase;
}