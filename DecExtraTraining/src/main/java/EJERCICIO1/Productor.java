package EJERCICIO1;


import EJERCICIO1.Model.Item;

/**
 * Desarrolla una clase hilo llamado Devolucion que pueda identificar de forma
 * univoca cada uno de los hilos lanzados, que de forma aleatoria genere un tipo de
 * coche (del 1 al 5) e introduzca ese nuevo tipo de coche en el BufferCoche. (1pto) */
public class Productor implements Runnable{
    private Thread thread;
    private static int name;
    private Buffer buffer;

    private int randInt;

    public Productor(Buffer buffer){
        this.buffer = buffer;
        this.thread = new Thread(this, "Productor Nº" + name);
        this.thread.start();
        name ++;
    }


    @Override
    public void  run() {
        produce();
    }

    public void  produce(){
        this.randInt = (int) (Math.random() * 5) + 1;
        Item newItem = new Item(randInt);
        System.out.println(this.thread.getName() + " ha producido el siguietne objeto:  " + newItem);
        this.buffer.add(newItem);

    }
}
