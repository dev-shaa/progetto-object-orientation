package GUI.Utilities;

import java.util.ArrayList;
import javax.swing.JTextField;

/**
 * Un JTextField adattato come campo di ricerca, che restituisce i termini inseriti delimitati da un separatore.
 */
public class JTermsField extends JTextField {
    private String separator;

    /**
     * Crea un campo di ricerca con il separatore indicato.
     * 
     * @param separator
     *            separatore dei termini
     */
    public JTermsField(String separator) {
        this.separator = separator;
    }

    /**
     * Restituisce il separatore.
     * 
     * @return
     *         separatore
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * Imposta l'espressione da usare come separatore.
     * 
     * @param separator
     *            esprresione separatore
     */
    public void setSeparator(String separator) {
        this.separator = separator;
    }

    /**
     * Restituisce i termini inseriti dall'utente che sono stati delimitati dal separatore indicato.</br>
     * Esempio: se il separatore impostato è {@code ,} e l'utente inserisce {@code Casa, Albero, Cane},
     * l'insieme restituito sarà {@code [Casa, Albero, Cane]}.
     * I termini vuoti inseriti non vengono contati.
     * 
     * @return
     *         i termini inseriti dall'utente, {@code null} se non ci sono termini validi
     */
    public String[] getTerms() {
        String[] text = getText().split(separator);

        ArrayList<String> terms = new ArrayList<>();
        for (int i = 0; i < text.length; i++) {
            text[i] = text[i].trim();

            if (!text[i].isBlank())
                terms.add(text[i]);
        }

        if (terms.isEmpty())
            return null;

        return terms.toArray(new String[terms.size()]);
    }

}
