import java.util.Date;

public class Riferimento {
    public String nome, autore;
    public Date data;

    public Riferimento(String nome, String autore) {
        this.nome = nome;
        this.autore = autore;
        this.data = new Date();
    }
}
