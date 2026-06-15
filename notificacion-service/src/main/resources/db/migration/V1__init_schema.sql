CREATE TABLE IF NOT EXISTS historial_notificaciones (
  id_notificacion INT AUTO_INCREMENT PRIMARY KEY,
  id_usuario INT NOT NULL,
  tipo_notificacion VARCHAR(50) NOT NULL,
  mensaje TEXT NOT NULL,
  fecha_envio DATETIME DEFAULT CURRENT_TIMESTAMP,
  estado VARCHAR(20) DEFAULT 'ENVIADO'
);

INSERT INTO historial_notificaciones (id_usuario, tipo_notificacion, mensaje, estado)
VALUES
    (2, 'RESERVA_CONFIRMADA', 'Tu reserva para Spider-Man: Across the Spider-Verse ha sido confirmada con éxito. Código de retiro: ABCD-1234.', 'ENVIADO'),
    (2, 'ORDEN_SNACK', 'Tu orden de confitería está lista para ser retirada en la caja.', 'ENVIADO'),
    (1, 'ALERTA_SISTEMA', 'Inicio de sesión detectado desde un nuevo dispositivo.', 'ENVIADO');
