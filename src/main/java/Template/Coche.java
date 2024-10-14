package Template;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coche implements Serializable {
    private static final long serialVersionUID =  1L;
    private static int nextId = 1;

    private int id;
    private String matricula;
    private String marca;
    private String modelo;
    private String color;


    public Coche(int id){
        this.id = id;
    }

    public Coche(String matricula, String marca, String modelo, String color) {
        this.id = nextId++;
        this.matricula = matricula;
        this.marca = marca + "_" + this.id;
        this.modelo = modelo + "_" + this.id;
        this.color = color + "_" + this.id;
    }

    public void mostrarDatos(){
        System.out.println("serialVersionUID = " + serialVersionUID);
        System.out.println("id = " + id);
        System.out.println("title = " + matricula);
        System.out.println("price = " + marca);
        System.out.println("stock = " + modelo);
        System.out.println("stock = " + color);
    }


    public static void setNextId(int maxId){
        nextId = maxId +1;
    }

    @Override
    public String toString() {
        return "Coche{" +
                "id=" + id +
                ", matricula='" + matricula + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coche)) return false;
        Coche coche = (Coche) o;
        return id == coche.id || Objects.equals(matricula, coche.matricula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, matricula);
    }
}
