package CLIENTE;
import CLIENTE.service.LibroService;
import model.Libro;
import com.google.gson.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) throws InterruptedException {
        String ipv4 = "localhost";
        int port = 2000;
        System.out.println("APLICACIÓN CLIENTE");
        System.out.println("-----------------------------------");
        Scanner scanner = new Scanner(System.in);
        String option = "";

        try {
            ClienteSocket clienteSocket = new ClienteSocket(ipv4, port);
            LibroService libroService = new LibroService(clienteSocket);

            while (!option.trim().equals("5")) {
                // MENÚ
                System.out.println("Menú de opciones:");
                System.out.println("1. Consultar libro por ISBN");
                System.out.println("2. Consultar libro por título");
                System.out.println("3. Consulta libro por autor");
                System.out.println("4. Añadir libro");
                System.out.println("5. Salir de la aplicación");
                System.out.print("Seleccione una opción: ");

                // LECTURA DE LA OPCIÓN INTRODUCIDA
                option = scanner.nextLine();
                JsonObject jsonRequest = new JsonObject();

                switch (option) {
                    case "1":
                        System.out.println("Introduce el ISBN");
                        libroService.find("ISBN", scanner.nextLine());
                        consoleLog(LibroService.getResultadoBusqueda());
                        break;
                    case "2":
                        System.out.println("Introduce el título");
                        libroService.find("Title", scanner.nextLine());
                        consoleLog(LibroService.getResultadoBusqueda());
                        break;
                    case "3":
                        System.out.println("Introduce el autor");
                        libroService.find("Author", scanner.nextLine());
                        consoleLog(LibroService.getResultadoBusqueda());
                        break;
                    case "4":
                        Libro newBook = recopilarDatosNewBook(scanner);
                        if (libroService.add(newBook)){
                            System.out.println("Libro añadido correctamente");
                        } else {
                            System.out.println("Lo lamentamos, pero no ha sido posible añadir el libro.");
                        }
                        break;
                    case "5":
                        System.out.println("Hasta pronto, gracias por establecer conexión");
                        jsonRequest.addProperty("header", "exit");
                        jsonRequest.addProperty("body", "exit");
                        clienteSocket.sendMessage(jsonRequest.toString());
                        clienteSocket.close();
                        break;
                    default:
                        System.out.println("Introduce una opción válida");
                }
            }

        } catch (UnknownHostException e) {
            System.out.println("No se puede establecer comunicación con el servidor");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Error de E/S");
            System.out.println(e.getMessage());
        }
    }


    private static Libro recopilarDatosNewBook(Scanner scanner){
        System.out.println("Introduce un ISBN");
        String isbn = scanner.nextLine();

        System.out.println("Introduce un título");
        String title = scanner.nextLine();

        System.out.println("Introduce un autor");
        String author = scanner.nextLine();

        System.out.println("Introduce un precio");
        String prize = scanner.nextLine();
        return new Libro(isbn, title, author, prize);
    }


    //Creamos un método para pintar por consola... no tiene mucha utilidad
    private static void consoleLog(Object object) throws InterruptedException {
        if (object instanceof ArrayList) {
            ArrayList<?> arrayList = (ArrayList<?>) object;
            if (arrayList.isEmpty()) {
                System.out.println("No hay coincidencias de búsqueda");
            } else {
                System.out.println("Encontrado: \n" + object.toString());
            }

        } else {
            System.out.println(object.toString());
        }
        Thread.sleep(2000);
    }

}

