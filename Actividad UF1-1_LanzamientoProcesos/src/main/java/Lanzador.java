import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lanzador {
    public static void main(String[] args){
        // Obtener el classpath actual del sistema

        String classpath = System.getProperty("java.class.path");

        //Creamos una ArrayList para guardar los procesos
        List<Process> procesos = new ArrayList<>();

        // Como alternativa simplificada, también podríamos haber iterado un for así al lanzar los procesos:
        //  for (int i = 5; i<=9; i += 2)
        String[] numeros = new String[3];
        numeros[0] = "5";
        numeros[1] = "7";
        numeros[2] = "9";

        try {
            for (int i = 0; i<numeros.length; i++){
                /* ARGUMENTOS DEL PROCESS BIULDER:
                1- Java es lo que va alanzar la máquina virtual de java.
                2- -cp es una mierda que dice que el tercer argumento es una ruta.
                3- después van la ruta
                4- El nombre del programa
                5 - los arugmentos ques e le van a pasar
                 */
                ProcessBuilder pb = new ProcessBuilder("java", "-cp", classpath, "Triangulo", numeros[i]);

                //El output irá a la carpeta de "resource"
                pb.redirectOutput(new File("./src/main/java/resource/salida"+numeros[i]+".txt"));
                pb.redirectError(new File("./src/main/java/resource/errores.txt"));

                // Inicializar el proceso (pero aún no lo terminamos)
                Process process = pb.start();
                procesos.add(process);
                System.out.println("Lanzando proceso nº " + i);
            }


            /**
             * Si no se llama al waitFor(), el programa Lanzador va a seguir
             * ejecutándose linea a línea, no va a esperar a que termine la ejecución
             * del proceso
             * */
            for (Process proceso: procesos){
                proceso.waitFor();
                System.out.println("Proceso terminado");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}