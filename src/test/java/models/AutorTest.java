package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class AutorTest {

    @Test
    void testBuilderAutor() {
        Autor autor = Autor.builder()
                .id(1L)
                .nombre("Gabriel García Márquez")
                .nacionalidad("Colombiana")
                .fechaNacimiento(LocalDate.of(1927, 3, 6))
                .build();

        assertEquals("Gabriel García Márquez", autor.getNombre());
        assertEquals("Colombiana", autor.getNacionalidad());
        assertEquals(LocalDate.of(1927, 3, 6), autor.getFechaNacimiento());
    }

    @Test
    void testSettersYGetters() {
        Autor autor = new Autor();
        autor.setNombre("Isabel Allende");
        autor.setNacionalidad("Chilena");
        autor.setFechaNacimiento(LocalDate.of(1942, 8, 2));

        assertEquals("Isabel Allende", autor.getNombre());
        assertEquals("Chilena", autor.getNacionalidad());
        assertEquals(LocalDate.of(1942, 8, 2), autor.getFechaNacimiento());
    }

    @Test
    void testLibrosInicializados() {
        Autor autor = new Autor();
        assertNotNull(autor.getLibros(), "La lista de libros no debe ser null");
        assertTrue(autor.getLibros().isEmpty(), "La lista de libros debe iniciar vacía");
    }

    @Test
    void testAgregarLibro() {
        Autor autor = new Autor();
        Libro libro = Libro.builder().titulo("Cien años de soledad").build();

        autor.agregarLibro(libro);

        assertTrue(autor.getLibros().contains(libro),
                "El autor debe contener el libro agregado");
        assertEquals(autor, libro.getAutor(),
                "El libro debe tener asignado al autor correctamente");
    }

    @Test
    void testRemoverLibro() {
        Autor autor = new Autor();
        Libro libro = Libro.builder().titulo("El amor en los tiempos del cólera").build();

        autor.agregarLibro(libro);
        autor.removerLibro(libro);

        assertFalse(autor.getLibros().contains(libro),
                "El libro debe ser removido de la lista del autor");
        assertNull(libro.getAutor(),
                "El libro ya no debe tener autor asignado");
    }

    @Test
    void testAgregarVariosLibros() {
        Autor autor = new Autor();

        Libro libro1 = Libro.builder().titulo("Cien años de soledad").build();
        Libro libro2 = Libro.builder().titulo("Crónica de una muerte anunciada").build();

        autor.agregarLibro(libro1);
        autor.agregarLibro(libro2);

        assertEquals(2, autor.getLibros().size(), "El autor debe tener 2 libros");
        assertTrue(autor.getLibros().contains(libro1));
        assertTrue(autor.getLibros().contains(libro2));
    }
}
