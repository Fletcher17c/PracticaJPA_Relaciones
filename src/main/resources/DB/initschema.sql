DROP TABLE IF EXISTS libro_categoria CASCADE;
DROP TABLE IF EXISTS libros CASCADE;
DROP TABLE IF EXISTS categorias CASCADE;
DROP TABLE IF EXISTS autores CASCADE;

CREATE TABLE autores (
                         id SERIAL PRIMARY KEY,
                         nombre VARCHAR(100) NOT NULL,
                         nacionalidad VARCHAR(50),
                         fecha_nacimiento DATE
);

CREATE TABLE categorias (
                            id SERIAL PRIMARY KEY,
                            nombre VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE libros (
                        id SERIAL PRIMARY KEY,
                        titulo VARCHAR(200) NOT NULL,
                        anio_publicacion INTEGER,
                        autor_id INTEGER,
                        CONSTRAINT fk_autor
                            FOREIGN KEY (autor_id)
                                REFERENCES autores (id)
                                ON DELETE SET NULL
);

CREATE TABLE libro_categoria (
                                 libro_id INTEGER NOT NULL,
                                 categoria_id INTEGER NOT NULL,
                                 PRIMARY KEY (libro_id, categoria_id),
                                 FOREIGN KEY (libro_id) REFERENCES libros (id) ON DELETE CASCADE,
                                 FOREIGN KEY (categoria_id) REFERENCES categorias (id) ON DELETE CASCADE
);
