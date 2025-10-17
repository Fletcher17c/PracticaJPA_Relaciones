package run;

import service.BibliotecaService;

public class Main {
    public static void main(String[] args) {
        BibliotecaService service = new BibliotecaService();

        try {
            // 0. Debug
            service.debugEntityManager();

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
