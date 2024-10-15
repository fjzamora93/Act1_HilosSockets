import java.util.Scanner;



public class Menu {
    public static void main(String[] args ) throws InterruptedException {

        GestorLibros biblioteca = new GestorLibros();

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            // Mostrar el menú de opciones
            System.out.println("Menú de opciones:");
            System.out.println("1. Añadir nuevo libro");
            System.out.println("2. Borrar libro por id");
            System.out.println("3. Consulta libro por id");
            System.out.println("4. Listado de libros");
            System.out.println("5. Terminar el programa");
            System.out.print("Seleccione una opción: ");

            // Leer la opción seleccionada por el usuario
            opcion = scanner.nextInt();

            // Manejar las opciones seleccionadas
            switch (opcion) {
                case 1:
                    System.out.println("Inserte la matrícula");
                    Thread.sleep(2000);
                    break;
                case 2:
                    System.out.println("Inserte el Id a borrar");
                    Thread.sleep(2000);
                    break;
                case 3:
                    System.out.println("Inserte el Id a buscar");
                    Thread.sleep(4000);
                    break;
                case 4:
                    System.out.println("Listado de todos los libros");
                    Thread.sleep(4000);
                    break;

                case 5:
                    System.out.println("Se ha seleccionado la opción: Terminar el programa");

                    Thread.sleep(2000);
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, elija una opción del 1 al 6.");
                    Thread.sleep(2000);
            }
        } while (opcion != 6);


        scanner.close();
        System.out.println("Programa terminado.");
    }
}


