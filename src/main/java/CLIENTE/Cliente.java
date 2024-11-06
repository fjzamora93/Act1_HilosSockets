package CLIENTE;
import SERVIDOR.model.Libro;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) throws InterruptedException {
        String ipv4 = "192.168.1.87";
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
                        jsonRequest.addProperty("method", "findByISBN");
                        jsonRequest.addProperty("body", bodyInput);
                        break;
                    case "2":
                        System.out.println("Introduce el título");
                        bodyInput = scanner.nextLine();
                        jsonRequest.addProperty("method", "findByTitle");
                        jsonRequest.addProperty("body", bodyInput);
                        break;
                    case "3":
                        System.out.println("Introduce el autor");
                        bodyInput = scanner.nextLine();
                        jsonRequest.addProperty("method", "findByAuthor");
                        jsonRequest.addProperty("body", bodyInput);
                        break;
                    case "4":
                        System.out.println("Introduce Isbn, Título, Autor y precio separado con comas. \n Ejemplo: \n 1234, Don Quijote, Miguel de Cervantes, 40€  ");
                        bodyInput = scanner.nextLine();
                        jsonRequest.addProperty("method", "add");
                        jsonRequest.addProperty("body", bodyInput);
                        break;
                    case "5":
                        System.out.println("¡Adiós!");
                        jsonRequest.addProperty("method", "exit");
                        jsonRequest.addProperty("body", "exit");
                        break;

                    default:
                        System.out.println("Introduce una opción válida");
                }

                if (!jsonRequest.has("method") || !jsonRequest.has("body")) {
                    System.out.println("Error: La solicitud debe contener 'method' y 'body'.");
                    continue;
                }

                salida.write((jsonRequest.toString() + "\n").getBytes());
                String mensaje = reader.readLine();

                JsonObject jsonResponse = JsonParser.parseString(mensaje).getAsJsonObject();
                String response = jsonResponse.get("response").getAsString();


                System.out.println("Servidor dice: \n" + response);

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

