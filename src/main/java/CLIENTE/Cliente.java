package CLIENTE;
import SERVIDOR.model.Libro;
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

        try {
            /**Un InetSocketAddress es solo una combinación de una dirección IP (o nombre de host) y un número de puerto.
             * No es una conexión en sí misma, sino una dirección que indica a qué IP y puerto se quiere conectar o enlazar. */
            ClienteSocket clienteSocket = new ClienteSocket(ipv4, port);
            String option = "";

            while (!option.trim().equals("5")) {
                Thread.sleep(2000); //Ralentizamos el bucle para que no salga muy de seguido

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
                        consultarPorISBN(scanner, clienteSocket);
                        break;
                    case "2":
                        consultarVarios("Title", scanner, clienteSocket);
                        break;
                    case "3":
                        consultarVarios("Author", scanner, clienteSocket);
                        break;
                    case "4":
                        añadirLibro(scanner, clienteSocket);
                        break;
                    case "5":
                        System.out.println("Hasta pronto, gracias por establecer conexión");
                        jsonRequest.addProperty("header", "exit");
                        jsonRequest.addProperty("body", "exit");
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


    private static void consultarPorISBN(Scanner scanner, ClienteSocket clienteSocket) throws IOException {
        System.out.println("Introduce el ISBN");
        String isbn = scanner.nextLine();
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("header", "getByISBN");
        jsonRequest.addProperty("body", isbn);
        clienteSocket.sendMessage(jsonRequest.toString());

        String respuesta = clienteSocket.receiveMessage();
        procesarRespuestaUnLibro(respuesta);
    }

    private static void consultarVarios(
            String criterioConsulta,
            Scanner scanner,
            ClienteSocket clienteSocket
    ) throws IOException {
        JsonObject jsonRequest = new JsonObject();
        System.out.println("Introduce el " + criterioConsulta);
        String bodyInput = scanner.nextLine();
        jsonRequest.addProperty("header", "getBy"+criterioConsulta);
        jsonRequest.addProperty("body", bodyInput);

        clienteSocket.sendMessage(jsonRequest.toString());
        String respuesta = clienteSocket.receiveMessage();
        procesarRespuestaVariosLibros(respuesta);

    }

    private static void procesarRespuestaUnLibro(String respuesta) {
        Gson gson = new Gson();
        JsonObject jsonResponse = JsonParser.parseString(respuesta).getAsJsonObject();
        JsonObject headerResponse = jsonResponse.getAsJsonObject("header");
        JsonObject bodyResponse = jsonResponse.getAsJsonObject("body");

        // Acceder a los valores dentro del header y el body
        String messageHeader = headerResponse.get("header").getAsString();
        JsonElement contentResponse = bodyResponse.get("content");
        JsonObject selectedBook = contentResponse.getAsJsonObject();

        if (selectedBook.size() == 0 ) {
            System.out.println("No se encontró ningún libro con el ISBN indicado.");
        } else {
            Libro libro = gson.fromJson(selectedBook, Libro.class);
            System.out.println("Libro encontrado: " + libro.toString());
        }
    }


    private static void procesarRespuestaVariosLibros(String respuesta){
        Gson gson = new Gson();
        JsonObject jsonResponse = JsonParser.parseString(respuesta).getAsJsonObject();
        JsonObject headerResponse = jsonResponse.getAsJsonObject("header");
        JsonObject bodyResponse = jsonResponse.getAsJsonObject("body");

        // Acceder a los valores dentro del header y el body
        String messageHeader = headerResponse.get("header").getAsString();
        JsonElement contentResponse = bodyResponse.get("content");
        JsonArray selectedBooks = contentResponse.getAsJsonArray();

        if (selectedBooks.isEmpty()) {
            System.out.println("No se encontró ningún libro con ese parámetro de búsqueda.");
        } else {
            System.out.println("Libros encontrados:");
            for (JsonElement book: selectedBooks) {
                // String isbn = libroElegido.get("ISBN").getAsString();
                // Acceso al resto de propiedades
                JsonObject libroElegido = book.getAsJsonObject();
                System.out.println(book.toString());
            }
        }
    }

    private static void añadirLibro(Scanner scanner, ClienteSocket clienteSocket) throws IOException {
        JsonObject jsonRequest = new JsonObject();
        JsonObject newBook = new JsonObject();

        System.out.println("Introduce un ISBN");
        String bodyInput = scanner.nextLine();
        newBook.addProperty("ISBN", bodyInput);

        System.out.println("Introduce un título");
        bodyInput = scanner.nextLine();
        newBook.addProperty("title", bodyInput);

        System.out.println("Introduce un autor");
        bodyInput = scanner.nextLine();
        newBook.addProperty("author", bodyInput);

        System.out.println("Introduce un precio");
        bodyInput = scanner.nextLine();
        newBook.addProperty("prize", bodyInput);


        jsonRequest.addProperty("header", "add");
        jsonRequest.add("body", newBook);
        System.out.println("ENVIANDO LIBRO AL SERVIDOR: " + newBook.toString());
        clienteSocket.sendMessage(jsonRequest.toString());


        //Recepción de la respuesta
        String mensaje = clienteSocket.receiveMessage();
        JsonObject jsonResponse = JsonParser.parseString(mensaje).getAsJsonObject();
        JsonObject headerResponse = jsonResponse.getAsJsonObject("header");
        JsonObject bodyResponse = jsonResponse.getAsJsonObject("body");

        // Acceder a los valores dentro del header y el body
        String messageHeader = headerResponse.get("header").getAsString();
        JsonElement contentResponse = bodyResponse.get("content");
        boolean value = contentResponse.getAsBoolean();
        if (value == true){
            System.out.println("Libro añadido correctamente");
        } else {
            System.out.println("Error al añadir libro al sistema.");
        }

    }

}

