DROP PROCEDURE IF EXISTS AddColumnsIfNotExist;

DELIMITER //

CREATE PROCEDURE AddColumnsIfNotExist()
BEGIN
    DECLARE col_exists INT;

    SELECT COUNT(*) INTO col_exists 
    FROM information_schema.COLUMNS 
    WHERE TABLE_SCHEMA = DATABASE() 
      AND TABLE_NAME = 'reservas' 
      AND COLUMN_NAME = 'id_pelicula';

    IF col_exists = 0 THEN
        ALTER TABLE reservas 
        ADD COLUMN id_pelicula INT NOT NULL DEFAULT 1,
        ADD COLUMN id_sala INT NOT NULL DEFAULT 1,
        ADD COLUMN cantidad_asientos INT NOT NULL DEFAULT 1;
    END IF;
END //

DELIMITER ;

CALL AddColumnsIfNotExist();
DROP PROCEDURE AddColumnsIfNotExist;
