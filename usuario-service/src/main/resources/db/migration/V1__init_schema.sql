CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO usuarios (nombre, email, password)
VALUES
    ('Alan Brito Delgado', 'alan@mail.com', 'abcdefg1123'),
    ('Elsa Capunta', 'elsa.capunta@mail.com', 'holamundo'),
    ('Lola Mento', 'lolam@mail.com', '123456789');
