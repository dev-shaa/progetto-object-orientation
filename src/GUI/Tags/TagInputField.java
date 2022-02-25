package GUI.Tags;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import Entities.Tag;

/**
 * Un {@code JTextField} che restituisce dei tag a partire dai termini inseriti dall'utente.
 */
public class TagInputField extends JTextField {

    private final String separator = ",";
    private final String tooltip = "Parole chiave associate al riferimento, separate da una virgola.\nNon fa distinzioni tra maiuscolo e minuscolo.\nEsempio: \"programmazione, object orientation\"";

    /**
     * Crea un nuovo {@code TagInputField}.
     */
    public TagInputField() {
        super();

        setToolTipText(tooltip);
    }

    /**
     * Imposta le parole chiave da mostrare.
     * <p>
     * Se {@code tags} è nullo o vuoto, ha lo stesso effetto di {@link #clear()}.
     * 
     * @param tags
     *            parole chiave da mostrare.
     */
    public void setTags(List<Tag> tags) {
        if (tags == null || tags.isEmpty())
            clear();
        else
            setText(tags.toString().substring(1, tags.toString().lastIndexOf(']')));
    }

    /**
     * Restituisce i tag inseriti dall'utente.
     * 
     * @return lista di tag
     */
    public ArrayList<Tag> getTags() {
        String[] inputText = getText().split(separator);

        ArrayList<Tag> tags = new ArrayList<>(inputText.length);

        for (String tagName : inputText) {
            tagName = tagName.trim();

            // può capitare che l'utente non inserisce nulla tra due virgole, tipo ", ,"
            // ovviamente non vogliamo aggiungerlo all'elenco
            if (tagName.isEmpty() || tagName.isBlank())
                continue;

            Tag tag = new Tag(tagName);

            // non mettiamo ripetizioni
            if (!tags.contains(tag))
                tags.add(tag);
        }

        tags.trimToSize();

        return tags;
    }

    /**
     * Reimposta il campo.
     */
    public void clear() {
        setText(null);
    }

}