CREATE TABLE IF NOT EXISTS ordenes_comida (
  id_orden INT AUTO_INCREMENT PRIMARY KEY,
  id_usuario INT NOT NULL,
  id_reserva INT,
  fecha_orden DATETIME DEFAULT CURRENT_TIMESTAMP,
  estado_preparacion VARCHAR(50) DEFAULT 'RECIBIDA',
  total_orden DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS detalle_orden (
   id_detalle_orden INT AUTO_INCREMENT PRIMARY KEY,
   id_orden INT NOT NULL,
   id_producto INT NOT NULL,
   cantidad INT NOT NULL,
   subtotal DECIMAL(10,2) NOT NULL,
   FOREIGN KEY (id_orden) REFERENCES ordenes_comida(id_orden)
);

INSERT INTO ordenes_comida (id_usuario, id_reserva, estado_preparacion, total_orden)
VALUES
    (2, 1, 'LISTO_PARA_RETIRO', 20000.00);

INSERT INTO detalle_orden (id_orden, id_producto, cantidad, subtotal)
VALUES
    (1, 1, 1, 20000.00);
