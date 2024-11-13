/*
Comando para compilar sin problemas de versiones:
javac -source 1.8 -target 1.8 Triangulo.java Lanzador.java

    -Para comenzar, partirás del siguiente programa Java (el de aquí abajo).

    -Se trata de un programa de consola, que requiere que el usuario introduzca un número como argumento
    y avisa en caso de que no sea introducido.

    -Crea otro programa Java que inicie tres procesos simultáneos lanzando
    tres veces el programa Triangulo con los parámetros 5, 7 y 9.

    -La salida de los tres procesos debe ser enviada a los archivos triangulo5.txt, triangulo7.txt y triangulo9.txt.

    -Al principio de cada fichero de salida y antes del triángulo,
    debe escribirse la fecha de inicio del proceso. Para ello, tendrás que modificar la clase Triangulo.

    -Al final de cada fichero de salida, después del triángulo debes escribir la fecha de finalización del proceso.
    Para ello, tendrás que modificar la clase Triangulo.

    */
import java.time.LocalDateTime;

public class Triangulo {
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
