package proyecto.snack_service.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "productos_confiteria")
@Data
public class Snack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @Column(name = "nombre_producto", nullable = false, length = 100)
    private String nombreProducto;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio_unitario", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double precioUnitario;

    @Column(name = "categoria", length = 50)
    private String categoria;

    @Column(name = "stock_disponible", nullable = false)
    private Integer stockDisponible;
}
