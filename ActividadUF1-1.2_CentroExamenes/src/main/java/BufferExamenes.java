import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Queue;
import java.util.LinkedList;

@Getter
@Setter
public class BufferExamenes {
    private Queue<String> colaExamenes ;

    public BufferExamenes() {
        colaExamenes  = new LinkedList<>();
    }

    //MÉTODO PARA AGREGAR
    public synchronized void fabricarNuevoExamen(String codigo) {
        colaExamenes.add(codigo);
        notify();
    }

    //MÉTODO PARA SACAR
    public synchronized String consumirExamen(){
        // Forzamos un tiempo de espera para que no se adelante
        int espera = 0;
        while (colaExamenes.isEmpty() && espera < 20){
            System.out.println("No hay exámenes que consumir");
            espera ++;
            try{
                wait(1000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        if (!colaExamenes.isEmpty()){
            return colaExamenes.remove();
        } else {
            return null;
        }
    }

}
