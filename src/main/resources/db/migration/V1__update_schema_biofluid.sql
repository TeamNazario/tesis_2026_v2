CREATE TABLE IF NOT EXISTS estado_usuario (
    id_estado_usuario INT AUTO_INCREMENT PRIMARY KEY,
    des_estado VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS estado_perfil (
    id_estado_perfil INT AUTO_INCREMENT PRIMARY KEY,
    des_estado VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS estado_cliente_contacto (
    id_estado_cliente_contacto INT AUTO_INCREMENT PRIMARY KEY,
    des_estado_cliente_contacto VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS estado_producto (
    id_estado_producto INT AUTO_INCREMENT PRIMARY KEY,
    desc_estado_producto VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS estado_cotizacion (
    id_estado_cotizacion INT AUTO_INCREMENT PRIMARY KEY,
    desc_estado_cotizacion VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO estado_usuario (des_estado)
SELECT 'Habilitado' WHERE NOT EXISTS (SELECT 1 FROM estado_usuario WHERE des_estado = 'Habilitado');
INSERT INTO estado_usuario (des_estado)
SELECT 'Inhabilitado' WHERE NOT EXISTS (SELECT 1 FROM estado_usuario WHERE des_estado = 'Inhabilitado');
INSERT INTO estado_usuario (des_estado)
SELECT 'Bloqueado' WHERE NOT EXISTS (SELECT 1 FROM estado_usuario WHERE des_estado = 'Bloqueado');

INSERT INTO estado_perfil (des_estado)
SELECT 'Activo' WHERE NOT EXISTS (SELECT 1 FROM estado_perfil WHERE des_estado = 'Activo');
INSERT INTO estado_perfil (des_estado)
SELECT 'Inactivo' WHERE NOT EXISTS (SELECT 1 FROM estado_perfil WHERE des_estado = 'Inactivo');

INSERT INTO estado_cliente_contacto (des_estado_cliente_contacto)
SELECT 'Activo' WHERE NOT EXISTS (SELECT 1 FROM estado_cliente_contacto WHERE des_estado_cliente_contacto = 'Activo');
INSERT INTO estado_cliente_contacto (des_estado_cliente_contacto)
SELECT 'Inactivo' WHERE NOT EXISTS (SELECT 1 FROM estado_cliente_contacto WHERE des_estado_cliente_contacto = 'Inactivo');
INSERT INTO estado_cliente_contacto (des_estado_cliente_contacto)
SELECT 'Bloqueado' WHERE NOT EXISTS (SELECT 1 FROM estado_cliente_contacto WHERE des_estado_cliente_contacto = 'Bloqueado');

INSERT INTO estado_producto (desc_estado_producto)
SELECT 'Activo' WHERE NOT EXISTS (SELECT 1 FROM estado_producto WHERE desc_estado_producto = 'Activo');
INSERT INTO estado_producto (desc_estado_producto)
SELECT 'Inactivo' WHERE NOT EXISTS (SELECT 1 FROM estado_producto WHERE desc_estado_producto = 'Inactivo');
INSERT INTO estado_producto (desc_estado_producto)
SELECT 'Bloqueado' WHERE NOT EXISTS (SELECT 1 FROM estado_producto WHERE desc_estado_producto = 'Bloqueado');

INSERT INTO estado_cotizacion (desc_estado_cotizacion)
SELECT 'Generada' WHERE NOT EXISTS (SELECT 1 FROM estado_cotizacion WHERE desc_estado_cotizacion = 'Generada');
INSERT INTO estado_cotizacion (desc_estado_cotizacion)
SELECT 'Aprobada' WHERE NOT EXISTS (SELECT 1 FROM estado_cotizacion WHERE desc_estado_cotizacion = 'Aprobada');
INSERT INTO estado_cotizacion (desc_estado_cotizacion)
SELECT 'Rechazada' WHERE NOT EXISTS (SELECT 1 FROM estado_cotizacion WHERE desc_estado_cotizacion = 'Rechazada');
INSERT INTO estado_cotizacion (desc_estado_cotizacion)
SELECT 'Anulada' WHERE NOT EXISTS (SELECT 1 FROM estado_cotizacion WHERE desc_estado_cotizacion = 'Anulada');

ALTER TABLE perfil ADD COLUMN IF NOT EXISTS id_estado_perfil INT NULL;
UPDATE perfil p
LEFT JOIN estado e ON e.id_estado = p.id_estado
LEFT JOIN estado_perfil ep ON ep.des_estado = CASE WHEN UPPER(COALESCE(e.desc_estado,'')) = 'HABILITADO' THEN 'Activo' ELSE 'Inactivo' END
SET p.id_estado_perfil = COALESCE(p.id_estado_perfil, ep.id_estado_perfil);
ALTER TABLE perfil ADD CONSTRAINT fk_perfil_estado_perfil FOREIGN KEY (id_estado_perfil) REFERENCES estado_perfil(id_estado_perfil);

ALTER TABLE usuario ADD COLUMN IF NOT EXISTS id_estado_usuario INT NULL;
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS fec_nacimiento DATE NULL;
UPDATE usuario u
LEFT JOIN estado e ON e.id_estado = u.id_estado
LEFT JOIN estado_usuario eu ON eu.des_estado = CASE
    WHEN UPPER(COALESCE(e.desc_estado,'')) = 'BLOQUEADO' THEN 'Bloqueado'
    WHEN UPPER(COALESCE(e.desc_estado,'')) = 'INHABILITADO' THEN 'Inhabilitado'
    ELSE 'Habilitado'
END
SET u.id_estado_usuario = COALESCE(u.id_estado_usuario, eu.id_estado_usuario);
ALTER TABLE usuario ADD CONSTRAINT fk_usuario_estado_usuario FOREIGN KEY (id_estado_usuario) REFERENCES estado_usuario(id_estado_usuario);

CREATE TABLE IF NOT EXISTS tipo_cliente (
    id_tipo_cliente INT AUTO_INCREMENT PRIMARY KEY,
    desc_tipo_cliente VARCHAR(120) NOT NULL UNIQUE,
    id_estado_cliente_contacto INT NOT NULL,
    usu_registro VARCHAR(50),
    fec_registro DATETIME,
    usu_actualiza VARCHAR(50),
    fec_actualiza DATETIME,
    CONSTRAINT fk_tipo_cliente_estado_cc FOREIGN KEY (id_estado_cliente_contacto) REFERENCES estado_cliente_contacto(id_estado_cliente_contacto)
);

INSERT INTO tipo_cliente (desc_tipo_cliente, id_estado_cliente_contacto, usu_registro, fec_registro)
SELECT 'Minorista / Consumidor Final', 1, 'flyway', NOW()
WHERE NOT EXISTS (SELECT 1 FROM tipo_cliente WHERE desc_tipo_cliente = 'Minorista / Consumidor Final');
INSERT INTO tipo_cliente (desc_tipo_cliente, id_estado_cliente_contacto, usu_registro, fec_registro)
SELECT 'Cliente Corporativo (Regular)', 1, 'flyway', NOW()
WHERE NOT EXISTS (SELECT 1 FROM tipo_cliente WHERE desc_tipo_cliente = 'Cliente Corporativo (Regular)');
INSERT INTO tipo_cliente (desc_tipo_cliente, id_estado_cliente_contacto, usu_registro, fec_registro)
SELECT 'Mayorista / Distribuidor', 1, 'flyway', NOW()
WHERE NOT EXISTS (SELECT 1 FROM tipo_cliente WHERE desc_tipo_cliente = 'Mayorista / Distribuidor');

ALTER TABLE cliente ADD COLUMN IF NOT EXISTS id_tipo_cliente INT NULL;
ALTER TABLE cliente ADD COLUMN IF NOT EXISTS id_estado_cliente_contacto INT NULL;
ALTER TABLE cliente ADD COLUMN IF NOT EXISTS usu_actualiza VARCHAR(50) NULL;
ALTER TABLE cliente ADD COLUMN IF NOT EXISTS fec_actualiza DATETIME NULL;
UPDATE cliente SET id_tipo_cliente = COALESCE(id_tipo_cliente, CASE WHEN ruc LIKE '20%' THEN 2 ELSE 1 END);
UPDATE cliente c
LEFT JOIN estado e ON e.id_estado = c.id_estado
LEFT JOIN estado_cliente_contacto ec ON ec.des_estado_cliente_contacto = CASE
    WHEN UPPER(COALESCE(e.desc_estado,'')) = 'BLOQUEADO' THEN 'Bloqueado'
    WHEN UPPER(COALESCE(e.desc_estado,'')) = 'INHABILITADO' THEN 'Inactivo'
    ELSE 'Activo'
END
SET c.id_estado_cliente_contacto = COALESCE(c.id_estado_cliente_contacto, ec.id_estado_cliente_contacto);
ALTER TABLE cliente ADD CONSTRAINT fk_cliente_tipo_cliente FOREIGN KEY (id_tipo_cliente) REFERENCES tipo_cliente(id_tipo_cliente);
ALTER TABLE cliente ADD CONSTRAINT fk_cliente_estado_cc FOREIGN KEY (id_estado_cliente_contacto) REFERENCES estado_cliente_contacto(id_estado_cliente_contacto);

ALTER TABLE contacto_cliente ADD COLUMN IF NOT EXISTS id_estado_cliente_contacto INT NULL;
ALTER TABLE contacto_cliente ADD COLUMN IF NOT EXISTS usu_registro VARCHAR(50) NULL;
ALTER TABLE contacto_cliente ADD COLUMN IF NOT EXISTS fec_registro DATETIME NULL;
ALTER TABLE contacto_cliente ADD COLUMN IF NOT EXISTS usu_actualiza VARCHAR(50) NULL;
ALTER TABLE contacto_cliente ADD COLUMN IF NOT EXISTS fec_actualiza DATETIME NULL;
UPDATE contacto_cliente cc
LEFT JOIN estado e ON e.id_estado = cc.id_estado
LEFT JOIN estado_cliente_contacto ec ON ec.des_estado_cliente_contacto = CASE
    WHEN UPPER(COALESCE(e.desc_estado,'')) = 'BLOQUEADO' THEN 'Bloqueado'
    WHEN UPPER(COALESCE(e.desc_estado,'')) = 'INHABILITADO' THEN 'Inactivo'
    ELSE 'Activo'
END
SET cc.id_estado_cliente_contacto = COALESCE(cc.id_estado_cliente_contacto, ec.id_estado_cliente_contacto);
ALTER TABLE contacto_cliente ADD CONSTRAINT fk_contacto_estado_cc FOREIGN KEY (id_estado_cliente_contacto) REFERENCES estado_cliente_contacto(id_estado_cliente_contacto);

ALTER TABLE producto ADD COLUMN IF NOT EXISTS peso DECIMAL(12,3) NULL;
ALTER TABLE producto ADD COLUMN IF NOT EXISTS volumen DECIMAL(12,3) NULL;
ALTER TABLE producto ADD COLUMN IF NOT EXISTS cant_min_venta INT NULL;
ALTER TABLE producto ADD COLUMN IF NOT EXISTS id_estado_producto INT NULL;
ALTER TABLE producto ADD COLUMN IF NOT EXISTS usu_registro VARCHAR(50) NULL;
ALTER TABLE producto ADD COLUMN IF NOT EXISTS fec_registro DATETIME NULL;
ALTER TABLE producto ADD COLUMN IF NOT EXISTS usu_actualiza VARCHAR(50) NULL;
ALTER TABLE producto ADD COLUMN IF NOT EXISTS fec_actualiza DATETIME NULL;
UPDATE producto p
LEFT JOIN estado e ON e.id_estado = p.id_estado
LEFT JOIN estado_producto ep ON ep.desc_estado_producto = CASE
    WHEN UPPER(COALESCE(e.desc_estado,'')) = 'BLOQUEADO' THEN 'Bloqueado'
    WHEN UPPER(COALESCE(e.desc_estado,'')) = 'INHABILITADO' THEN 'Inactivo'
    ELSE 'Activo'
END
SET p.id_estado_producto = COALESCE(p.id_estado_producto, ep.id_estado_producto),
    p.cant_min_venta = COALESCE(p.cant_min_venta, 1);
ALTER TABLE producto ADD CONSTRAINT fk_producto_estado_producto FOREIGN KEY (id_estado_producto) REFERENCES estado_producto(id_estado_producto);

CREATE TABLE IF NOT EXISTS precio_tipo_cliente (
    id_precio INT AUTO_INCREMENT PRIMARY KEY,
    precio_unitario DECIMAL(18,2) NOT NULL,
    moneda VARCHAR(10) NOT NULL,
    id_tipo_cliente INT NOT NULL,
    id_estado_producto INT NOT NULL,
    id_producto INT NOT NULL,
    usu_registro VARCHAR(50),
    fec_registro DATETIME,
    usu_actualiza VARCHAR(50),
    fec_actualiza DATETIME,
    CONSTRAINT fk_precio_tipo_cliente FOREIGN KEY (id_tipo_cliente) REFERENCES tipo_cliente(id_tipo_cliente),
    CONSTRAINT fk_precio_estado_producto FOREIGN KEY (id_estado_producto) REFERENCES estado_producto(id_estado_producto),
    CONSTRAINT fk_precio_producto FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
);

INSERT INTO precio_tipo_cliente (precio_unitario, moneda, id_tipo_cliente, id_estado_producto, id_producto, usu_registro, fec_registro)
SELECT p.precio_base_unitario, 'PEN', tc.id_tipo_cliente, COALESCE(p.id_estado_producto, 1), p.id_producto, 'flyway', NOW()
FROM producto p
CROSS JOIN tipo_cliente tc
WHERE NOT EXISTS (
  SELECT 1 FROM precio_tipo_cliente ptc WHERE ptc.id_producto = p.id_producto AND ptc.id_tipo_cliente = tc.id_tipo_cliente
);

ALTER TABLE cotizacion ADD COLUMN IF NOT EXISTS moneda VARCHAR(10) NULL;
ALTER TABLE cotizacion ADD COLUMN IF NOT EXISTS importe_total DECIMAL(18,2) NULL;
ALTER TABLE cotizacion ADD COLUMN IF NOT EXISTS direccion_despacho VARCHAR(255) NULL;
ALTER TABLE cotizacion ADD COLUMN IF NOT EXISTS dep_prov_dis VARCHAR(150) NULL;
ALTER TABLE cotizacion ADD COLUMN IF NOT EXISTS flag_cubierto TINYINT(1) NULL;
ALTER TABLE cotizacion ADD COLUMN IF NOT EXISTS observaciones VARCHAR(500) NULL;
ALTER TABLE cotizacion ADD COLUMN IF NOT EXISTS id_estado_cotizacion INT NULL;
UPDATE cotizacion c
LEFT JOIN estado_cotizacion ec ON ec.desc_estado_cotizacion = CASE UPPER(COALESCE(c.estado_cotizacion,''))
    WHEN 'APROBADA' THEN 'Aprobada'
    WHEN 'RECHAZADA' THEN 'Rechazada'
    WHEN 'ANULADA' THEN 'Anulada'
    ELSE 'Generada'
END
SET c.id_estado_cotizacion = COALESCE(c.id_estado_cotizacion, ec.id_estado_cotizacion),
    c.moneda = COALESCE(c.moneda, 'PEN'),
    c.importe_total = COALESCE(c.importe_total, c.monto_total),
    c.flag_cubierto = COALESCE(c.flag_cubierto, 0);
ALTER TABLE cotizacion ADD CONSTRAINT fk_cotizacion_estado FOREIGN KEY (id_estado_cotizacion) REFERENCES estado_cotizacion(id_estado_cotizacion);

ALTER TABLE cotizacion_detalle ADD COLUMN IF NOT EXISTS precio_uni DECIMAL(18,2) NULL;
UPDATE cotizacion_detalle SET precio_uni = COALESCE(precio_uni, precio_unitario_aplicado);
