package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import models.Autor;
import models.Libro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AutorDaoTest {

    private EntityManager em;
    private EntityTransaction tx;
    private AutorDao autorDao;
    private TypedQuery<Autor> typedQuery;

    @BeforeEach // Este metodo se ejecuta antes de los tests para inicializar los mocks, los mock simylan comportamientos de la clase real
    void setUp() {
        em = mock(EntityManager.class);
        tx = mock(EntityTransaction.class);
        typedQuery = mock(TypedQuery.class);

        when(em.getTransaction()).thenReturn(tx);

        autorDao = new AutorDao(em);
    }

    // Basicamente en los tests lo que se hace es usar el evento when().thenReturn() y lo que hace es regresar nustro mock para que no interactuemos como tal con la base de datos
    @Test
    void testFindByIdWithLibros() {
        Autor autor = Autor.builder().nombre("Gabriel García Márquez").build();
        autor.setId(1L);

        when(em.createQuery(anyString(), eq(Autor.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("id"), eq(1L))).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(autor);

        Optional<Autor> resultado = autorDao.findByIdWithLibros(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Gabriel García Márquez", resultado.get().getNombre());
    }

    @Test
    void testFindByIdWithLibrosNoExiste() {
        when(em.createQuery(anyString(), eq(Autor.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("id"), eq(1L))).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenThrow(new RuntimeException());

        Optional<Autor> resultado = autorDao.findByIdWithLibros(1L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void testAgregarLibroAlAutor() {
        Autor autor = Autor.builder().nombre("Gabriel García Márquez").build();
        autor.setId(1L);
        Libro libro = Libro.builder().titulo("Cien años de soledad").build();

        when(em.find(Autor.class, 1L)).thenReturn(autor);

        autorDao.agregarLibroAlAutor(1L, libro);

        verify(em).merge(autor);
        assertTrue(autor.getLibros().contains(libro));
        verify(tx).begin();
        verify(tx).commit();
    }

    @Test
    void testAgregarLibroAlAutorConError() {
        Autor autor = Autor.builder().nombre("Gabriel García Márquez").build();
        autor.setId(1L);
        Libro libro = Libro.builder().titulo("Cien años de soledad").build();

        when(em.find(Autor.class, 1L)).thenThrow(new RuntimeException());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                autorDao.agregarLibroAlAutor(1L, libro)
        );

        assertEquals("Error al agregar libro al autor", exception.getMessage());
        verify(tx).rollback();
    }

    @Test
    void testFindByNacionalidad() {
        Autor autor1 = Autor.builder().nombre("Autor1").build();
        Autor autor2 = Autor.builder().nombre("Autor2").build();
        List<Autor> autores = Arrays.asList(autor1, autor2);

        when(em.createQuery(anyString(), eq(Autor.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("nacionalidad"), eq("España"))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(autores);

        List<Autor> resultado = autorDao.findByNacionalidad("España");

        assertEquals(2, resultado.size());
    }

    @Test
    void testFindAutoresConMasDeUnLibro() {
        Autor autor1 = Autor.builder().nombre("Autor1").build();
        Autor autor2 = Autor.builder().nombre("Autor2").build();
        List<Autor> autores = Arrays.asList(autor1, autor2);

        when(em.createQuery(anyString(), eq(Autor.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(autores);

        List<Autor> resultado = autorDao.findAutoresConMasDeUnLibro();

        assertEquals(2, resultado.size());
    }
}
