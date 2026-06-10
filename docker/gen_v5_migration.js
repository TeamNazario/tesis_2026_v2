/**
 * Generates V5 Flyway migration from insert_ubigeo_inei.sql
 * Makes it idempotent: CREATE TABLE IF NOT EXISTS + INSERT only if table is empty
 */
const fs = require('fs');

const inputFile = 'docker/mysql/insert_ubigeo_inei.sql';
const outputFile = 'src/main/resources/db/migration/V5__create_ubigeo_inei.sql';

let content = fs.readFileSync(inputFile, 'utf8');

// Extract the INSERT VALUES block
const insertMatch = content.match(/INSERT INTO UBIGEO_INEI[\s\S]+?COMMIT;/i);
if (!insertMatch) {
    console.error('Could not find INSERT block');
    process.exit(1);
}

// Extract just the VALUES part
const valuesMatch = content.match(/\) VALUES\s*([\s\S]+?);[\s\S]*?COMMIT/i);
if (!valuesMatch) {
    console.error('Could not find VALUES block');
    process.exit(1);
}

const values = valuesMatch[1].trim();

const migration = `-- V5: Tabla Ubigeo INEI con datos del INEI Peru (1874 registros)
-- Idempotente: usa IF NOT EXISTS y solo inserta si la tabla esta vacia

CREATE TABLE IF NOT EXISTS ubigeo_inei (
    id_ubigeo_inei INT AUTO_INCREMENT PRIMARY KEY,
    ubigeo         VARCHAR(6)   NOT NULL,
    distrito       VARCHAR(150) NOT NULL,
    provincia      VARCHAR(150) NOT NULL,
    departamento   VARCHAR(150) NOT NULL,
    flag_cobertura INT          NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Indices para busquedas eficientes
CREATE INDEX IF NOT EXISTS idx_ubigeo_codigo ON ubigeo_inei (ubigeo);
CREATE INDEX IF NOT EXISTS idx_ubigeo_departamento ON ubigeo_inei (departamento);
CREATE INDEX IF NOT EXISTS idx_ubigeo_provincia ON ubigeo_inei (provincia);

-- Inserta datos solo si la tabla esta vacia (evita duplicados en deployments Docker)
INSERT INTO ubigeo_inei (ubigeo, distrito, provincia, departamento, flag_cobertura)
SELECT t.ubigeo, t.distrito, t.provincia, t.departamento, t.flag_cobertura
FROM (
    SELECT ubigeo, distrito, provincia, departamento, flag_cobertura FROM (VALUES
${values
    .replace(/\(\s*'/g, "    ROW(")
    .replace(/'\s*,\s*'/g, "', '")
    .replace(/'\s*\)/g, "')")
}
    ) AS v(ubigeo, distrito, provincia, departamento, flag_cobertura)
) AS t
WHERE NOT EXISTS (SELECT 1 FROM ubigeo_inei LIMIT 1);
`;

// Actually, MySQL doesn't support VALUES() clause like that in FROM.
// Use a different approach: INSERT INTO ... SELECT with subquery using UNION ALL

// Re-parse the values rows
const rowRegex = /\('([^']*)',\s*'([^']*)',\s*'([^']*)',\s*'([^']*)',\s*(\d+)\)/g;
const rows = [];
let match;
while ((match = rowRegex.exec(values)) !== null) {
    rows.push(`    SELECT '${match[1]}', '${match[2]}', '${match[3]}', '${match[4]}', ${match[5]}`);
}

const finalMigration = `-- V5: Tabla Ubigeo INEI con datos del INEI Peru (${rows.length} registros)
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
${rows.join('\n    UNION ALL\n')}
) AS datos(ubigeo, distrito, provincia, departamento, flag_cobertura)
WHERE NOT EXISTS (SELECT 1 FROM ubigeo_inei LIMIT 1);
`;

fs.writeFileSync(outputFile, finalMigration, 'utf8');
console.log(`Generated ${outputFile} with ${rows.length} rows`);
