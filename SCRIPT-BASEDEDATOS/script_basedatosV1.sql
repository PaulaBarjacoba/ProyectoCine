-- Copia esto y ponlo en la consulta de SQL y lo ejecutas :))
-- 1. MICROSERVICIO DE USUARIOS
CREATE DATABASE IF NOT EXISTS db_usuarios;
USE db_usuarios;

CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP
    );

-- Datos de prueba usuarios
USE db_usuarios;

INSERT INTO usuarios (nombre, email, password)
VALUES
    ('Alan Brito Delgado', 'alan@mail.com', 'abcdefg1123'),
    ('Elsa Capunta', 'elsa.capunta@mail.com', 'holamundo'),
    ('Lola Mento', 'lolam@mail.com', '123456789');

-- 2. MICROSERVICIO DE AUTENTICACION

CREATE DATABASE IF NOT EXISTS db_auth;
USE db_auth;

CREATE TABLE IF NOT EXISTS roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    nombre_rol VARCHAR(50) NOT NULL
    );

-- Datos de prueba autenticacion
USE db_auth;

INSERT INTO roles (id_usuario, nombre_rol)
VALUES
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_USER'),
    (3, 'ROLE_USER');



-- 3. MICROSERVICIO DE PELICULAS

CREATE DATABASE IF NOT EXISTS db_peliculas;
USE db_peliculas;

DROP TABLE IF EXISTS pelicula;
CREATE TABLE pelicula (
   id_pelicula INT AUTO_INCREMENT PRIMARY KEY,
   titulo VARCHAR(100) NOT NULL,
   sinopsis TEXT NOT NULL,
   duracion_minutos INT NOT NULL,
   clasificacion_edad VARCHAR(12) NOT NULL,
   url_poster VARCHAR(300),
   estado_cartelera BOOLEAN NOT NULL
);

-- Datos de prueba peliculas
USE db_peliculas;

INSERT INTO pelicula (titulo, sinopsis, duracion_minutos, clasificacion_edad, url_poster, estado_cartelera)
VALUES
    ('Spider-Man: Across the Spider-Verse', 'Aventura en el multiverso.', 140, 'TE', 'https://link.com/spiderman.jpg', true),
    ('Dragon Ball Super: Broly', 'Goku y Vegeta se enfrentan a Broly, un saiyajin con un poder descomunal que amenaza con destruir todo a su paso, mientras se revela su origen y conexión con el pasado de los saiyajin', 100, 'TE', 'https://link.com/dbs-broly.jpg', true);


-- 4. MICROSERVICIO DE SALAS

CREATE DATABASE IF NOT EXISTS db_salas;
USE db_salas;

CREATE TABLE IF NOT EXISTS salas (
    id_sala INT AUTO_INCREMENT PRIMARY KEY,
    nombre_sala VARCHAR(50) NOT NULL,
    capacidad_total INT NOT NULL,
    tipo_sala VARCHAR(50)
    );

-- Datos de prueba salas
USE db_salas;

INSERT INTO salas (nombre_sala, capacidad_total, tipo_sala)
VALUES
    ('Sala 1 - Principal', 150, '2D'),
    ('Sala 2 - 3D', 100, '3D'),
    ('Sala 3 - IMAX', 50, 'IMAX');


-- 5. MICROSERVICIO DE FUNCIONES

CREATE DATABASE IF NOT EXISTS db_funciones;
USE db_funciones;

CREATE TABLE IF NOT EXISTS funciones (
    id_funcion INT AUTO_INCREMENT PRIMARY KEY,
    id_pelicula INT NOT NULL,
    id_sala INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    precio_base DECIMAL(10,2) NOT NULL
    );

-- Datos de prueba funciones
USE db_funciones;

-- id_pelicula 1 = Spider-Man, id_pelicula 2 = Dragon Ball

INSERT INTO funciones (id_pelicula, id_sala, fecha_hora, precio_base)
VALUES
    (1, 1, '2026-05-28 15:30:00', 6500.00),
    (1, 3, '2026-05-28 18:00:00', 7500.00),
    (2, 2, '2026-05-28 16:15:00', 7000.00);



-- 6. MICROSERVICIO DE BUTACAS

CREATE DATABASE IF NOT EXISTS db_butacas;
USE db_butacas;

CREATE TABLE IF NOT EXISTS butacas (
    id_butaca INT AUTO_INCREMENT PRIMARY KEY,
    id_sala INT NOT NULL,
    fila VARCHAR(5) NOT NULL,
    numero INT NOT NULL,
    estado VARCHAR(20) DEFAULT 'DISPONIBLE'
    );

