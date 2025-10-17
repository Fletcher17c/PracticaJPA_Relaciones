package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<T> implements IDao<T> {

    private final Class<T> entityClass;
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public AbstractDao() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    // Add this constructor that accepts EntityManager
    public AbstractDao(EntityManager entityManager) {
        this();
        this.entityManager = entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    protected EntityManager getEntityManager() {
        if (entityManager == null) {
            throw new IllegalStateException("EntityManager no ha sido inicializado. ¿Olvidó llamar a setEntityManager()?");
        }
        return entityManager;
    }

    @Override
    public Optional<T> get(long id) {
        return Optional.ofNullable(getEntityManager().find(entityClass, id));
    }

    @Override
    public List<T> getAll() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        TypedQuery<T> query = getEntityManager().createQuery(cq);
        return query.getResultList();
    }

    @Override
    public void save(T t) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(t);
            em.flush(); // Force immediate insert
            tx.commit();
            System.out.println("DEBUG: Saved entity: " + t);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al guardar entidad: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(T t, String[] params) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T merged = em.merge(t);
            em.flush();
            tx.commit();
            System.out.println("DEBUG: Updated entity: " + merged);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al actualizar entidad: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(T t) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.contains(t) ? t : em.merge(t));
            em.flush();
            tx.commit();
            System.out.println("DEBUG: Deleted entity: " + t);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar entidad: " + e.getMessage(), e);
        }
    }
}