import java.util.ArrayList;

/**
 * 
 */
public class Book extends Publication {

    private String ISBN;

    /**
     * 
     * @param title
     * @throws IllegalArgumentException
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
    public ArrayList<BibliographicReferenceField> getInfoAsStrings() {
        ArrayList<BibliographicReferenceField> fields = super.getInfoAsStrings();

        fields.add(new BibliographicReferenceField("ISBN", getISBN()));

        return fields;
    }

}
