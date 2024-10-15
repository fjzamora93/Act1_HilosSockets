import model.Libro;

public class AplicacionCliente implements Runnable {
    private Thread hilo;
    private GestorLibros buffer;
    private static int contador = 0;
    public AplicacionCliente(GestorLibros buffer) {
        contador++;
        hilo = new Thread(this, "Consumidor" + contador);
        this.buffer = buffer;
        hilo.start();
    }
    @Override
    public void run() {
        consumirLibro();
    }
    public void consumirLibro() {
        Libro libro = buffer.sacarLibro();
        if (libro != null) {
            System.out.println("Libro = " + libro);
        }
        else {
            System.out.println("Cola vacía");
            // Implementar lógica de espera
        }
    }
}