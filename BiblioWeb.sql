-- BiblioWeb - Segundo Corte 2026
-- Desarrollo de Aplicaciones Empresariales - UTS
-- Estudiante: Martin Enrique Bernal Gomez
-- Profesor: Carlos Adolfo Beltran Castro
--
-- Credenciales academicas de demostracion:
-- Bibliotecario: admin@biblioweb.test / Admin123
-- Lector:        lector@biblioweb.test / Lector123
-- Las claves se almacenan con BCrypt; no se guarda texto plano en la tabla.

DROP DATABASE IF EXISTS biblioweb;
CREATE DATABASE biblioweb
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

USE biblioweb;

CREATE TABLE categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL UNIQUE,
    descripcion VARCHAR(255) NULL
) ENGINE=InnoDB;

CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(80) NOT NULL,
    apellidos VARCHAR(80) NOT NULL,
    correo VARCHAR(120) NOT NULL UNIQUE,
    clave_hash VARCHAR(255) NOT NULL,
    rol ENUM('BIBLIOTECARIO', 'LECTOR') NOT NULL DEFAULT 'LECTOR',
    estado ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO'
) ENGINE=InnoDB;

CREATE TABLE libro (
    id_libro INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(180) NOT NULL,
    autor VARCHAR(150) NOT NULL,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    anio_publicacion SMALLINT UNSIGNED NULL,
    estado ENUM('DISPONIBLE', 'PRESTADO', 'INACTIVO') NOT NULL DEFAULT 'DISPONIBLE',
    id_categoria INT NOT NULL,
    CONSTRAINT fk_libro_categoria
        FOREIGN KEY (id_categoria)
        REFERENCES categoria(id_categoria)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE prestamo (
    id_prestamo INT AUTO_INCREMENT PRIMARY KEY,
    fecha_prestamo DATE NOT NULL,
    fecha_limite DATE NOT NULL,
    fecha_devolucion DATE NULL,
    estado ENUM('ACTIVO', 'DEVUELTO') NOT NULL DEFAULT 'ACTIVO',
    id_usuario INT NOT NULL,
    id_libro INT NOT NULL,
    CONSTRAINT fk_prestamo_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_prestamo_libro
        FOREIGN KEY (id_libro)
        REFERENCES libro(id_libro)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT chk_fechas_prestamo
        CHECK (fecha_limite >= fecha_prestamo),
    CONSTRAINT chk_fecha_devolucion
        CHECK (fecha_devolucion IS NULL OR fecha_devolucion >= fecha_prestamo)
) ENGINE=InnoDB;

CREATE INDEX idx_libro_categoria ON libro(id_categoria);
CREATE INDEX idx_libro_estado ON libro(estado);
CREATE INDEX idx_prestamo_usuario ON prestamo(id_usuario);
CREATE INDEX idx_prestamo_libro ON prestamo(id_libro);
CREATE INDEX idx_prestamo_estado ON prestamo(estado);

INSERT INTO categoria (nombre, descripcion) VALUES
('Literatura', 'Novelas, cuentos y obras literarias.'),
('Tecnologia', 'Libros relacionados con informatica y tecnologia.'),
('Ciencias', 'Material de ciencias naturales y exactas.'),
('Historia', 'Obras de historia general y regional.');

-- Hashes BCrypt de demostracion. Prefijo 2a compatible con BCryptPasswordEncoder.
INSERT INTO usuario (nombres, apellidos, correo, clave_hash, rol, estado) VALUES
('Laura', 'Mendoza', 'admin@biblioweb.test', '$2a$10$KpILfIdb4PoV3sxbBZdUjO73B7fthwbdqBVG1XwjNwgTbedigAudm', 'BIBLIOTECARIO', 'ACTIVO'),
('Daniel', 'Rojas', 'lector@biblioweb.test', '$2a$10$jo4UP67cmmRavpKoKTUKtuUaqzCRd63u6lkrudsJBofn21Yt8Wyam', 'LECTOR', 'ACTIVO'),
('Camila', 'Torres', 'camila.torres@biblioweb.test', '$2a$10$jo4UP67cmmRavpKoKTUKtuUaqzCRd63u6lkrudsJBofn21Yt8Wyam', 'LECTOR', 'ACTIVO'),
('Andres', 'Lopez', 'andres.lopez@biblioweb.test', '$2a$10$jo4UP67cmmRavpKoKTUKtuUaqzCRd63u6lkrudsJBofn21Yt8Wyam', 'LECTOR', 'ACTIVO');

INSERT INTO libro (titulo, autor, isbn, anio_publicacion, estado, id_categoria) VALUES
('Cien anos de soledad', 'Gabriel Garcia Marquez', '9780307474728', 1967, 'DISPONIBLE', 1),
('El principito', 'Antoine de Saint-Exupery', '9780156012195', 1943, 'DISPONIBLE', 1),
('Clean Code', 'Robert C. Martin', '9780132350884', 2008, 'PRESTADO', 2),
('Introduction to Algorithms', 'Thomas H. Cormen', '9780262046305', 2022, 'DISPONIBLE', 2),
('Breve historia del tiempo', 'Stephen Hawking', '9780553380163', 1988, 'DISPONIBLE', 3),
('Sapiens', 'Yuval Noah Harari', '9780062316097', 2015, 'DISPONIBLE', 4);

INSERT INTO prestamo
(fecha_prestamo, fecha_limite, fecha_devolucion, estado, id_usuario, id_libro) VALUES
('2026-08-12', '2026-08-26', '2026-08-22', 'DEVUELTO', 2, 1),
('2026-10-10', '2026-10-24', NULL, 'ACTIVO', 3, 3),
('2026-08-05', '2026-08-19', '2026-08-17', 'DEVUELTO', 4, 5);

-- Verificaciones rapidas
SELECT * FROM categoria;
SELECT id_usuario, nombres, apellidos, correo, rol, estado FROM usuario;
SELECT * FROM libro;
SELECT * FROM prestamo;
