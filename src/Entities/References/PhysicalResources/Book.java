package Entities.References.PhysicalResources;

import java.util.ArrayList;
import Entities.Author;
import Entities.References.BibliographicReferenceField;

/**
 * Classe che rappresenta un riferimento bibliografico a un libro.
 */
public class Book extends Publication {

    private String ISBN;

    /**
     * Crea un nuovo riferimento a un libro con il titolo indicato.
     * 
     * @param title
     *            titolo del riferimento
     * @throws IllegalArgumentException
     *             se il titolo non è valido
     * @see #setTitle(String)
     */
    public Book(String title, Author[] authors) throws IllegalArgumentException {
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

}
