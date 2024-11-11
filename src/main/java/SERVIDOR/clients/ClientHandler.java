package SERVIDOR.clients;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import SERVIDOR.dao.LibroDAO;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Thread hilo;
    private static int contador = 1;
    private LibroDAO libroDao;
    private Socket clientConnection;

    // ENTRADA / SALIDA
    private OutputStream salida;
    private InputStream entrada;
    private BufferedReader reader;
    private String mensaje;

    public ClientHandler(Socket clientConnection) {
        this.libroDao = new LibroDAO();
        this.clientConnection = clientConnection;
        this.hilo = new Thread(this, "Usuario-" + contador++);
        this.hilo.start();
    }

    // MÉTODO PARA ABRIR EL FLUJO DE DATOS
    public void obtenerFlujoDatos() throws IOException {
        this.salida = clientConnection.getOutputStream();
        this.entrada = clientConnection.getInputStream();
        this.reader = new BufferedReader(new InputStreamReader(entrada));
    }

    @Override
    public void run() {
        try {
            obtenerFlujoDatos();
            while ((mensaje = reader.readLine()) != null) {
                JsonElement result = procesarPeticion();
                if (!clientConnection.isClosed()) {
                    sendResponse("Código 200: Ok", result);
                } else {
                    System.out.println("El socket está cerrado. Terminando la conexión.");
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al recibir o enviar datos: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("El mensaje recibido es nulo: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Error en la ejecución del servidor: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }


    //LÓGICA PARA GESTIONAR CADA PETICIÓN ENTRANTE
    public JsonElement procesarPeticion() throws IOException {
        // Objeto para RECIBIR petición
        JsonObject jsonRequest = JsonParser.parseString(mensaje).getAsJsonObject();
        String requestHeader = jsonRequest.get("header").getAsString();
        String requestBody;
        JsonElement jsonResult;

        switch (requestHeader) {
            case "getByISBN":
                requestBody = jsonRequest.get("body").getAsString();
                return this.libroDao.findByIsbn(requestBody);
            case "getByTitle":
                requestBody = jsonRequest.get("body").getAsString();
                return this.libroDao.findByTitle(requestBody);
            case "getByAuthor":
                requestBody = jsonRequest.get("body").getAsString();
                return this.libroDao.findByAuthor(requestBody);
            case "add":
                // Llamamos a un método synchronized para añadir libro
                JsonObject newBook = jsonRequest.get("body").getAsJsonObject();
                return this.libroDao.add(newBook);
            case "exit":
                salida.flush();
                entrada.close();
                salida.close();
                clientConnection.close();
                break;
            default:
                JsonObject errorResponse = new JsonObject();
                errorResponse.addProperty("error", "Solicitud no reconocida");
                return errorResponse;
        }
        return null;
    }


    // ENVÍO DE RESPUESTA (evitamos que se solapen el envío de varias respuestas simultaneas, por eso Synchronized)
    private synchronized JsonObject sendResponse(String headerContent, JsonElement bodyContent) throws IOException {
        try {
            JsonObject jsonResponse = new JsonObject();
            JsonObject responseHeader = new JsonObject();
            JsonObject responseBody = new JsonObject();
            jsonResponse.add("header", responseHeader);
            jsonResponse.add("body", responseBody);

            responseHeader.addProperty("header", headerContent);
            responseBody.add("content", bodyContent);

            System.out.println("SALIDA DEL SERVIDOR: " + jsonResponse);
            salida.write((jsonResponse.toString() + "\n").getBytes());

            return jsonResponse;

        } catch (IOException e) {
            throw(e);
        }
    }

    // CERRAR CONEXIÓN
    public void cerrarConexion(){
        try {
            if (clientConnection != null && !clientConnection.isClosed()) {
                clientConnection.close();
                System.out.println("Conexión cerrada correctamente.");
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar el socket: " + e.getMessage());
        }
    }
}





