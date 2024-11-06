# Actividad 1. Hilos y Sockets
Fecha de entrega:  24 nov


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

Sin embargo, en este contexto solamente disponemos de una consola que enviará un tipo de operación (del 1 al 5) y un cuerpo asociado a la petición que se esté enviando. Para simplificarlo al máximo, se ha optado por enviar una cadena de texto plano separada por el símbolo "/", que una vez llegue al servidor será dividida y guardada en distintas variables listas para ser usadas en la lógica interna del programa.

### Desarrollo del servidor

La arquitectura del servidor es sutilmente más compleja. En primer lugar, debemos recibir la información que llega del cliente.

```java
```
Una vez hemos recibido la información, es necesario formatearla. Si se tratase de un JSON, aprovecharíamos para acceder a cada valor del objeto a través de la clave (por ejemplo, podríamos acceder al tipo de operación utilizando la clave "method", y al contenido de la operación a través de la clave "body"). 

En nuestro caso, como hemos recibido un String, primeramente separaremos el texto y accederemos a cada índice sabiendo que el 0 recoge el método y el 1, el body. Hecho esto, utilizamos un switch para procesar la solicitud del cliente.

### Creación de hilos

Toda esta lógica anteriormente mencionada debe quedar resguardada creando un hilo para cada cliente que haga una interacción con el servidor. 

Para ello, crearemos un new Threat y aplicaremos una función lambda con todo lo que sucederá para cada hilo creado.

### Modelo DAO - Synchronus

El modelo DAO no ofrecerá mucho misterio, ya que las operaciones de consulta pueden ejecutarse de forma simultanea por diferentes hilos.

Sin embargo, la operación "añadir" debe quedar bloqueada en caso de que uno de los hilos acceda a ella, y solamente desbloqueará el programa cuando concluya. Para simular este "bloqueo" -y comprobar que efectivamente el programa ha quedado bloqueado- se ha decido implementar un Threat.sleep(3000).

Como comentábamos, esta función tiene el único uso de verificar que efectivamente el hilo está bloqueado (no importa cuantas veces el cliente marque la opción de añadir, siempre habrá un bloqueo mínimo de 3 segundos hasta que se curse la siguiente petición).






### Requerimiento 1

Se pide hacer dos programas cliente-servidor con sockets e hilos. 
La aplicación servidora programa consistirá en crear una aplicación
que gestione una serie de libros de una biblioteca virtual, 
la aplicación cliente consumirá dicha aplicación servidora.

Los libros tendrán un ISBN, un título, un autor y un precio. 
Se encontrarán alojados en el servidor. Dicho servidor cuando arranque tendrá 
5 libros preestablecidos con todos los datos rellenos. 
Los libros se guardarán en memoria en cualquier tipo de estructura de datos 
(como puede ser un lista). 

El servidor deberá estar preparado para que interactúen 
con él varios clientes (se deberá abrir un hilo por cada cliente).

### 2 formas de hacer el ejercicio
Hay 2 formas de plantear el ejercicio: 2 maquinas virtuales una actua 
como cliente y otra como servidor o que el propio codigo 
corra en una única máquina abriendo 2 pestañas de ejecución 
independientes en intellij y en cada una de ellas correrá 
de forma independiente el Servidor y el CLIENTE.main.java.Cliente.  

Personalmente creo que esta es la forma mas sencilla, 
pero estoy abierto a ambas propuestas.

La aplicación cliente mostrará un menú como el que sigue:

- Consultar libro por ISBN
- Consultar libro por titulo
- Salir de la aplicación
- La aplicación se ejecutará hasta que el cliente decida pulsar la opción de “salir de la aplicación”.


El cliente deberá de recoger todos los datos del usuario 
necesarios y mandarlos al servidor en un solo envio.



### Requerimiento 2

Se pide añadir otra opción que sea “Consultar libros por autor”. 
En este caso hay que tener en cuenta que puede haber varios libros por autor, 
por lo que el servidor podrá devolver una lista de libros. 

Se recomienda pensar en grupo el formato de envio de información.



### Requerimiento 3

Se pide añadir otra opción que sea “Añadir libro”. 
En este caso el cliente pedirá todos los datos del libro y 
los enviará al servidor para que este lo guarde en el servidor. 

La lista en el servidor deberá estar preparada para que solo pueda añadir
un libro cada hilo a la vez, si algún hilo está agregando un libro, 
los demás hilos deberán de esperar a que el hilo acabe.

El cliente deberá de recoger todos los datos del usuario y mandarlos al servidor en un solo envio.
