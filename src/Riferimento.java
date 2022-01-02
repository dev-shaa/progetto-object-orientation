// PLACEHOLDER

// import java.util.ArrayList;
import java.util.Date;

public class Riferimento {
    public String nome;
    public String autore;
    public Date data;

    public Riferimento(String nome, String autore) {
        this.nome = nome;
        this.autore = autore;
        this.data = new Date();
    }

    @Override
    public String toString() {
        return "Nome:\t" + this.nome + "\nAutore:\t" + autore + "\nData:\t" + this.data.toString();
    }
}
