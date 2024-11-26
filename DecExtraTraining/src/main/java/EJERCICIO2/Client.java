package EJERCICIO2;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Desarrolla una clase Cliente que realice una petición de establecimiento de
 * Socket sobre la ip 127.0.0.1 y al puerto del servidor 12233.  Muestre al usuario
 * 2 opciones:
 *
 * 1 NUMERO DE USUARIOS DEL SISTEMA
 * 2 NUMERO DE CURSOS DISPONIBLES
 *
 * Que el usuario por teclado escriba la opción y esta se envie al servidor. El cliente
 * se queda esperando la respuesta del servidor, mostramos en pantalla y luego
 * cierra la conexión. (1,5 ptos)
 * */


public class Client {

    public static Socket socket;
    public static BufferedReader reader;
    public static Writer writer;
    public static <JsonObject> void main(String[] args) throws InterruptedException {


        String ipv4 = "localhost";
        int port = 2000;
        System.out.println("APLICACIÓN CLIENTE");
        System.out.println("-----------------------------------");
        Scanner scanner = new Scanner(System.in);
        String option = "";


        try {
            socket = new Socket(ipv4, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            while (!option.trim().equals("exit")) {
                // MENÚ
                System.out.println("Menú de opciones:");
                System.out.println("1. Consultar Primera");
                System.out.println("2. Consultar Segunda");
                System.out.print("Seleccione una opción: ");

                // LECTURA DE LA OPCIÓN INTRODUCIDA
                option = scanner.nextLine();
                sendMessage(option);
                receiveMessage();

//                switch (option) {
//                    case "1":
//                        System.out.println("Consulta 1");
//                        break;
//                    case "2":
//                        System.out.println("Consulta 2");
//                        break;
//
//                    default:
//                        System.out.println("Introduce una opción válida");
//                }


            }
            closeConnection();

        } catch (UnknownHostException e) {
            System.out.println("No se puede establecer comunicación con el servidor");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Error de E/S");
            System.out.println(e.getMessage());
        }
    }

    public static void sendMessage(String message) throws IOException {
        writer.write(message + "\n");
        writer.flush();
    }

    public static String receiveMessage() throws IOException {
        String response = reader.readLine();
        System.out.println("La respuesta a la información requerida es: " + response);
        return response;
    }

    public static void closeConnection() throws IOException {
        reader.close();
        writer.close();
        socket.close();
    }

}

