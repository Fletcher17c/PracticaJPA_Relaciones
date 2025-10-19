package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AbstractDaoTest {

    private TestEntityDao testEntityDao;
    private EntityManager entityManager;
    private EntityTransaction transaction;

    @BeforeEach
    void setUp() {
        entityManager = Mockito.mock(EntityManager.class);
        transaction = Mockito.mock(EntityTransaction.class);
        when(entityManager.getTransaction()).thenReturn(transaction);

        testEntityDao = new TestEntityDao();
        testEntityDao.setEntityManager(entityManager);
    }

    @Test
    void testGet() {
        TestEntity entity = new TestEntity(1L, "Test");
        when(entityManager.find(TestEntity.class, 1L)).thenReturn(entity);

        Optional<TestEntity> result = testEntityDao.get(1L);

        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
        verify(entityManager, times(1)).find(TestEntity.class, 1L);
    }

    @Test
    void testGetAll() {
        List<TestEntity> entities = List.of(new TestEntity(1L, "Test1"), new TestEntity(2L, "Test2"));
        when(entityManager.getCriteriaBuilder()).thenReturn(mock(jakarta.persistence.criteria.CriteriaBuilder.class));
        // Mockeando geCriteriaQuery y TypedQuery para aislar el test del entorno de JPA
    }

    @Test
    void testSave() {
        TestEntity entity = new TestEntity(1L, "Test");

        testEntityDao.save(entity);

        verify(transaction, times(1)).begin();
        verify(entityManager, times(1)).persist(entity);
        verify(transaction, times(1)).commit();
    }

    @Test
    void testUpdate() {
        TestEntity entity = new TestEntity(1L, "Updated");

        testEntityDao.update(entity, new String[]{});

        verify(transaction, times(1)).begin();
        verify(entityManager, times(1)).merge(entity);
        verify(transaction, times(1)).commit();
    }

    @Test
    void testDelete() {
        TestEntity entity = new TestEntity(1L, "Test");
        when(entityManager.contains(entity)).thenReturn(true);

        testEntityDao.delete(entity);

        verify(transaction, times(1)).begin();
        verify(entityManager, times(1)).remove(entity);
        verify(transaction, times(1)).commit();
    }

    // heredando la clase AbstractDao para probar las entidades
    static class TestEntityDao extends AbstractDao<TestEntity> {
    }


    //clase para hacer las pruebas
    static class TestEntity {
        private Long id;
        private String name;

        public TestEntity(Long id, String name) {
            this.id = id;
            this.name = name;
        }

    }
}