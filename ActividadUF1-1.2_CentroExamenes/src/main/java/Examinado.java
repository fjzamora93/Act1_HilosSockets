import java.util.Random;



public class Examinado implements Runnable {
    private Thread hilo;
    private BufferExamenes buffer;
    private static int contador = 0;

    private String nombre;

    String[] options = {"A", "B", "C", "D", "E"};
    Random random = new Random();

    public Examinado(String alumno, BufferExamenes buffer) {
        contador++;
        hilo = new Thread(this, "Consumidor" + contador);
        this.buffer = buffer;
        this.nombre = alumno;
        hilo.start();
    }

    @Override
    public void run() {
        String codigoExamen = this.buffer.consumirExamen();
        if (codigoExamen != null) {
            int contadorExamen = 1;
            while(contadorExamen <= 10){
                int randomIndex = random.nextInt(options.length);
                String respuestaPregunta = options[randomIndex];
                System.out.println(codigoExamen + " - " + this.nombre + " Pregunta: " + contadorExamen + respuestaPregunta);
                contadorExamen ++;
            } System.out.println(codigoExamen + " - " + this.nombre + "- Examen terminado.");
        }
        else {
            System.out.println("Agotado tiempo de espera y " + "no hay más exámenes");
        }

    }
}