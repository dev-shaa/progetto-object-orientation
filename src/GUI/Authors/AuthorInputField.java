package GUI.Authors;

import java.util.ArrayList;

import Entities.Author;
import Exceptions.InvalidAuthorInputException;
import GUI.Utilities.TermsField;

/**
 * Un {@code JTermsField} che restituisce degli autori a partire dai termini inseriti dall'utente.
 */
public class AuthorInputField extends TermsField {

    /**
     * Crea un nuovo {@code AuthorInputField} con il separatore indicato.
     * 
     * @param separator
     *            separatore dei termini
     */
    public AuthorInputField(String separator) {
        super(separator);

        super.setToolTipText("Autori del riferimento, separati da una virgola.\n"
                + "È possibile specificare l'ORCID mettendolo tra parentesi quadre.\n"
                + "Esempio: \"Mario Rossi [0000-0000-0000-0000], Luigi Bianchi\"");
    }

    /**
     * Restituisce gli autori dai termini inseriti dall'utente.
     * 
     * @return lista di autori
     */
    public ArrayList<Author> getAuthors() throws InvalidAuthorInputException {
        ArrayList<String> terms = getTerms();

        if (terms == null || terms.isEmpty())
            return null;

        ArrayList<Author> authors = new ArrayList<>(terms.size());

        for (String term : terms) {
            int orcidStartIndex = term.indexOf('[');
            int orcidEndIndex = term.lastIndexOf(']');

            String orcid = null;

            if (orcidStartIndex != -1 && orcidEndIndex != -1 && orcidStartIndex < orcidEndIndex) {
                orcid = term.substring(orcidStartIndex + 1, orcidEndIndex);
            }

            String name = term.substring(0, orcidStartIndex == -1 ? term.length() : orcidStartIndex);

            try {
                authors.add(new Author(name, orcid));
            } catch (IllegalArgumentException e) {
                throw new InvalidAuthorInputException("L'ORCID inserito non è valido.");
            }
        }

        authors.trimToSize();

        return authors;
    }

}
