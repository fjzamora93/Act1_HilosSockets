package EJERCICIO1;


import EJERCICIO1.Buffer;
import EJERCICIO1.Productor;
import EJERCICIO1.Consumer;

import static java.lang.Thread.sleep;

public class Principal {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("___INICIALIANDO LA PRODUCCIÓN____");
        Buffer buffer = new Buffer();
        Consumer consumer1 = new Consumer(buffer);
        Productor productor1 = new Productor(buffer);
        Consumer consumer2 = new Consumer(buffer);
        Productor productor2 = new Productor(buffer);
        Consumer consumer3 = new Consumer(buffer);
        Productor productor3 = new Productor(buffer);
        sleep(1000);
        buffer.displayList();
    }

}


