CREATE TABLE IF NOT EXISTS reservas (
    id_reserva INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_funcion INT NOT NULL,
    fecha_reserva DATETIME DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL,
    estado VARCHAR(50) DEFAULT 'CONFIRMADA'
);

CREATE TABLE IF NOT EXISTS detalle_reservas (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_reserva INT NOT NULL,
    id_butaca INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_reserva) REFERENCES reservas(id_reserva)
);

INSERT INTO reservas (id_usuario, id_funcion, total, estado) VALUES
    (2, 1, 6500.00, 'CONFIRMADA');

INSERT INTO detalle_reservas (id_reserva, id_butaca, precio_unitario) VALUES
    (1, 2, 6500.00);
