package GUI;

import java.util.ArrayList;
import java.util.List;
import Entities.Author;
import Exceptions.Input.InvalidAuthorInputException;
import Utilities.TextField.MultipleTextField;

/**
 * Un pannello che permette all'utente di inserire degli autori.
 */
public class AuthorInputField extends MultipleTextField {

    /**
     * Crea un nuovo {@code AuthorInputField} con un solo campo iniziale.
     */
    public AuthorInputField() {
        super();

        setToolTipText("Un autore del riferimento."
                + "\nÈ possibile specificare l'ORCID mettendolo tra parentesi quadre."
                + "\nEsempio: \"Mario Rossi [0000-0000-0000-0000]\"");
    }

    /**
     * Imposta gli autori da mostrare.
     * <p>
     * Se {@code authors} è nullo o vuoto, ha lo stesso effetto di chiamare {@link #clear()}.
     * 
     * @param authors
     *            autori da mostrare
     */
    public void setAuthors(List<Author> authors) {
        if (authors == null || authors.isEmpty()) {
            clear();
        } else {
            ArrayList<String> authorsAsString = new ArrayList<>(authors.size());

            for (Author author : authors)
                authorsAsString.add(getStringFromAuthor(author));

            setInitialValues(authorsAsString);
        }
    }

    /**
     * Restituisce gli autori inseriti dall'utente.
     * 
     * @return lista di autori
     * @throws InvalidAuthorInputException
     *             se l'utente ha inserito dei valori errati
     */
    public ArrayList<Author> getAuthors() throws InvalidAuthorInputException {
        ArrayList<String> values = getValues();
        ArrayList<Author> authors = new ArrayList<>(values.size());

        for (String value : values) {
            Author author = getAuthorFromString(value);

            if (author != null && !authors.contains(author))
                authors.add(author);
        }

        authors.trimToSize();
        return authors;
    }

    private Author getAuthorFromString(String string) throws InvalidAuthorInputException {
        if (string == null || string.isEmpty() || string.isBlank())
            return null;

        int orcidStartIndex = string.indexOf('[');
        int orcidEndIndex = string.lastIndexOf(']');

        String orcid = null;

        if (orcidStartIndex != -1 && orcidEndIndex != -1 && orcidStartIndex < orcidEndIndex) {
            orcid = string.substring(orcidStartIndex + 1, orcidEndIndex);
        }

        String name = string.substring(0, orcidStartIndex == -1 ? string.length() : orcidStartIndex);

        try {
            return new Author(name, orcid);
        } catch (IllegalArgumentException e) {
            throw new InvalidAuthorInputException("L'ORCID inserito non è valido.");
        }
    }

    private String getStringFromAuthor(Author author) {
        if (author == null)
            return null;

        String output = author.getName();

        if (author.getORCID() != null)
            output += " [" + author.getORCID() + "]";

        return output;
    }

}