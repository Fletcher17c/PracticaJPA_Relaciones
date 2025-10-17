package dao;

import jakarta.persistence.EntityTransaction;
import models.Autor;
import models.Libro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class AutorDao extends AbstractDao<Autor> {

    // Fix the constructor to call super
    public AutorDao(EntityManager entityManager) {
        super(entityManager);
    }

    public Optional<Autor> findByIdWithLibros(Long id) {
        try {
            String jpql = "SELECT a FROM Autor a LEFT JOIN FETCH a.libros WHERE a.id = :id";
            TypedQuery<Autor> query = getEntityManager().createQuery(jpql, Autor.class);
            query.setParameter("id", id);
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            System.err.println("Error en findByIdWithLibros: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Autor> findByNacionalidad(String nacionalidad) {
        String jpql = "SELECT a FROM Autor a WHERE a.nacionalidad = :nacionalidad";
        TypedQuery<Autor> query = getEntityManager().createQuery(jpql, Autor.class);
        query.setParameter("nacionalidad", nacionalidad);
        return query.getResultList();
    }

    public void agregarLibroAlAutor(Long autorId, Libro libro) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Autor autor = em.find(Autor.class, autorId);
            if (autor != null) {
                autor.agregarLibro(libro);
                em.merge(autor);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al agregar libro al autor: " + e.getMessage(), e);
        }
    }

    public List<Autor> findAutoresConMasDeUnLibro() {
        String jpql = "SELECT a FROM Autor a WHERE SIZE(a.libros) > 1";
        TypedQuery<Autor> query = getEntityManager().createQuery(jpql, Autor.class);
        return query.getResultList();
    }
}