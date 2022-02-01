package GUI.Editor;

import java.util.ArrayList;

import Entities.Author;
import GUI.Utilities.JTermsField;

/**
 * TODO: commenta
 */
public class AuthorInputField extends JTermsField {

    /**
     * 
     * @param separator
     */
    public AuthorInputField(String separator) {
        super(separator);
    }

    /**
     * TODO:
     * 
     * @return
     */
    public ArrayList<Author> getAuthors() {
        ArrayList<String> terms = getTerms();

        if (terms == null || terms.isEmpty())
            return null;

        ArrayList<Author> authors = new ArrayList<>(terms.size());

        for (String term : terms) {
            int startIndex = term.indexOf('[');
            int endIndex = term.lastIndexOf(']');

            String orcid = null;
            if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
                orcid = term.substring(startIndex + 1, endIndex).trim();

                if (!Author.isORCIDValid(orcid))
                    orcid = null;
            }

            String name = term.substring(0, startIndex == -1 ? term.length() : startIndex).trim();

            authors.add(new Author(name, orcid));
        }

        authors.trimToSize();

        return authors;
    }

}