-- Datos de prueba butacas
USE db_butacas;

-- Asientos de prueba para la Sala 1 (2D)>
INSERT INTO butacas (id_sala, fila, numero, estado)
VALUES
    (1, 'A', 1, 'DISPONIBLE'),
    (1, 'A', 2, 'OCUPADA'),
    (1, 'A', 3, 'DISPONIBLE'),
    (1, 'B', 1, 'DISPONIBLE'),
    (1, 'B', 2, 'DISPONIBLE'),
    -- Asientos de prueba para la Sala 3 (IMAX)
    (3, 'A', 1, 'DISPONIBLE'),
    (3, 'A', 2, 'DISPONIBLE');

-- 7. MICROSERVICIO DE RESERVAS

CREATE DATABASE IF NOT EXISTS db_reservas;
USE db_reservas;

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

-- Datos de prueba reservas
USE db_reservas;

INSERT INTO reservas (id_usuario, id_funcion, total, estado)
VALUES
    (2, 1, 6500.00, 'CONFIRMADA');

INSERT INTO detalle_reservas (id_reserva, id_butaca, precio_unitario)
VALUES
    (1, 2, 6500.00);

-- 8. MICROSERVICIO DE CONFITEERIA

CREATE DATABASE IF NOT EXISTS db_snacks;
USE db_snacks;

CREATE TABLE IF NOT EXISTS productos_confiteria (
   id_producto INT AUTO_INCREMENT PRIMARY KEY,
   nombre_producto VARCHAR(100) NOT NULL,
   descripcion TEXT,
   precio_unitario DECIMAL(10,2) NOT NULL,
   categoria VARCHAR(50),
   stock_disponible INT NOT NULL
   );

-- Datos de prueba confiteria snacks
USE db_snacks;

INSERT INTO productos_confiteria (nombre_producto, descripcion, precio_unitario, categoria, stock_disponible)
VALUES
    ('Combo Tradicional', 'Palomitas grandes + 2 bebidas medianas', 20000.00, 'Combos', 50),
    ('Palomitas Grandes', 'Balde de palomitas dulces', 11000.00, 'Popcorn', 120),
    ('Bebida Mediana', 'Vaso de bebida 500ml a elección', 6000.00, 'Bebestibles', 200),
    ('Nachos con Queso', 'Porción de nachos con salsa de queso cheddar', 5500.00, 'Snacks', 45);

-- 9. MICROSERVICIO DE ORDENES (comida)

CREATE DATABASE IF NOT EXISTS db_ordenes;
USE db_ordenes;

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

-- Datos de prueba ordenes
USE db_ordenes;

INSERT INTO ordenes_comida (id_usuario, id_reserva, estado_preparacion, total_orden)
VALUES
    (2, 1, 'LISTO_PARA_RETIRO', 20000.00);

-- Detalle de que se compro en la orden
INSERT INTO detalle_orden (id_orden, id_producto, cantidad, subtotal)
VALUES
    (1, 1, 1, 20000.00);


-- 10. MICROSERVICIO DE NOTIFICACIONES

CREATE DATABASE IF NOT EXISTS db_notificaciones;
USE db_notificaciones;

CREATE TABLE IF NOT EXISTS historial_notificaciones (
  id_notificacion INT AUTO_INCREMENT PRIMARY KEY,
  id_usuario INT NOT NULL,
  tipo_notificacion VARCHAR(50) NOT NULL,
  mensaje TEXT NOT NULL,
  fecha_envio DATETIME DEFAULT CURRENT_TIMESTAMP,
  estado VARCHAR(20) DEFAULT 'ENVIADO'
  );

-- Datos de prueba notificaciones

USE db_notificaciones;

INSERT INTO historial_notificaciones (id_usuario, tipo_notificacion, mensaje, estado)
VALUES
    (2, 'RESERVA_CONFIRMADA', 'Tu reserva para Spider-Man: Across the Spider-Verse ha sido confirmada con éxito. Código de retiro: ABCD-1234.', 'ENVIADO'),
    (2, 'ORDEN_SNACK', 'Tu orden de confitería está lista para ser retirada en la caja.', 'ENVIADO'),
    (1, 'ALERTA_SISTEMA', 'Inicio de sesión detectado desde un nuevo dispositivo.', 'ENVIADO');