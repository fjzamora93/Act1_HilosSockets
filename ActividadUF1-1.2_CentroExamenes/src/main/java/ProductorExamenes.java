import java.time.LocalDateTime;

public class ProductorExamenes implements Runnable{
    private BufferExamenes buffer;
    private static int numeroExamen = 0;
    private Thread hilo;



    /**
     * Para instanciar la clase Thread, tenemos dos argumentos:
     * 1. El primero será this (es decir, la propia instancia de la clase).
     * 2. El segundo es el "nombre" del hilo, de ahí que se acceda con getName().
     *
     * */
    public ProductorExamenes(BufferExamenes buffer) {
        numeroExamen ++;
        this.buffer = buffer;
        hilo = new Thread(this, "E"+numeroExamen);
        hilo.start();
    }

    /**
     * El método run() será llamado dentro del constructor.
     * Esto sucede cuando decimos this.hilo.start(), que al ser de la clase "Thread"
     * podrá activar el método run de la instancia que hayamos creado de esta clase (this).
     * */
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

