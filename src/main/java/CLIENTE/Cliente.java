package CLIENTE;
import SERVIDOR.model.Libro;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) throws InterruptedException {
        String ipv4 = "localhost";
        System.out.println("APLICACIÓN CLIENTE");
        System.out.println("-----------------------------------");
        Scanner scanner = new Scanner(System.in);



        try {

            /**
             * Un InetSocketAddress es solo una combinación de una dirección IP (o nombre de host)
             * y un número de puerto. No es una conexión en sí misma,
             * sino una dirección que indica a qué IP y puerto se quiere conectar o enlazar.
             * */
            Socket cliente = new Socket();
            InetSocketAddress direccionServidor = new InetSocketAddress(ipv4, 2000);

            System.out.println("Esperando a que el servidor acepte la conexión");
            cliente.connect(direccionServidor);
            System.out.println("Comunicación establecida con el servidor");

            // getInputStream() y getOutputStream(): permiten enviar y recibir datos a través de la conexión.
            InputStream entrada = cliente.getInputStream();
            OutputStream salida = cliente.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entrada));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(salida), true);
            
            String option = "";
            String bodyInput = "";

            while (!option.trim().equals("5")) {

                // Mostrar el menú de opciones
                System.out.println("Menú de opciones:");
                System.out.println("1. Consultar libro por ISBN");
                System.out.println("2. Consultar libro por título");
                System.out.println("3. Consulta libro por autor");
                System.out.println("4. Añadir libro");
                System.out.println("5. Salir de la aplicación");
                System.out.print("Seleccione una opción: ");

                // Leer la opción seleccionada por el usuario
                option = scanner.nextLine();
                JsonObject jsonRequest = new JsonObject();


                switch (option) {
                    case "1":
                        System.out.println("Introduce el ISBN");
                        bodyInput = scanner.nextLine();
                        jsonRequest.addProperty("header", "getByISBN");
                        jsonRequest.addProperty("body", bodyInput);
                        break;
                    case "2":
                        System.out.println("Introduce el título");
                        bodyInput = scanner.nextLine();
                        jsonRequest.addProperty("header", "getByTitle");
                        jsonRequest.addProperty("body", bodyInput);
                        break;
                    case "3":
                        System.out.println("Introduce el autor");
                        bodyInput = scanner.nextLine();
                        jsonRequest.addProperty("header", "getByAuthor");
                        jsonRequest.addProperty("body", bodyInput);
                        break;
                    case "4":

                        JsonObject newBook = new JsonObject();
                        System.out.println("Introduce un ISBN");
                        bodyInput = scanner.nextLine();
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
                        break;
                    case "5":
                        System.out.println("¡Adiós!");
                        jsonRequest.addProperty("header", "exit");
                        jsonRequest.addProperty("body", "exit");
                        break;

                    default:
                        System.out.println("Introduce una opción válida");
                }

                if (!jsonRequest.has("header") || !jsonRequest.has("body")) {
                    System.out.println("Error: La solicitud debe contener 'header' y 'body'.");
                    continue;
                }

                salida.write((jsonRequest.toString() + "\n").getBytes());

                //Recepción de la respuesta
                String mensaje = reader.readLine();
                JsonObject jsonResponse = JsonParser.parseString(mensaje).getAsJsonObject();
                JsonObject headerResponse = jsonResponse.getAsJsonObject("header");
                JsonObject bodyResponse = jsonResponse.getAsJsonObject("body");

                // Acceder a los valores dentro del header y el body
                String messageHeader = headerResponse.get("header").getAsString();
                JsonElement contentResponse;

                switch (messageHeader){
                    case "getByISBN":
                        contentResponse = bodyResponse.getAsJsonObject("content");
                        JsonObject selectedBook = contentResponse.getAsJsonObject();
                        // En este punto, el cliente ya dispone del objeto "Libro", por lo que si quisiera podría extraer sus atributos.
                        // String isbn = libro.get("ISBN").getAsString();
                        // String title = libro.get("title").getAsString();
                        // etc...
                        if (selectedBook.size() == 0) {
                            System.out.println("No se encontró ningún libro con el ISBN indicado.");
                        } else {
                            System.out.println(selectedBook.toString());
                        }
                        break;
                    case "getByTitle":
                    case "getByAuthor":
                        contentResponse = bodyResponse.get("content");
                        JsonArray selectedBooks = contentResponse.getAsJsonArray();
                        if (selectedBooks.isEmpty()) {
                            System.out.println("No se encontró ningún libro con ese parámetro de búsqueda.");
                        } else {
                            System.out.println("Libros encontrados:");
                            for (JsonElement book: selectedBooks) {
                                JsonObject libroElegido = book.getAsJsonObject();
                                System.out.println(book.toString());
                            }
                        }
                        break;
                    case "postBook":
                        contentResponse = bodyResponse.get("content");
                        boolean value = contentResponse.getAsBoolean();
                        if (value == true){
                            System.out.println("Libro añadido correctamente");
                        } else {
                            System.out.println("Error al añadir libro al sistema.");
                        }

                        break;
                    default:
                        System.out.println("Aún estamos trabajando en ello");
                }


                if (option.equals("5")) {
                    System.out.println("Hasta pronto, gracias por establecer conexión");
                    break;
                }

            }

            entrada.close();
            salida.close();
            reader.close();
            writer.close();
            cliente.close();
            System.out.println("Comunicación cerrada");
        } catch (UnknownHostException e) {
            System.out.println("No se puede establecer comunicación con el servidor");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Error de E/S");
            System.out.println(e.getMessage());
        }
    }
}

