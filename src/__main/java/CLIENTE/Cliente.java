package CLIENTE;

import java.io.*;
import java.net.*;
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
            String texto = "";

            while (!texto.trim().equals("5")) {

                // Mostrar el menú de opciones
                System.out.println("Menú de opciones:");
                System.out.println("1. Consultar libro por ISBN");
                System.out.println("2. Consultar libro por título");
                System.out.println("3. Consulta libro por autor");
                System.out.println("4. Añadir libro");
                System.out.println("5. Salir de la aplicación");
                System.out.print("Seleccione una opción: ");

                // Leer la opción seleccionada por el usuario
                texto = scanner.nextLine();


                salida.write(texto.getBytes());

                // Crear un buffer para el mensaje del servidor y leer la respuesta
                byte[] mensaje = new byte[100];
                int bytesLeidos = entrada.read(mensaje); // Leer solo los bytes recibidos
                texto = new String(mensaje, 0, bytesLeidos); // Convertir solo los bytes leídos a String

                if (texto.trim().equals("FIN")) {
                    salida.write("Hasta pronto, gracias por establecer conexión".getBytes());
                } else {
                    System.out.println("Servidor dice: " + texto);
                    Thread.sleep(4000);

                }
            }

            entrada.close();
            salida.close();
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

