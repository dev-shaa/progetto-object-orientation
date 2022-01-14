package GUI.Utilities;

import java.util.ArrayList;
import javax.swing.JTextField;

/**
 * Un {@code JTextField} adattato come campo di ricerca, che restituisce i termini inseriti delimitati da un separatore.
 */
public class JTextSearchField extends JTextField {
    private String separator;

    /**
     * Crea un campo di ricerca con il separatore indicato.
     * 
     * @param separator
     *            separatore dei termini
     */
    public JTextSearchField(String separator) {
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
     * l'array restituito sarà {@code [Casa, Albero, Cane]}.
     * I termini vuoti inseriti non vengono contati.
     * 
     * @return
     *         i termini inseriti dall'utente
     */
    public String[] getSearchTerms() {
        String[] text = getText().split(separator);

        ArrayList<String> terms = new ArrayList<>();
        for (int i = 0; i < text.length; i++) {
            text[i] = text[i].trim();

            if (!text[i].isBlank())
                terms.add(text[i]);
        }

        return terms.toArray(new String[terms.size()]);
    }

}
