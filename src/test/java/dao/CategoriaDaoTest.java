package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import models.Categoria;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;


public class CategoriaDaoTest {
    private EntityManager entityManager;
    private CategoriaDao categoriaDao;

    @BeforeEach
    public void setUp() {
        entityManager = Mockito.mock(EntityManager.class);
        categoriaDao = new CategoriaDao(entityManager);
    }

    @Test
    void testFinByIdWithLibros(){
        //comportamiento simulado del em y de typedquery
        TypedQuery<Categoria> query = Mockito.mock(TypedQuery.class);
        Categoria categoria = new Categoria();
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Categoria.class))).thenReturn(query);
        Mockito.when(query.setParameter(Mockito.anyString(), Mockito.eq(Categoria.class))).thenReturn(query);
        Mockito.when(query.getSingleResult()).thenReturn(categoria);

        // Llamar al metodo a probar
        Optional<Categoria> resultado = categoriaDao.findByIdWithLibros(1L);
        assertTrue(resultado.isPresent());
        Mockito.verify(entityManager).createQuery(anyString(), eq(Categoria.class));
        Mockito.verify(query).setParameter("id", 1L);
        Mockito.verify(query).getSingleResult();
    }


    @Test
    void testFindByNombre(){
        TypedQuery<Categoria> query = Mockito.mock(TypedQuery.class);
        Categoria categoria = new Categoria();
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Categoria.class))).thenReturn(query);
        Mockito.when(query.setParameter(Mockito.anyString(), Mockito.eq(Categoria.class))).thenReturn(query);
        Mockito.when(query.getSingleResult()).thenReturn(categoria);

        Optional<Categoria> resultado = categoriaDao.findByNombre("Ficcion");
        assertTrue(resultado.isPresent());
        Mockito.verify(entityManager).createQuery(anyString(), eq(Categoria.class));
        Mockito.verify(query).setParameter("nombre", "Ficcion");
        Mockito.verify(query).getSingleResult();


    }


    @Test
    void testfindAllWithLibros(){
        TypedQuery<Categoria> query = Mockito.mock(TypedQuery.class);
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(Categoria.class))).thenReturn(query);
        Mockito.when(query.setParameter(Mockito.anyString(), Mockito.eq(Categoria.class))).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(null);

        Optional<List<Categoria>> resultado = Optional.ofNullable(categoriaDao.findAllWithLibros());
        try{
            if(resultado.isPresent()){
                assertEquals(0, resultado.get().size());
            }
        } catch (AssertionError e){
            fail("No hay libros o hubo un error");
        }
        Mockito.verify(entityManager).createQuery(anyString(), eq(Categoria.class));
        Mockito.verify(query).getResultList();

    }

}
