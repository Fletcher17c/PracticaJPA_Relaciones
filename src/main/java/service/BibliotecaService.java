package service;

import dao.AutorDao;
import dao.LibroDao;
import dao.CategoriaDao;
import models.Autor;
import models.Libro;
import models.Categoria;
import util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class BibliotecaService {

    private EntityManager entityManager;
    private AutorDao autorDao;
    private LibroDao libroDao;
    private CategoriaDao categoriaDao;

    public BibliotecaService() {
        this.entityManager = JPAUtil.getEntityManager();
        this.autorDao = new AutorDao(entityManager);
        this.libroDao = new LibroDao(entityManager);
        this.categoriaDao = new CategoriaDao(entityManager);
    }

    public void inicializarDatos() {
        System.out.println("=== INICIALIZANDO DATOS DE LA BIBLIOTECA ===\n");

        // Limpiar datos existentes (opcional, para pruebas)
        limpiarDatos();

        // Crear categorías
        System.out.println("Creando categorías...");
        Categoria ficcion = crearCategoria("Ficción");
        Categoria cienciaFiccion = crearCategoria("Ciencia Ficción");
        Categoria fantasia = crearCategoria("Fantasía");
        Categoria novela = crearCategoria("Novela");
        Categoria terror = crearCategoria("Terror");

        // Crear autores
        System.out.println("Creando autores...");
        Autor autor1 = crearAutor("Gabriel García Márquez", "Colombiano",
                LocalDate.of(1927, 3, 6));
        Autor autor2 = crearAutor("Stephen King", "Estadounidense",
                LocalDate.of(1947, 9, 21));

        // Crear libros y asignar relaciones
        System.out.println("Creando libros y asignando relaciones...");

        // Libros de Gabriel García Márquez
        Libro libro1 = crearLibro("Cien años de soledad", 1967, autor1);
        libro1.agregarCategoria(ficcion);
        libro1.agregarCategoria(fantasia);
        libro1.agregarCategoria(novela);
        guardarLibro(libro1);

        Libro libro2 = crearLibro("El amor en los tiempos del cólera", 1985, autor1);
        libro2.agregarCategoria(ficcion);
        libro2.agregarCategoria(novela);
        guardarLibro(libro2);

        // Libros de Stephen King
        Libro libro3 = crearLibro("It", 1986, autor2);
        libro3.agregarCategoria(terror);
        libro3.agregarCategoria(ficcion);
        guardarLibro(libro3);

        Libro libro4 = crearLibro("El resplandor", 1977, autor2);
        libro4.agregarCategoria(terror);
        libro4.agregarCategoria(ficcion);
        guardarLibro(libro4);

        Libro libro5 = crearLibro("La torre oscura I: El pistolero", 1982, autor2);
        libro5.agregarCategoria(ficcion);
        libro5.agregarCategoria(fantasia);
        libro5.agregarCategoria(cienciaFiccion);
        guardarLibro(libro5);

        System.out.println("\n=== DATOS INICIALIZADOS EXITOSAMENTE ===\n");
    }

    private Categoria crearCategoria(String nombre) {
        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoriaDao.save(categoria);
        System.out.println("  - Categoría creada: " + nombre);
        return categoria;
    }

    private Autor crearAutor(String nombre, String nacionalidad, LocalDate fechaNacimiento) {
        Autor autor = new Autor();
        autor.setNombre(nombre);
        autor.setNacionalidad(nacionalidad);
        autor.setFechaNacimiento(fechaNacimiento);
        autorDao.save(autor);
        System.out.println("  - Autor creado: " + nombre + " (" + nacionalidad + ")");
        return autor;
    }

    private Libro crearLibro(String titulo, int anioPublicacion, Autor autor) {
        Libro libro = new Libro();
        libro.setTitulo(titulo);
        libro.setAnioPublicacion(anioPublicacion);
        libro.setAutor(autor);
        autor.agregarLibro(libro); // Mantener relación bidireccional
        return libro;
    }

    private void guardarLibro(Libro libro) {
        libroDao.save(libro);
        System.out.println("  - Libro creado: \"" + libro.getTitulo() +
                "\" de " + libro.getAutor().getNombre());
    }

    public void mostrarResumenDatos() {
        System.out.println("=== RESUMEN DE DATOS INSERTADOS ===\n");

        // Mostrar autores y sus libros
        List<Autor> autores = autorDao.getAll();
        System.out.println("AUTORES (" + autores.size() + "):");
        for (Autor autor : autores) {
            System.out.println("\n📚 " + autor.getNombre() +
                    " (" + autor.getNacionalidad() + ")");

            List<Libro> librosDelAutor = libroDao.findByAutor(autor.getId());
            System.out.println("   Libros:");
            for (Libro libro : librosDelAutor) {
                System.out.println("   • " + libro.getTitulo() + " (" + libro.getAnioPublicacion() + ")");
            }
        }

        // Mostrar categorías
        List<Categoria> categorias = categoriaDao.getAll();
        System.out.println("\n📂 CATEGORÍAS (" + categorias.size() + "):");
        for (Categoria categoria : categorias) {
            System.out.println("   - " + categoria.getNombre());
        }
    }

    public void listarLibrosCompleto() {
        System.out.println("\n=== LISTA COMPLETA DE LIBROS CON AUTOR Y CATEGORÍAS ===");

        List<Libro> libros = libroDao.findAllWithAutorAndCategorias();

        for (Libro libro : libros) {
            System.out.println("\n📖 " + libro.getTitulo());
            System.out.println("   Año: " + libro.getAnioPublicacion());
            System.out.println("   Autor: " + (libro.getAutor() != null ?
                    libro.getAutor().getNombre() : "Sin autor"));
            System.out.println("   Categorías: " +
                    libro.getCategorias().stream()
                            .map(Categoria::getNombre)
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("Sin categorías"));
        }
    }

    public void demostrarRelaciones() {
        System.out.println("\n=== DEMOSTRACIÓN DE RELACIONES ===\n");

        // Mostrar que dos libros pueden tener el mismo autor
        System.out.println("1. LIBROS DEL MISMO AUTOR:");
        List<Autor> autores = autorDao.getAll();
        for (Autor autor : autores) {
            List<Libro> libros = libroDao.findByAutor(autor.getId());
            if (libros.size() > 1) {
                System.out.println("   " + autor.getNombre() + " tiene " + libros.size() + " libros:");
                for (Libro libro : libros) {
                    System.out.println("      - " + libro.getTitulo());
                }
            }
        }

        // Mostrar libros con múltiples categorías
        System.out.println("\n2. LIBROS CON MÚLTIPLES CATEGORÍAS:");
        List<Libro> libros = libroDao.getAll();
        for (Libro libro : libros) {
            if (libro.getCategorias().size() > 1) {
                System.out.println("   \"" + libro.getTitulo() + "\" tiene " +
                        libro.getCategorias().size() + " categorías:");
                for (Categoria categoria : libro.getCategorias()) {
                    System.out.println("      - " + categoria.getNombre());
                }
            }
        }

        // Mostrar categorías compartidas entre libros
        System.out.println("\n3. CATEGORÍAS COMPARTIDAS ENTRE LIBROS:");
        List<Categoria> categorias = categoriaDao.getAll();
        for (Categoria categoria : categorias) {
            List<Libro> librosEnCategoria = libroDao.getAll().stream()
                    .filter(libro -> libro.getCategorias().contains(categoria))
                    .toList();
            if (librosEnCategoria.size() > 1) {
                System.out.println("   La categoría \"" + categoria.getNombre() +
                        "\" aparece en " + librosEnCategoria.size() + " libros:");
                for (Libro libro : librosEnCategoria) {
                    System.out.println("      - " + libro.getTitulo());
                }
            }
        }
    }

    private void limpiarDatos() {
        // Solo para pruebas - limpiar datos existentes
        try {
            entityManager.getTransaction().begin();

            // Eliminar en orden para respetar restricciones de clave foránea
            entityManager.createQuery("DELETE FROM Libro l").executeUpdate();
            entityManager.createQuery("DELETE FROM Autor a").executeUpdate();
            entityManager.createQuery("DELETE FROM Categoria c").executeUpdate();

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    public void close() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
        JPAUtil.close();
    }

    public static void main(String[] args) {
        BibliotecaService service = new BibliotecaService();

        try {
            // 1. Inicializar datos
            service.inicializarDatos();

            // 2. Mostrar resumen
            service.mostrarResumenDatos();

            // 3. Listar libros completos
            service.listarLibrosCompleto();

            // 4. Demostrar relaciones
            service.demostrarRelaciones();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            service.close();
        }
    }
}