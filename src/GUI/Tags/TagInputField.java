package GUI.Tags;

import java.util.ArrayList;

import Entities.Tag;
import GUI.Utilities.TermsField;

/**
 * Un {@code TermsField} che restituisce dei tag a partire dai termini inseriti dall'utente.
 */
public class TagInputField extends TermsField {

    /**
     * Crea un nuovo {@code TagInputField} con il separatore indicato.
     * 
     * @param separator
     *            separatore dei termini
     */
    public TagInputField(String separator) {
        super(separator);

        super.setToolTipText("Parole chiave associate al riferimento, separate da una virgola.\n"
                + "Non fa distinzioni tra maiuscolo e minuscolo\n"
                + "Esempio: \"programmazione, object orientation\"");
    }

    /**
     * Restituisce i tag dai termini inseriti dall'utente.
     * 
     * @return lista di tag
     */
    public ArrayList<Tag> getTags() {
        ArrayList<String> tagsString = getTerms();

        if (tagsString == null)
            return null;

        ArrayList<Tag> tags = new ArrayList<>(tagsString.size());

        for (String string : tagsString) {
            Tag tag = new Tag(string);

            if (!tags.contains(tag))
                tags.add(new Tag(string));
        }

        tags.trimToSize();

        return tags;
    }

}
