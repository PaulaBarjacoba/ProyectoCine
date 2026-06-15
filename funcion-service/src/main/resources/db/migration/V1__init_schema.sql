CREATE TABLE IF NOT EXISTS funciones (
    id_funcion INT AUTO_INCREMENT PRIMARY KEY,
    id_pelicula INT NOT NULL,
    id_sala INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    precio_base DECIMAL(10,2) NOT NULL
);

INSERT INTO funciones (id_pelicula, id_sala, fecha_hora, precio_base) VALUES
    (1, 1, '2026-05-28 15:30:00', 6500.00),
    (1, 3, '2026-05-28 18:00:00', 7500.00),
    (2, 2, '2026-05-28 16:15:00', 7000.00);
