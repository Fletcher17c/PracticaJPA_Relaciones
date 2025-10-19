package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoriaTest {

    @Test
    void testNombre() {
        // Usando builder de Lombok
        Categoria categoria = Categoria.builder()
                .nombre("Novela")
                .build();

        assertEquals("Novela", categoria.getNombre(),
                "El nombre de la categoría debe ser 'Novela'");
    }

    @Test
    void testLibrosInicializacion() {
        // Verificar que la lista de libros se inicializa automáticamente
        Categoria categoria = new Categoria();

        assertNotNull(categoria.getLibros(),
                "La lista de libros no debe ser null");
        assertTrue(categoria.getLibros().isEmpty(),
                "La lista de libros debe iniciar vacía");
    }

    @Test
    void testSetNombre() {
        // Verificar setter de nombre
        Categoria categoria = new Categoria();
        categoria.setNombre("Ciencia");

        assertEquals("Ciencia", categoria.getNombre(),
                "El nombre de la categoría debe ser 'Ciencia'");
    }
}
