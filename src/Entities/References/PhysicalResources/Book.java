package Entities.References.PhysicalResources;

import Entities.Author;
import Entities.References.*;
import java.util.ArrayList;
// import java.util.regex.Pattern;

/**
 * Classe che rappresenta un riferimento bibliografico a un libro.
 */
public class Book extends Publication {

    private String ISBN;

    // private final Pattern isbnPattern = Pattern.compile("^[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{3}[0-9xX]$", Pattern.CASE_INSENSITIVE); // TODO: regex

    /**
     * Crea un nuovo riferimento a un libro con il titolo e gli autori indicati.
     * 
     * @param title
     *            titolo del riferimento
     * @param authors
     *            autori del riferimento
     * @throws IllegalArgumentException
     *             se il titolo non è valido
     * @see #setTitle(String)
     */
    public Book(String title, Author[] authors) throws IllegalArgumentException {
        super(title, authors);
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
