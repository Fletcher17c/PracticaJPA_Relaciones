package dao;

import jakarta.persistence.EntityTransaction;
import models.Libro;
import models.Autor;
import models.Categoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class LibroDao extends AbstractDao<Libro> {

    // Fix the constructor
    public LibroDao(EntityManager entityManager) {
        super(entityManager); // Call super constructor
    }

    public Optional<Libro> findByIdWithAutorAndCategorias(Long id) {
        try {
            String jpql = "SELECT l FROM Libro l " +
                    "LEFT JOIN FETCH l.autor " +
                    "LEFT JOIN FETCH l.categorias " +
                    "WHERE l.id = :id";
            TypedQuery<Libro> query = getEntityManager().createQuery(jpql, Libro.class);
            query.setParameter("id", id);
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            System.err.println("Error en findByIdWithAutorAndCategorias: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Libro> findAllWithAutorAndCategorias() {
        String jpql = "SELECT DISTINCT l FROM Libro l " +
                "LEFT JOIN FETCH l.autor " +
                "LEFT JOIN FETCH l.categorias";
        TypedQuery<Libro> query = getEntityManager().createQuery(jpql, Libro.class);
        return query.getResultList();
    }

    public List<Libro> findByTitulo(String titulo) {
        String jpql = "SELECT l FROM Libro l WHERE l.titulo LIKE :titulo";
        TypedQuery<Libro> query = getEntityManager().createQuery(jpql, Libro.class);
        query.setParameter("titulo", "%" + titulo + "%");
        return query.getResultList();
    }

    public List<Libro> findByAnioPublicacion(int anio) {
        String jpql = "SELECT l FROM Libro l WHERE l.anioPublicacion = :anio";
        TypedQuery<Libro> query = getEntityManager().createQuery(jpql, Libro.class);
        query.setParameter("anio", anio);
        return query.getResultList();
    }

    public List<Libro> findByAutor(Long autorId) {
        String jpql = "SELECT l FROM Libro l WHERE l.autor.id = :autorId";
        TypedQuery<Libro> query = getEntityManager().createQuery(jpql, Libro.class);
        query.setParameter("autorId", autorId);
        return query.getResultList();
    }

    public void agregarCategoriaALibro(Long libroId, Categoria categoria) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Libro libro = em.find(Libro.class, libroId);
            if (libro != null) {
                libro.agregarCategoria(categoria);
                em.merge(libro);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al agregar categoría al libro: " + e.getMessage(), e);
        }
    }
}