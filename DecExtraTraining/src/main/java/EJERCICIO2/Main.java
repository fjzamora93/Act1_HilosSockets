package EJERCICIO2;

public class Main {
    public static void main(String[] args){

        Server server = new Server("First Param","Second Param", 12233);
        Client client = new Client(12233);

    }
}
