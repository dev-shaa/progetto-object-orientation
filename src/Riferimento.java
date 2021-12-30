// PLACEHOLDER

import java.util.ArrayList;
import java.util.Date;

public class Riferimento {
    public String nome;
    public ArrayList<String> autori;
    public Date data;

    public Riferimento(String nome, ArrayList<String> autori) {
        this.nome = nome;
        this.autori = autori;
        this.data = new Date();
    }

    @Override
    public String toString() {
        String foo = "";
        for (String string : autori) {
            foo += string + ", ";
        }

        return "Nome:\t" + this.nome + "\nAutore:\t" + foo + "\nData:\t" + this.data.toString();
    }
}
