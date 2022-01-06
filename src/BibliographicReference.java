// PLACEHOLDER

// import java.util.ArrayList;
import java.util.Date;

public class BibliographicReference {
    public String name;
    public String author;
    public Date data;

    public BibliographicReference(String name, String author) {
        this.name = name;
        this.author = author;
        this.data = new Date();
    }

    @Override
    public String toString() {
        return "Nome:\t" + this.name + "\nAutore:\t" + this.author + "\nData:\t" + this.data.toString();
    }
}
