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

## Dependencias y compiler target
- ```JPA```(Jakarta Persistence API) para la gestión de entidades
- ```Hibernate``` (implementación JPA) como proveedor JPA
- ```jakarta.persistence-api``` para las anotaciones JPA
- ```Driver de postgresql``` para la conexión a la base de datos
- ```JUnit y Mockito``` para pruebas unitarias
- ```Lombok``` para reducir el código boilerplate
- ```Java 17``` como versión del lenguaje

## Tests unitarios
Utilizamos JUnit-API Jupiter, JUnit-Engine Jupiter y Mockito para hacer pruebas unitarias que aseguren que el programa funcione de manera correcta
- ```JUnit Jupiter```, permite escribir y ejecutar pruebas con anotaciones como @Test, @BeforeEach y AfterEach, mejorando la calidad del codigo e identificando fallas.
- ```Mockito``` Permite crear objetos simulados (Mocks) que imitan el comportamiento de clases reales permitiendo aislar las pruebas y verificar las interacciones entre objetos y metodos.

![imagen de los tests completados sin errores](/assets/Tests.png)