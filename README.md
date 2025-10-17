# Sistema de Biblioteca JPA

Sistema de gestión bibliográfica que modela las relaciones entre autores, libros y categorías usando JPA.

## Entidades

- **Autor**: id, nombre, nacionalidad, fecha de nacimiento
- **Libro**: id, título, año de publicación, autor
- **Categoria**: id, nombre

## Relaciones

- **Autor-Libro**: OneToMany/ManyToOne (un autor, muchos libros)
- **Libro-Categoria**: ManyToMany (libros con múltiples categorías)

## Funcionalidades

- Inserta autores, libros y categorías
- Establece relaciones entre entidades
- Consulta libros con su autor y categorías