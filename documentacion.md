# Actividad 1. Hilos y Sockets
Fecha de entrega:  24 nov

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
de forma independiente el Servidor y el CLIENTE.Cliente.  

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
