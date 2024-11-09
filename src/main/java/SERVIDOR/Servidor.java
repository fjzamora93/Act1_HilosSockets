package SERVIDOR;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetSocketAddress;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import SERVIDOR.dao.LibroDAO;
import com.google.gson.JsonParser;

public class Servidor {

    public static void main(String[] args) {
        LibroDAO biblioteca = new LibroDAO();

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

                // Crear un nuevo hilo para manejar al cliente
                new Thread(() -> {

                    try {
                        //ENTRADA
                        InputStream entrada = enchufeAlCliente.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(entrada));
                        String mensaje;

                        //SALIDA
                        OutputStream salida = enchufeAlCliente.getOutputStream();
                        PrintWriter writer = new PrintWriter(new OutputStreamWriter(salida), true);

                        while ((mensaje = reader.readLine()) != null) {
                            // Objeto para ENVIAR respuesta al CLIENTE
                            JsonObject jsonResponse = new JsonObject();
                            JsonObject header = new JsonObject();
                            JsonObject body = new JsonObject();
                            jsonResponse.add("header", header);
                            jsonResponse.add("body", body);

                            // Objeto para RECIBIR petición
                            JsonObject jsonRequest = JsonParser.parseString(mensaje).getAsJsonObject();
                            String requestHeader = jsonRequest.get("header").getAsString();
                            String requestBody;

                            //Declaración de variables
                            JsonArray listadoEncontrado;
                            JsonObject libroEncontrado;

                            switch (requestHeader) {
                                case "getByISBN":
                                    requestBody = jsonRequest.get("body").getAsString();
                                    libroEncontrado = biblioteca.findByIsbn(requestBody);
                                    header.addProperty("header", "getByISBN");
                                    body.add("content", libroEncontrado);
                                    break;

                                case "getByTitle":
                                    requestBody = jsonRequest.get("body").getAsString();
                                    listadoEncontrado = biblioteca.findByTitle(requestBody);
                                    header.addProperty("header", "getByTitle");
                                    body.add("content", listadoEncontrado);
                                    break;

                                case "getByAuthor":
                                    requestBody = jsonRequest.get("body").getAsString();
                                    listadoEncontrado = biblioteca.findByAuthor(requestBody);
                                    header.addProperty("header", "getByAuthor");
                                    body.add("content", listadoEncontrado);
                                    break;

                                case "add":
                                    // Llamamos a un método synchronized para añadir libro
                                    JsonObject newBook = jsonRequest.get("body").getAsJsonObject();
                                    boolean result = biblioteca.add(newBook);
                                    header.addProperty("header", "postBook");
                                    body.addProperty("content", result);
                                    break;
                                case "exit":
                                    header.addProperty("header", "exit");
                                    body.addProperty("content", "Hasta pronto, cerrando conexión");
                                    salida.write((jsonResponse.toString() + "\n").getBytes() );
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

