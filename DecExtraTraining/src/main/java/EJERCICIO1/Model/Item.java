package EJERCICIO1.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private int number;
    private String type;

    public Item(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Item{" +
                "number=" + number +
                ", type='" + type + '\'' +
                '}';
    }


}
