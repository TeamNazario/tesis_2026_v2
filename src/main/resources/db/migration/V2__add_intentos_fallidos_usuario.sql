SET @column_exists = (
    SELECT COUNT(*)
    FROM information_schema.columns
    WHERE table_schema = DATABASE()
      AND table_name = 'usuario'
      AND column_name = 'intentos_fallidos'
);

SET @ddl = IF(
    @column_exists = 0,
    'ALTER TABLE usuario ADD COLUMN intentos_fallidos INT NOT NULL DEFAULT 0 AFTER password_hash',
    'SELECT 1'
);

PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
