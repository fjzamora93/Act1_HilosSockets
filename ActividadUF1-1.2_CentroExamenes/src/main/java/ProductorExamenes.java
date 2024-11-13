import java.time.LocalDateTime;

public class ProductorExamenes implements Runnable{
    private BufferExamenes buffer;
    private static int numeroExamen = 0;
    private Thread hilo;

    public ProductorExamenes(BufferExamenes buffer) {
        numeroExamen ++;
        this.buffer = buffer;
        hilo = new Thread(this, "E"+numeroExamen);
        hilo.start();
    }

    @Override
    public void run() {
        // Generación del código de examen.
        int aa = LocalDateTime.now().getYear();
        String codigo = this.hilo.getName() + "-" +aa;

        // Añade el nuevo código al buffer de exámenes.

        buffer.fabricarNuevoExamen(codigo);

        // Muestra un mensaje en consola informando sobre el código del examen que se acaba de producir.
        System.out.println(hilo.getName() + " Se ha generado examen con código = " + codigo);



    }
}

