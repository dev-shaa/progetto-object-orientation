package GUI;

import java.util.ArrayList;
import java.util.List;
import Entities.Tag;
import Exceptions.Input.InvalidTagInputException;
import Utilities.TextField.MultipleTextField;

/**
 * Un {@code JTextField} che restituisce dei tag a partire dai termini inseriti dall'utente.
 */
public class TagInputField extends MultipleTextField {

    /**
     * Crea un nuovo {@code TagInputField}.
     */
    public TagInputField() {
        super("Parola chiave associata al riferimento.", Tag.NAME_MAX_LENGTH);
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
        if (tags == null || tags.isEmpty()) {
            clear();
        } else {
            ArrayList<String> tagsValues = new ArrayList<>(tags.size());

            for (Tag tag : tags)
                tagsValues.add(tag.getName());

            setInitialValues(tagsValues);
        }
    }

    /**
     * Restituisce i tag inseriti dall'utente.
     *
     * @return lista di tag
     * @throws InvalidTagInputException
     *             se i tag inseriti non sono validi
     */
    public ArrayList<Tag> getTags() throws InvalidTagInputException {
        ArrayList<String> values = getValues();
        ArrayList<Tag> tags = new ArrayList<>(values.size());

        try {
            for (String value : values) {
                if (value.isEmpty() || value.isBlank() || value.isEmpty())
                    continue;

                Tag tag = new Tag(value);

                // non mettiamo ripetizioni
                if (!tags.contains(tag))
                    tags.add(tag);
            }

            tags.trimToSize();
            return tags;
        } catch (Exception e) {
            throw new InvalidTagInputException("I tag inseriti non sono validi.", e);
        }
    }

}