-- V5: Tabla Ubigeo INEI con datos del INEI Peru (1874 registros)
-- Idempotente: usa IF NOT EXISTS y solo inserta si la tabla esta vacia

CREATE TABLE IF NOT EXISTS ubigeo_inei (
    id_ubigeo_inei INT AUTO_INCREMENT PRIMARY KEY,
    ubigeo         VARCHAR(6)   NOT NULL,
    distrito       VARCHAR(150) NOT NULL,
    provincia      VARCHAR(150) NOT NULL,
    departamento   VARCHAR(150) NOT NULL,
    flag_cobertura INT          NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Inserta datos solo si la tabla esta vacia (evita duplicados en deployments Docker)
INSERT INTO ubigeo_inei (ubigeo, distrito, provincia, departamento, flag_cobertura)
SELECT ubigeo, distrito, provincia, departamento, flag_cobertura FROM (
    SELECT '010101', 'Chachapoyas', 'Chachapoyas', 'Amazonas', 0
    UNION ALL
    SELECT '010102', 'Asuncion', 'Chachapoyas', 'Amazonas', 0
    UNION ALL
    SELECT '010103', 'Balsas', 'Chachapoyas', 'Amazonas', 0
    UNION ALL
    SELECT '010104', 'Cheto', 'Chachapoyas', 'Amazonas', 0
    UNION ALL
    SELECT '010105', 'Chiliquin', 'Chachapoyas', 'Amazonas', 0
    UNION ALL
    SELECT '010106', 'Chuquibamba', 'Chachapoyas', 'Amazonas', 0
    UNION ALL
    SELECT '010107', 'Granada', 'Chachapoyas', 'Amazonas', 0
    UNION ALL
    SELECT '010108', 'Huancas', 'Chachapoyas', 'Amazonas', 0
    UNION ALL
    SELECT '010109', 'La Jalca', 'Chachapoyas', 'Amazonas', 0
    UNION ALL
    SELECT '010110', 'Leimebamba', 'Chachapoyas', 'Amazonas', 0
    UNION ALL
    SELECT '010111', 'Levanto', 'Chachapoyas', 'Amazonas', 0
    UNION ALL
    SELECT '010112', 'Magdalena', 'Chachapoyas', 'Amazonas', 0
    UNION ALL
    SELECT '010113', 'Mariscal Castilla', 'Chachapoyas', 'Amazonas', 0
    UNION ALL
    SELECT '010114', 'Molinopampa', 'Chachapoyas', 'Amazonas', 0
    UNION ALL
    SELECT '010115', 'Montevideo', 'Chachapoyas', 'Amazonas', 0
    UNION ALL
    SELECT '010116', 'Olleros', 'Chachapoyas', 'Amazonas', 0
    UNION ALL
    SELECT '010117', 'Quinjalca', 'Chachapoyas', 'Amazonas', 0
    UNION ALL
    SELECT '010118', 'San Francisco de Daguas', 'Chachapoyas', 'Amazonas', 0
    UNION ALL
    SELECT '010119', 'San Isidro de Maino', 'Chachapoyas', 'Amazonas', 0
    UNION ALL
    SELECT '010120', 'Soloco', 'Chachapoyas', 'Amazonas', 0
    UNION ALL
    SELECT '010121', 'Sonche', 'Chachapoyas', 'Amazonas', 0
    UNION ALL
    SELECT '010201', 'Bagua', 'Bagua', 'Amazonas', 0
    UNION ALL
    SELECT '010202', 'Aramango', 'Bagua', 'Amazonas', 0
    UNION ALL
    SELECT '010203', 'Copallin', 'Bagua', 'Amazonas', 0
    UNION ALL
    SELECT '010204', 'El Parco', 'Bagua', 'Amazonas', 0
    UNION ALL
    SELECT '010205', 'Imaza', 'Bagua', 'Amazonas', 0
    UNION ALL
    SELECT '010206', 'La Peca', 'Bagua', 'Amazonas', 0
    UNION ALL
    SELECT '010301', 'Jumbilla', 'Bongara', 'Amazonas', 0
    UNION ALL
    SELECT '010302', 'Chisquilla', 'Bongara', 'Amazonas', 0
    UNION ALL
    SELECT '010303', 'Churuja', 'Bongara', 'Amazonas', 0
    UNION ALL
    SELECT '010304', 'Corosha', 'Bongara', 'Amazonas', 0
    UNION ALL
    SELECT '010305', 'Cuispes', 'Bongara', 'Amazonas', 0
    UNION ALL
    SELECT '010306', 'Florida', 'Bongara', 'Amazonas', 0
    UNION ALL
    SELECT '010307', 'Jazan', 'Bongara', 'Amazonas', 0
    UNION ALL
    SELECT '010308', 'Recta', 'Bongara', 'Amazonas', 0
    UNION ALL
    SELECT '010309', 'San Carlos', 'Bongara', 'Amazonas', 0
    UNION ALL
    SELECT '010310', 'Shipasbamba', 'Bongara', 'Amazonas', 0
    UNION ALL
    SELECT '010311', 'Valera', 'Bongara', 'Amazonas', 0
    UNION ALL
    SELECT '010312', 'Yambrasbamba', 'Bongara', 'Amazonas', 0
    UNION ALL
    SELECT '010401', 'Nieva', 'Condorcanqui', 'Amazonas', 0
    UNION ALL
    SELECT '010402', 'El Cenepa', 'Condorcanqui', 'Amazonas', 0
    UNION ALL
    SELECT '010403', 'Rio Santiago', 'Condorcanqui', 'Amazonas', 0
    UNION ALL
    SELECT '010501', 'Lamud', 'Luya', 'Amazonas', 0
    UNION ALL
    SELECT '010502', 'Camporredondo', 'Luya', 'Amazonas', 0
    UNION ALL
    SELECT '010503', 'Cocabamba', 'Luya', 'Amazonas', 0
    UNION ALL
    SELECT '010504', 'Colcamar', 'Luya', 'Amazonas', 0
    UNION ALL
    SELECT '010505', 'Conila', 'Luya', 'Amazonas', 0
    UNION ALL
    SELECT '010506', 'Inguilpata', 'Luya', 'Amazonas', 0
    UNION ALL
    SELECT '010507', 'Longuita', 'Luya', 'Amazonas', 0
    UNION ALL
    SELECT '010508', 'Lonya Chico', 'Luya', 'Amazonas', 0
    UNION ALL
    SELECT '010509', 'Luya', 'Luya', 'Amazonas', 0
    UNION ALL
    SELECT '010510', 'Luya Viejo', 'Luya', 'Amazonas', 0
    UNION ALL
    SELECT '010511', 'Maria', 'Luya', 'Amazonas', 0
    UNION ALL
    SELECT '010512', 'Ocalli', 'Luya', 'Amazonas', 0
    UNION ALL
    SELECT '010513', 'Ocumal', 'Luya', 'Amazonas', 0
    UNION ALL
    SELECT '010514', 'Pisuquia', 'Luya', 'Amazonas', 0
    UNION ALL
    SELECT '010515', 'Providencia', 'Luya', 'Amazonas', 0
    UNION ALL
    SELECT '010516', 'San Cristobal', 'Luya', 'Amazonas', 0
    UNION ALL
    SELECT '010517', 'San Francisco del Yeso', 'Luya', 'Amazonas', 0
    UNION ALL
    SELECT '010518', 'San Jeronimo', 'Luya', 'Amazonas', 0
    UNION ALL
    SELECT '010519', 'San Juan de Lopecancha', 'Luya', 'Amazonas', 0
    UNION ALL
    SELECT '010520', 'Santa Catalina', 'Luya', 'Amazonas', 0
    UNION ALL
    SELECT '010521', 'Santo Tomas', 'Luya', 'Amazonas', 0
    UNION ALL
    SELECT '010522', 'Tingo', 'Luya', 'Amazonas', 0
    UNION ALL
    SELECT '010523', 'Trita', 'Luya', 'Amazonas', 0
    UNION ALL
    SELECT '010601', 'San Nicolas', 'Rodriguez de Mendoza', 'Amazonas', 0
    UNION ALL
    SELECT '010602', 'Chirimoto', 'Rodriguez de Mendoza', 'Amazonas', 0
    UNION ALL
    SELECT '010603', 'Cochamal', 'Rodriguez de Mendoza', 'Amazonas', 0
    UNION ALL
    SELECT '010604', 'Huambo', 'Rodriguez de Mendoza', 'Amazonas', 0
    UNION ALL
    SELECT '010605', 'Limabamba', 'Rodriguez de Mendoza', 'Amazonas', 0
    UNION ALL
    SELECT '010606', 'Longar', 'Rodriguez de Mendoza', 'Amazonas', 0
    UNION ALL
    SELECT '010607', 'Mariscal Benavides', 'Rodriguez de Mendoza', 'Amazonas', 0
    UNION ALL
    SELECT '010608', 'Milpuc', 'Rodriguez de Mendoza', 'Amazonas', 0
    UNION ALL
    SELECT '010609', 'Omia', 'Rodriguez de Mendoza', 'Amazonas', 0
    UNION ALL
    SELECT '010610', 'Santa Rosa', 'Rodriguez de Mendoza', 'Amazonas', 0
    UNION ALL
    SELECT '010611', 'Totora', 'Rodriguez de Mendoza', 'Amazonas', 0
    UNION ALL
    SELECT '010612', 'Vista Alegre', 'Rodriguez de Mendoza', 'Amazonas', 0
    UNION ALL
    SELECT '010701', 'Bagua Grande', 'Utcubamba', 'Amazonas', 0
    UNION ALL
    SELECT '010702', 'Cajaruro', 'Utcubamba', 'Amazonas', 0
    UNION ALL
    SELECT '010703', 'Cumba', 'Utcubamba', 'Amazonas', 0
    UNION ALL
    SELECT '010704', 'El Milagro', 'Utcubamba', 'Amazonas', 0
    UNION ALL
    SELECT '010705', 'Jamalca', 'Utcubamba', 'Amazonas', 0
    UNION ALL
    SELECT '010706', 'Lonya Grande', 'Utcubamba', 'Amazonas', 0
    UNION ALL
    SELECT '010707', 'Yamon', 'Utcubamba', 'Amazonas', 0
    UNION ALL
    SELECT '020101', 'Huaraz', 'Huaraz', 'Ancash', 0
    UNION ALL
    SELECT '020102', 'Cochabamba', 'Huaraz', 'Ancash', 0
    UNION ALL
    SELECT '020103', 'Colcabamba', 'Huaraz', 'Ancash', 0
    UNION ALL
    SELECT '020104', 'Huanchay', 'Huaraz', 'Ancash', 0
    UNION ALL
    SELECT '020105', 'Independencia', 'Huaraz', 'Ancash', 0
    UNION ALL
    SELECT '020106', 'Jangas', 'Huaraz', 'Ancash', 0
    UNION ALL
    SELECT '020107', 'La Libertad', 'Huaraz', 'Ancash', 0
    UNION ALL
    SELECT '020108', 'Olleros', 'Huaraz', 'Ancash', 0
    UNION ALL
    SELECT '020109', 'Pampas', 'Huaraz', 'Ancash', 0
    UNION ALL
    SELECT '020110', 'Pariacoto', 'Huaraz', 'Ancash', 0
    UNION ALL
    SELECT '020111', 'Pira', 'Huaraz', 'Ancash', 0
    UNION ALL
    SELECT '020112', 'Tarica', 'Huaraz', 'Ancash', 0
    UNION ALL
    SELECT '020201', 'Aija', 'Aija', 'Ancash', 0
    UNION ALL
    SELECT '020202', 'Coris', 'Aija', 'Ancash', 0
    UNION ALL
    SELECT '020203', 'Huacllan', 'Aija', 'Ancash', 0
    UNION ALL
    SELECT '020204', 'La Merced', 'Aija', 'Ancash', 0
    UNION ALL
    SELECT '020205', 'Succha', 'Aija', 'Ancash', 0
    UNION ALL
    SELECT '020301', 'Llamellin', 'Antonio Raymondi', 'Ancash', 0
    UNION ALL
    SELECT '020302', 'Aczo', 'Antonio Raymondi', 'Ancash', 0
    UNION ALL
    SELECT '020303', 'Chaccho', 'Antonio Raymondi', 'Ancash', 0
    UNION ALL
    SELECT '020304', 'Chingas', 'Antonio Raymondi', 'Ancash', 0
    UNION ALL
    SELECT '020305', 'Mirgas', 'Antonio Raymondi', 'Ancash', 0
    UNION ALL
    SELECT '020306', 'San Juan de Rontoy', 'Antonio Raymondi', 'Ancash', 0
    UNION ALL
    SELECT '020401', 'Chacas', 'Asuncion', 'Ancash', 0
    UNION ALL
    SELECT '020402', 'Acochaca', 'Asuncion', 'Ancash', 0
    UNION ALL
    SELECT '020501', 'Chiquian', 'Bolognesi', 'Ancash', 0
    UNION ALL
    SELECT '020502', 'Abelardo Pardo Lezameta', 'Bolognesi', 'Ancash', 0
    UNION ALL
    SELECT '020503', 'Antonio Raymondi', 'Bolognesi', 'Ancash', 0
    UNION ALL
    SELECT '020504', 'Aquia', 'Bolognesi', 'Ancash', 0
    UNION ALL
    SELECT '020505', 'Cajacay', 'Bolognesi', 'Ancash', 0
    UNION ALL
    SELECT '020506', 'Canis', 'Bolognesi', 'Ancash', 0
    UNION ALL
    SELECT '020507', 'Colquioc', 'Bolognesi', 'Ancash', 0
    UNION ALL
    SELECT '020508', 'Huallanca', 'Bolognesi', 'Ancash', 0
    UNION ALL
    SELECT '020509', 'Huasta', 'Bolognesi', 'Ancash', 0
    UNION ALL
    SELECT '020510', 'Huayllacayan', 'Bolognesi', 'Ancash', 0
    UNION ALL
    SELECT '020511', 'La Primavera', 'Bolognesi', 'Ancash', 0
    UNION ALL
    SELECT '020512', 'Mangas', 'Bolognesi', 'Ancash', 0
    UNION ALL
    SELECT '020513', 'Pacllon', 'Bolognesi', 'Ancash', 0
    UNION ALL
    SELECT '020514', 'San Miguel de Corpanqui', 'Bolognesi', 'Ancash', 0
    UNION ALL
    SELECT '020515', 'Ticllos', 'Bolognesi', 'Ancash', 0
    UNION ALL
    SELECT '020601', 'Carhuaz', 'Carhuaz', 'Ancash', 0
    UNION ALL
    SELECT '020602', 'Acopampa', 'Carhuaz', 'Ancash', 0
    UNION ALL
    SELECT '020603', 'Amashca', 'Carhuaz', 'Ancash', 0
    UNION ALL
    SELECT '020604', 'Anta', 'Carhuaz', 'Ancash', 0
    UNION ALL
    SELECT '020605', 'Ataquero', 'Carhuaz', 'Ancash', 0
    UNION ALL
    SELECT '020606', 'Marcara', 'Carhuaz', 'Ancash', 0
    UNION ALL
    SELECT '020607', 'Pariahuanca', 'Carhuaz', 'Ancash', 0
    UNION ALL
    SELECT '020608', 'San Miguel de Aco', 'Carhuaz', 'Ancash', 0
    UNION ALL
    SELECT '020609', 'Shilla', 'Carhuaz', 'Ancash', 0
    UNION ALL
    SELECT '020610', 'Tinco', 'Carhuaz', 'Ancash', 0
    UNION ALL
    SELECT '020611', 'Yungar', 'Carhuaz', 'Ancash', 0
    UNION ALL
    SELECT '020701', 'San Luis', 'Carlos Fermin Fitzca', 'Ancash', 0
    UNION ALL
    SELECT '020702', 'San Nicolas', 'Carlos Fermin Fitzca', 'Ancash', 0
    UNION ALL
    SELECT '020703', 'Yauya', 'Carlos Fermin Fitzca', 'Ancash', 0
    UNION ALL
    SELECT '020801', 'Casma', 'Casma', 'Ancash', 0
    UNION ALL
    SELECT '020802', 'Buena Vista Alta', 'Casma', 'Ancash', 0
    UNION ALL
    SELECT '020803', 'Comandante Noel', 'Casma', 'Ancash', 0
    UNION ALL
    SELECT '020804', 'Yautan', 'Casma', 'Ancash', 0
    UNION ALL
    SELECT '020901', 'Corongo', 'Corongo', 'Ancash', 0
    UNION ALL
    SELECT '020902', 'Aco', 'Corongo', 'Ancash', 0
    UNION ALL
    SELECT '020903', 'Bambas', 'Corongo', 'Ancash', 0
    UNION ALL
    SELECT '020904', 'Cusca', 'Corongo', 'Ancash', 0
    UNION ALL
    SELECT '020905', 'La Pampa', 'Corongo', 'Ancash', 0
    UNION ALL
    SELECT '020906', 'Yanac', 'Corongo', 'Ancash', 0
    UNION ALL
    SELECT '020907', 'Yupan', 'Corongo', 'Ancash', 0
    UNION ALL
    SELECT '021001', 'Huari', 'Huari', 'Ancash', 0
    UNION ALL
    SELECT '021002', 'Anra', 'Huari', 'Ancash', 0
    UNION ALL
    SELECT '021003', 'Cajay', 'Huari', 'Ancash', 0
    UNION ALL
    SELECT '021004', 'Chavin de Huantar', 'Huari', 'Ancash', 0
    UNION ALL
    SELECT '021005', 'Huacachi', 'Huari', 'Ancash', 0
    UNION ALL
    SELECT '021006', 'Huacchis', 'Huari', 'Ancash', 0
    UNION ALL
    SELECT '021007', 'Huachis', 'Huari', 'Ancash', 0
    UNION ALL
    SELECT '021008', 'Huantar', 'Huari', 'Ancash', 0
    UNION ALL
    SELECT '021009', 'Masin', 'Huari', 'Ancash', 0
    UNION ALL
    SELECT '021010', 'Paucas', 'Huari', 'Ancash', 0
    UNION ALL
    SELECT '021011', 'Ponto', 'Huari', 'Ancash', 0
    UNION ALL
    SELECT '021012', 'Rahuapampa', 'Huari', 'Ancash', 0
    UNION ALL
    SELECT '021013', 'Rapayan', 'Huari', 'Ancash', 0
    UNION ALL
    SELECT '021014', 'San Marcos', 'Huari', 'Ancash', 0
    UNION ALL
    SELECT '021015', 'San Pedro de Chana', 'Huari', 'Ancash', 0
    UNION ALL
    SELECT '021016', 'Uco', 'Huari', 'Ancash', 0
    UNION ALL
    SELECT '021101', 'Huarmey', 'Huarmey', 'Ancash', 0
    UNION ALL
    SELECT '021102', 'Cochapeti', 'Huarmey', 'Ancash', 0
    UNION ALL
    SELECT '021103', 'Culebras', 'Huarmey', 'Ancash', 0
    UNION ALL
    SELECT '021104', 'Huayan', 'Huarmey', 'Ancash', 0
    UNION ALL
    SELECT '021105', 'Malvas', 'Huarmey', 'Ancash', 0
    UNION ALL
    SELECT '021201', 'Caraz', 'Huaylas', 'Ancash', 0
    UNION ALL
    SELECT '021202', 'Huallanca', 'Huaylas', 'Ancash', 0
    UNION ALL
    SELECT '021203', 'Huata', 'Huaylas', 'Ancash', 0
    UNION ALL
    SELECT '021204', 'Huaylas', 'Huaylas', 'Ancash', 0
    UNION ALL
    SELECT '021205', 'Mato', 'Huaylas', 'Ancash', 0
    UNION ALL
    SELECT '021206', 'Pamparomas', 'Huaylas', 'Ancash', 0
    UNION ALL
    SELECT '021207', 'Pueblo Libre', 'Huaylas', 'Ancash', 0
    UNION ALL
    SELECT '021208', 'Santa Cruz', 'Huaylas', 'Ancash', 0
    UNION ALL
    SELECT '021209', 'Santo Toribio', 'Huaylas', 'Ancash', 0
    UNION ALL
    SELECT '021210', 'Yuracmarca', 'Huaylas', 'Ancash', 0
    UNION ALL
    SELECT '021301', 'Piscobamba', 'Mariscal Luzuriaga', 'Ancash', 0
    UNION ALL
    SELECT '021302', 'Casca', 'Mariscal Luzuriaga', 'Ancash', 0
    UNION ALL
    SELECT '021303', 'Eleazar Guzman Barron', 'Mariscal Luzuriaga', 'Ancash', 0
    UNION ALL
    SELECT '021304', 'Fidel Olivas Escudero', 'Mariscal Luzuriaga', 'Ancash', 0
    UNION ALL
    SELECT '021305', 'Llama', 'Mariscal Luzuriaga', 'Ancash', 0
    UNION ALL
    SELECT '021306', 'Llumpa', 'Mariscal Luzuriaga', 'Ancash', 0
    UNION ALL
    SELECT '021307', 'Lucma', 'Mariscal Luzuriaga', 'Ancash', 0
    UNION ALL
    SELECT '021308', 'Musga', 'Mariscal Luzuriaga', 'Ancash', 0
    UNION ALL
    SELECT '021401', 'Ocros', 'Ocros', 'Ancash', 0
    UNION ALL
    SELECT '021402', 'Acas', 'Ocros', 'Ancash', 0
    UNION ALL
    SELECT '021403', 'Cajamarquilla', 'Ocros', 'Ancash', 0
    UNION ALL
    SELECT '021404', 'Carhuapampa', 'Ocros', 'Ancash', 0
    UNION ALL
    SELECT '021405', 'Cochas', 'Ocros', 'Ancash', 0
    UNION ALL
    SELECT '021406', 'Congas', 'Ocros', 'Ancash', 0
    UNION ALL
    SELECT '021407', 'Llipa', 'Ocros', 'Ancash', 0
    UNION ALL
    SELECT '021408', 'San Cristobal de Rajan', 'Ocros', 'Ancash', 0
    UNION ALL
    SELECT '021409', 'San Pedro', 'Ocros', 'Ancash', 0
    UNION ALL
    SELECT '021410', 'Santiago de Chilcas', 'Ocros', 'Ancash', 0
    UNION ALL
    SELECT '021501', 'Cabana', 'Pallasca', 'Ancash', 0
    UNION ALL
    SELECT '021502', 'Bolognesi', 'Pallasca', 'Ancash', 0
    UNION ALL
    SELECT '021503', 'Conchucos', 'Pallasca', 'Ancash', 0
    UNION ALL
    SELECT '021504', 'Huacaschuque', 'Pallasca', 'Ancash', 0
    UNION ALL
    SELECT '021505', 'Huandoval', 'Pallasca', 'Ancash', 0
    UNION ALL
    SELECT '021506', 'Lacabamba', 'Pallasca', 'Ancash', 0
    UNION ALL
    SELECT '021507', 'Llapo', 'Pallasca', 'Ancash', 0
    UNION ALL
    SELECT '021508', 'Pallasca', 'Pallasca', 'Ancash', 0
    UNION ALL
    SELECT '021509', 'Pampas', 'Pallasca', 'Ancash', 0
    UNION ALL
    SELECT '021510', 'Santa Rosa', 'Pallasca', 'Ancash', 0
    UNION ALL
    SELECT '021511', 'Tauca', 'Pallasca', 'Ancash', 0
    UNION ALL
    SELECT '021601', 'Pomabamba', 'Pomabamba', 'Ancash', 0
    UNION ALL
    SELECT '021602', 'Huayllan', 'Pomabamba', 'Ancash', 0
    UNION ALL
    SELECT '021603', 'Parobamba', 'Pomabamba', 'Ancash', 0
    UNION ALL
    SELECT '021604', 'Quinuabamba', 'Pomabamba', 'Ancash', 0
    UNION ALL
    SELECT '021701', 'Recuay', 'Recuay', 'Ancash', 0
    UNION ALL
    SELECT '021702', 'Catac', 'Recuay', 'Ancash', 0
    UNION ALL
    SELECT '021703', 'Cotaparaco', 'Recuay', 'Ancash', 0
    UNION ALL
    SELECT '021704', 'Huayllapampa', 'Recuay', 'Ancash', 0
    UNION ALL
    SELECT '021705', 'Llacllin', 'Recuay', 'Ancash', 0
    UNION ALL
    SELECT '021706', 'Marca', 'Recuay', 'Ancash', 0
    UNION ALL
    SELECT '021707', 'Pampas Chico', 'Recuay', 'Ancash', 0
    UNION ALL
    SELECT '021708', 'Pararin', 'Recuay', 'Ancash', 0
    UNION ALL
    SELECT '021709', 'Tapacocha', 'Recuay', 'Ancash', 0
    UNION ALL
    SELECT '021710', 'Ticapampa', 'Recuay', 'Ancash', 0
    UNION ALL
    SELECT '021801', 'Chimbote', 'Santa', 'Ancash', 0
    UNION ALL
    SELECT '021802', 'Caceres del Peru', 'Santa', 'Ancash', 0
    UNION ALL
    SELECT '021803', 'Coishco', 'Santa', 'Ancash', 0
    UNION ALL
    SELECT '021804', 'Macate', 'Santa', 'Ancash', 0
    UNION ALL
    SELECT '021805', 'Moro', 'Santa', 'Ancash', 0
    UNION ALL
    SELECT '021806', 'Nepeña', 'Santa', 'Ancash', 0
    UNION ALL
    SELECT '021807', 'Samanco', 'Santa', 'Ancash', 0
    UNION ALL
    SELECT '021808', 'Santa', 'Santa', 'Ancash', 0
    UNION ALL
    SELECT '021809', 'Nuevo Chimbote', 'Santa', 'Ancash', 0
    UNION ALL
    SELECT '021901', 'Sihuas', 'Sihuas', 'Ancash', 0
    UNION ALL
    SELECT '021902', 'Acobamba', 'Sihuas', 'Ancash', 0
    UNION ALL
    SELECT '021903', 'Alfonso Ugarte', 'Sihuas', 'Ancash', 0
    UNION ALL
    SELECT '021904', 'Cashapampa', 'Sihuas', 'Ancash', 0
    UNION ALL
    SELECT '021905', 'Chingalpo', 'Sihuas', 'Ancash', 0
    UNION ALL
    SELECT '021906', 'Huayllabamba', 'Sihuas', 'Ancash', 0
    UNION ALL
    SELECT '021907', 'Quiches', 'Sihuas', 'Ancash', 0
    UNION ALL
    SELECT '021908', 'Ragash', 'Sihuas', 'Ancash', 0
    UNION ALL
    SELECT '021909', 'San Juan', 'Sihuas', 'Ancash', 0
    UNION ALL
    SELECT '021910', 'Sicsibamba', 'Sihuas', 'Ancash', 0
    UNION ALL
    SELECT '022001', 'Yungay', 'Yungay', 'Ancash', 0
    UNION ALL
    SELECT '022002', 'Cascapara', 'Yungay', 'Ancash', 0
    UNION ALL
    SELECT '022003', 'Mancos', 'Yungay', 'Ancash', 0
    UNION ALL
    SELECT '022004', 'Matacoto', 'Yungay', 'Ancash', 0
    UNION ALL
    SELECT '022005', 'Quillo', 'Yungay', 'Ancash', 0
    UNION ALL
    SELECT '022006', 'Ranrahirca', 'Yungay', 'Ancash', 0
    UNION ALL
    SELECT '022007', 'Shupluy', 'Yungay', 'Ancash', 0
    UNION ALL
    SELECT '022008', 'Yanama', 'Yungay', 'Ancash', 0
    UNION ALL
    SELECT '030101', 'Abancay', 'Abancay', 'Apurimac', 0
    UNION ALL
    SELECT '030102', 'Chacoche', 'Abancay', 'Apurimac', 0
    UNION ALL
    SELECT '030103', 'Circa', 'Abancay', 'Apurimac', 0
    UNION ALL
    SELECT '030104', 'Curahuasi', 'Abancay', 'Apurimac', 0
    UNION ALL
    SELECT '030105', 'Huanipaca', 'Abancay', 'Apurimac', 0
    UNION ALL
    SELECT '030106', 'Lambrama', 'Abancay', 'Apurimac', 0
    UNION ALL
    SELECT '030107', 'Pichirhua', 'Abancay', 'Apurimac', 0
    UNION ALL
    SELECT '030108', 'San Pedro de Cachora', 'Abancay', 'Apurimac', 0
    UNION ALL
    SELECT '030109', 'Tamburco', 'Abancay', 'Apurimac', 0
    UNION ALL
    SELECT '030201', 'Andahuaylas', 'Andahuaylas', 'Apurimac', 0
    UNION ALL
    SELECT '030202', 'Andarapa', 'Andahuaylas', 'Apurimac', 0
    UNION ALL
    SELECT '030203', 'Chiara', 'Andahuaylas', 'Apurimac', 0
    UNION ALL
    SELECT '030204', 'Huancarama', 'Andahuaylas', 'Apurimac', 0
    UNION ALL
    SELECT '030205', 'Huancaray', 'Andahuaylas', 'Apurimac', 0
    UNION ALL
    SELECT '030206', 'Huayana', 'Andahuaylas', 'Apurimac', 0
    UNION ALL
    SELECT '030207', 'Kishuara', 'Andahuaylas', 'Apurimac', 0
    UNION ALL
    SELECT '030208', 'Pacobamba', 'Andahuaylas', 'Apurimac', 0
    UNION ALL
    SELECT '030209', 'Pacucha', 'Andahuaylas', 'Apurimac', 0
    UNION ALL
    SELECT '030210', 'Pampachiri', 'Andahuaylas', 'Apurimac', 0
    UNION ALL
    SELECT '030211', 'Pomacocha', 'Andahuaylas', 'Apurimac', 0
    UNION ALL
    SELECT '030212', 'San Antonio de Cachi', 'Andahuaylas', 'Apurimac', 0
    UNION ALL
    SELECT '030213', 'San Jeronimo', 'Andahuaylas', 'Apurimac', 0
    UNION ALL
    SELECT '030214', 'San Miguel de Chaccrampa', 'Andahuaylas', 'Apurimac', 0
    UNION ALL
    SELECT '030215', 'Santa Maria de Chicmo', 'Andahuaylas', 'Apurimac', 0
    UNION ALL
    SELECT '030216', 'Talavera', 'Andahuaylas', 'Apurimac', 0
    UNION ALL
    SELECT '030217', 'Tumay Huaraca', 'Andahuaylas', 'Apurimac', 0
    UNION ALL
    SELECT '030218', 'Turpo', 'Andahuaylas', 'Apurimac', 0
    UNION ALL
    SELECT '030219', 'Kaquiabamba', 'Andahuaylas', 'Apurimac', 0
    UNION ALL
    SELECT '030220', 'José María Arguedas', 'Andahuaylas', 'Apurimac', 0
    UNION ALL
    SELECT '030301', 'Antabamba', 'Antabamba', 'Apurimac', 0
    UNION ALL
    SELECT '030302', 'El Oro', 'Antabamba', 'Apurimac', 0
    UNION ALL
    SELECT '030303', 'Huaquirca', 'Antabamba', 'Apurimac', 0
    UNION ALL
    SELECT '030304', 'Juan Espinoza Medrano', 'Antabamba', 'Apurimac', 0
    UNION ALL
    SELECT '030305', 'Oropesa', 'Antabamba', 'Apurimac', 0
    UNION ALL
    SELECT '030306', 'Pachaconas', 'Antabamba', 'Apurimac', 0
    UNION ALL
    SELECT '030307', 'Sabaino', 'Antabamba', 'Apurimac', 0
    UNION ALL
    SELECT '030401', 'Chalhuanca', 'Aymaraes', 'Apurimac', 0
    UNION ALL
    SELECT '030402', 'Capaya', 'Aymaraes', 'Apurimac', 0
    UNION ALL
    SELECT '030403', 'Caraybamba', 'Aymaraes', 'Apurimac', 0
    UNION ALL
    SELECT '030404', 'Chapimarca', 'Aymaraes', 'Apurimac', 0
    UNION ALL
    SELECT '030405', 'Colcabamba', 'Aymaraes', 'Apurimac', 0
    UNION ALL
    SELECT '030406', 'Cotaruse', 'Aymaraes', 'Apurimac', 0
    UNION ALL
    SELECT '030407', 'Huayllo', 'Aymaraes', 'Apurimac', 0
    UNION ALL
    SELECT '030408', 'Justo Apu Sahuaraura', 'Aymaraes', 'Apurimac', 0
    UNION ALL
    SELECT '030409', 'Lucre', 'Aymaraes', 'Apurimac', 0
    UNION ALL
    SELECT '030410', 'Pocohuanca', 'Aymaraes', 'Apurimac', 0
    UNION ALL
    SELECT '030411', 'San Juan de Chacña', 'Aymaraes', 'Apurimac', 0
    UNION ALL
    SELECT '030412', 'Sañayca', 'Aymaraes', 'Apurimac', 0
    UNION ALL
    SELECT '030413', 'Soraya', 'Aymaraes', 'Apurimac', 0
    UNION ALL
    SELECT '030414', 'Tapairihua', 'Aymaraes', 'Apurimac', 0
    UNION ALL
    SELECT '030415', 'Tintay', 'Aymaraes', 'Apurimac', 0
    UNION ALL
    SELECT '030416', 'Toraya', 'Aymaraes', 'Apurimac', 0
    UNION ALL
    SELECT '030417', 'Yanaca', 'Aymaraes', 'Apurimac', 0
    UNION ALL
    SELECT '030501', 'Tambobamba', 'Cotabambas', 'Apurimac', 0
    UNION ALL
    SELECT '030502', 'Cotabambas', 'Cotabambas', 'Apurimac', 0
    UNION ALL
    SELECT '030503', 'Coyllurqui', 'Cotabambas', 'Apurimac', 0
    UNION ALL
    SELECT '030504', 'Haquira', 'Cotabambas', 'Apurimac', 0
    UNION ALL
    SELECT '030505', 'Mara', 'Cotabambas', 'Apurimac', 0
    UNION ALL
    SELECT '030506', 'Challhuahuacho', 'Cotabambas', 'Apurimac', 0
    UNION ALL
    SELECT '030601', 'Chincheros', 'Chincheros', 'Apurimac', 0
    UNION ALL
    SELECT '030602', 'Anco_Huallo', 'Chincheros', 'Apurimac', 0
    UNION ALL
    SELECT '030603', 'Cocharcas', 'Chincheros', 'Apurimac', 0
    UNION ALL
    SELECT '030604', 'Huaccana', 'Chincheros', 'Apurimac', 0
    UNION ALL
    SELECT '030605', 'Ocobamba', 'Chincheros', 'Apurimac', 0
    UNION ALL
    SELECT '030606', 'Ongoy', 'Chincheros', 'Apurimac', 0
    UNION ALL
    SELECT '030607', 'Uranmarca', 'Chincheros', 'Apurimac', 0
    UNION ALL
    SELECT '030608', 'Ranracancha', 'Chincheros', 'Apurimac', 0
    UNION ALL
    SELECT '030609', 'Rocchacc', 'Chincheros', 'Apurimac', 0
    UNION ALL
    SELECT '030610', 'El Porvenir', 'Chincheros', 'Apurimac', 0
    UNION ALL
    SELECT '030611', 'Los Chankas', 'Chincheros', 'Apurimac', 0
    UNION ALL
    SELECT '030701', 'Chuquibambilla', 'Grau', 'Apurimac', 0
    UNION ALL
    SELECT '030702', 'Curpahuasi', 'Grau', 'Apurimac', 0
    UNION ALL
    SELECT '030703', 'Gamarra', 'Grau', 'Apurimac', 0
    UNION ALL
    SELECT '030704', 'Huayllati', 'Grau', 'Apurimac', 0
    UNION ALL
    SELECT '030705', 'Mamara', 'Grau', 'Apurimac', 0
    UNION ALL
    SELECT '030706', 'Micaela Bastidas', 'Grau', 'Apurimac', 0
    UNION ALL
    SELECT '030707', 'Pataypampa', 'Grau', 'Apurimac', 0
    UNION ALL
    SELECT '030708', 'Progreso', 'Grau', 'Apurimac', 0
    UNION ALL
    SELECT '030709', 'San Antonio', 'Grau', 'Apurimac', 0
    UNION ALL
    SELECT '030710', 'Santa Rosa', 'Grau', 'Apurimac', 0
    UNION ALL
    SELECT '030711', 'Turpay', 'Grau', 'Apurimac', 0
    UNION ALL
    SELECT '030712', 'Vilcabamba', 'Grau', 'Apurimac', 0
    UNION ALL
    SELECT '030713', 'Virundo', 'Grau', 'Apurimac', 0
    UNION ALL
    SELECT '030714', 'Curasco', 'Grau', 'Apurimac', 0
    UNION ALL
    SELECT '040101', 'Arequipa', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040102', 'Alto Selva Alegre', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040103', 'Cayma', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040104', 'Cerro Colorado', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040105', 'Characato', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040106', 'Chiguata', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040107', 'Jacobo Hunter', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040108', 'La Joya', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040109', 'Mariano Melgar', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040110', 'Miraflores', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040111', 'Mollebaya', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040112', 'Paucarpata', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040113', 'Pocsi', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040114', 'Polobaya', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040115', 'Quequeña', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040116', 'Sabandia', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040117', 'Sachaca', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040118', 'San Juan de Siguas', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040119', 'San Juan de Tarucani', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040120', 'Santa Isabel de Siguas', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040121', 'Santa Rita de Siguas', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040122', 'Socabaya', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040123', 'Tiabaya', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040124', 'Uchumayo', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040125', 'Vitor', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040126', 'Yanahuara', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040127', 'Yarabamba', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040128', 'Yura', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040129', 'Jose Luis Bustamante y Rivero', 'Arequipa', 'Arequipa', 0
    UNION ALL
    SELECT '040201', 'Camana', 'Camana', 'Arequipa', 0
    UNION ALL
    SELECT '040202', 'Jose Maria Quimper', 'Camana', 'Arequipa', 0
    UNION ALL
    SELECT '040203', 'Mariano Nicolas Valcarcel', 'Camana', 'Arequipa', 0
    UNION ALL
    SELECT '040204', 'Mariscal Caceres', 'Camana', 'Arequipa', 0
    UNION ALL
    SELECT '040205', 'Nicolas de Pierola', 'Camana', 'Arequipa', 0
    UNION ALL
    SELECT '040206', 'Ocoña', 'Camana', 'Arequipa', 0
    UNION ALL
    SELECT '040207', 'Quilca', 'Camana', 'Arequipa', 0
    UNION ALL
    SELECT '040208', 'Samuel Pastor', 'Camana', 'Arequipa', 0
    UNION ALL
    SELECT '040301', 'Caraveli', 'Caraveli', 'Arequipa', 0
    UNION ALL
    SELECT '040302', 'Acari', 'Caraveli', 'Arequipa', 0
    UNION ALL
    SELECT '040303', 'Atico', 'Caraveli', 'Arequipa', 0
    UNION ALL
    SELECT '040304', 'Atiquipa', 'Caraveli', 'Arequipa', 0
    UNION ALL
    SELECT '040305', 'Bella Union', 'Caraveli', 'Arequipa', 0
    UNION ALL
    SELECT '040306', 'Cahuacho', 'Caraveli', 'Arequipa', 0
    UNION ALL
    SELECT '040307', 'Chala', 'Caraveli', 'Arequipa', 0
    UNION ALL
    SELECT '040308', 'Chaparra', 'Caraveli', 'Arequipa', 0
    UNION ALL
    SELECT '040309', 'Huanuhuanu', 'Caraveli', 'Arequipa', 0
    UNION ALL
    SELECT '040310', 'Jaqui', 'Caraveli', 'Arequipa', 0
    UNION ALL
    SELECT '040311', 'Lomas', 'Caraveli', 'Arequipa', 0
    UNION ALL
    SELECT '040312', 'Quicacha', 'Caraveli', 'Arequipa', 0
    UNION ALL
    SELECT '040313', 'Yauca', 'Caraveli', 'Arequipa', 0
    UNION ALL
    SELECT '040401', 'Aplao', 'Castilla', 'Arequipa', 0
    UNION ALL
    SELECT '040402', 'Andagua', 'Castilla', 'Arequipa', 0
    UNION ALL
    SELECT '040403', 'Ayo', 'Castilla', 'Arequipa', 0
    UNION ALL
    SELECT '040404', 'Chachas', 'Castilla', 'Arequipa', 0
    UNION ALL
    SELECT '040405', 'Chilcaymarca', 'Castilla', 'Arequipa', 0
    UNION ALL
    SELECT '040406', 'Choco', 'Castilla', 'Arequipa', 0
    UNION ALL
    SELECT '040407', 'Huancarqui', 'Castilla', 'Arequipa', 0
    UNION ALL
    SELECT '040408', 'Machaguay', 'Castilla', 'Arequipa', 0
    UNION ALL
    SELECT '040409', 'Orcopampa', 'Castilla', 'Arequipa', 0
    UNION ALL
    SELECT '040410', 'Pampacolca', 'Castilla', 'Arequipa', 0
    UNION ALL
    SELECT '040411', 'Tipan', 'Castilla', 'Arequipa', 0
    UNION ALL
    SELECT '040412', 'Uñon', 'Castilla', 'Arequipa', 0
    UNION ALL
    SELECT '040413', 'Uraca', 'Castilla', 'Arequipa', 0
    UNION ALL
    SELECT '040414', 'Viraco', 'Castilla', 'Arequipa', 0
    UNION ALL
    SELECT '040501', 'Chivay', 'Caylloma', 'Arequipa', 0
    UNION ALL
    SELECT '040502', 'Achoma', 'Caylloma', 'Arequipa', 0
    UNION ALL
    SELECT '040503', 'Cabanaconde', 'Caylloma', 'Arequipa', 0
    UNION ALL
    SELECT '040504', 'Callalli', 'Caylloma', 'Arequipa', 0
    UNION ALL
    SELECT '040505', 'Caylloma', 'Caylloma', 'Arequipa', 0
    UNION ALL
    SELECT '040506', 'Coporaque', 'Caylloma', 'Arequipa', 0
    UNION ALL
    SELECT '040507', 'Huambo', 'Caylloma', 'Arequipa', 0
    UNION ALL
    SELECT '040508', 'Huanca', 'Caylloma', 'Arequipa', 0
    UNION ALL
    SELECT '040509', 'Ichupampa', 'Caylloma', 'Arequipa', 0
    UNION ALL
    SELECT '040510', 'Lari', 'Caylloma', 'Arequipa', 0
    UNION ALL
    SELECT '040511', 'Lluta', 'Caylloma', 'Arequipa', 0
    UNION ALL
    SELECT '040512', 'Maca', 'Caylloma', 'Arequipa', 0
    UNION ALL
    SELECT '040513', 'Madrigal', 'Caylloma', 'Arequipa', 0
    UNION ALL
    SELECT '040514', 'San Antonio de Chuca', 'Caylloma', 'Arequipa', 0
    UNION ALL
    SELECT '040515', 'Sibayo', 'Caylloma', 'Arequipa', 0
    UNION ALL
    SELECT '040516', 'Tapay', 'Caylloma', 'Arequipa', 0
    UNION ALL
    SELECT '040517', 'Tisco', 'Caylloma', 'Arequipa', 0
    UNION ALL
    SELECT '040518', 'Tuti', 'Caylloma', 'Arequipa', 0
    UNION ALL
    SELECT '040519', 'Yanque', 'Caylloma', 'Arequipa', 0
    UNION ALL
    SELECT '040520', 'Majes', 'Caylloma', 'Arequipa', 0
    UNION ALL
    SELECT '040601', 'Chuquibamba', 'Condesuyos', 'Arequipa', 0
    UNION ALL
    SELECT '040602', 'Andaray', 'Condesuyos', 'Arequipa', 0
    UNION ALL
    SELECT '040603', 'Cayarani', 'Condesuyos', 'Arequipa', 0
    UNION ALL
    SELECT '040604', 'Chichas', 'Condesuyos', 'Arequipa', 0
    UNION ALL
    SELECT '040605', 'Iray', 'Condesuyos', 'Arequipa', 0
    UNION ALL
    SELECT '040606', 'Rio Grande', 'Condesuyos', 'Arequipa', 0
    UNION ALL
    SELECT '040607', 'Salamanca', 'Condesuyos', 'Arequipa', 0
    UNION ALL
    SELECT '040608', 'Yanaquihua', 'Condesuyos', 'Arequipa', 0
    UNION ALL
    SELECT '040701', 'Mollendo', 'Islay', 'Arequipa', 0
    UNION ALL
    SELECT '040702', 'Cocachacra', 'Islay', 'Arequipa', 0
    UNION ALL
    SELECT '040703', 'Dean Valdivia', 'Islay', 'Arequipa', 0
    UNION ALL
    SELECT '040704', 'Islay', 'Islay', 'Arequipa', 0
    UNION ALL
    SELECT '040705', 'Mejia', 'Islay', 'Arequipa', 0
    UNION ALL
    SELECT '040706', 'Punta de Bombon', 'Islay', 'Arequipa', 0
    UNION ALL
    SELECT '040801', 'Cotahuasi', 'La Union', 'Arequipa', 0
    UNION ALL
    SELECT '040802', 'Alca', 'La Union', 'Arequipa', 0
    UNION ALL
    SELECT '040803', 'Charcana', 'La Union', 'Arequipa', 0
    UNION ALL
    SELECT '040804', 'Huaynacotas', 'La Union', 'Arequipa', 0
    UNION ALL
    SELECT '040805', 'Pampamarca', 'La Union', 'Arequipa', 0
    UNION ALL
    SELECT '040806', 'Puyca', 'La Union', 'Arequipa', 0
    UNION ALL
    SELECT '040807', 'Quechualla', 'La Union', 'Arequipa', 0
    UNION ALL
    SELECT '040808', 'Sayla', 'La Union', 'Arequipa', 0
    UNION ALL
    SELECT '040809', 'Tauria', 'La Union', 'Arequipa', 0
    UNION ALL
    SELECT '040810', 'Tomepampa', 'La Union', 'Arequipa', 0
    UNION ALL
    SELECT '040811', 'Toro', 'La Union', 'Arequipa', 0
    UNION ALL
    SELECT '050101', 'Ayacucho', 'Huamanga', 'Ayacucho', 0
    UNION ALL
    SELECT '050102', 'Acocro', 'Huamanga', 'Ayacucho', 0
    UNION ALL
    SELECT '050103', 'Acos Vinchos', 'Huamanga', 'Ayacucho', 0
    UNION ALL
    SELECT '050104', 'Carmen Alto', 'Huamanga', 'Ayacucho', 0
    UNION ALL
    SELECT '050105', 'Chiara', 'Huamanga', 'Ayacucho', 0
    UNION ALL
    SELECT '050106', 'Ocros', 'Huamanga', 'Ayacucho', 0
    UNION ALL
    SELECT '050107', 'Pacaycasa', 'Huamanga', 'Ayacucho', 0
    UNION ALL
    SELECT '050108', 'Quinua', 'Huamanga', 'Ayacucho', 0
    UNION ALL
    SELECT '050109', 'San Jose de Ticllas', 'Huamanga', 'Ayacucho', 0
    UNION ALL
    SELECT '050110', 'San Juan Bautista', 'Huamanga', 'Ayacucho', 0
    UNION ALL
    SELECT '050111', 'Santiago de Pischa', 'Huamanga', 'Ayacucho', 0
    UNION ALL
    SELECT '050112', 'Socos', 'Huamanga', 'Ayacucho', 0
    UNION ALL
    SELECT '050113', 'Tambillo', 'Huamanga', 'Ayacucho', 0
    UNION ALL
    SELECT '050114', 'Vinchos', 'Huamanga', 'Ayacucho', 0
    UNION ALL
    SELECT '050115', 'Jesus Nazareno', 'Huamanga', 'Ayacucho', 0
    UNION ALL
    SELECT '050116', 'Andrés Avelino Cáceres Dorregaray', 'Huamanga', 'Ayacucho', 0
    UNION ALL
    SELECT '050201', 'Cangallo', 'Cangallo', 'Ayacucho', 0
    UNION ALL
    SELECT '050202', 'Chuschi', 'Cangallo', 'Ayacucho', 0
    UNION ALL
    SELECT '050203', 'Los Morochucos', 'Cangallo', 'Ayacucho', 0
    UNION ALL
    SELECT '050204', 'Maria Parado de Bellido', 'Cangallo', 'Ayacucho', 0
    UNION ALL
    SELECT '050205', 'Paras', 'Cangallo', 'Ayacucho', 0
    UNION ALL
    SELECT '050206', 'Totos', 'Cangallo', 'Ayacucho', 0
    UNION ALL
    SELECT '050301', 'Sancos', 'Huanca Sancos', 'Ayacucho', 0
    UNION ALL
    SELECT '050302', 'Carapo', 'Huanca Sancos', 'Ayacucho', 0
    UNION ALL
    SELECT '050303', 'Sacsamarca', 'Huanca Sancos', 'Ayacucho', 0
    UNION ALL
    SELECT '050304', 'Santiago de Lucanamarca', 'Huanca Sancos', 'Ayacucho', 0
    UNION ALL
    SELECT '050401', 'Huanta', 'Huanta', 'Ayacucho', 0
    UNION ALL
    SELECT '050402', 'Ayahuanco', 'Huanta', 'Ayacucho', 0
    UNION ALL
    SELECT '050403', 'Huamanguilla', 'Huanta', 'Ayacucho', 0
    UNION ALL
    SELECT '050404', 'Iguain', 'Huanta', 'Ayacucho', 0
    UNION ALL
    SELECT '050405', 'Luricocha', 'Huanta', 'Ayacucho', 0
    UNION ALL
    SELECT '050406', 'Santillana', 'Huanta', 'Ayacucho', 0
    UNION ALL
    SELECT '050407', 'Sivia', 'Huanta', 'Ayacucho', 0
    UNION ALL
    SELECT '050408', 'Llochegua', 'Huanta', 'Ayacucho', 0
    UNION ALL
    SELECT '050409', 'Canayre', 'Huanta', 'Ayacucho', 0
    UNION ALL
    SELECT '050410', 'Uchuraccay', 'Huanta', 'Ayacucho', 0
    UNION ALL
    SELECT '050411', 'Pucacolpa', 'Huanta', 'Ayacucho', 0
    UNION ALL
    SELECT '050412', 'Chaca', 'Huanta', 'Ayacucho', 0
    UNION ALL
    SELECT '050501', 'San Miguel', 'La Mar', 'Ayacucho', 0
    UNION ALL
    SELECT '050502', 'Anco', 'La Mar', 'Ayacucho', 0
    UNION ALL
    SELECT '050503', 'Ayna', 'La Mar', 'Ayacucho', 0
    UNION ALL
    SELECT '050504', 'Chilcas', 'La Mar', 'Ayacucho', 0
    UNION ALL
    SELECT '050505', 'Chungui', 'La Mar', 'Ayacucho', 0
    UNION ALL
    SELECT '050506', 'Luis Carranza', 'La Mar', 'Ayacucho', 0
    UNION ALL
    SELECT '050507', 'Santa Rosa', 'La Mar', 'Ayacucho', 0
    UNION ALL
    SELECT '050508', 'Tambo', 'La Mar', 'Ayacucho', 0
    UNION ALL
    SELECT '050509', 'Samugari', 'La Mar', 'Ayacucho', 0
    UNION ALL
    SELECT '050510', 'Anchihuay', 'La Mar', 'Ayacucho', 0
    UNION ALL
    SELECT '050511', 'Oronccoy', 'La Mar', 'Ayacucho', 0
    UNION ALL
    SELECT '050601', 'Puquio', 'Lucanas', 'Ayacucho', 0
    UNION ALL
    SELECT '050602', 'Aucara', 'Lucanas', 'Ayacucho', 0
    UNION ALL
    SELECT '050603', 'Cabana', 'Lucanas', 'Ayacucho', 0
    UNION ALL
    SELECT '050604', 'Carmen Salcedo', 'Lucanas', 'Ayacucho', 0
    UNION ALL
    SELECT '050605', 'Chaviña', 'Lucanas', 'Ayacucho', 0
    UNION ALL
    SELECT '050606', 'Chipao', 'Lucanas', 'Ayacucho', 0
    UNION ALL
    SELECT '050607', 'Huac-Huas', 'Lucanas', 'Ayacucho', 0
    UNION ALL
    SELECT '050608', 'Laramate', 'Lucanas', 'Ayacucho', 0
    UNION ALL
    SELECT '050609', 'Leoncio Prado', 'Lucanas', 'Ayacucho', 0
    UNION ALL
    SELECT '050610', 'Llauta', 'Lucanas', 'Ayacucho', 0
    UNION ALL
    SELECT '050611', 'Lucanas', 'Lucanas', 'Ayacucho', 0
    UNION ALL
    SELECT '050612', 'Ocaña', 'Lucanas', 'Ayacucho', 0
    UNION ALL
    SELECT '050613', 'Otoca', 'Lucanas', 'Ayacucho', 0
    UNION ALL
    SELECT '050614', 'Saisa', 'Lucanas', 'Ayacucho', 0
    UNION ALL
    SELECT '050615', 'San Cristobal', 'Lucanas', 'Ayacucho', 0
    UNION ALL
    SELECT '050616', 'San Juan', 'Lucanas', 'Ayacucho', 0
    UNION ALL
    SELECT '050617', 'San Pedro', 'Lucanas', 'Ayacucho', 0
    UNION ALL
    SELECT '050618', 'San Pedro de Palco', 'Lucanas', 'Ayacucho', 0
    UNION ALL
    SELECT '050619', 'Sancos', 'Lucanas', 'Ayacucho', 0
    UNION ALL
    SELECT '050620', 'Santa Ana de Huaycahuacho', 'Lucanas', 'Ayacucho', 0
    UNION ALL
    SELECT '050621', 'Santa Lucia', 'Lucanas', 'Ayacucho', 0
    UNION ALL
    SELECT '050701', 'Coracora', 'Parinacochas', 'Ayacucho', 0
    UNION ALL
    SELECT '050702', 'Chumpi', 'Parinacochas', 'Ayacucho', 0
    UNION ALL
    SELECT '050703', 'Coronel Castañeda', 'Parinacochas', 'Ayacucho', 0
    UNION ALL
    SELECT '050704', 'Pacapausa', 'Parinacochas', 'Ayacucho', 0
    UNION ALL
    SELECT '050705', 'Pullo', 'Parinacochas', 'Ayacucho', 0
    UNION ALL
    SELECT '050706', 'Puyusca', 'Parinacochas', 'Ayacucho', 0
    UNION ALL
    SELECT '050707', 'San Francisco de Ravacayco', 'Parinacochas', 'Ayacucho', 0
    UNION ALL
    SELECT '050708', 'Upahuacho', 'Parinacochas', 'Ayacucho', 0
    UNION ALL
    SELECT '050801', 'Pausa', 'Paucar del Sara Sara', 'Ayacucho', 0
    UNION ALL
    SELECT '050802', 'Colta', 'Paucar del Sara Sara', 'Ayacucho', 0
    UNION ALL
    SELECT '050803', 'Corculla', 'Paucar del Sara Sara', 'Ayacucho', 0
    UNION ALL
    SELECT '050804', 'Lampa', 'Paucar del Sara Sara', 'Ayacucho', 0
    UNION ALL
    SELECT '050805', 'Marcabamba', 'Paucar del Sara Sara', 'Ayacucho', 0
    UNION ALL
    SELECT '050806', 'Oyolo', 'Paucar del Sara Sara', 'Ayacucho', 0
    UNION ALL
    SELECT '050807', 'Pararca', 'Paucar del Sara Sara', 'Ayacucho', 0
    UNION ALL
    SELECT '050808', 'San Javier de Alpabamba', 'Paucar del Sara Sara', 'Ayacucho', 0
    UNION ALL
    SELECT '050809', 'San Jose de Ushua', 'Paucar del Sara Sara', 'Ayacucho', 0
    UNION ALL
    SELECT '050810', 'Sara Sara', 'Paucar del Sara Sara', 'Ayacucho', 0
    UNION ALL
    SELECT '050901', 'Querobamba', 'Sucre', 'Ayacucho', 0
    UNION ALL
    SELECT '050902', 'Belen', 'Sucre', 'Ayacucho', 0
    UNION ALL
    SELECT '050903', 'Chalcos', 'Sucre', 'Ayacucho', 0
    UNION ALL
    SELECT '050904', 'Chilcayoc', 'Sucre', 'Ayacucho', 0
    UNION ALL
    SELECT '050905', 'Huacaña', 'Sucre', 'Ayacucho', 0
    UNION ALL
    SELECT '050906', 'Morcolla', 'Sucre', 'Ayacucho', 0
    UNION ALL
    SELECT '050907', 'Paico', 'Sucre', 'Ayacucho', 0
    UNION ALL
    SELECT '050908', 'San Pedro de Larcay', 'Sucre', 'Ayacucho', 0
    UNION ALL
    SELECT '050909', 'San Salvador de Quije', 'Sucre', 'Ayacucho', 0
    UNION ALL
    SELECT '050910', 'Santiago de Paucaray', 'Sucre', 'Ayacucho', 0
    UNION ALL
    SELECT '050911', 'Soras', 'Sucre', 'Ayacucho', 0
    UNION ALL
    SELECT '051001', 'Huancapi', 'Victor Fajardo', 'Ayacucho', 0
    UNION ALL
    SELECT '051002', 'Alcamenca', 'Victor Fajardo', 'Ayacucho', 0
    UNION ALL
    SELECT '051003', 'Apongo', 'Victor Fajardo', 'Ayacucho', 0
    UNION ALL
    SELECT '051004', 'Asquipata', 'Victor Fajardo', 'Ayacucho', 0
    UNION ALL
    SELECT '051005', 'Canaria', 'Victor Fajardo', 'Ayacucho', 0
    UNION ALL
    SELECT '051006', 'Cayara', 'Victor Fajardo', 'Ayacucho', 0
    UNION ALL
    SELECT '051007', 'Colca', 'Victor Fajardo', 'Ayacucho', 0
    UNION ALL
    SELECT '051008', 'Huamanquiquia', 'Victor Fajardo', 'Ayacucho', 0
    UNION ALL
    SELECT '051009', 'Huancaraylla', 'Victor Fajardo', 'Ayacucho', 0
    UNION ALL
    SELECT '051010', 'Huaya', 'Victor Fajardo', 'Ayacucho', 0
    UNION ALL
    SELECT '051011', 'Sarhua', 'Victor Fajardo', 'Ayacucho', 0
    UNION ALL
    SELECT '051012', 'Vilcanchos', 'Victor Fajardo', 'Ayacucho', 0
    UNION ALL
    SELECT '051101', 'Vilcas Huaman', 'Vilcas Huaman', 'Ayacucho', 0
    UNION ALL
    SELECT '051102', 'Accomarca', 'Vilcas Huaman', 'Ayacucho', 0
    UNION ALL
    SELECT '051103', 'Carhuanca', 'Vilcas Huaman', 'Ayacucho', 0
    UNION ALL
    SELECT '051104', 'Concepcion', 'Vilcas Huaman', 'Ayacucho', 0
    UNION ALL
    SELECT '051105', 'Huambalpa', 'Vilcas Huaman', 'Ayacucho', 0
    UNION ALL
    SELECT '051106', 'Independencia', 'Vilcas Huaman', 'Ayacucho', 0
    UNION ALL
    SELECT '051107', 'Saurama', 'Vilcas Huaman', 'Ayacucho', 0
    UNION ALL
    SELECT '051108', 'Vischongo', 'Vilcas Huaman', 'Ayacucho', 0
    UNION ALL
    SELECT '060101', 'Cajamarca', 'Cajamarca', 'Cajamarca', 0
    UNION ALL
    SELECT '060102', 'Asuncion', 'Cajamarca', 'Cajamarca', 0
    UNION ALL
    SELECT '060103', 'Chetilla', 'Cajamarca', 'Cajamarca', 0
    UNION ALL
    SELECT '060104', 'Cospan', 'Cajamarca', 'Cajamarca', 0
    UNION ALL
    SELECT '060105', 'Encañada', 'Cajamarca', 'Cajamarca', 0
    UNION ALL
    SELECT '060106', 'Jesus', 'Cajamarca', 'Cajamarca', 0
    UNION ALL
    SELECT '060107', 'Llacanora', 'Cajamarca', 'Cajamarca', 0
    UNION ALL
    SELECT '060108', 'Los Baños del Inca', 'Cajamarca', 'Cajamarca', 0
    UNION ALL
    SELECT '060109', 'Magdalena', 'Cajamarca', 'Cajamarca', 0
    UNION ALL
    SELECT '060110', 'Matara', 'Cajamarca', 'Cajamarca', 0
    UNION ALL
    SELECT '060111', 'Namora', 'Cajamarca', 'Cajamarca', 0
    UNION ALL
    SELECT '060112', 'San Juan', 'Cajamarca', 'Cajamarca', 0
    UNION ALL
    SELECT '060201', 'Cajabamba', 'Cajabamba', 'Cajamarca', 0
    UNION ALL
    SELECT '060202', 'Cachachi', 'Cajabamba', 'Cajamarca', 0
    UNION ALL
    SELECT '060203', 'Condebamba', 'Cajabamba', 'Cajamarca', 0
    UNION ALL
    SELECT '060204', 'Sitacocha', 'Cajabamba', 'Cajamarca', 0
    UNION ALL
    SELECT '060301', 'Celendin', 'Celendin', 'Cajamarca', 0
    UNION ALL
    SELECT '060302', 'Chumuch', 'Celendin', 'Cajamarca', 0
    UNION ALL
    SELECT '060303', 'Cortegana', 'Celendin', 'Cajamarca', 0
    UNION ALL
    SELECT '060304', 'Huasmin', 'Celendin', 'Cajamarca', 0
    UNION ALL
    SELECT '060305', 'Jorge Chavez', 'Celendin', 'Cajamarca', 0
    UNION ALL
    SELECT '060306', 'Jose Galvez', 'Celendin', 'Cajamarca', 0
    UNION ALL
    SELECT '060307', 'Miguel Iglesias', 'Celendin', 'Cajamarca', 0
    UNION ALL
    SELECT '060308', 'Oxamarca', 'Celendin', 'Cajamarca', 0
    UNION ALL
    SELECT '060309', 'Sorochuco', 'Celendin', 'Cajamarca', 0
    UNION ALL
    SELECT '060310', 'Sucre', 'Celendin', 'Cajamarca', 0
    UNION ALL
    SELECT '060311', 'Utco', 'Celendin', 'Cajamarca', 0
    UNION ALL
    SELECT '060312', 'La Libertad de Pallan', 'Celendin', 'Cajamarca', 0
    UNION ALL
    SELECT '060401', 'Chota', 'Chota', 'Cajamarca', 0
    UNION ALL
    SELECT '060402', 'Anguia', 'Chota', 'Cajamarca', 0
    UNION ALL
    SELECT '060403', 'Chadin', 'Chota', 'Cajamarca', 0
    UNION ALL
    SELECT '060404', 'Chiguirip', 'Chota', 'Cajamarca', 0
    UNION ALL
    SELECT '060405', 'Chimban', 'Chota', 'Cajamarca', 0
    UNION ALL
    SELECT '060406', 'Choropampa', 'Chota', 'Cajamarca', 0
    UNION ALL
    SELECT '060407', 'Cochabamba', 'Chota', 'Cajamarca', 0
    UNION ALL
    SELECT '060408', 'Conchan', 'Chota', 'Cajamarca', 0
    UNION ALL
    SELECT '060409', 'Huambos', 'Chota', 'Cajamarca', 0
    UNION ALL
    SELECT '060410', 'Lajas', 'Chota', 'Cajamarca', 0
    UNION ALL
    SELECT '060411', 'Llama', 'Chota', 'Cajamarca', 0
    UNION ALL
    SELECT '060412', 'Miracosta', 'Chota', 'Cajamarca', 0
    UNION ALL
    SELECT '060413', 'Paccha', 'Chota', 'Cajamarca', 0
    UNION ALL
    SELECT '060414', 'Pion', 'Chota', 'Cajamarca', 0
    UNION ALL
    SELECT '060415', 'Querocoto', 'Chota', 'Cajamarca', 0
    UNION ALL
    SELECT '060416', 'San Juan de Licupis', 'Chota', 'Cajamarca', 0
    UNION ALL
    SELECT '060417', 'Tacabamba', 'Chota', 'Cajamarca', 0
    UNION ALL
    SELECT '060418', 'Tocmoche', 'Chota', 'Cajamarca', 0
    UNION ALL
    SELECT '060419', 'Chalamarca', 'Chota', 'Cajamarca', 0
    UNION ALL
    SELECT '060501', 'Contumaza', 'Contumaza', 'Cajamarca', 0
    UNION ALL
    SELECT '060502', 'Chilete', 'Contumaza', 'Cajamarca', 0
    UNION ALL
    SELECT '060503', 'Cupisnique', 'Contumaza', 'Cajamarca', 0
    UNION ALL
    SELECT '060504', 'Guzmango', 'Contumaza', 'Cajamarca', 0
    UNION ALL
    SELECT '060505', 'San Benito', 'Contumaza', 'Cajamarca', 0
    UNION ALL
    SELECT '060506', 'Santa Cruz de Toled', 'Contumaza', 'Cajamarca', 0
    UNION ALL
    SELECT '060507', 'Tantarica', 'Contumaza', 'Cajamarca', 0
    UNION ALL
    SELECT '060508', 'Yonan', 'Contumaza', 'Cajamarca', 0
    UNION ALL
    SELECT '060601', 'Cutervo', 'Cutervo', 'Cajamarca', 0
    UNION ALL
    SELECT '060602', 'Callayuc', 'Cutervo', 'Cajamarca', 0
    UNION ALL
    SELECT '060603', 'Choros', 'Cutervo', 'Cajamarca', 0
    UNION ALL
    SELECT '060604', 'Cujillo', 'Cutervo', 'Cajamarca', 0
    UNION ALL
    SELECT '060605', 'La Ramada', 'Cutervo', 'Cajamarca', 0
    UNION ALL
    SELECT '060606', 'Pimpingos', 'Cutervo', 'Cajamarca', 0
    UNION ALL
    SELECT '060607', 'Querocotillo', 'Cutervo', 'Cajamarca', 0
    UNION ALL
    SELECT '060608', 'San Andres de Cutervo', 'Cutervo', 'Cajamarca', 0
    UNION ALL
    SELECT '060609', 'San Juan de Cutervo', 'Cutervo', 'Cajamarca', 0
    UNION ALL
    SELECT '060610', 'San Luis de Lucma', 'Cutervo', 'Cajamarca', 0
    UNION ALL
    SELECT '060611', 'Santa Cruz', 'Cutervo', 'Cajamarca', 0
    UNION ALL
    SELECT '060612', 'Santo Domingo de La Capilla', 'Cutervo', 'Cajamarca', 0
    UNION ALL
    SELECT '060613', 'Santo Tomas', 'Cutervo', 'Cajamarca', 0
    UNION ALL
    SELECT '060614', 'Socota', 'Cutervo', 'Cajamarca', 0
    UNION ALL
    SELECT '060615', 'Toribio Casanova', 'Cutervo', 'Cajamarca', 0
    UNION ALL
    SELECT '060701', 'Bambamarca', 'Hualgayoc', 'Cajamarca', 0
    UNION ALL
    SELECT '060702', 'Chugur', 'Hualgayoc', 'Cajamarca', 0
    UNION ALL
    SELECT '060703', 'Hualgayoc', 'Hualgayoc', 'Cajamarca', 0
    UNION ALL
    SELECT '060801', 'Jaen', 'Jaen', 'Cajamarca', 0
    UNION ALL
    SELECT '060802', 'Bellavista', 'Jaen', 'Cajamarca', 0
    UNION ALL
    SELECT '060803', 'Chontali', 'Jaen', 'Cajamarca', 0
    UNION ALL
    SELECT '060804', 'Colasay', 'Jaen', 'Cajamarca', 0
    UNION ALL
    SELECT '060805', 'Huabal', 'Jaen', 'Cajamarca', 0
    UNION ALL
    SELECT '060806', 'Las Pirias', 'Jaen', 'Cajamarca', 0
    UNION ALL
    SELECT '060807', 'Pomahuaca', 'Jaen', 'Cajamarca', 0
    UNION ALL
    SELECT '060808', 'Pucara', 'Jaen', 'Cajamarca', 0
    UNION ALL
    SELECT '060809', 'Sallique', 'Jaen', 'Cajamarca', 0
    UNION ALL
    SELECT '060810', 'San Felipe', 'Jaen', 'Cajamarca', 0
    UNION ALL
    SELECT '060811', 'San Jose del Alto', 'Jaen', 'Cajamarca', 0
    UNION ALL
    SELECT '060812', 'Santa Rosa', 'Jaen', 'Cajamarca', 0
    UNION ALL
    SELECT '060901', 'San Ignacio', 'San Ignacio', 'Cajamarca', 0
    UNION ALL
    SELECT '060902', 'Chirinos', 'San Ignacio', 'Cajamarca', 0
    UNION ALL
    SELECT '060903', 'Huarango', 'San Ignacio', 'Cajamarca', 0
    UNION ALL
    SELECT '060904', 'La Coipa', 'San Ignacio', 'Cajamarca', 0
    UNION ALL
    SELECT '060905', 'Namballe', 'San Ignacio', 'Cajamarca', 0
    UNION ALL
    SELECT '060906', 'San Jose de Lourdes', 'San Ignacio', 'Cajamarca', 0
    UNION ALL
    SELECT '060907', 'Tabaconas', 'San Ignacio', 'Cajamarca', 0
    UNION ALL
    SELECT '061001', 'Pedro Galvez', 'San Marcos', 'Cajamarca', 0
    UNION ALL
    SELECT '061002', 'Chancay', 'San Marcos', 'Cajamarca', 0
    UNION ALL
    SELECT '061003', 'Eduardo Villanueva', 'San Marcos', 'Cajamarca', 0
    UNION ALL
    SELECT '061004', 'Gregorio Pita', 'San Marcos', 'Cajamarca', 0
    UNION ALL
    SELECT '061005', 'Ichocan', 'San Marcos', 'Cajamarca', 0
    UNION ALL
    SELECT '061006', 'Jose Manuel Quiroz', 'San Marcos', 'Cajamarca', 0
    UNION ALL
    SELECT '061007', 'Jose Sabogal', 'San Marcos', 'Cajamarca', 0
    UNION ALL
    SELECT '061101', 'San Miguel', 'San Miguel', 'Cajamarca', 0
    UNION ALL
    SELECT '061102', 'Bolivar', 'San Miguel', 'Cajamarca', 0
    UNION ALL
    SELECT '061103', 'Calquis', 'San Miguel', 'Cajamarca', 0
    UNION ALL
    SELECT '061104', 'Catilluc', 'San Miguel', 'Cajamarca', 0
    UNION ALL
    SELECT '061105', 'El Prado', 'San Miguel', 'Cajamarca', 0
    UNION ALL
    SELECT '061106', 'La Florida', 'San Miguel', 'Cajamarca', 0
    UNION ALL
    SELECT '061107', 'Llapa', 'San Miguel', 'Cajamarca', 0
    UNION ALL
    SELECT '061108', 'Nanchoc', 'San Miguel', 'Cajamarca', 0
    UNION ALL
    SELECT '061109', 'Niepos', 'San Miguel', 'Cajamarca', 0
    UNION ALL
    SELECT '061110', 'San Gregorio', 'San Miguel', 'Cajamarca', 0
    UNION ALL
    SELECT '061111', 'San Silvestre de Cochan', 'San Miguel', 'Cajamarca', 0
    UNION ALL
    SELECT '061112', 'Tongod', 'San Miguel', 'Cajamarca', 0
    UNION ALL
    SELECT '061113', 'Union Agua Blanca', 'San Miguel', 'Cajamarca', 0
    UNION ALL
    SELECT '061201', 'San Pablo', 'San Pablo', 'Cajamarca', 0
    UNION ALL
    SELECT '061202', 'San Bernardino', 'San Pablo', 'Cajamarca', 0
    UNION ALL
    SELECT '061203', 'San Luis', 'San Pablo', 'Cajamarca', 0
    UNION ALL
    SELECT '061204', 'Tumbaden', 'San Pablo', 'Cajamarca', 0
    UNION ALL
    SELECT '061301', 'Santa Cruz', 'Santa Cruz', 'Cajamarca', 0
    UNION ALL
    SELECT '061302', 'Andabamba', 'Santa Cruz', 'Cajamarca', 0
    UNION ALL
    SELECT '061303', 'Catache', 'Santa Cruz', 'Cajamarca', 0
    UNION ALL
    SELECT '061304', 'Chancaybaños', 'Santa Cruz', 'Cajamarca', 0
    UNION ALL
    SELECT '061305', 'La Esperanza', 'Santa Cruz', 'Cajamarca', 0
    UNION ALL
    SELECT '061306', 'Ninabamba', 'Santa Cruz', 'Cajamarca', 0
    UNION ALL
    SELECT '061307', 'Pulan', 'Santa Cruz', 'Cajamarca', 0
    UNION ALL
    SELECT '061308', 'Saucepampa', 'Santa Cruz', 'Cajamarca', 0
    UNION ALL
    SELECT '061309', 'Sexi', 'Santa Cruz', 'Cajamarca', 0
    UNION ALL
    SELECT '061310', 'Uticyacu', 'Santa Cruz', 'Cajamarca', 0
    UNION ALL
    SELECT '061311', 'Yauyucan', 'Santa Cruz', 'Cajamarca', 0
    UNION ALL
    SELECT '070101', 'Callao', 'Callao', 'Callao', 1
    UNION ALL
    SELECT '070102', 'Bellavista', 'Callao', 'Callao', 1
    UNION ALL
    SELECT '070103', 'Carmen de La Legua', 'Callao', 'Callao', 1
    UNION ALL
    SELECT '070104', 'La Perla', 'Callao', 'Callao', 1
    UNION ALL
    SELECT '070105', 'La Punta', 'Callao', 'Callao', 1
    UNION ALL
    SELECT '070106', 'Ventanilla', 'Callao', 'Callao', 1
    UNION ALL
    SELECT '070107', 'Mi Peru', 'Callao', 'Callao', 0
    UNION ALL
    SELECT '080101', 'Cusco', 'Cusco', 'Cusco', 0
    UNION ALL
    SELECT '080102', 'Ccorca', 'Cusco', 'Cusco', 0
    UNION ALL
    SELECT '080103', 'Poroy', 'Cusco', 'Cusco', 0
    UNION ALL
    SELECT '080104', 'San Jeronimo', 'Cusco', 'Cusco', 0
    UNION ALL
    SELECT '080105', 'San Sebastian', 'Cusco', 'Cusco', 0
    UNION ALL
    SELECT '080106', 'Santiago', 'Cusco', 'Cusco', 0
    UNION ALL
    SELECT '080107', 'Saylla', 'Cusco', 'Cusco', 0
    UNION ALL
    SELECT '080108', 'Wanchaq', 'Cusco', 'Cusco', 0
    UNION ALL
    SELECT '080201', 'Acomayo', 'Acomayo', 'Cusco', 0
    UNION ALL
    SELECT '080202', 'Acopia', 'Acomayo', 'Cusco', 0
    UNION ALL
    SELECT '080203', 'Acos', 'Acomayo', 'Cusco', 0
    UNION ALL
    SELECT '080204', 'Mosoc Llacta', 'Acomayo', 'Cusco', 0
    UNION ALL
    SELECT '080205', 'Pomacanchi', 'Acomayo', 'Cusco', 0
    UNION ALL
    SELECT '080206', 'Rondocan', 'Acomayo', 'Cusco', 0
    UNION ALL
    SELECT '080207', 'Sangarara', 'Acomayo', 'Cusco', 0
    UNION ALL
    SELECT '080301', 'Anta', 'Anta', 'Cusco', 0
    UNION ALL
    SELECT '080302', 'Ancahuasi', 'Anta', 'Cusco', 0
    UNION ALL
    SELECT '080303', 'Cachimayo', 'Anta', 'Cusco', 0
    UNION ALL
    SELECT '080304', 'Chinchaypujio', 'Anta', 'Cusco', 0
    UNION ALL
    SELECT '080305', 'Huarocondo', 'Anta', 'Cusco', 0
    UNION ALL
    SELECT '080306', 'Limatambo', 'Anta', 'Cusco', 0
    UNION ALL
    SELECT '080307', 'Mollepata', 'Anta', 'Cusco', 0
    UNION ALL
    SELECT '080308', 'Pucyura', 'Anta', 'Cusco', 0
    UNION ALL
    SELECT '080309', 'Zurite', 'Anta', 'Cusco', 0
    UNION ALL
    SELECT '080401', 'Calca', 'Calca', 'Cusco', 0
    UNION ALL
    SELECT '080402', 'Coya', 'Calca', 'Cusco', 0
    UNION ALL
    SELECT '080403', 'Lamay', 'Calca', 'Cusco', 0
    UNION ALL
    SELECT '080404', 'Lares', 'Calca', 'Cusco', 0
    UNION ALL
    SELECT '080405', 'Pisac', 'Calca', 'Cusco', 0
    UNION ALL
    SELECT '080406', 'San Salvador', 'Calca', 'Cusco', 0
    UNION ALL
    SELECT '080407', 'Taray', 'Calca', 'Cusco', 0
    UNION ALL
    SELECT '080408', 'Yanatile', 'Calca', 'Cusco', 0
    UNION ALL
    SELECT '080501', 'Yanaoca', 'Canas', 'Cusco', 0
    UNION ALL
    SELECT '080502', 'Checca', 'Canas', 'Cusco', 0
    UNION ALL
    SELECT '080503', 'Kunturkanki', 'Canas', 'Cusco', 0
    UNION ALL
    SELECT '080504', 'Langui', 'Canas', 'Cusco', 0
    UNION ALL
    SELECT '080505', 'Layo', 'Canas', 'Cusco', 0
    UNION ALL
    SELECT '080506', 'Pampamarca', 'Canas', 'Cusco', 0
    UNION ALL
    SELECT '080507', 'Quehue', 'Canas', 'Cusco', 0
    UNION ALL
    SELECT '080508', 'Tupac Amaru', 'Canas', 'Cusco', 0
    UNION ALL
    SELECT '080601', 'Sicuani', 'Canchis', 'Cusco', 0
    UNION ALL
    SELECT '080602', 'Checacupe', 'Canchis', 'Cusco', 0
    UNION ALL
    SELECT '080603', 'Combapata', 'Canchis', 'Cusco', 0
    UNION ALL
    SELECT '080604', 'Marangani', 'Canchis', 'Cusco', 0
    UNION ALL
    SELECT '080605', 'Pitumarca', 'Canchis', 'Cusco', 0
    UNION ALL
    SELECT '080606', 'San Pablo', 'Canchis', 'Cusco', 0
    UNION ALL
    SELECT '080607', 'San Pedro', 'Canchis', 'Cusco', 0
    UNION ALL
    SELECT '080608', 'Tinta', 'Canchis', 'Cusco', 0
    UNION ALL
    SELECT '080701', 'Santo Tomas', 'Chumbivilcas', 'Cusco', 0
    UNION ALL
    SELECT '080702', 'Capacmarca', 'Chumbivilcas', 'Cusco', 0
    UNION ALL
    SELECT '080703', 'Chamaca', 'Chumbivilcas', 'Cusco', 0
    UNION ALL
    SELECT '080704', 'Colquemarca', 'Chumbivilcas', 'Cusco', 0
    UNION ALL
    SELECT '080705', 'Livitaca', 'Chumbivilcas', 'Cusco', 0
    UNION ALL
    SELECT '080706', 'Llusco', 'Chumbivilcas', 'Cusco', 0
    UNION ALL
    SELECT '080707', 'Quiñota', 'Chumbivilcas', 'Cusco', 0
    UNION ALL
    SELECT '080708', 'Velille', 'Chumbivilcas', 'Cusco', 0
    UNION ALL
    SELECT '080801', 'Espinar', 'Espinar', 'Cusco', 0
    UNION ALL
    SELECT '080802', 'Condoroma', 'Espinar', 'Cusco', 0
    UNION ALL
    SELECT '080803', 'Coporaque', 'Espinar', 'Cusco', 0
    UNION ALL
    SELECT '080804', 'Ocoruro', 'Espinar', 'Cusco', 0
    UNION ALL
    SELECT '080805', 'Pallpata', 'Espinar', 'Cusco', 0
    UNION ALL
    SELECT '080806', 'Pichigua', 'Espinar', 'Cusco', 0
    UNION ALL
    SELECT '080807', 'Suyckutambo', 'Espinar', 'Cusco', 0
    UNION ALL
    SELECT '080808', 'Alto Pichigua', 'Espinar', 'Cusco', 0
    UNION ALL
    SELECT '080901', 'Santa Ana', 'La Convencion', 'Cusco', 0
    UNION ALL
    SELECT '080902', 'Echarate', 'La Convencion', 'Cusco', 0
    UNION ALL
    SELECT '080903', 'Huayopata', 'La Convencion', 'Cusco', 0
    UNION ALL
    SELECT '080904', 'Maranura', 'La Convencion', 'Cusco', 0
    UNION ALL
    SELECT '080905', 'Ocobamba', 'La Convencion', 'Cusco', 0
    UNION ALL
    SELECT '080906', 'Quellouno', 'La Convencion', 'Cusco', 0
    UNION ALL
    SELECT '080907', 'Kimbiri', 'La Convencion', 'Cusco', 0
    UNION ALL
    SELECT '080908', 'Santa Teresa', 'La Convencion', 'Cusco', 0
    UNION ALL
    SELECT '080909', 'Vilcabamba', 'La Convencion', 'Cusco', 0
    UNION ALL
    SELECT '080910', 'Pichari', 'La Convencion', 'Cusco', 0
    UNION ALL
    SELECT '080911', 'Inkawasi', 'La Convencion', 'Cusco', 0
    UNION ALL
    SELECT '080912', 'Villa Virgen', 'La Convencion', 'Cusco', 0
    UNION ALL
    SELECT '080913', 'Villa Kintiarina', 'La Convencion', 'Cusco', 0
    UNION ALL
    SELECT '080914', 'Megantoni', 'La Convencion', 'Cusco', 0
    UNION ALL
    SELECT '081001', 'Paruro', 'Paruro', 'Cusco', 0
    UNION ALL
    SELECT '081002', 'Accha', 'Paruro', 'Cusco', 0
    UNION ALL
    SELECT '081003', 'Ccapi', 'Paruro', 'Cusco', 0
    UNION ALL
    SELECT '081004', 'Colcha', 'Paruro', 'Cusco', 0
    UNION ALL
    SELECT '081005', 'Huanoquite', 'Paruro', 'Cusco', 0
    UNION ALL
    SELECT '081006', 'Omacha', 'Paruro', 'Cusco', 0
    UNION ALL
    SELECT '081007', 'Paccaritambo', 'Paruro', 'Cusco', 0
    UNION ALL
    SELECT '081008', 'Pillpinto', 'Paruro', 'Cusco', 0
    UNION ALL
    SELECT '081009', 'Yaurisque', 'Paruro', 'Cusco', 0
    UNION ALL
    SELECT '081101', 'Paucartambo', 'Paucartambo', 'Cusco', 0
    UNION ALL
    SELECT '081102', 'Caicay', 'Paucartambo', 'Cusco', 0
    UNION ALL
    SELECT '081103', 'Challabamba', 'Paucartambo', 'Cusco', 0
    UNION ALL
    SELECT '081104', 'Colquepata', 'Paucartambo', 'Cusco', 0
    UNION ALL
    SELECT '081105', 'Huancarani', 'Paucartambo', 'Cusco', 0
    UNION ALL
    SELECT '081106', 'Kosñipata', 'Paucartambo', 'Cusco', 0
    UNION ALL
    SELECT '081201', 'Urcos', 'Quispicanchi', 'Cusco', 0
    UNION ALL
    SELECT '081202', 'Andahuaylillas', 'Quispicanchi', 'Cusco', 0
    UNION ALL
    SELECT '081203', 'Camanti', 'Quispicanchi', 'Cusco', 0
    UNION ALL
    SELECT '081204', 'Ccarhuayo', 'Quispicanchi', 'Cusco', 0
    UNION ALL
    SELECT '081205', 'Ccatca', 'Quispicanchi', 'Cusco', 0
    UNION ALL
    SELECT '081206', 'Cusipata', 'Quispicanchi', 'Cusco', 0
    UNION ALL
    SELECT '081207', 'Huaro', 'Quispicanchi', 'Cusco', 0
    UNION ALL
    SELECT '081208', 'Lucre', 'Quispicanchi', 'Cusco', 0
    UNION ALL
    SELECT '081209', 'Marcapata', 'Quispicanchi', 'Cusco', 0
    UNION ALL
    SELECT '081210', 'Ocongate', 'Quispicanchi', 'Cusco', 0
    UNION ALL
    SELECT '081211', 'Oropesa', 'Quispicanchi', 'Cusco', 0
    UNION ALL
    SELECT '081212', 'Quiquijana', 'Quispicanchi', 'Cusco', 0
    UNION ALL
    SELECT '081301', 'Urubamba', 'Urubamba', 'Cusco', 0
    UNION ALL
    SELECT '081302', 'Chinchero', 'Urubamba', 'Cusco', 0
    UNION ALL
    SELECT '081303', 'Huayllabamba', 'Urubamba', 'Cusco', 0
    UNION ALL
    SELECT '081304', 'Machupicchu', 'Urubamba', 'Cusco', 0
    UNION ALL
    SELECT '081305', 'Maras', 'Urubamba', 'Cusco', 0
    UNION ALL
    SELECT '081306', 'Ollantaytambo', 'Urubamba', 'Cusco', 0
    UNION ALL
    SELECT '081307', 'Yucay', 'Urubamba', 'Cusco', 0
    UNION ALL
    SELECT '090101', 'Huancavelica', 'Huancavelica', 'Huancavelica', 0
    UNION ALL
    SELECT '090102', 'Acobambilla', 'Huancavelica', 'Huancavelica', 0
    UNION ALL
    SELECT '090103', 'Acoria', 'Huancavelica', 'Huancavelica', 0
    UNION ALL
    SELECT '090104', 'Conayca', 'Huancavelica', 'Huancavelica', 0
    UNION ALL
    SELECT '090105', 'Cuenca', 'Huancavelica', 'Huancavelica', 0
    UNION ALL
    SELECT '090106', 'Huachocolpa', 'Huancavelica', 'Huancavelica', 0
    UNION ALL
    SELECT '090107', 'Huayllahuara', 'Huancavelica', 'Huancavelica', 0
    UNION ALL
    SELECT '090108', 'Izcuchaca', 'Huancavelica', 'Huancavelica', 0
    UNION ALL
    SELECT '090109', 'Laria', 'Huancavelica', 'Huancavelica', 0
    UNION ALL
    SELECT '090110', 'Manta', 'Huancavelica', 'Huancavelica', 0
    UNION ALL
    SELECT '090111', 'Mariscal Caceres', 'Huancavelica', 'Huancavelica', 0
    UNION ALL
    SELECT '090112', 'Moya', 'Huancavelica', 'Huancavelica', 0
    UNION ALL
    SELECT '090113', 'Nuevo Occoro', 'Huancavelica', 'Huancavelica', 0
    UNION ALL
    SELECT '090114', 'Palca', 'Huancavelica', 'Huancavelica', 0
    UNION ALL
    SELECT '090115', 'Pilchaca', 'Huancavelica', 'Huancavelica', 0
    UNION ALL
    SELECT '090116', 'Vilca', 'Huancavelica', 'Huancavelica', 0
    UNION ALL
    SELECT '090117', 'Yauli', 'Huancavelica', 'Huancavelica', 0
    UNION ALL
    SELECT '090118', 'Ascension', 'Huancavelica', 'Huancavelica', 0
    UNION ALL
    SELECT '090119', 'Huando', 'Huancavelica', 'Huancavelica', 0
    UNION ALL
    SELECT '090201', 'Acobamba', 'Acobamba', 'Huancavelica', 0
    UNION ALL
    SELECT '090202', 'Andabamba', 'Acobamba', 'Huancavelica', 0
    UNION ALL
    SELECT '090203', 'Anta', 'Acobamba', 'Huancavelica', 0
    UNION ALL
    SELECT '090204', 'Caja', 'Acobamba', 'Huancavelica', 0
    UNION ALL
    SELECT '090205', 'Marcas', 'Acobamba', 'Huancavelica', 0
    UNION ALL
    SELECT '090206', 'Paucara', 'Acobamba', 'Huancavelica', 0
    UNION ALL
    SELECT '090207', 'Pomacocha', 'Acobamba', 'Huancavelica', 0
    UNION ALL
    SELECT '090208', 'Rosario', 'Acobamba', 'Huancavelica', 0
    UNION ALL
    SELECT '090301', 'Lircay', 'Angaraes', 'Huancavelica', 0
    UNION ALL
    SELECT '090302', 'Anchonga', 'Angaraes', 'Huancavelica', 0
    UNION ALL
    SELECT '090303', 'Callanmarca', 'Angaraes', 'Huancavelica', 0
    UNION ALL
    SELECT '090304', 'Ccochaccasa', 'Angaraes', 'Huancavelica', 0
    UNION ALL
    SELECT '090305', 'Chincho', 'Angaraes', 'Huancavelica', 0
    UNION ALL
    SELECT '090306', 'Congalla', 'Angaraes', 'Huancavelica', 0
    UNION ALL
    SELECT '090307', 'Huanca-Huanca', 'Angaraes', 'Huancavelica', 0
    UNION ALL
    SELECT '090308', 'Huayllay Grande', 'Angaraes', 'Huancavelica', 0
    UNION ALL
    SELECT '090309', 'Julcamarca', 'Angaraes', 'Huancavelica', 0
    UNION ALL
    SELECT '090310', 'San Antonio de Antaparco', 'Angaraes', 'Huancavelica', 0
    UNION ALL
    SELECT '090311', 'Santo Tomas de Pata', 'Angaraes', 'Huancavelica', 0
    UNION ALL
    SELECT '090312', 'Secclla', 'Angaraes', 'Huancavelica', 0
    UNION ALL
    SELECT '090401', 'Castrovirreyna', 'Castrovirreyna', 'Huancavelica', 0
    UNION ALL
    SELECT '090402', 'Arma', 'Castrovirreyna', 'Huancavelica', 0
    UNION ALL
    SELECT '090403', 'Aurahua', 'Castrovirreyna', 'Huancavelica', 0
    UNION ALL
    SELECT '090404', 'Capillas', 'Castrovirreyna', 'Huancavelica', 0
    UNION ALL
    SELECT '090405', 'Chupamarca', 'Castrovirreyna', 'Huancavelica', 0
    UNION ALL
    SELECT '090406', 'Cocas', 'Castrovirreyna', 'Huancavelica', 0
    UNION ALL
    SELECT '090407', 'Huachos', 'Castrovirreyna', 'Huancavelica', 0
    UNION ALL
    SELECT '090408', 'Huamatambo', 'Castrovirreyna', 'Huancavelica', 0
    UNION ALL
    SELECT '090409', 'Mollepampa', 'Castrovirreyna', 'Huancavelica', 0
    UNION ALL
    SELECT '090410', 'San Juan', 'Castrovirreyna', 'Huancavelica', 0
    UNION ALL
    SELECT '090411', 'Santa Ana', 'Castrovirreyna', 'Huancavelica', 0
    UNION ALL
    SELECT '090412', 'Tantara', 'Castrovirreyna', 'Huancavelica', 0
    UNION ALL
    SELECT '090413', 'Ticrapo', 'Castrovirreyna', 'Huancavelica', 0
    UNION ALL
    SELECT '090501', 'Churcampa', 'Churcampa', 'Huancavelica', 0
    UNION ALL
    SELECT '090502', 'Anco', 'Churcampa', 'Huancavelica', 0
    UNION ALL
    SELECT '090503', 'Chinchihuasi', 'Churcampa', 'Huancavelica', 0
    UNION ALL
    SELECT '090504', 'El Carmen', 'Churcampa', 'Huancavelica', 0
    UNION ALL
    SELECT '090505', 'La Merced', 'Churcampa', 'Huancavelica', 0
    UNION ALL
    SELECT '090506', 'Locroja', 'Churcampa', 'Huancavelica', 0
    UNION ALL
    SELECT '090507', 'Paucarbamba', 'Churcampa', 'Huancavelica', 0
    UNION ALL
    SELECT '090508', 'San Miguel de Mayocc', 'Churcampa', 'Huancavelica', 0
    UNION ALL
    SELECT '090509', 'San Pedro de Coris', 'Churcampa', 'Huancavelica', 0
    UNION ALL
    SELECT '090510', 'Pachamarca', 'Churcampa', 'Huancavelica', 0
    UNION ALL
    SELECT '090511', 'Cosme', 'Churcampa', 'Huancavelica', 0
    UNION ALL
    SELECT '090601', 'Huaytara', 'Huaytara', 'Huancavelica', 0
    UNION ALL
    SELECT '090602', 'Ayavi', 'Huaytara', 'Huancavelica', 0
    UNION ALL
    SELECT '090603', 'Cordova', 'Huaytara', 'Huancavelica', 0
    UNION ALL
    SELECT '090604', 'Huayacundo Arma', 'Huaytara', 'Huancavelica', 0
    UNION ALL
    SELECT '090605', 'Laramarca', 'Huaytara', 'Huancavelica', 0
    UNION ALL
    SELECT '090606', 'Ocoyo', 'Huaytara', 'Huancavelica', 0
    UNION ALL
    SELECT '090607', 'Pilpichaca', 'Huaytara', 'Huancavelica', 0
    UNION ALL
    SELECT '090608', 'Querco', 'Huaytara', 'Huancavelica', 0
    UNION ALL
    SELECT '090609', 'Quito-Arma', 'Huaytara', 'Huancavelica', 0
    UNION ALL
    SELECT '090610', 'San Antonio de Cusicancha', 'Huaytara', 'Huancavelica', 0
    UNION ALL
    SELECT '090611', 'San Francisco de Sangayaico', 'Huaytara', 'Huancavelica', 0
    UNION ALL
    SELECT '090612', 'San Isidro', 'Huaytara', 'Huancavelica', 0
    UNION ALL
    SELECT '090613', 'Santiago de Chocorvos', 'Huaytara', 'Huancavelica', 0
    UNION ALL
    SELECT '090614', 'Santiago de Quirahuara', 'Huaytara', 'Huancavelica', 0
    UNION ALL
    SELECT '090615', 'Santo Domingo de Capillas', 'Huaytara', 'Huancavelica', 0
    UNION ALL
    SELECT '090616', 'Tambo', 'Huaytara', 'Huancavelica', 0
    UNION ALL
    SELECT '090701', 'Pampas', 'Tayacaja', 'Huancavelica', 0
    UNION ALL
    SELECT '090702', 'Acostambo', 'Tayacaja', 'Huancavelica', 0
    UNION ALL
    SELECT '090703', 'Acraquia', 'Tayacaja', 'Huancavelica', 0
    UNION ALL
    SELECT '090704', 'Ahuaycha', 'Tayacaja', 'Huancavelica', 0
    UNION ALL
    SELECT '090705', 'Colcabamba', 'Tayacaja', 'Huancavelica', 0
    UNION ALL
    SELECT '090706', 'Daniel Hernandez', 'Tayacaja', 'Huancavelica', 0
    UNION ALL
    SELECT '090707', 'Huachocolpa', 'Tayacaja', 'Huancavelica', 0
    UNION ALL
    SELECT '090709', 'Huaribamba', 'Tayacaja', 'Huancavelica', 0
    UNION ALL
    SELECT '090710', 'Ñahuimpuquio', 'Tayacaja', 'Huancavelica', 0
    UNION ALL
    SELECT '090711', 'Pazos', 'Tayacaja', 'Huancavelica', 0
    UNION ALL
    SELECT '090713', 'Quishuar', 'Tayacaja', 'Huancavelica', 0
    UNION ALL
    SELECT '090714', 'Salcabamba', 'Tayacaja', 'Huancavelica', 0
    UNION ALL
    SELECT '090715', 'Salcahuasi', 'Tayacaja', 'Huancavelica', 0
    UNION ALL
    SELECT '090716', 'San Marcos de Rocchac', 'Tayacaja', 'Huancavelica', 0
    UNION ALL
    SELECT '090717', 'Surcubamba', 'Tayacaja', 'Huancavelica', 0
    UNION ALL
    SELECT '090718', 'Tintay Puncu', 'Tayacaja', 'Huancavelica', 0
    UNION ALL
    SELECT '090719', 'Quichuas', 'Tayacaja', 'Huancavelica', 0
    UNION ALL
    SELECT '090720', 'Andaymarca', 'Tayacaja', 'Huancavelica', 0
    UNION ALL
    SELECT '090721', 'Roble', 'Tayacaja', 'Huancavelica', 0
    UNION ALL
    SELECT '090722', 'Pichos', 'Tayacaja', 'Huancavelica', 0
    UNION ALL
    SELECT '090723', 'Santiago de Túcuma', 'Tayacaja', 'Huancavelica', 0
    UNION ALL
    SELECT '100101', 'Huanuco', 'Huanuco', 'Huanuco', 0
    UNION ALL
    SELECT '100102', 'Amarilis', 'Huanuco', 'Huanuco', 0
    UNION ALL
    SELECT '100103', 'Chinchao', 'Huanuco', 'Huanuco', 0
    UNION ALL
    SELECT '100104', 'Churubamba', 'Huanuco', 'Huanuco', 0
    UNION ALL
    SELECT '100105', 'Margos', 'Huanuco', 'Huanuco', 0
    UNION ALL
    SELECT '100106', 'Quisqui', 'Huanuco', 'Huanuco', 0
    UNION ALL
    SELECT '100107', 'San Francisco de Cayran', 'Huanuco', 'Huanuco', 0
    UNION ALL
    SELECT '100108', 'San Pedro de Chaulan', 'Huanuco', 'Huanuco', 0
    UNION ALL
    SELECT '100109', 'Santa Maria del Valle', 'Huanuco', 'Huanuco', 0
    UNION ALL
    SELECT '100110', 'Yarumayo', 'Huanuco', 'Huanuco', 0
    UNION ALL
    SELECT '100111', 'Pillco Marca', 'Huanuco', 'Huanuco', 0
    UNION ALL
    SELECT '100112', 'Yacus', 'Huanuco', 'Huanuco', 0
    UNION ALL
    SELECT '100113', 'San Pablo de Pillao', 'Huanuco', 'Huanuco', 0
    UNION ALL
    SELECT '100201', 'Ambo', 'Ambo', 'Huanuco', 0
    UNION ALL
    SELECT '100202', 'Cayna', 'Ambo', 'Huanuco', 0
    UNION ALL
    SELECT '100203', 'Colpas', 'Ambo', 'Huanuco', 0
    UNION ALL
    SELECT '100204', 'Conchamarca', 'Ambo', 'Huanuco', 0
    UNION ALL
    SELECT '100205', 'Huacar', 'Ambo', 'Huanuco', 0
    UNION ALL
    SELECT '100206', 'San Francisco', 'Ambo', 'Huanuco', 0
    UNION ALL
    SELECT '100207', 'San Rafael', 'Ambo', 'Huanuco', 0
    UNION ALL
    SELECT '100208', 'Tomay Kichwa', 'Ambo', 'Huanuco', 0
    UNION ALL
    SELECT '100301', 'La Union', 'Dos de Mayo', 'Huanuco', 0
    UNION ALL
    SELECT '100307', 'Chuquis', 'Dos de Mayo', 'Huanuco', 0
    UNION ALL
    SELECT '100311', 'Marias', 'Dos de Mayo', 'Huanuco', 0
    UNION ALL
    SELECT '100313', 'Pachas', 'Dos de Mayo', 'Huanuco', 0
    UNION ALL
    SELECT '100316', 'Quivilla', 'Dos de Mayo', 'Huanuco', 0
    UNION ALL
    SELECT '100317', 'Ripan', 'Dos de Mayo', 'Huanuco', 0
    UNION ALL
    SELECT '100321', 'Shunqui', 'Dos de Mayo', 'Huanuco', 0
    UNION ALL
    SELECT '100322', 'Sillapata', 'Dos de Mayo', 'Huanuco', 0
    UNION ALL
    SELECT '100323', 'Yanas', 'Dos de Mayo', 'Huanuco', 0
    UNION ALL
    SELECT '100401', 'Huacaybamba', 'Huacaybamba', 'Huanuco', 0
    UNION ALL
    SELECT '100402', 'Canchabamba', 'Huacaybamba', 'Huanuco', 0
    UNION ALL
    SELECT '100403', 'Cochabamba', 'Huacaybamba', 'Huanuco', 0
    UNION ALL
    SELECT '100404', 'Pinra', 'Huacaybamba', 'Huanuco', 0
    UNION ALL
    SELECT '100501', 'Llata', 'Huamalies', 'Huanuco', 0
    UNION ALL
    SELECT '100502', 'Arancay', 'Huamalies', 'Huanuco', 0
    UNION ALL
    SELECT '100503', 'Chavin de Pariarca', 'Huamalies', 'Huanuco', 0
    UNION ALL
    SELECT '100504', 'Jacas Grande', 'Huamalies', 'Huanuco', 0
    UNION ALL
    SELECT '100505', 'Jircan', 'Huamalies', 'Huanuco', 0
    UNION ALL
    SELECT '100506', 'Miraflores', 'Huamalies', 'Huanuco', 0
    UNION ALL
    SELECT '100507', 'Monzon', 'Huamalies', 'Huanuco', 0
    UNION ALL
    SELECT '100508', 'Punchao', 'Huamalies', 'Huanuco', 0
    UNION ALL
    SELECT '100509', 'Puños', 'Huamalies', 'Huanuco', 0
    UNION ALL
    SELECT '100510', 'Singa', 'Huamalies', 'Huanuco', 0
    UNION ALL
    SELECT '100511', 'Tantamayo', 'Huamalies', 'Huanuco', 0
    UNION ALL
    SELECT '100601', 'Rupa-Rupa', 'Leoncio Prado', 'Huanuco', 0
    UNION ALL
    SELECT '100602', 'Daniel Alomias Robles', 'Leoncio Prado', 'Huanuco', 0
    UNION ALL
    SELECT '100603', 'Hermilio Valdizan', 'Leoncio Prado', 'Huanuco', 0
    UNION ALL
    SELECT '100604', 'Jose Crespo y Castillo', 'Leoncio Prado', 'Huanuco', 0
    UNION ALL
    SELECT '100605', 'Luyando', 'Leoncio Prado', 'Huanuco', 0
    UNION ALL
    SELECT '100606', 'Mariano Damaso Beraun', 'Leoncio Prado', 'Huanuco', 0
    UNION ALL
    SELECT '100607', 'Pucayacu', 'Leoncio Prado', 'Huanuco', 0
    UNION ALL
    SELECT '100608', 'Castillo Grande', 'Leoncio Prado', 'Huanuco', 0
    UNION ALL
    SELECT '100609', 'Pueblo Nuevo', 'Leoncio Prado', 'Huanuco', 0
    UNION ALL
    SELECT '100610', 'Santo Domingo de Anda', 'Leoncio Prado', 'Huanuco', 0
    UNION ALL
    SELECT '100701', 'Huacrachuco', 'Marañon', 'Huanuco', 0
    UNION ALL
    SELECT '100702', 'Cholon', 'Marañon', 'Huanuco', 0
    UNION ALL
    SELECT '100703', 'San Buenaventura', 'Marañon', 'Huanuco', 0
    UNION ALL
    SELECT '100704', 'La Morada', 'Marañon', 'Huanuco', 0
    UNION ALL
    SELECT '100705', 'Santa Rosa de Alto Yanajanca', 'Marañon', 'Huanuco', 0
    UNION ALL
    SELECT '100801', 'Panao', 'Pachitea', 'Huanuco', 0
    UNION ALL
    SELECT '100802', 'Chaglla', 'Pachitea', 'Huanuco', 0
    UNION ALL
    SELECT '100803', 'Molino', 'Pachitea', 'Huanuco', 0
    UNION ALL
    SELECT '100804', 'Umari', 'Pachitea', 'Huanuco', 0
    UNION ALL
    SELECT '100901', 'Puerto Inca', 'Puerto Inca', 'Huanuco', 0
    UNION ALL
    SELECT '100902', 'Codo del Pozuzo', 'Puerto Inca', 'Huanuco', 0
    UNION ALL
    SELECT '100903', 'Honoria', 'Puerto Inca', 'Huanuco', 0
    UNION ALL
    SELECT '100904', 'Tournavista', 'Puerto Inca', 'Huanuco', 0
    UNION ALL
    SELECT '100905', 'Yuyapichis', 'Puerto Inca', 'Huanuco', 0
    UNION ALL
    SELECT '101001', 'Jesus', 'Lauricocha', 'Huanuco', 0
    UNION ALL
    SELECT '101002', 'Baños', 'Lauricocha', 'Huanuco', 0
    UNION ALL
    SELECT '101003', 'Jivia', 'Lauricocha', 'Huanuco', 0
    UNION ALL
    SELECT '101004', 'Queropalca', 'Lauricocha', 'Huanuco', 0
    UNION ALL
    SELECT '101005', 'Rondos', 'Lauricocha', 'Huanuco', 0
    UNION ALL
    SELECT '101006', 'San Francisco de Asis', 'Lauricocha', 'Huanuco', 0
    UNION ALL
    SELECT '101007', 'San Miguel de Cauri', 'Lauricocha', 'Huanuco', 0
    UNION ALL
    SELECT '101101', 'Chavinillo', 'Yarowilca', 'Huanuco', 0
    UNION ALL
    SELECT '101102', 'Cahuac', 'Yarowilca', 'Huanuco', 0
    UNION ALL
    SELECT '101103', 'Chacabamba', 'Yarowilca', 'Huanuco', 0
    UNION ALL
    SELECT '101104', 'Aparicio Pomares', 'Yarowilca', 'Huanuco', 0
    UNION ALL
    SELECT '101105', 'Jacas Chico', 'Yarowilca', 'Huanuco', 0
    UNION ALL
    SELECT '101106', 'Obas', 'Yarowilca', 'Huanuco', 0
    UNION ALL
    SELECT '101107', 'Pampamarca', 'Yarowilca', 'Huanuco', 0
    UNION ALL
    SELECT '101108', 'Choras', 'Yarowilca', 'Huanuco', 0
    UNION ALL
    SELECT '110101', 'Ica', 'Ica', 'Ica', 0
    UNION ALL
    SELECT '110102', 'La Tinguiña', 'Ica', 'Ica', 0
    UNION ALL
    SELECT '110103', 'Los Aquijes', 'Ica', 'Ica', 0
    UNION ALL
    SELECT '110104', 'Ocucaje', 'Ica', 'Ica', 0
    UNION ALL
    SELECT '110105', 'Pachacutec', 'Ica', 'Ica', 0
    UNION ALL
    SELECT '110106', 'Parcona', 'Ica', 'Ica', 0
    UNION ALL
    SELECT '110107', 'Pueblo Nuevo', 'Ica', 'Ica', 0
    UNION ALL
    SELECT '110108', 'Salas', 'Ica', 'Ica', 0
    UNION ALL
    SELECT '110109', 'San Jose de los Molinos', 'Ica', 'Ica', 0
    UNION ALL
    SELECT '110110', 'San Juan Bautista', 'Ica', 'Ica', 0
    UNION ALL
    SELECT '110111', 'Santiago', 'Ica', 'Ica', 0
    UNION ALL
    SELECT '110112', 'Subtanjalla', 'Ica', 'Ica', 0
    UNION ALL
    SELECT '110113', 'Tate', 'Ica', 'Ica', 0
    UNION ALL
    SELECT '110114', 'Yauca del Rosario', 'Ica', 'Ica', 0
    UNION ALL
    SELECT '110201', 'Chincha Alta', 'Chincha', 'Ica', 0
    UNION ALL
    SELECT '110202', 'Alto Laran', 'Chincha', 'Ica', 0
    UNION ALL
    SELECT '110203', 'Chavin', 'Chincha', 'Ica', 0
    UNION ALL
    SELECT '110204', 'Chincha Baja', 'Chincha', 'Ica', 0
    UNION ALL
    SELECT '110205', 'El Carmen', 'Chincha', 'Ica', 0
    UNION ALL
    SELECT '110206', 'Grocio Prado', 'Chincha', 'Ica', 0
    UNION ALL
    SELECT '110207', 'Pueblo Nuevo', 'Chincha', 'Ica', 0
    UNION ALL
    SELECT '110208', 'San Juan de Yanac', 'Chincha', 'Ica', 0
    UNION ALL
    SELECT '110209', 'San Pedro de Huacarpana', 'Chincha', 'Ica', 0
    UNION ALL
    SELECT '110210', 'Sunampe', 'Chincha', 'Ica', 0
    UNION ALL
    SELECT '110211', 'Tambo de Mora', 'Chincha', 'Ica', 0
    UNION ALL
    SELECT '110301', 'Nazca', 'Nazca', 'Ica', 0
    UNION ALL
    SELECT '110302', 'Changuillo', 'Nazca', 'Ica', 0
    UNION ALL
    SELECT '110303', 'El Ingenio', 'Nazca', 'Ica', 0
    UNION ALL
    SELECT '110304', 'Marcona', 'Nazca', 'Ica', 0
    UNION ALL
    SELECT '110305', 'Vista Alegre', 'Nazca', 'Ica', 0
    UNION ALL
    SELECT '110401', 'Palpa', 'Palpa', 'Ica', 0
    UNION ALL
    SELECT '110402', 'Llipata', 'Palpa', 'Ica', 0
    UNION ALL
    SELECT '110403', 'Rio Grande', 'Palpa', 'Ica', 0
    UNION ALL
    SELECT '110404', 'Santa Cruz', 'Palpa', 'Ica', 0
    UNION ALL
    SELECT '110405', 'Tibillo', 'Palpa', 'Ica', 0
    UNION ALL
    SELECT '110501', 'Pisco', 'Pisco', 'Ica', 0
    UNION ALL
    SELECT '110502', 'Huancano', 'Pisco', 'Ica', 0
    UNION ALL
    SELECT '110503', 'Humay', 'Pisco', 'Ica', 0
    UNION ALL
    SELECT '110504', 'Independencia', 'Pisco', 'Ica', 0
    UNION ALL
    SELECT '110505', 'Paracas', 'Pisco', 'Ica', 0
    UNION ALL
    SELECT '110506', 'San Andres', 'Pisco', 'Ica', 0
    UNION ALL
    SELECT '110507', 'San Clemente', 'Pisco', 'Ica', 0
    UNION ALL
    SELECT '110508', 'Tupac Amaru Inca', 'Pisco', 'Ica', 0
    UNION ALL
    SELECT '120101', 'Huancayo', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120104', 'Carhuacallanga', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120105', 'Chacapampa', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120106', 'Chicche', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120107', 'Chilca', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120108', 'Chongos Alto', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120111', 'Chupuro', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120112', 'Colca', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120113', 'Cullhuas', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120114', 'El Tambo', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120116', 'Huacrapuquio', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120117', 'Hualhuas', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120119', 'Huancan', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120120', 'Huasicancha', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120121', 'Huayucachi', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120122', 'Ingenio', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120124', 'Pariahuanca', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120125', 'Pilcomayo', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120126', 'Pucara', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120127', 'Quichuay', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120128', 'Quilcas', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120129', 'San Agustin', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120130', 'San Jeronimo de Tunan', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120132', 'Saño', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120133', 'Sapallanga', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120134', 'Sicaya', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120135', 'Santo Domingo de Acobamba', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120136', 'Viques', 'Huancayo', 'Junin', 0
    UNION ALL
    SELECT '120201', 'Concepcion', 'Concepcion', 'Junin', 0
    UNION ALL
    SELECT '120202', 'Aco', 'Concepcion', 'Junin', 0
    UNION ALL
    SELECT '120203', 'Andamarca', 'Concepcion', 'Junin', 0
    UNION ALL
    SELECT '120204', 'Chambara', 'Concepcion', 'Junin', 0
    UNION ALL
    SELECT '120205', 'Cochas', 'Concepcion', 'Junin', 0
    UNION ALL
    SELECT '120206', 'Comas', 'Concepcion', 'Junin', 0
    UNION ALL
    SELECT '120207', 'Heroinas Toledo', 'Concepcion', 'Junin', 0
    UNION ALL
    SELECT '120208', 'Manzanares', 'Concepcion', 'Junin', 0
    UNION ALL
    SELECT '120209', 'Mariscal Castilla', 'Concepcion', 'Junin', 0
    UNION ALL
    SELECT '120210', 'Matahuasi', 'Concepcion', 'Junin', 0
    UNION ALL
    SELECT '120211', 'Mito', 'Concepcion', 'Junin', 0
    UNION ALL
    SELECT '120212', 'Nueve de Julio', 'Concepcion', 'Junin', 0
    UNION ALL
    SELECT '120213', 'Orcotuna', 'Concepcion', 'Junin', 0
    UNION ALL
    SELECT '120214', 'San Jose de Quero', 'Concepcion', 'Junin', 0
    UNION ALL
    SELECT '120215', 'Santa Rosa de Ocopa', 'Concepcion', 'Junin', 0
    UNION ALL
    SELECT '120301', 'Chanchamayo', 'Chanchamayo', 'Junin', 0
    UNION ALL
    SELECT '120302', 'Perene', 'Chanchamayo', 'Junin', 0
    UNION ALL
    SELECT '120303', 'Pichanaqui', 'Chanchamayo', 'Junin', 0
    UNION ALL
    SELECT '120304', 'San Luis de Shuaro', 'Chanchamayo', 'Junin', 0
    UNION ALL
    SELECT '120305', 'San Ramon', 'Chanchamayo', 'Junin', 0
    UNION ALL
    SELECT '120306', 'Vitoc', 'Chanchamayo', 'Junin', 0
    UNION ALL
    SELECT '120401', 'Jauja', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120402', 'Acolla', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120403', 'Apata', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120404', 'Ataura', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120405', 'Canchayllo', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120406', 'Curicaca', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120407', 'El Mantaro', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120408', 'Huamali', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120409', 'Huaripampa', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120410', 'Huertas', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120411', 'Janjaillo', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120412', 'Julcan', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120413', 'Leonor Ordoñez', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120414', 'Llocllapampa', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120415', 'Marco', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120416', 'Masma', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120417', 'Masma Chicche', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120418', 'Molinos', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120419', 'Monobamba', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120420', 'Muqui', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120421', 'Muquiyauyo', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120422', 'Paca', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120423', 'Paccha', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120424', 'Pancan', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120425', 'Parco', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120426', 'Pomacancha', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120427', 'Ricran', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120428', 'San Lorenzo', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120429', 'San Pedro de Chunan', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120430', 'Sausa', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120431', 'Sincos', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120432', 'Tunan Marca', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120433', 'Yauli', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120434', 'Yauyos', 'Jauja', 'Junin', 0
    UNION ALL
    SELECT '120501', 'Junin', 'Junin', 'Junin', 0
    UNION ALL
    SELECT '120502', 'Carhuamayo', 'Junin', 'Junin', 0
    UNION ALL
    SELECT '120503', 'Ondores', 'Junin', 'Junin', 0
    UNION ALL
    SELECT '120504', 'Ulcumayo', 'Junin', 'Junin', 0
    UNION ALL
    SELECT '120601', 'Satipo', 'Satipo', 'Junin', 0
    UNION ALL
    SELECT '120602', 'Coviriali', 'Satipo', 'Junin', 0
    UNION ALL
    SELECT '120603', 'Llaylla', 'Satipo', 'Junin', 0
    UNION ALL
    SELECT '120604', 'Mazamari', 'Satipo', 'Junin', 0
    UNION ALL
    SELECT '120605', 'Pampa Hermosa', 'Satipo', 'Junin', 0
    UNION ALL
    SELECT '120606', 'Pangoa', 'Satipo', 'Junin', 0
    UNION ALL
    SELECT '120607', 'Rio Negro', 'Satipo', 'Junin', 0
    UNION ALL
    SELECT '120608', 'Rio Tambo', 'Satipo', 'Junin', 0
    UNION ALL
    SELECT '120609', 'Vizcatán del Ene', 'Satipo', 'Junin', 0
    UNION ALL
    SELECT '120701', 'Tarma', 'Tarma', 'Junin', 0
    UNION ALL
    SELECT '120702', 'Acobamba', 'Tarma', 'Junin', 0
    UNION ALL
    SELECT '120703', 'Huaricolca', 'Tarma', 'Junin', 0
    UNION ALL
    SELECT '120704', 'Huasahuasi', 'Tarma', 'Junin', 0
    UNION ALL
    SELECT '120705', 'La Union', 'Tarma', 'Junin', 0
    UNION ALL
    SELECT '120706', 'Palca', 'Tarma', 'Junin', 0
    UNION ALL
    SELECT '120707', 'Palcamayo', 'Tarma', 'Junin', 0
    UNION ALL
    SELECT '120708', 'San Pedro de Cajas', 'Tarma', 'Junin', 0
    UNION ALL
    SELECT '120709', 'Tapo', 'Tarma', 'Junin', 0
    UNION ALL
    SELECT '120801', 'La Oroya', 'Yauli', 'Junin', 0
    UNION ALL
    SELECT '120802', 'Chacapalpa', 'Yauli', 'Junin', 0
    UNION ALL
    SELECT '120803', 'Huay-Huay', 'Yauli', 'Junin', 0
    UNION ALL
    SELECT '120804', 'Marcapomacocha', 'Yauli', 'Junin', 0
    UNION ALL
    SELECT '120805', 'Morococha', 'Yauli', 'Junin', 0
    UNION ALL
    SELECT '120806', 'Paccha', 'Yauli', 'Junin', 0
    UNION ALL
    SELECT '120807', 'Santa Barbara de Carhuacayan', 'Yauli', 'Junin', 0
    UNION ALL
    SELECT '120808', 'Santa Rosa de Sacco', 'Yauli', 'Junin', 0
    UNION ALL
    SELECT '120809', 'Suitucancha', 'Yauli', 'Junin', 0
    UNION ALL
    SELECT '120810', 'Yauli', 'Yauli', 'Junin', 0
    UNION ALL
    SELECT '120901', 'Chupaca', 'Chupaca', 'Junin', 0
    UNION ALL
    SELECT '120902', 'Ahuac', 'Chupaca', 'Junin', 0
    UNION ALL
    SELECT '120903', 'Chongos Bajo', 'Chupaca', 'Junin', 0
    UNION ALL
    SELECT '120904', 'Huachac', 'Chupaca', 'Junin', 0
    UNION ALL
    SELECT '120905', 'Huamancaca Chico', 'Chupaca', 'Junin', 0
    UNION ALL
    SELECT '120906', 'San Juan de Yscos', 'Chupaca', 'Junin', 0
    UNION ALL
    SELECT '120907', 'San Juan de Jarpa', 'Chupaca', 'Junin', 0
    UNION ALL
    SELECT '120908', 'Tres de Diciembre', 'Chupaca', 'Junin', 0
    UNION ALL
    SELECT '120909', 'Yanacancha', 'Chupaca', 'Junin', 0
    UNION ALL
    SELECT '130101', 'Trujillo', 'Trujillo', 'La Libertad', 0
    UNION ALL
    SELECT '130102', 'El Porvenir', 'Trujillo', 'La Libertad', 0
    UNION ALL
    SELECT '130103', 'Florencia de Mora', 'Trujillo', 'La Libertad', 0
    UNION ALL
    SELECT '130104', 'Huanchaco', 'Trujillo', 'La Libertad', 0
    UNION ALL
    SELECT '130105', 'La Esperanza', 'Trujillo', 'La Libertad', 0
    UNION ALL
    SELECT '130106', 'Laredo', 'Trujillo', 'La Libertad', 0
    UNION ALL
    SELECT '130107', 'Moche', 'Trujillo', 'La Libertad', 0
    UNION ALL
    SELECT '130108', 'Poroto', 'Trujillo', 'La Libertad', 0
    UNION ALL
    SELECT '130109', 'Salaverry', 'Trujillo', 'La Libertad', 0
    UNION ALL
    SELECT '130110', 'Simbal', 'Trujillo', 'La Libertad', 0
    UNION ALL
    SELECT '130111', 'Victor Larco Herrera', 'Trujillo', 'La Libertad', 0
    UNION ALL
    SELECT '130201', 'Ascope', 'Ascope', 'La Libertad', 0
    UNION ALL
    SELECT '130202', 'Chicama', 'Ascope', 'La Libertad', 0
    UNION ALL
    SELECT '130203', 'Chocope', 'Ascope', 'La Libertad', 0
    UNION ALL
    SELECT '130204', 'Magdalena de Cao', 'Ascope', 'La Libertad', 0
    UNION ALL
    SELECT '130205', 'Paijan', 'Ascope', 'La Libertad', 0
    UNION ALL
    SELECT '130206', 'Razuri', 'Ascope', 'La Libertad', 0
    UNION ALL
    SELECT '130207', 'Santiago de Cao', 'Ascope', 'La Libertad', 0
    UNION ALL
    SELECT '130208', 'Casa Grande', 'Ascope', 'La Libertad', 0
    UNION ALL
    SELECT '130301', 'Bolivar', 'Bolivar', 'La Libertad', 0
    UNION ALL
    SELECT '130302', 'Bambamarca', 'Bolivar', 'La Libertad', 0
    UNION ALL
    SELECT '130303', 'Condormarca', 'Bolivar', 'La Libertad', 0
    UNION ALL
    SELECT '130304', 'Longotea', 'Bolivar', 'La Libertad', 0
    UNION ALL
    SELECT '130305', 'Uchumarca', 'Bolivar', 'La Libertad', 0
    UNION ALL
    SELECT '130306', 'Ucuncha', 'Bolivar', 'La Libertad', 0
    UNION ALL
    SELECT '130401', 'Chepen', 'Chepen', 'La Libertad', 0
    UNION ALL
    SELECT '130402', 'Pacanga', 'Chepen', 'La Libertad', 0
    UNION ALL
    SELECT '130403', 'Pueblo Nuevo', 'Chepen', 'La Libertad', 0
    UNION ALL
    SELECT '130501', 'Julcan', 'Julcan', 'La Libertad', 0
    UNION ALL
    SELECT '130502', 'Calamarca', 'Julcan', 'La Libertad', 0
    UNION ALL
    SELECT '130503', 'Carabamba', 'Julcan', 'La Libertad', 0
    UNION ALL
    SELECT '130504', 'Huaso', 'Julcan', 'La Libertad', 0
    UNION ALL
    SELECT '130601', 'Otuzco', 'Otuzco', 'La Libertad', 0
    UNION ALL
    SELECT '130602', 'Agallpampa', 'Otuzco', 'La Libertad', 0
    UNION ALL
    SELECT '130604', 'Charat', 'Otuzco', 'La Libertad', 0
    UNION ALL
    SELECT '130605', 'Huaranchal', 'Otuzco', 'La Libertad', 0
    UNION ALL
    SELECT '130606', 'La Cuesta', 'Otuzco', 'La Libertad', 0
    UNION ALL
    SELECT '130608', 'Mache', 'Otuzco', 'La Libertad', 0
    UNION ALL
    SELECT '130610', 'Paranday', 'Otuzco', 'La Libertad', 0
    UNION ALL
    SELECT '130611', 'Salpo', 'Otuzco', 'La Libertad', 0
    UNION ALL
    SELECT '130613', 'Sinsicap', 'Otuzco', 'La Libertad', 0
    UNION ALL
    SELECT '130614', 'Usquil', 'Otuzco', 'La Libertad', 0
    UNION ALL
    SELECT '130701', 'San Pedro de Lloc', 'Pacasmayo', 'La Libertad', 0
    UNION ALL
    SELECT '130702', 'Guadalupe', 'Pacasmayo', 'La Libertad', 0
    UNION ALL
    SELECT '130703', 'Jequetepeque', 'Pacasmayo', 'La Libertad', 0
    UNION ALL
    SELECT '130704', 'Pacasmayo', 'Pacasmayo', 'La Libertad', 0
    UNION ALL
    SELECT '130705', 'San Jose', 'Pacasmayo', 'La Libertad', 0
    UNION ALL
    SELECT '130801', 'Tayabamba', 'Pataz', 'La Libertad', 0
    UNION ALL
    SELECT '130802', 'Buldibuyo', 'Pataz', 'La Libertad', 0
    UNION ALL
    SELECT '130803', 'Chillia', 'Pataz', 'La Libertad', 0
    UNION ALL
    SELECT '130804', 'Huancaspata', 'Pataz', 'La Libertad', 0
    UNION ALL
    SELECT '130805', 'Huaylillas', 'Pataz', 'La Libertad', 0
    UNION ALL
    SELECT '130806', 'Huayo', 'Pataz', 'La Libertad', 0
    UNION ALL
    SELECT '130807', 'Ongon', 'Pataz', 'La Libertad', 0
    UNION ALL
    SELECT '130808', 'Parcoy', 'Pataz', 'La Libertad', 0
    UNION ALL
    SELECT '130809', 'Pataz', 'Pataz', 'La Libertad', 0
    UNION ALL
    SELECT '130810', 'Pias', 'Pataz', 'La Libertad', 0
    UNION ALL
    SELECT '130811', 'Santiago de Challas', 'Pataz', 'La Libertad', 0
    UNION ALL
    SELECT '130812', 'Taurija', 'Pataz', 'La Libertad', 0
    UNION ALL
    SELECT '130813', 'Urpay', 'Pataz', 'La Libertad', 0
    UNION ALL
    SELECT '130901', 'Huamachuco', 'Sanchez Carrion', 'La Libertad', 0
    UNION ALL
    SELECT '130902', 'Chugay', 'Sanchez Carrion', 'La Libertad', 0
    UNION ALL
    SELECT '130903', 'Cochorco', 'Sanchez Carrion', 'La Libertad', 0
    UNION ALL
    SELECT '130904', 'Curgos', 'Sanchez Carrion', 'La Libertad', 0
    UNION ALL
    SELECT '130905', 'Marcabal', 'Sanchez Carrion', 'La Libertad', 0
    UNION ALL
    SELECT '130906', 'Sanagoran', 'Sanchez Carrion', 'La Libertad', 0
    UNION ALL
    SELECT '130907', 'Sarin', 'Sanchez Carrion', 'La Libertad', 0
    UNION ALL
    SELECT '130908', 'Sartimbamba', 'Sanchez Carrion', 'La Libertad', 0
    UNION ALL
    SELECT '131001', 'Santiago de Chuco', 'Santiago de Chuco', 'La Libertad', 0
    UNION ALL
    SELECT '131002', 'Angasmarca', 'Santiago de Chuco', 'La Libertad', 0
    UNION ALL
    SELECT '131003', 'Cachicadan', 'Santiago de Chuco', 'La Libertad', 0
    UNION ALL
    SELECT '131004', 'Mollebamba', 'Santiago de Chuco', 'La Libertad', 0
    UNION ALL
    SELECT '131005', 'Mollepata', 'Santiago de Chuco', 'La Libertad', 0
    UNION ALL
    SELECT '131006', 'Quiruvilca', 'Santiago de Chuco', 'La Libertad', 0
    UNION ALL
    SELECT '131007', 'Santa Cruz de Chuca', 'Santiago de Chuco', 'La Libertad', 0
    UNION ALL
    SELECT '131008', 'Sitabamba', 'Santiago de Chuco', 'La Libertad', 0
    UNION ALL
    SELECT '131101', 'Cascas', 'Gran Chimu', 'La Libertad', 0
    UNION ALL
    SELECT '131102', 'Lucma', 'Gran Chimu', 'La Libertad', 0
    UNION ALL
    SELECT '131103', 'Compin', 'Gran Chimu', 'La Libertad', 0
    UNION ALL
    SELECT '131104', 'Sayapullo', 'Gran Chimu', 'La Libertad', 0
    UNION ALL
    SELECT '131201', 'Viru', 'Viru', 'La Libertad', 0
    UNION ALL
    SELECT '131202', 'Chao', 'Viru', 'La Libertad', 0
    UNION ALL
    SELECT '131203', 'Guadalupito', 'Viru', 'La Libertad', 0
    UNION ALL
    SELECT '140101', 'Chiclayo', 'Chiclayo', 'Lambayeque', 0
    UNION ALL
    SELECT '140102', 'Chongoyape', 'Chiclayo', 'Lambayeque', 0
    UNION ALL
    SELECT '140103', 'Eten', 'Chiclayo', 'Lambayeque', 0
    UNION ALL
    SELECT '140104', 'Eten Puerto', 'Chiclayo', 'Lambayeque', 0
    UNION ALL
    SELECT '140105', 'Jose Leonardo Ortiz', 'Chiclayo', 'Lambayeque', 0
    UNION ALL
    SELECT '140106', 'La Victoria', 'Chiclayo', 'Lambayeque', 0
    UNION ALL
    SELECT '140107', 'Lagunas', 'Chiclayo', 'Lambayeque', 0
    UNION ALL
    SELECT '140108', 'Monsefu', 'Chiclayo', 'Lambayeque', 0
    UNION ALL
    SELECT '140109', 'Nueva Arica', 'Chiclayo', 'Lambayeque', 0
    UNION ALL
    SELECT '140110', 'Oyotun', 'Chiclayo', 'Lambayeque', 0
    UNION ALL
    SELECT '140111', 'Picsi', 'Chiclayo', 'Lambayeque', 0
    UNION ALL
    SELECT '140112', 'Pimentel', 'Chiclayo', 'Lambayeque', 0
    UNION ALL
    SELECT '140113', 'Reque', 'Chiclayo', 'Lambayeque', 0
    UNION ALL
    SELECT '140114', 'Santa Rosa', 'Chiclayo', 'Lambayeque', 0
    UNION ALL
    SELECT '140115', 'Saña', 'Chiclayo', 'Lambayeque', 0
    UNION ALL
    SELECT '140116', 'Cayalti', 'Chiclayo', 'Lambayeque', 0
    UNION ALL
    SELECT '140117', 'Patapo', 'Chiclayo', 'Lambayeque', 0
    UNION ALL
    SELECT '140118', 'Pomalca', 'Chiclayo', 'Lambayeque', 0
    UNION ALL
    SELECT '140119', 'Pucala', 'Chiclayo', 'Lambayeque', 0
    UNION ALL
    SELECT '140120', 'Tuman', 'Chiclayo', 'Lambayeque', 0
    UNION ALL
    SELECT '140201', 'Ferreñafe', 'Ferreñafe', 'Lambayeque', 0
    UNION ALL
    SELECT '140202', 'Cañaris', 'Ferreñafe', 'Lambayeque', 0
    UNION ALL
    SELECT '140203', 'Incahuasi', 'Ferreñafe', 'Lambayeque', 0
    UNION ALL
    SELECT '140204', 'Manuel Antonio Mesones Muro', 'Ferreñafe', 'Lambayeque', 0
    UNION ALL
    SELECT '140205', 'Pitipo', 'Ferreñafe', 'Lambayeque', 0
    UNION ALL
    SELECT '140206', 'Pueblo Nuevo', 'Ferreñafe', 'Lambayeque', 0
    UNION ALL
    SELECT '140301', 'Lambayeque', 'Lambayeque', 'Lambayeque', 0
    UNION ALL
    SELECT '140302', 'Chochope', 'Lambayeque', 'Lambayeque', 0
    UNION ALL
    SELECT '140303', 'Illimo', 'Lambayeque', 'Lambayeque', 0
    UNION ALL
    SELECT '140304', 'Jayanca', 'Lambayeque', 'Lambayeque', 0
    UNION ALL
    SELECT '140305', 'Mochumi', 'Lambayeque', 'Lambayeque', 0
    UNION ALL
    SELECT '140306', 'Morrope', 'Lambayeque', 'Lambayeque', 0
    UNION ALL
    SELECT '140307', 'Motupe', 'Lambayeque', 'Lambayeque', 0
    UNION ALL
    SELECT '140308', 'Olmos', 'Lambayeque', 'Lambayeque', 0
    UNION ALL
    SELECT '140309', 'Pacora', 'Lambayeque', 'Lambayeque', 0
    UNION ALL
    SELECT '140310', 'Salas', 'Lambayeque', 'Lambayeque', 0
    UNION ALL
    SELECT '140311', 'San Jose', 'Lambayeque', 'Lambayeque', 0
    UNION ALL
    SELECT '140312', 'Tucume', 'Lambayeque', 'Lambayeque', 0
    UNION ALL
    SELECT '150101', 'Lima', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150102', 'Ancon', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150103', 'Ate', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150104', 'Barranco', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150105', 'Breña', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150106', 'Carabayllo', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150107', 'Chaclacayo', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150108', 'Chorrillos', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150109', 'Cieneguilla', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150110', 'Comas', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150111', 'El Agustino', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150112', 'Independencia', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150113', 'Jesus Maria', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150114', 'La Molina', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150115', 'La Victoria', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150116', 'Lince', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150117', 'Los Olivos', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150118', 'Lurigancho', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150119', 'Lurin', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150120', 'Magdalena del Mar', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150121', 'Pueblo Libre', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150122', 'Miraflores', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150123', 'Pachacamac', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150124', 'Pucusana', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150125', 'Puente Piedra', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150126', 'Punta Hermosa', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150127', 'Punta Negra', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150128', 'Rimac', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150129', 'San Bartolo', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150130', 'San Borja', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150131', 'San Isidro', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150132', 'San Juan de Lurigancho', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150133', 'San Juan de Miraflores', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150134', 'San Luis', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150135', 'San Martin de Porres', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150136', 'San Miguel', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150137', 'Santa Anita', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150138', 'Santa Maria del Mar', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150139', 'Santa Rosa', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150140', 'Santiago de Surco', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150141', 'Surquillo', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150142', 'Villa El Salvador', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150143', 'Villa Maria del Triunfo', 'Lima', 'Lima', 1
    UNION ALL
    SELECT '150201', 'Barranca', 'Barranca', 'Lima', 0
    UNION ALL
    SELECT '150202', 'Paramonga', 'Barranca', 'Lima', 0
    UNION ALL
    SELECT '150203', 'Pativilca', 'Barranca', 'Lima', 0
    UNION ALL
    SELECT '150204', 'Supe', 'Barranca', 'Lima', 0
    UNION ALL
    SELECT '150205', 'Supe Puerto', 'Barranca', 'Lima', 0
    UNION ALL
    SELECT '150301', 'Cajatambo', 'Cajatambo', 'Lima', 0
    UNION ALL
    SELECT '150302', 'Copa', 'Cajatambo', 'Lima', 0
    UNION ALL
    SELECT '150303', 'Gorgor', 'Cajatambo', 'Lima', 0
    UNION ALL
    SELECT '150304', 'Huancapon', 'Cajatambo', 'Lima', 0
    UNION ALL
    SELECT '150305', 'Manas', 'Cajatambo', 'Lima', 0
    UNION ALL
    SELECT '150401', 'Canta', 'Canta', 'Lima', 0
    UNION ALL
    SELECT '150402', 'Arahuay', 'Canta', 'Lima', 0
    UNION ALL
    SELECT '150403', 'Huamantanga', 'Canta', 'Lima', 0
    UNION ALL
    SELECT '150404', 'Huaros', 'Canta', 'Lima', 0
    UNION ALL
    SELECT '150405', 'Lachaqui', 'Canta', 'Lima', 0
    UNION ALL
    SELECT '150406', 'San Buenaventura', 'Canta', 'Lima', 0
    UNION ALL
    SELECT '150407', 'Santa Rosa de Quives', 'Canta', 'Lima', 0
    UNION ALL
    SELECT '150501', 'San Vicente de Cañete', 'Cañete', 'Lima', 0
    UNION ALL
    SELECT '150502', 'Asia', 'Cañete', 'Lima', 0
    UNION ALL
    SELECT '150503', 'Calango', 'Cañete', 'Lima', 0
    UNION ALL
    SELECT '150504', 'Cerro Azul', 'Cañete', 'Lima', 0
    UNION ALL
    SELECT '150505', 'Chilca', 'Cañete', 'Lima', 0
    UNION ALL
    SELECT '150506', 'Coayllo', 'Cañete', 'Lima', 0
    UNION ALL
    SELECT '150507', 'Imperial', 'Cañete', 'Lima', 0
    UNION ALL
    SELECT '150508', 'Lunahuana', 'Cañete', 'Lima', 0
    UNION ALL
    SELECT '150509', 'Mala', 'Cañete', 'Lima', 0
    UNION ALL
    SELECT '150510', 'Nuevo Imperial', 'Cañete', 'Lima', 0
    UNION ALL
    SELECT '150511', 'Pacaran', 'Cañete', 'Lima', 0
    UNION ALL
    SELECT '150512', 'Quilmana', 'Cañete', 'Lima', 0
    UNION ALL
    SELECT '150513', 'San Antonio', 'Cañete', 'Lima', 0
    UNION ALL
    SELECT '150514', 'San Luis', 'Cañete', 'Lima', 0
    UNION ALL
    SELECT '150515', 'Santa Cruz de Flores', 'Cañete', 'Lima', 0
    UNION ALL
    SELECT '150516', 'Zuñiga', 'Cañete', 'Lima', 0
    UNION ALL
    SELECT '150601', 'Huaral', 'Huaral', 'Lima', 0
    UNION ALL
    SELECT '150602', 'Atavillos Alto', 'Huaral', 'Lima', 0
    UNION ALL
    SELECT '150603', 'Atavillos Bajo', 'Huaral', 'Lima', 0
    UNION ALL
    SELECT '150604', 'Aucallama', 'Huaral', 'Lima', 0
    UNION ALL
    SELECT '150605', 'Chancay', 'Huaral', 'Lima', 0
    UNION ALL
    SELECT '150606', 'Ihuari', 'Huaral', 'Lima', 0
    UNION ALL
    SELECT '150607', 'Lampian', 'Huaral', 'Lima', 0
    UNION ALL
    SELECT '150608', 'Pacaraos', 'Huaral', 'Lima', 0
    UNION ALL
    SELECT '150609', 'San Miguel de Acos', 'Huaral', 'Lima', 0
    UNION ALL
    SELECT '150610', 'Santa Cruz de Andamarca', 'Huaral', 'Lima', 0
    UNION ALL
    SELECT '150611', 'Sumbilca', 'Huaral', 'Lima', 0
    UNION ALL
    SELECT '150612', 'Veintisiete de Noviembre', 'Huaral', 'Lima', 0
    UNION ALL
    SELECT '150701', 'Matucana', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150702', 'Antioquia', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150703', 'Callahuanca', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150704', 'Carampoma', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150705', 'Chicla', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150706', 'Cuenca', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150707', 'Huachupampa', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150708', 'Huanza', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150709', 'Huarochiri', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150710', 'Lahuaytambo', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150711', 'Langa', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150712', 'Laraos', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150713', 'Mariatana', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150714', 'Ricardo Palma', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150715', 'San Andres de Tupicocha', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150716', 'San Antonio', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150717', 'San Bartolome', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150718', 'San Damian', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150719', 'San Juan de Iris', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150720', 'San Juan de Tantaranche', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150721', 'San Lorenzo de Quinti', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150722', 'San Mateo', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150723', 'San Mateo de Otao', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150724', 'San Pedro de Casta', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150725', 'San Pedro de Huancayre', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150726', 'Sangallaya', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150727', 'Santa Cruz de Cocachacra', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150728', 'Santa Eulalia', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150729', 'Santiago de Anchucaya', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150730', 'Santiago de Tuna', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150731', 'Santo Domingo de los Olleros', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150732', 'Surco', 'Huarochiri', 'Lima', 0
    UNION ALL
    SELECT '150801', 'Huacho', 'Huaura', 'Lima', 0
    UNION ALL
    SELECT '150802', 'Ambar', 'Huaura', 'Lima', 0
    UNION ALL
    SELECT '150803', 'Caleta de Carquin', 'Huaura', 'Lima', 0
    UNION ALL
    SELECT '150804', 'Checras', 'Huaura', 'Lima', 0
    UNION ALL
    SELECT '150805', 'Hualmay', 'Huaura', 'Lima', 0
    UNION ALL
    SELECT '150806', 'Huaura', 'Huaura', 'Lima', 0
    UNION ALL
    SELECT '150807', 'Leoncio Prado', 'Huaura', 'Lima', 0
    UNION ALL
    SELECT '150808', 'Paccho', 'Huaura', 'Lima', 0
    UNION ALL
    SELECT '150809', 'Santa Leonor', 'Huaura', 'Lima', 0
    UNION ALL
    SELECT '150810', 'Santa Maria', 'Huaura', 'Lima', 0
    UNION ALL
    SELECT '150811', 'Sayan', 'Huaura', 'Lima', 0
    UNION ALL
    SELECT '150812', 'Vegueta', 'Huaura', 'Lima', 0
    UNION ALL
    SELECT '150901', 'Oyon', 'Oyon', 'Lima', 0
    UNION ALL
    SELECT '150902', 'Andajes', 'Oyon', 'Lima', 0
    UNION ALL
    SELECT '150903', 'Caujul', 'Oyon', 'Lima', 0
    UNION ALL
    SELECT '150904', 'Cochamarca', 'Oyon', 'Lima', 0
    UNION ALL
    SELECT '150905', 'Navan', 'Oyon', 'Lima', 0
    UNION ALL
    SELECT '150906', 'Pachangara', 'Oyon', 'Lima', 0
    UNION ALL
    SELECT '151001', 'Yauyos', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151002', 'Alis', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151003', 'Ayauca', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151004', 'Ayaviri', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151005', 'Azangaro', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151006', 'Cacra', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151007', 'Carania', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151008', 'Catahuasi', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151009', 'Chocos', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151010', 'Cochas', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151011', 'Colonia', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151012', 'Hongos', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151013', 'Huampara', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151014', 'Huancaya', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151015', 'Huangascar', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151016', 'Huantan', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151017', 'Huañec', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151018', 'Laraos', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151019', 'Lincha', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151020', 'Madean', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151021', 'Miraflores', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151022', 'Omas', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151023', 'Putinza', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151024', 'Quinches', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151025', 'Quinocay', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151026', 'San Joaquin', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151027', 'San Pedro de Pilas', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151028', 'Tanta', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151029', 'Tauripampa', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151030', 'Tomas', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151031', 'Tupe', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151032', 'Viñac', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '151033', 'Vitis', 'Yauyos', 'Lima', 0
    UNION ALL
    SELECT '160101', 'Iquitos', 'Maynas', 'Loreto', 0
    UNION ALL
    SELECT '160102', 'Alto Nanay', 'Maynas', 'Loreto', 0
    UNION ALL
    SELECT '160103', 'Fernando Lores', 'Maynas', 'Loreto', 0
    UNION ALL
    SELECT '160104', 'Indiana', 'Maynas', 'Loreto', 0
    UNION ALL
    SELECT '160105', 'Las Amazonas', 'Maynas', 'Loreto', 0
    UNION ALL
    SELECT '160106', 'Mazan', 'Maynas', 'Loreto', 0
    UNION ALL
    SELECT '160107', 'Napo', 'Maynas', 'Loreto', 0
    UNION ALL
    SELECT '160108', 'Punchana', 'Maynas', 'Loreto', 0
    UNION ALL
    SELECT '160110', 'Torres Causana', 'Maynas', 'Loreto', 0
    UNION ALL
    SELECT '160112', 'Belen', 'Maynas', 'Loreto', 0
    UNION ALL
    SELECT '160113', 'San Juan Bautista', 'Maynas', 'Loreto', 0
    UNION ALL
    SELECT '160201', 'Yurimaguas', 'Alto Amazonas', 'Loreto', 0
    UNION ALL
    SELECT '160202', 'Balsapuerto', 'Alto Amazonas', 'Loreto', 0
    UNION ALL
    SELECT '160205', 'Jeberos', 'Alto Amazonas', 'Loreto', 0
    UNION ALL
    SELECT '160206', 'Lagunas', 'Alto Amazonas', 'Loreto', 0
    UNION ALL
    SELECT '160210', 'Santa Cruz', 'Alto Amazonas', 'Loreto', 0
    UNION ALL
    SELECT '160211', 'Teniente Cesar Lopez Rojas', 'Alto Amazonas', 'Loreto', 0
    UNION ALL
    SELECT '160301', 'Nauta', 'Loreto', 'Loreto', 0
    UNION ALL
    SELECT '160302', 'Parinari', 'Loreto', 'Loreto', 0
    UNION ALL
    SELECT '160303', 'Tigre', 'Loreto', 'Loreto', 0
    UNION ALL
    SELECT '160304', 'Trompeteros', 'Loreto', 'Loreto', 0
    UNION ALL
    SELECT '160305', 'Urarinas', 'Loreto', 'Loreto', 0
    UNION ALL
    SELECT '160401', 'Ramon Castilla', 'Mariscal Ramon Castilla', 'Loreto', 0
    UNION ALL
    SELECT '160402', 'Pebas', 'Mariscal Ramon Castilla', 'Loreto', 0
    UNION ALL
    SELECT '160403', 'Yavari', 'Mariscal Ramon Castilla', 'Loreto', 0
    UNION ALL
    SELECT '160404', 'San Pablo', 'Mariscal Ramon Castilla', 'Loreto', 0
    UNION ALL
    SELECT '160501', 'Requena', 'Requena', 'Loreto', 0
    UNION ALL
    SELECT '160502', 'Alto Tapiche', 'Requena', 'Loreto', 0
    UNION ALL
    SELECT '160503', 'Capelo', 'Requena', 'Loreto', 0
    UNION ALL
    SELECT '160504', 'Emilio San Martin', 'Requena', 'Loreto', 0
    UNION ALL
    SELECT '160505', 'Maquia', 'Requena', 'Loreto', 0
    UNION ALL
    SELECT '160506', 'Puinahua', 'Requena', 'Loreto', 0
    UNION ALL
    SELECT '160507', 'Saquena', 'Requena', 'Loreto', 0
    UNION ALL
    SELECT '160508', 'Soplin', 'Requena', 'Loreto', 0
    UNION ALL
    SELECT '160509', 'Tapiche', 'Requena', 'Loreto', 0
    UNION ALL
    SELECT '160510', 'Jenaro Herrera', 'Requena', 'Loreto', 0
    UNION ALL
    SELECT '160511', 'Yaquerana', 'Requena', 'Loreto', 0
    UNION ALL
    SELECT '160601', 'Contamana', 'Ucayali', 'Loreto', 0
    UNION ALL
    SELECT '160602', 'Inahuaya', 'Ucayali', 'Loreto', 0
    UNION ALL
    SELECT '160603', 'Padre Marquez', 'Ucayali', 'Loreto', 0
    UNION ALL
    SELECT '160604', 'Pampa Hermosa', 'Ucayali', 'Loreto', 0
    UNION ALL
    SELECT '160605', 'Sarayacu', 'Ucayali', 'Loreto', 0
    UNION ALL
    SELECT '160606', 'Vargas Guerra', 'Ucayali', 'Loreto', 0
    UNION ALL
    SELECT '160701', 'Barranca', 'Datem del Marañon', 'Loreto', 0
    UNION ALL
    SELECT '160702', 'Cahuapanas', 'Datem del Marañon', 'Loreto', 0
    UNION ALL
    SELECT '160703', 'Manseriche', 'Datem del Marañon', 'Loreto', 0
    UNION ALL
    SELECT '160704', 'Morona', 'Datem del Marañon', 'Loreto', 0
    UNION ALL
    SELECT '160705', 'Pastaza', 'Datem del Marañon', 'Loreto', 0
    UNION ALL
    SELECT '160706', 'Andoas', 'Datem del Marañon', 'Loreto', 0
    UNION ALL
    SELECT '160801', 'Putumayo', 'Maynas', 'Loreto', 0
    UNION ALL
    SELECT '160802', 'Rosa Panduro', 'Maynas', 'Loreto', 0
    UNION ALL
    SELECT '160803', 'Teniente Manuel Clavero', 'Maynas', 'Loreto', 0
    UNION ALL
    SELECT '160804', 'Yaguas', 'Maynas', 'Loreto', 0
    UNION ALL
    SELECT '170101', 'Tambopata', 'Tambopata', 'Madre de Dios', 0
    UNION ALL
    SELECT '170102', 'Inambari', 'Tambopata', 'Madre de Dios', 0
    UNION ALL
    SELECT '170103', 'Las Piedras', 'Tambopata', 'Madre de Dios', 0
    UNION ALL
    SELECT '170104', 'Laberinto', 'Tambopata', 'Madre de Dios', 0
    UNION ALL
    SELECT '170201', 'Manu', 'Manu', 'Madre de Dios', 0
    UNION ALL
    SELECT '170202', 'Fitzcarrald', 'Manu', 'Madre de Dios', 0
    UNION ALL
    SELECT '170203', 'Madre de Dios', 'Manu', 'Madre de Dios', 0
    UNION ALL
    SELECT '170204', 'Huepetuhe', 'Manu', 'Madre de Dios', 0
    UNION ALL
    SELECT '170301', 'Iñapari', 'Tahuamanu', 'Madre de Dios', 0
    UNION ALL
    SELECT '170302', 'Iberia', 'Tahuamanu', 'Madre de Dios', 0
    UNION ALL
    SELECT '170303', 'Tahuamanu', 'Tahuamanu', 'Madre de Dios', 0
    UNION ALL
    SELECT '180101', 'Moquegua', 'Mariscal Nieto', 'Moquegua', 0
    UNION ALL
    SELECT '180102', 'Carumas', 'Mariscal Nieto', 'Moquegua', 0
    UNION ALL
    SELECT '180103', 'Cuchumbaya', 'Mariscal Nieto', 'Moquegua', 0
    UNION ALL
    SELECT '180104', 'Samegua', 'Mariscal Nieto', 'Moquegua', 0
    UNION ALL
    SELECT '180105', 'San Cristobal', 'Mariscal Nieto', 'Moquegua', 0
    UNION ALL
    SELECT '180106', 'Torata', 'Mariscal Nieto', 'Moquegua', 0
    UNION ALL
    SELECT '180201', 'Omate', 'General Sanchez Cerr', 'Moquegua', 0
    UNION ALL
    SELECT '180202', 'Chojata', 'General Sanchez Cerr', 'Moquegua', 0
    UNION ALL
    SELECT '180203', 'Coalaque', 'General Sanchez Cerr', 'Moquegua', 0
    UNION ALL
    SELECT '180204', 'Ichuña', 'General Sanchez Cerr', 'Moquegua', 0
    UNION ALL
    SELECT '180205', 'La Capilla', 'General Sanchez Cerr', 'Moquegua', 0
    UNION ALL
    SELECT '180206', 'Lloque', 'General Sanchez Cerr', 'Moquegua', 0
    UNION ALL
    SELECT '180207', 'Matalaque', 'General Sanchez Cerr', 'Moquegua', 0
    UNION ALL
    SELECT '180208', 'Puquina', 'General Sanchez Cerr', 'Moquegua', 0
    UNION ALL
    SELECT '180209', 'Quinistaquillas', 'General Sanchez Cerr', 'Moquegua', 0
    UNION ALL
    SELECT '180210', 'Ubinas', 'General Sanchez Cerr', 'Moquegua', 0
    UNION ALL
    SELECT '180211', 'Yunga', 'General Sanchez Cerr', 'Moquegua', 0
    UNION ALL
    SELECT '180301', 'Ilo', 'Ilo', 'Moquegua', 0
    UNION ALL
    SELECT '180302', 'El Algarrobal', 'Ilo', 'Moquegua', 0
    UNION ALL
    SELECT '180303', 'Pacocha', 'Ilo', 'Moquegua', 0
    UNION ALL
    SELECT '190101', 'Chaupimarca', 'Pasco', 'Pasco', 0
    UNION ALL
    SELECT '190102', 'Huachon', 'Pasco', 'Pasco', 0
    UNION ALL
    SELECT '190103', 'Huariaca', 'Pasco', 'Pasco', 0
    UNION ALL
    SELECT '190104', 'Huayllay', 'Pasco', 'Pasco', 0
    UNION ALL
    SELECT '190105', 'Ninacaca', 'Pasco', 'Pasco', 0
    UNION ALL
    SELECT '190106', 'Pallanchacra', 'Pasco', 'Pasco', 0
    UNION ALL
    SELECT '190107', 'Paucartambo', 'Pasco', 'Pasco', 0
    UNION ALL
    SELECT '190108', 'San Francisco de Asis de Yarusyacan', 'Pasco', 'Pasco', 0
    UNION ALL
    SELECT '190109', 'Simon Bolivar', 'Pasco', 'Pasco', 0
    UNION ALL
    SELECT '190110', 'Ticlacayan', 'Pasco', 'Pasco', 0
    UNION ALL
    SELECT '190111', 'Tinyahuarco', 'Pasco', 'Pasco', 0
    UNION ALL
    SELECT '190112', 'Vicco', 'Pasco', 'Pasco', 0
    UNION ALL
    SELECT '190113', 'Yanacancha', 'Pasco', 'Pasco', 0
    UNION ALL
    SELECT '190201', 'Yanahuanca', 'Daniel Alcides Carri', 'Pasco', 0
    UNION ALL
    SELECT '190202', 'Chacayan', 'Daniel Alcides Carri', 'Pasco', 0
    UNION ALL
    SELECT '190203', 'Goyllarisquizga', 'Daniel Alcides Carri', 'Pasco', 0
    UNION ALL
    SELECT '190204', 'Paucar', 'Daniel Alcides Carri', 'Pasco', 0
    UNION ALL
    SELECT '190205', 'San Pedro de Pillao', 'Daniel Alcides Carri', 'Pasco', 0
    UNION ALL
    SELECT '190206', 'Santa Ana de Tusi', 'Daniel Alcides Carri', 'Pasco', 0
    UNION ALL
    SELECT '190207', 'Tapuc', 'Daniel Alcides Carri', 'Pasco', 0
    UNION ALL
    SELECT '190208', 'Vilcabamba', 'Daniel Alcides Carri', 'Pasco', 0
    UNION ALL
    SELECT '190301', 'Oxapampa', 'Oxapampa', 'Pasco', 0
    UNION ALL
    SELECT '190302', 'Chontabamba', 'Oxapampa', 'Pasco', 0
    UNION ALL
    SELECT '190303', 'Huancabamba', 'Oxapampa', 'Pasco', 0
    UNION ALL
    SELECT '190304', 'Palcazu', 'Oxapampa', 'Pasco', 0
    UNION ALL
    SELECT '190305', 'Pozuzo', 'Oxapampa', 'Pasco', 0
    UNION ALL
    SELECT '190306', 'Puerto Bermudez', 'Oxapampa', 'Pasco', 0
    UNION ALL
    SELECT '190307', 'Villa Rica', 'Oxapampa', 'Pasco', 0
    UNION ALL
    SELECT '190308', 'Constitución', 'Oxapampa', 'Pasco', 0
    UNION ALL
    SELECT '200101', 'Piura', 'Piura', 'Piura', 0
    UNION ALL
    SELECT '200104', 'Castilla', 'Piura', 'Piura', 0
    UNION ALL
    SELECT '200105', 'Catacaos', 'Piura', 'Piura', 0
    UNION ALL
    SELECT '200107', 'Cura Mori', 'Piura', 'Piura', 0
    UNION ALL
    SELECT '200108', 'El Tallan', 'Piura', 'Piura', 0
    UNION ALL
    SELECT '200109', 'La Arena', 'Piura', 'Piura', 0
    UNION ALL
    SELECT '200110', 'La Union', 'Piura', 'Piura', 0
    UNION ALL
    SELECT '200111', 'Las Lomas', 'Piura', 'Piura', 0
    UNION ALL
    SELECT '200114', 'Tambo Grande', 'Piura', 'Piura', 0
    UNION ALL
    SELECT '200115', '26 de Octubre', 'Piura', 'Piura', 0
    UNION ALL
    SELECT '200201', 'Ayabaca', 'Ayabaca', 'Piura', 0
    UNION ALL
    SELECT '200202', 'Frias', 'Ayabaca', 'Piura', 0
    UNION ALL
    SELECT '200203', 'Jilili', 'Ayabaca', 'Piura', 0
    UNION ALL
    SELECT '200204', 'Lagunas', 'Ayabaca', 'Piura', 0
    UNION ALL
    SELECT '200205', 'Montero', 'Ayabaca', 'Piura', 0
    UNION ALL
    SELECT '200206', 'Pacaipampa', 'Ayabaca', 'Piura', 0
    UNION ALL
    SELECT '200207', 'Paimas', 'Ayabaca', 'Piura', 0
    UNION ALL
    SELECT '200208', 'Sapillica', 'Ayabaca', 'Piura', 0
    UNION ALL
    SELECT '200209', 'Sicchez', 'Ayabaca', 'Piura', 0
    UNION ALL
    SELECT '200210', 'Suyo', 'Ayabaca', 'Piura', 0
    UNION ALL
    SELECT '200301', 'Huancabamba', 'Huancabamba', 'Piura', 0
    UNION ALL
    SELECT '200302', 'Canchaque', 'Huancabamba', 'Piura', 0
    UNION ALL
    SELECT '200303', 'El Carmen de La Frontera', 'Huancabamba', 'Piura', 0
    UNION ALL
    SELECT '200304', 'Huarmaca', 'Huancabamba', 'Piura', 0
    UNION ALL
    SELECT '200305', 'Lalaquiz', 'Huancabamba', 'Piura', 0
    UNION ALL
    SELECT '200306', 'San Miguel de El Faique', 'Huancabamba', 'Piura', 0
    UNION ALL
    SELECT '200307', 'Sondor', 'Huancabamba', 'Piura', 0
    UNION ALL
    SELECT '200308', 'Sondorillo', 'Huancabamba', 'Piura', 0
    UNION ALL
    SELECT '200401', 'Chulucanas', 'Morropon', 'Piura', 0
    UNION ALL
    SELECT '200402', 'Buenos Aires', 'Morropon', 'Piura', 0
    UNION ALL
    SELECT '200403', 'Chalaco', 'Morropon', 'Piura', 0
    UNION ALL
    SELECT '200404', 'La Matanza', 'Morropon', 'Piura', 0
    UNION ALL
    SELECT '200405', 'Morropon', 'Morropon', 'Piura', 0
    UNION ALL
    SELECT '200406', 'Salitral', 'Morropon', 'Piura', 0
    UNION ALL
    SELECT '200407', 'San Juan de Bigote', 'Morropon', 'Piura', 0
    UNION ALL
    SELECT '200408', 'Santa Catalina de Mossa', 'Morropon', 'Piura', 0
    UNION ALL
    SELECT '200409', 'Santo Domingo', 'Morropon', 'Piura', 0
    UNION ALL
    SELECT '200410', 'Yamango', 'Morropon', 'Piura', 0
    UNION ALL
    SELECT '200501', 'Paita', 'Paita', 'Piura', 0
    UNION ALL
    SELECT '200502', 'Amotape', 'Paita', 'Piura', 0
    UNION ALL
    SELECT '200503', 'Arenal', 'Paita', 'Piura', 0
    UNION ALL
    SELECT '200504', 'Colan', 'Paita', 'Piura', 0
    UNION ALL
    SELECT '200505', 'La Huaca', 'Paita', 'Piura', 0
    UNION ALL
    SELECT '200506', 'Tamarindo', 'Paita', 'Piura', 0
    UNION ALL
    SELECT '200507', 'Vichayal', 'Paita', 'Piura', 0
    UNION ALL
    SELECT '200601', 'Sullana', 'Sullana', 'Piura', 0
    UNION ALL
    SELECT '200602', 'Bellavista', 'Sullana', 'Piura', 0
    UNION ALL
    SELECT '200603', 'Ignacio Escudero', 'Sullana', 'Piura', 0
    UNION ALL
    SELECT '200604', 'Lancones', 'Sullana', 'Piura', 0
    UNION ALL
    SELECT '200605', 'Marcavelica', 'Sullana', 'Piura', 0
    UNION ALL
    SELECT '200606', 'Miguel Checa', 'Sullana', 'Piura', 0
    UNION ALL
    SELECT '200607', 'Querecotillo', 'Sullana', 'Piura', 0
    UNION ALL
    SELECT '200608', 'Salitral', 'Sullana', 'Piura', 0
    UNION ALL
    SELECT '200701', 'Pariñas', 'Talara', 'Piura', 0
    UNION ALL
    SELECT '200702', 'El Alto', 'Talara', 'Piura', 0
    UNION ALL
    SELECT '200703', 'La Brea', 'Talara', 'Piura', 0
    UNION ALL
    SELECT '200704', 'Lobitos', 'Talara', 'Piura', 0
    UNION ALL
    SELECT '200705', 'Los Organos', 'Talara', 'Piura', 0
    UNION ALL
    SELECT '200706', 'Mancora', 'Talara', 'Piura', 0
    UNION ALL
    SELECT '200801', 'Sechura', 'Sechura', 'Piura', 0
    UNION ALL
    SELECT '200802', 'Bellavista de La Union', 'Sechura', 'Piura', 0
    UNION ALL
    SELECT '200803', 'Bernal', 'Sechura', 'Piura', 0
    UNION ALL
    SELECT '200804', 'Cristo Nos Valga', 'Sechura', 'Piura', 0
    UNION ALL
    SELECT '200805', 'Vice', 'Sechura', 'Piura', 0
    UNION ALL
    SELECT '200806', 'Rinconada Llicuar', 'Sechura', 'Piura', 0
    UNION ALL
    SELECT '210101', 'Puno', 'Puno', 'Puno', 0
    UNION ALL
    SELECT '210102', 'Acora', 'Puno', 'Puno', 0
    UNION ALL
    SELECT '210103', 'Amantani', 'Puno', 'Puno', 0
    UNION ALL
    SELECT '210104', 'Atuncolla', 'Puno', 'Puno', 0
    UNION ALL
    SELECT '210105', 'Capachica', 'Puno', 'Puno', 0
    UNION ALL
    SELECT '210106', 'Chucuito', 'Puno', 'Puno', 0
    UNION ALL
    SELECT '210107', 'Coata', 'Puno', 'Puno', 0
    UNION ALL
    SELECT '210108', 'Huata', 'Puno', 'Puno', 0
    UNION ALL
    SELECT '210109', 'Mañazo', 'Puno', 'Puno', 0
    UNION ALL
    SELECT '210110', 'Paucarcolla', 'Puno', 'Puno', 0
    UNION ALL
    SELECT '210111', 'Pichacani', 'Puno', 'Puno', 0
    UNION ALL
    SELECT '210112', 'Plateria', 'Puno', 'Puno', 0
    UNION ALL
    SELECT '210113', 'San Antonio', 'Puno', 'Puno', 0
    UNION ALL
    SELECT '210114', 'Tiquillaca', 'Puno', 'Puno', 0
    UNION ALL
    SELECT '210115', 'Vilque', 'Puno', 'Puno', 0
    UNION ALL
    SELECT '210201', 'Azangaro', 'Azangaro', 'Puno', 0
    UNION ALL
    SELECT '210202', 'Achaya', 'Azangaro', 'Puno', 0
    UNION ALL
    SELECT '210203', 'Arapa', 'Azangaro', 'Puno', 0
    UNION ALL
    SELECT '210204', 'Asillo', 'Azangaro', 'Puno', 0
    UNION ALL
    SELECT '210205', 'Caminaca', 'Azangaro', 'Puno', 0
    UNION ALL
    SELECT '210206', 'Chupa', 'Azangaro', 'Puno', 0
    UNION ALL
    SELECT '210207', 'Jose Domingo Choquehuanca', 'Azangaro', 'Puno', 0
    UNION ALL
    SELECT '210208', 'Muñani', 'Azangaro', 'Puno', 0
    UNION ALL
    SELECT '210209', 'Potoni', 'Azangaro', 'Puno', 0
    UNION ALL
    SELECT '210210', 'Saman', 'Azangaro', 'Puno', 0
    UNION ALL
    SELECT '210211', 'San Anton', 'Azangaro', 'Puno', 0
    UNION ALL
    SELECT '210212', 'San Jose', 'Azangaro', 'Puno', 0
    UNION ALL
    SELECT '210213', 'San Juan de Salinas', 'Azangaro', 'Puno', 0
    UNION ALL
    SELECT '210214', 'Santiago de Pupuja', 'Azangaro', 'Puno', 0
    UNION ALL
    SELECT '210215', 'Tirapata', 'Azangaro', 'Puno', 0
    UNION ALL
    SELECT '210301', 'Macusani', 'Carabaya', 'Puno', 0
    UNION ALL
    SELECT '210302', 'Ajoyani', 'Carabaya', 'Puno', 0
    UNION ALL
    SELECT '210303', 'Ayapata', 'Carabaya', 'Puno', 0
    UNION ALL
    SELECT '210304', 'Coasa', 'Carabaya', 'Puno', 0
    UNION ALL
    SELECT '210305', 'Corani', 'Carabaya', 'Puno', 0
    UNION ALL
    SELECT '210306', 'Crucero', 'Carabaya', 'Puno', 0
    UNION ALL
    SELECT '210307', 'Ituata', 'Carabaya', 'Puno', 0
    UNION ALL
    SELECT '210308', 'Ollachea', 'Carabaya', 'Puno', 0
    UNION ALL
    SELECT '210309', 'San Gaban', 'Carabaya', 'Puno', 0
    UNION ALL
    SELECT '210310', 'Usicayos', 'Carabaya', 'Puno', 0
    UNION ALL
    SELECT '210401', 'Juli', 'Chucuito', 'Puno', 0
    UNION ALL
    SELECT '210402', 'Desaguadero', 'Chucuito', 'Puno', 0
    UNION ALL
    SELECT '210403', 'Huacullani', 'Chucuito', 'Puno', 0
    UNION ALL
    SELECT '210404', 'Kelluyo', 'Chucuito', 'Puno', 0
    UNION ALL
    SELECT '210405', 'Pisacoma', 'Chucuito', 'Puno', 0
    UNION ALL
    SELECT '210406', 'Pomata', 'Chucuito', 'Puno', 0
    UNION ALL
    SELECT '210407', 'Zepita', 'Chucuito', 'Puno', 0
    UNION ALL
    SELECT '210501', 'Ilave', 'El Collao', 'Puno', 0
    UNION ALL
    SELECT '210502', 'Capazo', 'El Collao', 'Puno', 0
    UNION ALL
    SELECT '210503', 'Pilcuyo', 'El Collao', 'Puno', 0
    UNION ALL
    SELECT '210504', 'Santa Rosa', 'El Collao', 'Puno', 0
    UNION ALL
    SELECT '210505', 'Conduriri', 'El Collao', 'Puno', 0
    UNION ALL
    SELECT '210601', 'Huancane', 'Huancane', 'Puno', 0
    UNION ALL
    SELECT '210602', 'Cojata', 'Huancane', 'Puno', 0
    UNION ALL
    SELECT '210603', 'Huatasani', 'Huancane', 'Puno', 0
    UNION ALL
    SELECT '210604', 'Inchupalla', 'Huancane', 'Puno', 0
    UNION ALL
    SELECT '210605', 'Pusi', 'Huancane', 'Puno', 0
    UNION ALL
    SELECT '210606', 'Rosaspata', 'Huancane', 'Puno', 0
    UNION ALL
    SELECT '210607', 'Taraco', 'Huancane', 'Puno', 0
    UNION ALL
    SELECT '210608', 'Vilque Chico', 'Huancane', 'Puno', 0
    UNION ALL
    SELECT '210701', 'Lampa', 'Lampa', 'Puno', 0
    UNION ALL
    SELECT '210702', 'Cabanilla', 'Lampa', 'Puno', 0
    UNION ALL
    SELECT '210703', 'Calapuja', 'Lampa', 'Puno', 0
    UNION ALL
    SELECT '210704', 'Nicasio', 'Lampa', 'Puno', 0
    UNION ALL
    SELECT '210705', 'Ocuviri', 'Lampa', 'Puno', 0
    UNION ALL
    SELECT '210706', 'Palca', 'Lampa', 'Puno', 0
    UNION ALL
    SELECT '210707', 'Paratia', 'Lampa', 'Puno', 0
    UNION ALL
    SELECT '210708', 'Pucara', 'Lampa', 'Puno', 0
    UNION ALL
    SELECT '210709', 'Santa Lucia', 'Lampa', 'Puno', 0
    UNION ALL
    SELECT '210710', 'Vilavila', 'Lampa', 'Puno', 0
    UNION ALL
    SELECT '210801', 'Ayaviri', 'Melgar', 'Puno', 0
    UNION ALL
    SELECT '210802', 'Antauta', 'Melgar', 'Puno', 0
    UNION ALL
    SELECT '210803', 'Cupi', 'Melgar', 'Puno', 0
    UNION ALL
    SELECT '210804', 'Llalli', 'Melgar', 'Puno', 0
    UNION ALL
    SELECT '210805', 'Macari', 'Melgar', 'Puno', 0
    UNION ALL
    SELECT '210806', 'Nuñoa', 'Melgar', 'Puno', 0
    UNION ALL
    SELECT '210807', 'Orurillo', 'Melgar', 'Puno', 0
    UNION ALL
    SELECT '210808', 'Santa Rosa', 'Melgar', 'Puno', 0
    UNION ALL
    SELECT '210809', 'Umachiri', 'Melgar', 'Puno', 0
    UNION ALL
    SELECT '210901', 'Moho', 'Moho', 'Puno', 0
    UNION ALL
    SELECT '210902', 'Conima', 'Moho', 'Puno', 0
    UNION ALL
    SELECT '210903', 'Huayrapata', 'Moho', 'Puno', 0
    UNION ALL
    SELECT '210904', 'Tilali', 'Moho', 'Puno', 0
    UNION ALL
    SELECT '211001', 'Putina', 'San Antonio de Putin', 'Puno', 0
    UNION ALL
    SELECT '211002', 'Ananea', 'San Antonio de Putin', 'Puno', 0
    UNION ALL
    SELECT '211003', 'Pedro Vilca Apaza', 'San Antonio de Putin', 'Puno', 0
    UNION ALL
    SELECT '211004', 'Quilcapuncu', 'San Antonio de Putin', 'Puno', 0
    UNION ALL
    SELECT '211005', 'Sina', 'San Antonio de Putin', 'Puno', 0
    UNION ALL
    SELECT '211101', 'Juliaca', 'San Roman', 'Puno', 0
    UNION ALL
    SELECT '211102', 'Cabana', 'San Roman', 'Puno', 0
    UNION ALL
    SELECT '211103', 'Cabanillas', 'San Roman', 'Puno', 0
    UNION ALL
    SELECT '211104', 'Caracoto', 'San Roman', 'Puno', 0
    UNION ALL
    SELECT '211105', 'San Miguel', 'San Roman', 'Puno', 0
    UNION ALL
    SELECT '211201', 'Sandia', 'Sandia', 'Puno', 0
    UNION ALL
    SELECT '211202', 'Cuyocuyo', 'Sandia', 'Puno', 0
    UNION ALL
    SELECT '211203', 'Limbani', 'Sandia', 'Puno', 0
    UNION ALL
    SELECT '211204', 'Patambuco', 'Sandia', 'Puno', 0
    UNION ALL
    SELECT '211205', 'Phara', 'Sandia', 'Puno', 0
    UNION ALL
    SELECT '211206', 'Quiaca', 'Sandia', 'Puno', 0
    UNION ALL
    SELECT '211207', 'San Juan del Oro', 'Sandia', 'Puno', 0
    UNION ALL
    SELECT '211208', 'Yanahuaya', 'Sandia', 'Puno', 0
    UNION ALL
    SELECT '211209', 'Alto Inambari', 'Sandia', 'Puno', 0
    UNION ALL
    SELECT '211210', 'San Pedro de Putina Punco', 'Sandia', 'Puno', 0
    UNION ALL
    SELECT '211301', 'Yunguyo', 'Yunguyo', 'Puno', 0
    UNION ALL
    SELECT '211302', 'Anapia', 'Yunguyo', 'Puno', 0
    UNION ALL
    SELECT '211303', 'Copani', 'Yunguyo', 'Puno', 0
    UNION ALL
    SELECT '211304', 'Cuturapi', 'Yunguyo', 'Puno', 0
    UNION ALL
    SELECT '211305', 'Ollaraya', 'Yunguyo', 'Puno', 0
    UNION ALL
    SELECT '211306', 'Tinicachi', 'Yunguyo', 'Puno', 0
    UNION ALL
    SELECT '211307', 'Unicachi', 'Yunguyo', 'Puno', 0
    UNION ALL
    SELECT '220101', 'Moyobamba', 'Moyobamba', 'San Martin', 0
    UNION ALL
    SELECT '220102', 'Calzada', 'Moyobamba', 'San Martin', 0
    UNION ALL
    SELECT '220103', 'Habana', 'Moyobamba', 'San Martin', 0
    UNION ALL
    SELECT '220104', 'Jepelacio', 'Moyobamba', 'San Martin', 0
    UNION ALL
    SELECT '220105', 'Soritor', 'Moyobamba', 'San Martin', 0
    UNION ALL
    SELECT '220106', 'Yantalo', 'Moyobamba', 'San Martin', 0
    UNION ALL
    SELECT '220201', 'Bellavista', 'Bellavista', 'San Martin', 0
    UNION ALL
    SELECT '220202', 'Alto Biavo', 'Bellavista', 'San Martin', 0
    UNION ALL
    SELECT '220203', 'Bajo Biavo', 'Bellavista', 'San Martin', 0
    UNION ALL
    SELECT '220204', 'Huallaga', 'Bellavista', 'San Martin', 0
    UNION ALL
    SELECT '220205', 'San Pablo', 'Bellavista', 'San Martin', 0
    UNION ALL
    SELECT '220206', 'San Rafael', 'Bellavista', 'San Martin', 0
    UNION ALL
    SELECT '220301', 'San Jose de Sisa', 'El Dorado', 'San Martin', 0
    UNION ALL
    SELECT '220302', 'Agua Blanca', 'El Dorado', 'San Martin', 0
    UNION ALL
    SELECT '220303', 'San Martin', 'El Dorado', 'San Martin', 0
    UNION ALL
    SELECT '220304', 'Santa Rosa', 'El Dorado', 'San Martin', 0
    UNION ALL
    SELECT '220305', 'Shatoja', 'El Dorado', 'San Martin', 0
    UNION ALL
    SELECT '220401', 'Saposoa', 'Huallaga', 'San Martin', 0
    UNION ALL
    SELECT '220402', 'Alto Saposoa', 'Huallaga', 'San Martin', 0
    UNION ALL
    SELECT '220403', 'El Eslabon', 'Huallaga', 'San Martin', 0
    UNION ALL
    SELECT '220404', 'Piscoyacu', 'Huallaga', 'San Martin', 0
    UNION ALL
    SELECT '220405', 'Sacanche', 'Huallaga', 'San Martin', 0
    UNION ALL
    SELECT '220406', 'Tingo de Saposoa', 'Huallaga', 'San Martin', 0
    UNION ALL
    SELECT '220501', 'Lamas', 'Lamas', 'San Martin', 0
    UNION ALL
    SELECT '220502', 'Alonso de Alvarado', 'Lamas', 'San Martin', 0
    UNION ALL
    SELECT '220503', 'Barranquita', 'Lamas', 'San Martin', 0
    UNION ALL
    SELECT '220504', 'Caynarachi', 'Lamas', 'San Martin', 0
    UNION ALL
    SELECT '220505', 'Cuñumbuqui', 'Lamas', 'San Martin', 0
    UNION ALL
    SELECT '220506', 'Pinto Recodo', 'Lamas', 'San Martin', 0
    UNION ALL
    SELECT '220507', 'Rumisapa', 'Lamas', 'San Martin', 0
    UNION ALL
    SELECT '220508', 'San Roque de Cumbaza', 'Lamas', 'San Martin', 0
    UNION ALL
    SELECT '220509', 'Shanao', 'Lamas', 'San Martin', 0
    UNION ALL
    SELECT '220510', 'Tabalosos', 'Lamas', 'San Martin', 0
    UNION ALL
    SELECT '220511', 'Zapatero', 'Lamas', 'San Martin', 0
    UNION ALL
    SELECT '220601', 'Juanjui', 'Mariscal Caceres', 'San Martin', 0
    UNION ALL
    SELECT '220602', 'Campanilla', 'Mariscal Caceres', 'San Martin', 0
    UNION ALL
    SELECT '220603', 'Huicungo', 'Mariscal Caceres', 'San Martin', 0
    UNION ALL
    SELECT '220604', 'Pachiza', 'Mariscal Caceres', 'San Martin', 0
    UNION ALL
    SELECT '220605', 'Pajarillo', 'Mariscal Caceres', 'San Martin', 0
    UNION ALL
    SELECT '220701', 'Picota', 'Picota', 'San Martin', 0
    UNION ALL
    SELECT '220702', 'Buenos Aires', 'Picota', 'San Martin', 0
    UNION ALL
    SELECT '220703', 'Caspisapa', 'Picota', 'San Martin', 0
    UNION ALL
    SELECT '220704', 'Pilluana', 'Picota', 'San Martin', 0
    UNION ALL
    SELECT '220705', 'Pucacaca', 'Picota', 'San Martin', 0
    UNION ALL
    SELECT '220706', 'San Cristobal', 'Picota', 'San Martin', 0
    UNION ALL
    SELECT '220707', 'San Hilarion', 'Picota', 'San Martin', 0
    UNION ALL
    SELECT '220708', 'Shamboyacu', 'Picota', 'San Martin', 0
    UNION ALL
    SELECT '220709', 'Tingo de Ponasa', 'Picota', 'San Martin', 0
    UNION ALL
    SELECT '220710', 'Tres Unidos', 'Picota', 'San Martin', 0
    UNION ALL
    SELECT '220801', 'Rioja', 'Rioja', 'San Martin', 0
    UNION ALL
    SELECT '220802', 'Awajun', 'Rioja', 'San Martin', 0
    UNION ALL
    SELECT '220803', 'Elias Soplin Vargas', 'Rioja', 'San Martin', 0
    UNION ALL
    SELECT '220804', 'Nueva Cajamarca', 'Rioja', 'San Martin', 0
    UNION ALL
    SELECT '220805', 'Pardo Miguel', 'Rioja', 'San Martin', 0
    UNION ALL
    SELECT '220806', 'Posic', 'Rioja', 'San Martin', 0
    UNION ALL
    SELECT '220807', 'San Fernando', 'Rioja', 'San Martin', 0
    UNION ALL
    SELECT '220808', 'Yorongos', 'Rioja', 'San Martin', 0
    UNION ALL
    SELECT '220809', 'Yuracyacu', 'Rioja', 'San Martin', 0
    UNION ALL
    SELECT '220901', 'Tarapoto', 'San Martin', 'San Martin', 0
    UNION ALL
    SELECT '220902', 'Alberto Leveau', 'San Martin', 'San Martin', 0
    UNION ALL
    SELECT '220903', 'Cacatachi', 'San Martin', 'San Martin', 0
    UNION ALL
    SELECT '220904', 'Chazuta', 'San Martin', 'San Martin', 0
    UNION ALL
    SELECT '220905', 'Chipurana', 'San Martin', 'San Martin', 0
    UNION ALL
    SELECT '220906', 'El Porvenir', 'San Martin', 'San Martin', 0
    UNION ALL
    SELECT '220907', 'Huimbayoc', 'San Martin', 'San Martin', 0
    UNION ALL
    SELECT '220908', 'Juan Guerra', 'San Martin', 'San Martin', 0
    UNION ALL
    SELECT '220909', 'La Banda de Shilcayo', 'San Martin', 'San Martin', 0
    UNION ALL
    SELECT '220910', 'Morales', 'San Martin', 'San Martin', 0
    UNION ALL
    SELECT '220911', 'Papaplaya', 'San Martin', 'San Martin', 0
    UNION ALL
    SELECT '220912', 'San Antonio', 'San Martin', 'San Martin', 0
    UNION ALL
    SELECT '220913', 'Sauce', 'San Martin', 'San Martin', 0
    UNION ALL
    SELECT '220914', 'Shapaja', 'San Martin', 'San Martin', 0
    UNION ALL
    SELECT '221001', 'Tocache', 'Tocache', 'San Martin', 0
    UNION ALL
    SELECT '221002', 'Nuevo Progreso', 'Tocache', 'San Martin', 0
    UNION ALL
    SELECT '221003', 'Polvora', 'Tocache', 'San Martin', 0
    UNION ALL
    SELECT '221004', 'Shunte', 'Tocache', 'San Martin', 0
    UNION ALL
    SELECT '221005', 'Uchiza', 'Tocache', 'San Martin', 0
    UNION ALL
    SELECT '230101', 'Tacna', 'Tacna', 'Tacna', 0
    UNION ALL
    SELECT '230102', 'Alto de La Alianza', 'Tacna', 'Tacna', 0
    UNION ALL
    SELECT '230103', 'Calana', 'Tacna', 'Tacna', 0
    UNION ALL
    SELECT '230104', 'Ciudad Nueva', 'Tacna', 'Tacna', 0
    UNION ALL
    SELECT '230105', 'Inclan', 'Tacna', 'Tacna', 0
    UNION ALL
    SELECT '230106', 'Pachia', 'Tacna', 'Tacna', 0
    UNION ALL
    SELECT '230107', 'Palca', 'Tacna', 'Tacna', 0
    UNION ALL
    SELECT '230108', 'Pocollay', 'Tacna', 'Tacna', 0
    UNION ALL
    SELECT '230109', 'Sama', 'Tacna', 'Tacna', 0
    UNION ALL
    SELECT '230110', 'Coronel Gregorio Albarracin Lanchipa', 'Tacna', 'Tacna', 0
    UNION ALL
    SELECT '230111', 'La Yarada-Los Palos', 'Tacna', 'Tacna', 0
    UNION ALL
    SELECT '230201', 'Candarave', 'Candarave', 'Tacna', 0
    UNION ALL
    SELECT '230202', 'Cairani', 'Candarave', 'Tacna', 0
    UNION ALL
    SELECT '230203', 'Camilaca', 'Candarave', 'Tacna', 0
    UNION ALL
    SELECT '230204', 'Curibaya', 'Candarave', 'Tacna', 0
    UNION ALL
    SELECT '230205', 'Huanuara', 'Candarave', 'Tacna', 0
    UNION ALL
    SELECT '230206', 'Quilahuani', 'Candarave', 'Tacna', 0
    UNION ALL
    SELECT '230301', 'Locumba', 'Jorge Basadre', 'Tacna', 0
    UNION ALL
    SELECT '230302', 'Ilabaya', 'Jorge Basadre', 'Tacna', 0
    UNION ALL
    SELECT '230303', 'Ite', 'Jorge Basadre', 'Tacna', 0
    UNION ALL
    SELECT '230401', 'Tarata', 'Tarata', 'Tacna', 0
    UNION ALL
    SELECT '230402', 'Heroes Albarracin', 'Tarata', 'Tacna', 0
    UNION ALL
    SELECT '230403', 'Estique', 'Tarata', 'Tacna', 0
    UNION ALL
    SELECT '230404', 'Estique-Pampa', 'Tarata', 'Tacna', 0
    UNION ALL
    SELECT '230405', 'Sitajara', 'Tarata', 'Tacna', 0
    UNION ALL
    SELECT '230406', 'Susapaya', 'Tarata', 'Tacna', 0
    UNION ALL
    SELECT '230407', 'Tarucachi', 'Tarata', 'Tacna', 0
    UNION ALL
    SELECT '230408', 'Ticaco', 'Tarata', 'Tacna', 0
    UNION ALL
    SELECT '240101', 'Tumbes', 'Tumbes', 'Tumbes', 0
    UNION ALL
    SELECT '240102', 'Corrales', 'Tumbes', 'Tumbes', 0
    UNION ALL
    SELECT '240103', 'La Cruz', 'Tumbes', 'Tumbes', 0
    UNION ALL
    SELECT '240104', 'Pampas de Hospital', 'Tumbes', 'Tumbes', 0
    UNION ALL
    SELECT '240105', 'San Jacinto', 'Tumbes', 'Tumbes', 0
    UNION ALL
    SELECT '240106', 'San Juan de La Virgen', 'Tumbes', 'Tumbes', 0
    UNION ALL
    SELECT '240201', 'Zorritos', 'Contralmirante Villa', 'Tumbes', 0
    UNION ALL
    SELECT '240202', 'Casitas', 'Contralmirante Villa', 'Tumbes', 0
    UNION ALL
    SELECT '240203', 'Canoas de Punta Sal', 'Contralmirante Villa', 'Tumbes', 0
    UNION ALL
    SELECT '240301', 'Zarumilla', 'Zarumilla', 'Tumbes', 0
    UNION ALL
    SELECT '240302', 'Aguas Verdes', 'Zarumilla', 'Tumbes', 0
    UNION ALL
    SELECT '240303', 'Matapalo', 'Zarumilla', 'Tumbes', 0
    UNION ALL
    SELECT '240304', 'Papayal', 'Zarumilla', 'Tumbes', 0
    UNION ALL
    SELECT '250101', 'Calleria', 'Coronel Portillo', 'Ucayali', 0
    UNION ALL
    SELECT '250102', 'Campoverde', 'Coronel Portillo', 'Ucayali', 0
    UNION ALL
    SELECT '250103', 'Iparia', 'Coronel Portillo', 'Ucayali', 0
    UNION ALL
    SELECT '250104', 'Masisea', 'Coronel Portillo', 'Ucayali', 0
    UNION ALL
    SELECT '250105', 'Yarinacocha', 'Coronel Portillo', 'Ucayali', 0
    UNION ALL
    SELECT '250106', 'Nueva Requena', 'Coronel Portillo', 'Ucayali', 0
    UNION ALL
    SELECT '250107', 'Manantay', 'Coronel Portillo', 'Ucayali', 0
    UNION ALL
    SELECT '250201', 'Raymondi', 'Atalaya', 'Ucayali', 0
    UNION ALL
    SELECT '250202', 'Sepahua', 'Atalaya', 'Ucayali', 0
    UNION ALL
    SELECT '250203', 'Tahuania', 'Atalaya', 'Ucayali', 0
    UNION ALL
    SELECT '250204', 'Yurua', 'Atalaya', 'Ucayali', 0
    UNION ALL
    SELECT '250301', 'Padre Abad', 'Padre Abad', 'Ucayali', 0
    UNION ALL
    SELECT '250302', 'Irazola', 'Padre Abad', 'Ucayali', 0
    UNION ALL
    SELECT '250303', 'Curimana', 'Padre Abad', 'Ucayali', 0
    UNION ALL
    SELECT '250304', 'Neshuya', 'Padre Abad', 'Ucayali', 0
    UNION ALL
    SELECT '250305', 'Alexander von Humboldt', 'Padre Abad', 'Ucayali', 0
    UNION ALL
    SELECT '250401', 'Purus', 'Purus', 'Ucayali', 0
) AS datos(ubigeo, distrito, provincia, departamento, flag_cobertura)
WHERE NOT EXISTS (SELECT 1 FROM ubigeo_inei LIMIT 1);
