package EJERCICIO2;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/***
 * Crea una clase Servidor que escuche en el puerto 12233 con la intención de
 * establecer un Socket con un cliente. El servidor debe tener dos parámetros:
 * parámetro uno, NumeroUsuarios con valor = 15 y NumeroCursos = 25. Una vez
 * aceptado se queda esperando un mensaje del cliente y posteriormente envia
 * al cliente una información:
 *
 * Si recibe un 1 envia el contenido de la variable NumeroUsuarios y si recibe un
 * 2 envia el contenido de la variable NumeroCursos. Cualquier otra información,
 * manda un mensaje diciendo, no se encuentra disponible esa opción en el
 * menú. Posteriormente cierra la conexión.  (1,5 ptos)
 *
 */


public class Server {
    String info1 = "Primer tipo de información";
    String info2 = "Segundo tipo de información";

    String mensaje;

    static OutputStream salida;
    static InputStream entrada;
    static BufferedReader reader;
    static Socket clientConnection;

    public static void main(String[] args) {

        String ipv4 = "localhost";
        System.out.println("APLICACIÓN DE SERVIDOR");
        System.out.println("----------------------------------");

        try {
            ServerSocket servidor = new ServerSocket();
            InetSocketAddress direccion = new InetSocketAddress(ipv4, 2000);
            servidor.bind(direccion);
            System.out.println("Servidor creado y escuchando .... ");
            System.out.println("Dirección IP: " + direccion.getAddress());

            while (true) {
                clientConnection = servidor.accept();
                obtenerFlujoDatos();
                System.out.println("Comunicación entrante");

                // AQUÍ ES DONDE SE DEBERÍA CREAR CADA HILO EN CASO DE HABERLO
                while ((mensaje = reader.readLine()) != null) {
                    JsonElement result = procesarPeticion();
                    if (!clientConnection.isClosed()) {
                        sendResponse("Código 200: Ok", result);
                    } else {
                        System.out.println("El socket está cerrado. Terminando la conexión.");
                        break;
                    }
                }


            }
        } catch (IOException e) {
            System.out.println("Error al leer o escribir datos, posiblemente por una desconexión del cliente: " + e.getMessage());

        } catch (RuntimeException e){
            System.out.println("El cliente se ha desconectado del servidor");
        }
    }

    public static void obtenerFlujoDatos() throws IOException {
        salida = clientConnection.getOutputStream();
        entrada = clientConnection.getInputStream();
        reader = new BufferedReader(new InputStreamReader(entrada));
    }

}
