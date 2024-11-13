import java.time.LocalDateTime;

public class ProductorExamenes implements Runnable{
    private BufferExamenes buffer;
    private static int numeroExamen = 0;
    private Thread hilo;
    private int examenesRequeridos;

    public ProductorExamenes(BufferExamenes buffer, int argsLength) {
        numeroExamen ++;
        this.buffer = buffer;
        this.examenesRequeridos = argsLength;
        hilo = new Thread(this, "E"+numeroExamen);
        hilo.start();
    }

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

}

