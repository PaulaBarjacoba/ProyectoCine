CREATE TABLE IF NOT EXISTS roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    nombre_rol VARCHAR(50) NOT NULL
);

INSERT INTO roles (id_usuario, nombre_rol) VALUES
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_USER'),
    (3, 'ROLE_USER');
