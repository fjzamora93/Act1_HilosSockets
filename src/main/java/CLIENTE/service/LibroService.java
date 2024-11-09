package CLIENTE.service;
import CLIENTE.ClienteSocket;
import model.Libro;
import com.google.gson.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LibroService {
    private final ClienteSocket clienteSocket;
    private final Gson gson = new Gson();

    public LibroService(ClienteSocket clienteSocket) {
        this.clienteSocket = clienteSocket;
    }

    public String sendRequest(String criterio, String value) throws IOException {
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("header", "getBy" + criterio);
        jsonRequest.addProperty("body", value);
        clienteSocket.sendMessage(jsonRequest.toString());
        return clienteSocket.receiveMessage();
    }

    public Libro findOne(String criterio, String value) throws IOException {
        String respuesta = sendRequest(criterio, value);
        return procesarRespuestaUnLibro(respuesta);
    }

    public ArrayList<Libro> findMany(String criterio, String value) throws IOException {
        String respuesta = sendRequest(criterio, value);
        return procesarRespuestaVariosLibros(respuesta);
    }

    public boolean add(Libro libro) throws IOException {
        JsonObject jsonRequest = new JsonObject();
        JsonObject newBook = gson.toJsonTree(libro).getAsJsonObject();
        jsonRequest.addProperty("header", "add");
        jsonRequest.add("body", newBook);

        clienteSocket.sendMessage(jsonRequest.toString());
        String respuesta = clienteSocket.receiveMessage();
        ArrayList<Libro> listadoResultados = procesarRespuestaVariosLibros(respuesta);

        if (listadoResultados.isEmpty()){
            return false;
        } else {
            return true;
        }
    }

    private Libro procesarRespuestaUnLibro(String respuesta) {
        JsonObject jsonResponse = JsonParser.parseString(respuesta).getAsJsonObject();
        JsonObject bodyResponse = jsonResponse.getAsJsonObject("body");
        JsonObject selectedBook = bodyResponse.getAsJsonObject("content");

        if (selectedBook.size() == 0) {
            return null;
        } else {
            return gson.fromJson(selectedBook, Libro.class);
        }
    }

    private ArrayList<Libro> procesarRespuestaVariosLibros(String respuesta) {
        ArrayList<Libro> libros = new ArrayList<>();
        JsonObject jsonResponse = JsonParser.parseString(respuesta).getAsJsonObject();
        JsonArray selectedBooks = jsonResponse.getAsJsonObject("body").getAsJsonArray("content");

        for (JsonElement book : selectedBooks) {
            libros.add(gson.fromJson(book, Libro.class));
        }

        if (libros.isEmpty()){
            return null;
        }
        return libros;
    }
}