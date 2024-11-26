public class Principal {
    public static void main(String[] args) throws InterruptedException {
        if (args.length == 0) {
            System.out.println("Introduce los alumnos que se van a examinar.");
            return;
        }
        int argsLength = args.length;

        BufferExamenes generador = new BufferExamenes();

        // Creamos ProductorExamenes
        new ProductorExamenes(generador, argsLength);

        // Creamos un Examinado por cada argumento
        for (String nombre : args) {
            System.out.println("Nuevo examinado: " + nombre);
            new Examinado(nombre, generador);
        }
    }
}


