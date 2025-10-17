package dao;

import models.Categoria;
import models.Libro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class CategoriaDao extends AbstractDao<Categoria> {

    // Fix the constructor
    public CategoriaDao(EntityManager entityManager) {
        super(entityManager); // Call super constructor
    }

    public Optional<Categoria> findByIdWithLibros(Long id) {
        try {
            String jpql = "SELECT c FROM Categoria c LEFT JOIN FETCH c.libros WHERE c.id = :id";
            TypedQuery<Categoria> query = getEntityManager().createQuery(jpql, Categoria.class);
            query.setParameter("id", id);
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            System.err.println("Error en findByIdWithLibros: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Categoria> findByNombre(String nombre) {
        try {
            String jpql = "SELECT c FROM Categoria c WHERE c.nombre = :nombre";
            TypedQuery<Categoria> query = getEntityManager().createQuery(jpql, Categoria.class);
            query.setParameter("nombre", nombre);
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            System.err.println("Error en findByNombre: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Categoria> findAllWithLibros() {
        String jpql = "SELECT DISTINCT c FROM Categoria c LEFT JOIN FETCH c.libros";
        TypedQuery<Categoria> query = getEntityManager().createQuery(jpql, Categoria.class);
        return query.getResultList();
    }
}