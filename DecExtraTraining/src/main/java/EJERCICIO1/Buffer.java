package EJERCICIO1;
import java.util.LinkedList;
import lombok.*;
import EJERCICIO1.Model.Item;


@Getter
@Setter
public class Buffer {

    private LinkedList<Item> itemList;

    public Buffer(){
        this.itemList = new LinkedList<>();
    }


    /** El método poner se encarga de añadir un nuevo tipo de coche a la lista */
    public synchronized  void add(Item item){
        displayList();
        itemList.add(item);
        notifyAll();
    }

    /** el método sacar que devuelva el primer valor de la lista y elimine el tipo de coche de la misma (1 pto) */
    public synchronized void remove() throws InterruptedException {
        displayList();
        while (this.itemList.isEmpty()){
            System.out.println("Consumidor esperando");
            wait();
        }
        this.itemList.remove();

    }

    public void displayList(){
        System.out.println("Estado actual de la cola: ");
        if (this.itemList.size() != 0) {
            for (Item item: this.itemList){
                System.out.println("-" + item.toString());
            }
        } else {
            System.out.println("La cola esta vacía ahora");
        }


    }

}
