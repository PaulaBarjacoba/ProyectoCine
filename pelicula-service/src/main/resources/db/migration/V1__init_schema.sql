CREATE TABLE IF NOT EXISTS pelicula (
   id_pelicula INT AUTO_INCREMENT PRIMARY KEY,
   titulo VARCHAR(100) NOT NULL,
   sinopsis TEXT NOT NULL,
   duracion_minutos INT NOT NULL,
   clasificacion_edad VARCHAR(12) NOT NULL,
   url_poster VARCHAR(300),
   estado_cartelera BOOLEAN NOT NULL
);

INSERT INTO pelicula (titulo, sinopsis, duracion_minutos, clasificacion_edad, url_poster, estado_cartelera)
VALUES
    ('Spider-Man: Across the Spider-Verse', 'Aventura en el multiverso.', 140, 'TE', 'https://link.com/spiderman.jpg', true),
    ('Dragon Ball Super: Broly', 'Goku y Vegeta se enfrentan a Broly, un saiyajin con un poder descomunal que amenaza con destruir todo a su paso, mientras se revela su origen y conexión con el pasado de los saiyajin', 100, 'TE', 'https://link.com/dbs-broly.jpg', true);
