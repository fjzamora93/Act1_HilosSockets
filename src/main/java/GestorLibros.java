import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.Libro;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GestorLibros {
    private Queue<Libro> libros;

    public GestorLibros() {
        libros = new LinkedList<>();
        precargarLibros(); // Precargar los libros al iniciar
    }

    private void precargarLibros() {
        libros.add(new Libro("978-3-16-148410-0", "El Quijote", "Miguel de Cervantes", 20.99));
        libros.add(new Libro("978-1-4028-9462-6", "Cien Años de Soledad", "Gabriel García Márquez", 25.50));
        libros.add(new Libro("978-0-14-044913-6", "Crimen y Castigo", "Fiódor Dostoyevski", 15.75));
        libros.add(new Libro("978-0-452-28423-4", "1984", "George Orwell", 18.00));
        libros.add(new Libro("978-0-7432-7356-5", "El Gran Gatsby", "F. Scott Fitzgerald", 14.99));
    }

    //MÉTODO PARA AGREGAR
    public synchronized void agregarLibro(Libro libro) {
        libros.add(libro);
        notify();
    }

    //MÉTODO PARA SACAR
    public synchronized Libro sacarLibro(){
        // Forzamos un tiempo de espera para que no se adelante
        int espera = 0;
        while (libros.isEmpty() || espera < 20){
            System.out.println("No hay libros que extraer");
            espera ++;
            try{
                wait(10);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        if (espera < 20){
            return libros.remove();
        } else {
            System.out.println("El comprador se va sin nada, efectivamente no hay nada que extraer");
            return null;
        }
    }

    public Queue<Libro> obtenerLibros() {
        return libros;
    }

    public Libro buscarLibroPorISBN(String isbn) {
        for (Libro libro : libros) {
            if (libro.getISBN().equals(isbn)) {
                return libro;
            }
        }
        return null; // Si no se encuentra el libro
    }

    public void mostrarLibros() {
        for (Libro libro : libros) {
            System.out.println(libro);
        }
    }
}
