package CLIENTE.service;
import CLIENTE.ClienteSocket;
import model.Libro;
import com.google.gson.*;

import java.io.IOException;
import java.util.ArrayList;


public class LibroService {
    private final ClienteSocket clienteSocket;
    private final Gson gson = new Gson();
    private static ArrayList<Libro> resultadoBusqueda;

    public LibroService(ClienteSocket clienteSocket) {
        this.clienteSocket = clienteSocket;
    }


    // MÉTODO PARA BUSCAR LIBROS
    public ArrayList<Libro> find(String criterio, String value) throws IOException {
        String respuesta = sendRequest(criterio, value);
        procesarRespuesta(respuesta);
        return resultadoBusqueda;
    }


    // MÉTODO PARA AÑADIR LIBRO
    public boolean add(Libro libro) throws IOException {
        JsonObject jsonRequest = new JsonObject();
        JsonObject newBook = gson.toJsonTree(libro).getAsJsonObject();
        jsonRequest.addProperty("header", "add");
        jsonRequest.add("body", newBook);

        clienteSocket.sendMessage(jsonRequest.toString());
        String respuesta = clienteSocket.receiveMessage();
        procesarRespuesta(respuesta);

        if (resultadoBusqueda.isEmpty()){
            return false;
        } else {
            return true;
        }
    }

    // ENVÍO DE PETICIÓN
    public String sendRequest(String criterio, String value) throws IOException {
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("header", "getBy" + criterio);
        jsonRequest.addProperty("body", value);
        clienteSocket.sendMessage(jsonRequest.toString());
        return clienteSocket.receiveMessage();
    }


    // MÉTODO PARA PROCESAR LA RESPUESTA ENTRANTE DEL SERVIDOR
    private void procesarRespuesta(String respuesta) {
        ArrayList<Libro> libros = new ArrayList<>();
        JsonObject jsonResponse = JsonParser.parseString(respuesta).getAsJsonObject();
        JsonArray selectedBooks = jsonResponse.getAsJsonObject("body").getAsJsonArray("content");
        for (JsonElement book : selectedBooks) {
            libros.add(gson.fromJson(book, Libro.class));
        }
        resultadoBusqueda = libros;
    }

    // MÉTODO ESTÁTICO PARA DEVOLVER EL RESULTADO DESDE CUALQUIER PARTE DE LA APLICACIÓN
    public static ArrayList<Libro> getResultadoBusqueda() {
        return resultadoBusqueda;
    }
}