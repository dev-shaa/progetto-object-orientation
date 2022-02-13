package GUI.Tags;

import java.util.ArrayList;

import javax.swing.JTextField;

import Entities.Tag;

/**
 * Un {@code JTextField} che restituisce dei tag a partire dai termini inseriti dall'utente.
 */
public class TagInputField extends JTextField {

    private final String separator = ",";

    /**
     * Crea un nuovo {@code TagInputField}.
     */
    public TagInputField() {
        super();

        setToolTipText("Parole chiave associate al riferimento, separate da una virgola.\n"
                + "Non fa distinzioni tra maiuscolo e minuscolo.\n"
                + "Esempio: \"programmazione, object orientation\"");
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

            if (tagName.isEmpty() || tagName.isBlank())
                continue;

            Tag tag = new Tag(tagName);

            if (!tags.contains(tag))
                tags.add(tag);
        }

        tags.trimToSize();

        return tags;
    }

}
