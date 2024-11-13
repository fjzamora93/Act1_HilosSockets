package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class Libro {
    private String ISBN;
    private String title;
    private String author;
    private String prize;

    public Libro(String ISBN, String title, String author, String prize) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.prize = prize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Libro)) return false;
        Libro libro = (Libro) o;
        return Objects.equals(ISBN, libro.ISBN);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ISBN);
    }

    @Override
    public String toString() {
        return "Libro{" +
                "ISBN='" + ISBN + '\'' +
                ", titulo='" + title + '\'' +
                ", autor='" + author + '\'' +
                ", prize=" + prize +
                '}' + '\n';
    }



}
