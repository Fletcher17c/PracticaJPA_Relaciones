package dao;
import jakarta.persistence.Entity;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import models.Categoria;
import models.Autor;
import models.Libro;


public class LibroDaoTest {
    private EntityManager em;
    private EntityTransaction tx;
    private LibroDao libroDao;
    private TypedQuery<Libro> typedQuery;
    private TypedQuery<Long> countQuery;

    @BeforeEach
    public void setUp() {
        em = mock(EntityManager.class);
        tx = mock(EntityTransaction.class);
        typedQuery = mock(TypedQuery.class);
        countQuery = mock(TypedQuery.class);

        when(em.getTransaction()).thenReturn(tx);

        libroDao = new LibroDao(em);
    }

    @Test
    void testFindByIdWithAutorAndCategorias() {
        Libro libro = Libro.builder().titulo("Cien años de soledad").build();
        libro.setId(1L);
        libro.setAutor(new Autor());
        libro.setCategorias(Arrays.asList(new Categoria()));

        when(em.createQuery(anyString(), eq(Libro.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("id"), eq(1L))).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(libro);

        Optional<Libro> resultado = libroDao.findByIdWithAutorAndCategorias(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Cien años de soledad", resultado.get().getTitulo());
        assertNotNull(resultado.get().getAutor());
        assertEquals(1, resultado.get().getCategorias().size());
    }

    @Test
    void testFindByIdWithAutorAndCategoriasNoExiste() {
        when(em.createQuery(anyString(), eq(Libro.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("id"), eq(1L))).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenThrow(new RuntimeException());

        Optional<Libro> resultado = libroDao.findByIdWithAutorAndCategorias(1L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void testFindByTitulo() {
        Libro libro = Libro.builder().titulo("Cien años de soledad").build();
        List<Libro> libros = Arrays.asList(libro);

        when(em.createQuery(anyString(), eq(Libro.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("titulo"), anyString())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(libros);

        List<Libro> resultado = libroDao.findByTitulo("Cien");

        assertEquals(1, resultado.size());
        assertEquals("Cien años de soledad", resultado.get(0).getTitulo());
    }

    @Test
    void testFindByAnioPublicacion() {
        Libro libro = Libro.builder().titulo("Libro 1").build();
        List<Libro> libros = Arrays.asList(libro);

        when(em.createQuery(anyString(), eq(Libro.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("anio"), eq(2020))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(libros);

        List<Libro> resultado = libroDao.findByAnioPublicacion(2020);

        assertEquals(1, resultado.size());
    }

    @Test
    void testFindByAutor() {
        Libro libro = Libro.builder().titulo("Libro 1").build();
        List<Libro> libros = Arrays.asList(libro);

        when(em.createQuery(anyString(), eq(Libro.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("autorId"), eq(1L))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(libros);
        List<Libro> resultado = libroDao.findByAutor(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void testAgregarCategoriaALibro() {
        Libro libro = Libro.builder().titulo("Libro 1").build();
        libro.setId(1L);
        Categoria categoria = Categoria.builder().nombre("Novela").build();

        when(em.find(Libro.class, 1L)).thenReturn(libro);

        libroDao.agregarCategoriaALibro(1L, categoria);

        verify(em).merge(libro);
        verify(tx).begin();
        verify(tx).commit();
        assertTrue(libro.getCategorias().contains(categoria));
    }
    @Test
    void testAgregarCategoriaALibroConError() {
        Categoria categoria = Categoria.builder().nombre("Novela").build();
        when(em.find(Libro.class, 1L)).thenThrow(new RuntimeException());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                libroDao.agregarCategoriaALibro(1L, categoria)
        );

        assertEquals("Error al agregar categoría al libro", exception.getMessage());
        verify(tx).rollback();
    }
    @Test
    void testFindByCategoria() {
        Libro libro = Libro.builder().titulo("Libro1").build();
        List<Libro> libros = Arrays.asList(libro);

        when(em.createQuery(anyString(), eq(Libro.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("categoriaId"), eq(1L))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(libros);

        List<Libro> resultado = libroDao.findByCategoria(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void testCountLibrosPorAutor() {
        when(em.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.setParameter(eq("autorId"), eq(1L))).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(5L);

        long total = libroDao.countLibrosPorAutor(1L);

        assertEquals(5L, total);
    }
}

