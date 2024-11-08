package SERVIDOR.dao;
import SERVIDOR.model.Libro;
import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class LibroDAO {

    private Libro libro;
    private ArrayList<Libro> listaLibros;

    public LibroDAO() {
        this.listaLibros = new ArrayList<>();
        this.listaLibros.add(new Libro("1A", "Harry Potter y la piedra filosofal", "J.K. Rowling", "15.99"));
        this.listaLibros.add(new Libro("2B", "Harry Potter y el cáliz de fuego", "J.K. Rowling", "18.50"));
        this.listaLibros.add(new Libro("3C", "El señor de los anillos: Las dos torres", "J.R.R. Tolkien", "22.99"));
        this.listaLibros.add(new Libro("4D", "El hobbit", "J.R.R. Tolkien", "14.25"));
        this.listaLibros.add(new Libro("5E", "La llamada de Cthulhu", "H.P. Lovecraft", "10.99"));
    }



    public JsonObject findByIsbn(String isbn){
        JsonObject jsonBook = new JsonObject();

        for (Libro book: listaLibros){
            if (book.getISBN().equals(isbn)){
                jsonBook.addProperty("ISBN", book.getISBN());
                jsonBook.addProperty("title", book.getTitle());
                jsonBook.addProperty("author", book.getAuthor());
                jsonBook.addProperty("prize", book.getPrize());
            }
        }
        return jsonBook;
    }

    public JsonArray findByAuthor(String research){
        JsonArray jsonBookList = new JsonArray();
        for (Libro book: listaLibros){
            if (book.getAuthor().contains(research)){
                JsonObject jsonBook = new JsonObject();
                jsonBook.addProperty("ISBN", book.getISBN());
                jsonBook.addProperty("title", book.getTitle());
                jsonBook.addProperty("author", book.getAuthor());
                jsonBook.addProperty("prize", book.getPrize());
                jsonBookList.add(jsonBook);
            }
        }
        return jsonBookList;
    }

    public JsonArray findByTitle(String research){
        JsonArray jsonBookList = new JsonArray();
        for (Libro book: listaLibros){
            if (book.getTitle().contains(research)){
                JsonObject jsonBook = new JsonObject();
                jsonBook.addProperty("ISBN", book.getISBN());
                jsonBook.addProperty("title", book.getTitle());
                jsonBook.addProperty("author", book.getAuthor());
                jsonBook.addProperty("prize", book.getPrize());
                jsonBookList.add(jsonBook);
            }
        }
        return jsonBookList;
    }


    public synchronized boolean add(JsonObject bookObject) {
        String isbn = bookObject.get("ISBN").getAsString();
        String title = bookObject.get("title").getAsString();
        String author = bookObject.get("author").getAsString();
        String prize = bookObject.get("prize").getAsString();
        System.out.println("AÑADIENDO LIBRO: " + bookObject.toString());

        Libro nuevoLibro = new Libro(isbn, title, author, prize);
        try {
            /*Simulamos un retraso de 4 segundos.
            * Si el hilo está bien sincronizado, nunca podrán salir por consola
            * simultaneamente varios mensajes, ya que el hilo está bloqueando el proceso.*/
            Thread.sleep(4000);

            if (findByIsbn(nuevoLibro.getISBN()).size() != 0){
                System.out.println("El libro ya existe");
                return false;

            } else {
                listaLibros.add(nuevoLibro);
            }
            return true;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaura el estado de interrupción
        } catch (Exception e){
            System.out.println("Datos incompletos, introduce un formato de libro válido");
            return false;
        }
        return true;
    }


}
