INSERT INTO estado_cotizacion (desc_estado_cotizacion)
SELECT 'Vencida'
WHERE NOT EXISTS (
    SELECT 1
    FROM estado_cotizacion
    WHERE LOWER(desc_estado_cotizacion) = LOWER('Vencida')
);
