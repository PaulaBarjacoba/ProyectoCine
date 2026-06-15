CREATE TABLE IF NOT EXISTS butacas (
    id_butaca INT AUTO_INCREMENT PRIMARY KEY,
    id_sala INT NOT NULL,
    fila VARCHAR(5) NOT NULL,
    numero INT NOT NULL,
    estado VARCHAR(20) DEFAULT 'DISPONIBLE'
);

INSERT INTO butacas (id_sala, fila, numero, estado) VALUES
    (1, 'A', 1, 'DISPONIBLE'),
    (1, 'A', 2, 'OCUPADA'),
    (1, 'A', 3, 'DISPONIBLE'),
    (1, 'B', 1, 'DISPONIBLE'),
    (1, 'B', 2, 'DISPONIBLE'),
    (3, 'A', 1, 'DISPONIBLE'),
    (3, 'A', 2, 'DISPONIBLE');
