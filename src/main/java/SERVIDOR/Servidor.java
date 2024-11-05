package SERVIDOR;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

import SERVIDOR.dao.LibroDAO;

public class Servidor {
    public static void main(String[] args) {
        LibroDAO biblioteca = new LibroDAO();

        String ipv4 = "192.168.1.87";
        System.out.println("APLICACIÓN DE SERVIDOR");
        System.out.println("----------------------------------");

        try {
            ServerSocket servidor = new ServerSocket();
            InetSocketAddress direccion = new InetSocketAddress(ipv4, 2000);
            servidor.bind(direccion);
            System.out.println("Servidor creado y escuchando .... ");
            System.out.println("Dirección IP: " + direccion.getAddress());

            while (true) {
                Socket enchufeAlCliente = servidor.accept();
                System.out.println("Comunicación entrante");

                // Crear un nuevo hilo para manejar al cliente
                new Thread(() -> {
                    String research = "";
                    try {
                        InputStream entrada = enchufeAlCliente.getInputStream();
                        OutputStream salida = enchufeAlCliente.getOutputStream();
                        String inputClient = "";

                        while (!inputClient.trim().equals("5")) {
                            byte[] mensaje = new byte[1000];
                            int bytesLeidos = entrada.read(mensaje); // Leer solo los bytes recibidos
                            inputClient = new String(mensaje, 0, bytesLeidos).trim(); // Convertir solo los bytes leídos a String
                            String outputServer = "";

                            if (inputClient.equals("5")) {
                                salida.write("Hasta pronto, gracias por establecer conexión".getBytes());
                            } else {
                                System.out.println("Cliente dice: " + inputClient);
                                // Manejar las opciones seleccionadas
                                switch (inputClient) {
                                    case "1":
                                        research = "1A";
                                        outputServer = biblioteca.findByIsbn(research).toString();
                                        break;
                                    case "2":
                                        research = "Harry Potter";
                                        outputServer =  biblioteca.findByTitle(research).toString();
                                        break;
                                    case "3":
                                        research = "Tolkien";
                                        outputServer =   biblioteca.findByAuthor(research).toString();
                                        break;
                                    case "4":

                                        // Llamar al método synchronized para añadir libro
                                        outputServer = biblioteca.add();
                                        break;
                                    default:
                                        outputServer = "Opción no válida. Por favor, elija una opción del 1 al 5.";
                                        break;
                                }

                                // El método getBytes convierte la salida a bytes para que se pueda recibir por el cliente.
                                System.out.println("SALIDA DEL SERVIDOR: " + outputServer);
                                salida.write((outputServer).getBytes());
                                Thread.sleep(3000);
                            }
                        }

                        entrada.close();
                        salida.close();
                        enchufeAlCliente.close();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start(); // Iniciar el hilo para el cliente
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
