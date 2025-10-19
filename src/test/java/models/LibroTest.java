package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LibroTest {

    @Test
    void testCategoriasInicializadas() {
        Libro libro = new Libro();
        assertNotNull(libro.getCategorias(), "La lista de categorías debe inicializarse automáticamente");
        assertTrue(libro.getCategorias().isEmpty(), "La lista debe iniciar vacía");
    }

    @Test
    void testAgregarCategoria() {
        // Arrange
        Libro libro = new Libro();
        Categoria categoria = new Categoria();
        categoria.setNombre("Novela");

        // Act
        libro.agregarCategoria(categoria);

        // Assert
        assertTrue(libro.getCategorias().contains(categoria),
                "El libro debe contener la categoría agregada");
        assertTrue(categoria.getLibros().contains(libro),
                "La categoría debe contener el libro agregado");
    }

    @Test
    void testRemoverCategoria() {
        // Arrange
        Libro libro = new Libro();
        Categoria categoria = new Categoria();
        libro.agregarCategoria(categoria); // Primero agregamos

        // Act
        libro.removerCategoria(categoria);

        // Assert
        assertFalse(libro.getCategorias().contains(categoria),
                "La categoría debe ser removida de la lista del libro");
        assertFalse(categoria.getLibros().contains(libro),
                "El libro debe ser removido de la lista de la categoría");
    }

    @Test
    void testBuilderLibro() {
        Autor autor = Autor.builder()
                .id(1L)
                .nombre("Gabriel García Márquez")
                .build();

        Libro libro = Libro.builder()
                .id(10L)
                .titulo("Cien años de soledad")
                .anioPublicacion(1967)
                .autor(autor)
                .build();

        assertEquals("Cien años de soledad", libro.getTitulo());
        assertEquals(1967, libro.getAnioPublicacion());
        assertEquals("Gabriel García Márquez", libro.getAutor().getNombre());
    }

    @Test
    void testAgregarVariasCategorias() {
        Libro libro = new Libro();
        Categoria cat1 = new Categoria();
        cat1.setNombre("Novela");
        Categoria cat2 = new Categoria();
        cat2.setNombre("Ciencia");

        libro.agregarCategoria(cat1);
        libro.agregarCategoria(cat2);

        assertEquals(2, libro.getCategorias().size(), "El libro debe tener 2 categorías");
        assertTrue(cat1.getLibros().contains(libro), "Cat1 debe contener el libro");
        assertTrue(cat2.getLibros().contains(libro), "Cat2 debe contener el libro");
    }
}
