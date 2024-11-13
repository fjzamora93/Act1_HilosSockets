# Actividad Actividad UF1-2. Multitarea. Centro de exámenes

Comenzamos modificando los argumentos que le vamos a pasar a la case principal y creamos un atributo que será el "número de argumentos" (este atributo nos servirá para representar posteriormente cuántos exámenes es necesario realizar).


```java

public static void main(String[] args) throws InterruptedException {
        if (args.length == 0) {
            System.out.println("Introduce los alumnos que se van a examinar.");
            return;
        }
        int argsLength = args.length;
        
        // resto del código

        for (String nombre : args) {
            System.out.println("Nuevo examinado: " + nombre);
            new Examinado(nombre, generador);
        }
}
```

La segunda parte del método Principal simplemente se encargará de instanciar a cada examinado en un bucle for.

## Clase ProductorExamenes
El buffer de exámenes y los examinados no sufrirán ninguna modificación. Sin embargo, a diferencia del ejercicio Centro de Exámenes anterior, ahora solamente existe un examinador para cada proceso. Eso quiere decir que cada examinador debe preparar tantos exámenes como argumentos haya en la clase Principal. Por lo tanto, nos iremos a la clase ProductorExamenes y realizaremos una pequeña modificación:

````java
    @Override
    public void run() {
        for (int i = 0; i < this.examenesRequeridos; i++) {
            // Generación del código de examen.
            int aa = LocalDateTime.now().getYear();
            String codigo = this.hilo.getName() + "-" +aa;
            buffer.fabricarNuevoExamen(codigo);
            System.out.println(hilo.getName() + " Se ha generado examen con código = " + codigo);

        }
    }
    
````

## Clase Lanzador

La nueva clase que nos propone el ejercicio es un Lanzador, igual que veíamos en la primera tarea (la del Triángulo).
Aquí necesitaremos realizar las siguietnes operaciones:
1. Crear nuevos procesos a partir del directorio actual (usamos el System.getProperty("java.class.path")).
2. Redirigir la salida y los posibles errores a un fichero de texto.
3. Esperar a que los procesos terminen.

````java


public class Lanzador {
    public static void main(String[] args) {
        String classpath = System.getProperty("java.class.path");

        try {
            // Lanzar proceso para el primer grupo y redirigir salida a examen1.txt
            ProcessBuilder proceso1 = new ProcessBuilder("java", "-cp", classpath, "Principal", "Pepe", "Juan", "Luis");
            proceso1.redirectOutput(new File("./src/resources/examen1.txt"));
            proceso1.redirectError(new File("./src/resources/examen1_error.txt"));  // Opcional para errores
            Process p1 = proceso1.start();

            // Repetimos la operación con el proceso número 2
            // Esperar a que los procesos terminen
            p1.waitFor();
            System.out.println("Exámenes completados y guardados en examen1.txt y examen2.txt.");
        }

````

## Resultados

Si navegamos al directorio src/resources, encontraremos 4 ficheros de texto. Si todo ha salido bien, veremos que tanto examen1.txt como examen2.txt contienen los resultados la ejecución con éxito.