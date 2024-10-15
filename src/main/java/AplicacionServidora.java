import model.Libro;

public class AplicacionServidora implements Runnable{
    private GestorLibros buffer;
    private static int contador = 0;
    private Thread hilo;

    public AplicacionServidora(GestorLibros buffer) {
        contador ++;
        this.buffer = buffer;
        hilo = new Thread(this, "Productor"+contador);
        hilo.start();
    }

    @Override
    public void run() {
        Libro libro = new Libro("978-0-7432-7356-5", "El Gran Gatsby", "F. Scott Fitzgerald", 14.99);
        System.out.println(hilo.getName() + " va a generar un Libro = " + libro);
        buffer.agregarLibro(libro);
    }
}
