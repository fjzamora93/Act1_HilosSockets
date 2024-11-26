# Resumen de procesos

Para lanzar un proceso con Java, todo parte de la clase ProcessBuilder().

Toda estructura de un proyecto que vaya a ser lanzado por ProcessBuilder va a tener la siguiente arquitectura:
1. La clase principal o main, que es el programa que va a ser lanzado.
2. La clase "lanzador", que es el programa que va a lanzar la clase principal.
3. Las modelos, buffers o DAOs de los que se alimenta la clase main.

## Clase Lanzador

La clase lanzador se compone de los siguientes elementos:
- Un ClassPath, para reconocer el directorio en el que se encuentra la clase main, que va aser lanzado.
- Un ProcessBuilder, que recibirá como mínimo 4 parámetros (java,-cp ,claspath, nombre programa, argumentos adicionales).
- La instancia de ProcessBuilder redirigirá la salida a un documento.
- La instancia de ProcessBuilder llamará al método start.
- Un método waitFor() para esperar a que se cierre el programa.
- El bloque try-catch para capturar errores.

El resultado debería ser algo parecido a esto:


```java

    public class Lanzador {
    public static void main(String[] args) {
        String classpath = System.getProperty("java.class.path");

        try {
            // Lanzamos el primer proceso y redirigimos la salida
            ProcessBuilder proceso1 = new ProcessBuilder("java", "-cp", classpath, "Principal", "Pepe", "Juan", "Luis");
            proceso1.redirectOutput(new File("./src/resources/examen1.txt"));
            proceso1.redirectError(new File("./src/resources/examen1_error.txt"));  // Opcional para errores
            Process p1 = proceso1.start();

            // Esperamos a que los procesos terminen
            p1.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

```

## Clase main (la clase lanzada)

Por su parte, la clase principal debe estar preparada para poder recibir N número de argumentos por parte del lanzador.


```java 

    public class Principal {
        public static void  main(String[] args){
        LocalDateTime fechaInicio = LocalDateTime.now();
        
        
                if (args.length == 0){
                    System.out.println("Se requiere un argumento");
                    return;
                }
                int filas = Integer.parseInt(args[0]);
                System.out.println(fechaInicio);
                for (int i=filas; i>=1; i--){
                    for (int n=1; n<=i; n++){
                        System.out.print(n);
        
                    }
                }
                LocalDateTime fechaFinalizacion = LocalDateTime.now();
                System.out.println();
                System.out.println(fechaFinalizacion);
        
            }
        
    }
    
```

# Resumen de sockets

Para establecer una conexión con sockets, bastará con abrir un socket en una package SERVER y otro en un package CLIENT.

## Client class

1. Creamos una clase ClientSocket que recibirá como parámetors en el constructor una IP y un puerto.
2. A partir de esa clase, creamos una instancia con la que ejecutar todos sus métodos:
    - Establecer conexión (en el constructor con el new Socket(ip, port)).
    - Enviar mensajes (con el PrintWriter)
    - Recibir mensajes (con el BufferReader)
    - Cerrar conexión (llamar al método .close() del socket, writer y reader).


```java

    public class ClienteSocket {
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;
    
        public ClienteSocket(String ipv4, int port) throws IOException {
            this.socket = new Socket(ipv4, port);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        }
    
        public void sendMessage(String message) throws IOException {
            writer.write(message + "\n");
            writer.flush();
        }
    
        public String receiveMessage() throws IOException {
            return reader.readLine();
        }
    
        public void close() throws IOException {
            reader.close();
            writer.close();
            socket.close();
        }
    
    }

```


A partir de la construcción de esa clase cliente, ya todo se basa en enviar y recibir respuestas desde la clase afectada (normalmente un servicio).


```java

  /** Método para añadir*/
    public boolean add(Item item) throws IOException {
        
        JsonObject jsonRequest = new JsonObject();
        JsonObject newItem = gson.toJsonTree(libro).getAsJsonObject();
        jsonRequest.addProperty("header", "add");
        jsonRequest.add("body", newItem);

        // La petición se reciben y se envían inmediatamente
        clienteSocket.sendMessage(jsonRequest.toString());
        String respuesta = clienteSocket.receiveMessage();
        procesarRespuesta(respuesta);

        if (resultadoBusqueda.isEmpty()){
            return false;
        } else {
            return true;
        }
    }

```

## Server Class

1. Un servidor que abre una conexión y acepta peticiones.
   - Creamos un new ServerSocket()..
   - Guardamos la direccion y el puerto en el InetSocketAdrress.
   - Vinculamos la dirección al servidor con .bind().
   - Creamos un enchufe al cliente con .accept()
   - A cada nuevo accept que llegue, se va a crear un nuevo ClientHandler(que es un hilo).
2. Un gestor de clientes (ClienteHandle) para crear un hilo por cliente.

### Clase Servidor

````java

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

````

# Hilos

### Clase Cliente Handler (implementa un hilo para cada cliente)

En la clase Client Handler vamos a encontrarnos lo siguiente:
- Implementa la interface Runnable.
  - Por tanto, habrá una instancia de Thread(this, "String nombre del hilo").
  - La instancia 
  - Habrá un @override del método run.
  - Necesitaremos un socket que implemente la conexión al cliente, con el enchufe accepted que se le pasó.
  - Un InputStream para la entrada con un BufferReader para leer.
  - Un OutputStream para la salida.


````java
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
````

### Entrada y salida:

Para la entrada y salida del servidor, la clvave está en el método obtenerFlujo de Datos.


````java
 public void obtenerFlujoDatos() throws IOException {
        this.salida = clientConnection.getOutputStream();
        this.entrada = clientConnection.getInputStream();
        this.reader = new BufferedReader(new InputStreamReader(entrada));
    }
````

Cada vez que queramos hcer una lectura, tendremos que leer una línea del bufferReader.
En caso de que haya mensaje, habrá una siguietne linea y se procesará la petición, es decir, 
se mandará una respuesta al cliente y el servidor quedará nuevamente a la espera de una nueva petición.