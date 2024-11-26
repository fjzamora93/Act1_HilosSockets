import java.io.File;
import java.io.IOException;

public class Lanzador {
    public static void main(String[] args) {
        String classpath = System.getProperty("java.class.path");

        try {
            // Lanzamos el primer proceso y redirigimos la salida
            ProcessBuilder proceso1 = new ProcessBuilder("java", "-cp", classpath, "Principal", "Pepe", "Juan", "Luis");
            proceso1.redirectOutput(new File("./src/resources/examen1.txt"));
            proceso1.redirectError(new File("./src/resources/examen1_error.txt"));  // Opcional para errores
            Process p1 = proceso1.start();

            // Lanzamos el segundo proceso y redirigimos la salida
            ProcessBuilder proceso2 = new ProcessBuilder("java", "-cp", classpath, "Principal", "Rosa", "Miguel", "Pedro");
            proceso2.redirectOutput(new File("./src/resources/examen2.txt"));
            proceso2.redirectError(new File("./src/resources/examen2_error.txt"));  // Opcional para errores
            Process p2 = proceso2.start();

            // Esperamos a que los procesos terminen
            p1.waitFor();
            p2.waitFor();

            System.out.println("Exámenes completados y guardados en examen1.txt y examen2.txt.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
