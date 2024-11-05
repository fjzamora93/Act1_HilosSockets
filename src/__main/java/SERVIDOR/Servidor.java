package SERVIDOR;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

import SERVIDOR.dao.LibroDAO;


public class Servidor {
    public static void main(String[] args) throws  InterruptedException{

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

            Socket enchufeAlCliente = servidor.accept();
            System.out.println("Comunicación entrante");

            InputStream entrada = enchufeAlCliente.getInputStream();
            OutputStream salida = enchufeAlCliente.getOutputStream();
            String inputClient = "";

            while (!inputClient.trim().equals("5")) {
                byte[] mensaje = new byte[100];
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
                            outputServer = "Usted seleccionó opción 1: consulta por ISBN";
                            biblioteca.findByIsbn();
                            break;
                        case "2":
                            outputServer = "Usted seleccionó opción 2: consulta por Título";
                            biblioteca.findByTitle();
                            break;
                        case "3":
                            outputServer = "Usted seleccionó opción 3: consulta por autor";
                            biblioteca.findByAuthor();
                            break;
                        case "4":
                            outputServer = "Usted seleccionó opción 4: Añadir libro";
                            biblioteca.add();
                            break;
                        case "5":
                            outputServer = "FIN";
                            break;

                        default:
                            outputServer = "Opción no válida. Por favor, elija una opción del 1 al 5.";
                            break;
                    }

                    //El métoo getBytes convierte la salida a bytes para que se pueda recibir por el cliente.
                    salida.write((outputServer).getBytes());
                }
            }

            entrada.close();
            salida.close();
            enchufeAlCliente.close();
            servidor.close();
            System.out.println("Comunicación cerrada");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}