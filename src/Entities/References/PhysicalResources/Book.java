package Entities.References.PhysicalResources;

import Entities.References.*;
import java.util.ArrayList;
// import java.util.regex.Pattern;

/**
 * 
 */
public class Book extends Publication {

    private String ISBN;

    // private final Pattern isbnPattern = Pattern.compile("^[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{3}[0-9xX]$", Pattern.CASE_INSENSITIVE);

    /**
     * Crea un riferimento a un libro con il titolo indicato.
     * 
     * @param title
     *            titolo del riferimento
     * @throws IllegalArgumentException
     *             se il titolo non è valido
     */
    public Book(String title) throws IllegalArgumentException {
        super(title);
    }

    /**
     * Imposta il codice identificativo ISBN del libro.
     * 
     * @param ISBN
     *            codice ISBN del libro
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * Restituisce il codice identificato ISBN del libro.
     * 
     * @return
     *         codice ISBN del libro
     */
    public String getISBN() {
        return this.ISBN;
    }

    @Override
    public ArrayList<BibliographicReferenceField> getReferenceFields() {
        ArrayList<BibliographicReferenceField> fields = super.getReferenceFields();

        fields.add(new BibliographicReferenceField("ISBN", getISBN()));

        return fields;
    }

    // private boolean isISBNValid(String string) {
    // return isbnPattern.matcher(string).find();
    // }

}
