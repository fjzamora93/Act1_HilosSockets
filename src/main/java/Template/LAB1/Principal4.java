package Template.LAB1;

import java.io.File;
import java.io.IOException;
public class Principal4 {
    public static void main(String[] args) {
        ProcessBuilder proceso;
        proceso = new ProcessBuilder("C:/Windows/System32/notepad.exe", "agenda.txt");
        try {
            proceso.directory(new File("C:\\tmp"));
            proceso.start().waitFor();
            System.out.println("Proceso lanzado con éxito");
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
