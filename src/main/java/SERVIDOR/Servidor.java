package SERVIDOR;

import SERVIDOR.clients.ClientHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class Servidor {
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
                Socket enchufeAlCliente = servidor.accept();
                System.out.println("Comunicación entrante");

                // Creamos un nuevo hilo para manejar a cada cliente entrante
                new ClientHandler(enchufeAlCliente);
            }
        } catch (IOException e) {
            System.out.println("Error al leer o escribir datos, posiblemente por una desconexión del cliente: " + e.getMessage());

        } catch (RuntimeException e){
            System.out.println("El cliente se ha desconectado del servidor");
        }
    }

}

