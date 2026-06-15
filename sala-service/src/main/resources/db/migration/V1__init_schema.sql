CREATE TABLE IF NOT EXISTS salas (
    id_sala INT AUTO_INCREMENT PRIMARY KEY,
    nombre_sala VARCHAR(50) NOT NULL,
    capacidad_total INT NOT NULL,
    tipo_sala VARCHAR(50)
);

INSERT INTO salas (nombre_sala, capacidad_total, tipo_sala) VALUES
    ('Sala 1 - Principal', 150, '2D'),
    ('Sala 2 - 3D', 100, '3D'),
    ('Sala 3 - IMAX', 50, 'IMAX');
