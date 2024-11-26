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


public class Server implements Runnable{
    private int port;
    private final String ipv4 = "localhost";

    private Thread thread;

    private String response1;
    private String response2;

    private String request;

    private OutputStream salida;
    private InputStream entrada;
    private BufferedReader reader;
    private Socket clientConnection;


    public Server(String param1, String param2, int port){
        this.port = port;
        this.response1 = param1;
        this.response2 = param2;
        this.thread = new Thread(this, "Servidor");
        thread.start();
    }


    @Override
    public void run() {


        System.out.println("APLICACIÓN DE SERVIDOR");
        System.out.println("----------------------------------");

        try {
            ServerSocket servidor = new ServerSocket();
            InetSocketAddress direccion = new InetSocketAddress(this.ipv4 , this.port);
            servidor.bind(direccion);
            System.out.println("Servidor creado y escuchando .... ");
            System.out.println("Dirección IP: " + direccion.getAddress());

            while (true) {
                clientConnection = servidor.accept();
                obtenerFlujoDatos();
                System.out.println("\nComunicación entrante");

                while ((request = reader.readLine()) != null) {
                    System.out.println("Petición recibida: " + request);
                    switch(request){
                        case "1":
                            salida.write((response1 + "\n").getBytes());
                            break;
                        case "2":
                            salida.write((response2 + "\n").getBytes());
                            break;
                        default:
                            salida.write(("no se encuentra disponible esa opción en el menú\n").getBytes());
                            clientConnection.close();
                            break;
                    }
                }

            }
            
        } catch (IOException e) {
            System.out.println( e.getMessage());

        } catch (RuntimeException e){
            System.out.println("El cliente se ha desconectado del servidor");
        }
    }

    public  void obtenerFlujoDatos() throws IOException {
        salida = clientConnection.getOutputStream();
        entrada = clientConnection.getInputStream();
        reader = new BufferedReader(new InputStreamReader(entrada));
    }


}
