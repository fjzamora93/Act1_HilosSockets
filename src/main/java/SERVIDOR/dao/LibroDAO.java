package SERVIDOR.dao;
import SERVIDOR.model.Libro;
import java.util.ArrayList;

public class LibroDAO {

    private Libro libro;
    private ArrayList<Libro> listaLibros;

    public LibroDAO() {
        this.listaLibros = new ArrayList<>();
        this.listaLibros.add(new Libro("1A", "Harry Potter y la piedra filosofal", "J.K. Rowling", 15.99));
        this.listaLibros.add(new Libro("2B", "Harry Potter y el cáliz de fuego", "J.K. Rowling", 18.50));
        this.listaLibros.add(new Libro("3C", "El señor de los anillos: Las dos torres", "J.R.R. Tolkien", 22.99));
        this.listaLibros.add(new Libro("4D", "El hobbit", "J.R.R. Tolkien", 14.25));
        this.listaLibros.add(new Libro("5E", "La llamada de Cthulhu", "H.P. Lovecraft", 10.99));
    }



    public Libro findByIsbn(String isbn){
        for (Libro book: listaLibros){
            if (book.getISBN().equals(isbn)){
                return book;
            }
        }
        return null;
    }

    public ArrayList<Libro> findByAuthor(String research){
        ArrayList<Libro> auxList = new ArrayList<>();
        for (Libro book: listaLibros){
            if (book.getAuthor().contains(research)){
                auxList.add(book);
            }
        }
        return auxList;
    }

    public ArrayList<Libro> findByTitle(String research){
        ArrayList<Libro> auxList = new ArrayList<>();
        for (Libro book: listaLibros){
            if (book.getTitle().contains(research)){
                auxList.add(book);
            }
        }
        return auxList;
    }


    public synchronized String add() {
        Libro nuevoLibro = new Libro("6D", "Harry Potter y la Cámara Secreta", "J.K. Rowling", 15.99);
        try {
            /*Simulamos un retraso de 4 segundos.
            * Si el hilo está bien sincronizado, nunca podrán salir por consola
            * simultaneamente varios mensajes, ya que el hilo está bloqueando el proceso.*/
            Thread.sleep(4000);

            if (findByIsbn(nuevoLibro.getISBN()) != null){
                return "El libro ya se encuentra registrado";

            } else {
                listaLibros.add(nuevoLibro);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaura el estado de interrupción
        }
        return nuevoLibro.getTitle() + " añadido con éxito a la base de datos";
    }


}
