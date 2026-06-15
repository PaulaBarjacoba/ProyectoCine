CREATE TABLE IF NOT EXISTS productos_confiteria (
   id_producto INT AUTO_INCREMENT PRIMARY KEY,
   nombre_producto VARCHAR(100) NOT NULL,
   descripcion TEXT,
   precio_unitario DECIMAL(10,2) NOT NULL,
   categoria VARCHAR(50),
   stock_disponible INT NOT NULL
);

INSERT INTO productos_confiteria (nombre_producto, descripcion, precio_unitario, categoria, stock_disponible)
VALUES
    ('Combo Tradicional', 'Palomitas grandes + 2 bebidas medianas', 20000.00, 'Combos', 50),
    ('Palomitas Grandes', 'Balde de palomitas dulces', 11000.00, 'Popcorn', 120),
    ('Bebida Mediana', 'Vaso de bebida 500ml a elección', 6000.00, 'Bebestibles', 200),
    ('Nachos con Queso', 'Porción de nachos con salsa de queso cheddar', 5500.00, 'Snacks', 45);
