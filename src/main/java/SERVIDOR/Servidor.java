package SERVIDOR;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetSocketAddress;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import SERVIDOR.dao.LibroDAO;
import com.google.gson.JsonParser;

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

                // Creamos un nuevo hilo para manejar al cliente
                new Thread(() -> manejarCliente(enchufeAlCliente)).start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
    }

    //LÓGICA PARA GESTIONAR CADA PETICIÓN ENTRANTE
    public static void manejarCliente(Socket enchufeAlCliente){
        LibroDAO biblioteca = new LibroDAO();
        try {
            //ENTRADA
            InputStream entrada = enchufeAlCliente.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entrada));
            String mensaje;
            //SALIDA
            OutputStream salida = enchufeAlCliente.getOutputStream();

            while ((mensaje = reader.readLine()) != null) {

                // Objeto para RECIBIR petición
                JsonObject jsonRequest = JsonParser.parseString(mensaje).getAsJsonObject();
                String requestHeader = jsonRequest.get("header").getAsString();
                String requestBody;
                JsonElement jsonResult;

                switch (requestHeader) {
                    case "getByISBN":
                        requestBody = jsonRequest.get("body").getAsString();
                        jsonResult = biblioteca.findByIsbn(requestBody);
                        sendResponse("getByISBN", jsonResult, salida );
                        break;

                    case "getByTitle":
                        requestBody = jsonRequest.get("body").getAsString();
                        jsonResult = biblioteca.findByTitle(requestBody);
                        sendResponse("getByTitle", jsonResult, salida );
                        break;

                    case "getByAuthor":
                        requestBody = jsonRequest.get("body").getAsString();
                        jsonResult = biblioteca.findByAuthor(requestBody);
                        sendResponse("getByTitle", jsonResult, salida );
                        break;

                    case "add":
                        // Llamamos a un método synchronized para añadir libro
                        JsonObject newBook = jsonRequest.get("body").getAsJsonObject();
                        jsonResult = biblioteca.add(newBook);
                        sendResponse("add", jsonResult, salida );
                        break;

                    case "exit":
                        jsonResult = new JsonObject();
                        sendResponse("exit", jsonResult, salida );
                        salida.flush();
                        entrada.close();
                        salida.close();
                        enchufeAlCliente.close();
                        System.out.println("El cliente se ha desconectado del servidor");
                        break;
                }
            }
        }  catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // ENVÍO DE RESPUESTA
    private static JsonObject sendResponse(String headerContent, JsonElement bodyContent, OutputStream salida ) throws IOException {
        JsonObject jsonResponse = new JsonObject();
        JsonObject responseHeader = new JsonObject();
        JsonObject responseBody = new JsonObject();
        jsonResponse.add("header", responseHeader);
        jsonResponse.add("body", responseBody);

        responseHeader.addProperty("header", headerContent);
        responseBody.add("content", bodyContent);

        System.out.println("SALIDA DEL SERVIDOR: " + jsonResponse);
        salida.write((jsonResponse.toString() + "\n").getBytes() );

        return jsonResponse;
    }

}



