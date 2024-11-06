# Actividad 1. Hilos y Sockets

### Arquitectura del programa

Como paso previo comenzamos diseñando la arquitectura del programa a desarrollar.
De acuerdo a los requirementos del ejercicio, y simplemente leyendo el enunciado, sabemos que necesitaremos los siguientes componentes:
1. Un servidor, donde se ejecutarán todas las operaciones "sensibles" y la lógica interna de la aplicación.
2. Un cliente, donde se ejecutarán las operaciones de interacción con el servidor.
3. Un modelo "Libro", correspondiente a la clase principal de nuestro programa.
4. Un modelo DAO, que encapsulará la lógica de todas las operaciones relacionadas con el Libro.

Opcionalmente, sería posible crear un "buffer libros", pero dado que el concepto del progrmaa es una "Biblioteca", se da por hecho que habrá cierta persistencia en la recopilación y almacenamiento de datos, por lo que es más apropiado trabajar con un DAO.

### Desarrollo del cliente

El cliente únicamnete debe preocuparse de recopilar todos los datos que puedan ser de interés para el usuario y enviarlos de forma correcta al servidor.

En nuestro caso, nos enfrentamos a la problemática de que el usuario requiere enviar dos tipos de datos: por un lado un tipo de operación (method), por otro lado un cuerpo con cierta información (body).

Si estuviésemos en un contexto de html y servidor web esto podríamos resolverlo a través de un sistema de URLs capaz de redirigir las peticiones de GET, PUT y POST con su respectivo body. Esta parte la podriamos resolver con el envío de un XML o un JSON con su respectivo header y cuerpo. 

En nuestro caso, hemos optado por enviar un objeto de tipo JSON con "method" y un "body".  Para ello bastará con crear un JSON object e añadir ambas propiedades, tal que así:

````java
    JsonObject jsonRequest = new JsonObject();
    jsonRequest.addProperty("method", "findByISBN");
    jsonRequest.addProperty("body", bodyInput);
                        
````

Posteriormente, a la hora de enviar la petición, bastará con convertir el JSON  a texto plano antes de su envío.

````java
    salida.write((jsonRequest.toString() + "\n").getBytes());
````

### Desarrollo del servidor

La arquitectura del servidor es sutilmente más compleja. En primer lugar, debemos recibir la información que llega del cliente. Esta información llega como texto plano por lo que será necesario parsearla.

```java
    JsonObject jsonRequest = JsonParser.parseString(mensaje).getAsJsonObject();
    String method = jsonRequest.get("method").getAsString();
    String body = jsonRequest.get("body").getAsString();
  
    //Preparamos la respuesta, que también irá en JSON
    JsonObject jsonResponse = new JsonObject();
```
Puesto que ya tenemos el tipo de método que se quiere aplicar y un cuerpo, será suficiente con aplicar un switch-cas y guardar el resultado dentro de nuestro jsonResponse (la respuesta del servidor).

**NOTA**
Es importante observar que tal y como se ha diseñado el programa, la respuesta del JSON irá en texto plano. Es decir, la respuesta no va a contener un objeto "Libro" o un "ArrayList" como tal. Sino que entregará una cadena de texto plano con "apareciencia" de objeto o de array -aunque sigue siendo texto plano, por lo que no podremos iterar sobre estos objetos o acceder a las propiedades del libro.

### Creación de hilos

Toda esta lógica anteriormente mencionada debe quedar resguardada creando un hilo para cada cliente que haga una interacción con el servidor.  Para ello, crearemos un new Threat y aplicaremos una función lambda con todo lo que sucederá para cada hilo creado.

````java
new Thread(() -> {
    try {
        InputStream entrada = enchufeAlCliente.getInputStream();
        OutputStream salida = enchufeAlCliente.getOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(entrada));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(salida), true);
        String mensaje;

        //Resto del código
````



### Modelos DAO (synchronus) y Libro

En este punto no nos entretendremos demasiado. Es suficinete con saber que tanto el DAO como el Libro encapsulan la lógica de los objetos creados y las operaciones CRUD (aunque en este ejercicio solo se presentan operaciones de consulta y añadido).

Como detalle importante, la operación "añadir" debe quedar bloqueada en caso de que uno de los hilos acceda a ella, y solamente desbloqueará el programa cuando concluya. Para simular este "bloqueo" -y comprobar que efectivamente el programa ha quedado bloqueado- se ha decido implementar un Threat.sleep(3000) para simular que el bloqueo funciona correctamente, es decir, nunca van a llegar varias peticiones de añadir libro simultáneamente.


### Recepción de la respuesta por el cliente

Para cerrar el ciclo, el cliente recibirá una response a cada petición. Para manejo de errores, es importante que el campo de "response" nunca contenga una respuesta nula. Es decir, si no existe un resultado de búsqueda, o después de añadir un libro, debe haber algún tipo de respuesta para evitar el null pointer exception. En este caso, la respuesta será un mensaje que ayudará al cliente a entender qué parámetro de la petición ha resultado infructuoso.

Aquí, repetimos la operación en sentido inverso: parseamos el JSON y obtenemos el contenido de la respuesta:

```java
    JsonObject jsonResponse = JsonParser.parseString(mensaje).getAsJsonObject();
    String response = jsonResponse.get("response").getAsString();
```
