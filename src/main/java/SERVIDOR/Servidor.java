package SERVIDOR;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import SERVIDOR.model.Libro;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import SERVIDOR.dao.LibroDAO;
import com.google.gson.JsonParser;

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

                    try {
                        InputStream entrada = enchufeAlCliente.getInputStream();
                        OutputStream salida = enchufeAlCliente.getOutputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(entrada));
                        PrintWriter writer = new PrintWriter(new OutputStreamWriter(salida), true);
                        String mensaje;


                        while ((mensaje = reader.readLine()) != null) {

                            // Convertir la cadena JSON recibida en un objeto JsonObject
                            JsonObject jsonRequest = JsonParser.parseString(mensaje).getAsJsonObject();

                            // Procesar el JSON en función del método solicitado
                            String method = jsonRequest.get("method").getAsString();
                            String body = jsonRequest.get("body").getAsString();
                            String libroEncontrado;
                            ArrayList <Libro> listadoEncontrado;
                            JsonObject jsonResponse = new JsonObject();

                            System.out.println(method + "\n" + body);

                            switch (method) {
                                case "findByISBN":

                                    if (biblioteca.findByIsbn(body) == null){
                                        libroEncontrado = "Ningún libro se corresponde al ISBN indicado";
                                        System.out.println(libroEncontrado);
                                    } else {
                                        libroEncontrado = biblioteca.findByIsbn(body).toString();
                                    }
                                    jsonResponse.addProperty("response", libroEncontrado);

                                    break;
                                case "findByTitle":

                                    listadoEncontrado = biblioteca.findByTitle(body);
                                    jsonResponse.addProperty("response", String.valueOf(listadoEncontrado));
                                    break;
                                case "findByAuthor":
                                    listadoEncontrado = biblioteca.findByAuthor(body);
                                    jsonResponse.addProperty("response", String.valueOf(listadoEncontrado));
                                    break;
                                case "add":
                                    // Llamar al método synchronized para añadir libro
                                    String result;
                                    try{
                                        result = biblioteca.add(body);
                                    } catch (Exception e){
                                        result = "Error al tratar de añadir libro";
                                    }
                                    jsonResponse.addProperty("response", result);
                                    break;
                                case "exit":
                                    jsonResponse.addProperty("response", "Hasta pronto, cerrando conexión");
                                    salida.write((jsonResponse.toString() + "\n").getBytes());
                                    salida.flush();
                                    entrada.close();
                                    salida.close();
                                    enchufeAlCliente.close();
                                    break;
                                default:
                                    jsonResponse.addProperty("response", "Opción no válida. Por favor, elija una opción del 1 al 5.");
                                    break;
                            }

                            // El método getBytes convierte la salida a bytes para que se pueda recibir por el cliente.
                            System.out.println("SALIDA DEL SERVIDOR: " + jsonResponse);
                            salida.write((jsonResponse.toString() + "\n").getBytes() );

                        }
                    }  catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }).start();

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
    }
}

