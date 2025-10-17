package run;

import service.BibliotecaService;

public class Main {

    public static void main(String[] args) {
        BibliotecaService service = new BibliotecaService();

        try {
            // Inicializar datos de ejemplo (inserta autores, libros y categorías)
            service.inicializarDatos();

            // Mostrar un resumen de los datos insertados
            service.mostrarResumenDatos();

            // Listar todos los libros con su autor y categorías
            service.listarLibrosCompleto();

            // Demostraciones adicionales sobre relaciones
            service.demostrarRelaciones();

        } catch (Exception e) {
            System.err.println("Error ejecutando la aplicación: " + e.getMessage());
            e.printStackTrace();
        } finally {
            service.close();
        }
    }
}
