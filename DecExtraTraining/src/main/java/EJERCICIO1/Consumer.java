package EJERCICIO1;


import EJERCICIO1.Model.Item;

import static java.lang.Thread.sleep;

/**
 * Desarrolla una clase hilo llamada Reserva que identifique de forma univoca cada
 * uno de los hilos lanzados, que cuando sea lanzado reserve el primer coche que se
 * encuentre en la lista. (1 pto)
 * */
public class Consumer implements Runnable {
    private static int name;
    private Thread thread;
    private Buffer buffer;

    private Item item;


    public Consumer(Buffer buffer){
        this.buffer = buffer;
        this.thread = new Thread(this, "Consumidor Nº" + name);
        this.thread.start();
        name ++;
    }


    @Override
    public void run() {
        try {
            consume();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void consume() throws InterruptedException {
        System.out.println(this.thread.getName() + " intenta consumir");
        this.buffer.remove();
        System.out.println(this.thread.getName() + " ya ha consumido con éxito");


    }
}
