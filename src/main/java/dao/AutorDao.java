package dao;

import models.Autor;
import models.Libro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class AutorDao extends AbstractDao<Autor> {

    public AutorDao(EntityManager entityManager) {
        setEntityManager(entityManager);
    }

    public Optional<Autor> findByIdWithLibros(Long id) {
        try {
            String jpql = "SELECT a FROM Autor a LEFT JOIN FETCH a.libros WHERE a.id = :id";
            TypedQuery<Autor> query = getEntityManager().createQuery(jpql, Autor.class);
            query.setParameter("id", id);
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
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
        em.getTransaction().begin();
        try {
            Autor autor = em.find(Autor.class, autorId);
            if (autor != null) {
                autor.agregarLibro(libro);
                em.merge(autor);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al agregar libro al autor", e);
        }
    }
}