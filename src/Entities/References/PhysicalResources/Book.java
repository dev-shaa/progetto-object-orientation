package Entities.References.PhysicalResources;

/**
 * Classe che rappresenta un riferimento bibliografico a un libro.
 */
public class Book extends Publication {

    private String ISBN;

    public static final int ISBN_LENGTH = 13;

    /**
     * Crea un nuovo riferimento a un libro con il titolo indicato.
     * 
     * @param title
     *            titolo del riferimento
     * @throws IllegalArgumentException
     *             se il titolo non è valido
     * @see #setTitle(String)
     */
    public Book(String title) {
        super(title);
    }

    /**
     * Imposta il codice identificativo ISBN del libro.
     * 
     * @param ISBN
     *            codice ISBN del libro
     */
    public void setISBN(String ISBN) {
        if (isStringNullOrEmpty(ISBN))
            this.ISBN = null;
        else {
            ISBN = ISBN.trim();

            if (ISBN.length() == ISBN_LENGTH)
                this.ISBN = ISBN;
            else
                throw new IllegalArgumentException("Il codice ISBN non è valido.");
        }
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
    public String getInfo() {
        return super.getInfo()
                + "\nISBN: " + (getISBN() == null ? "" : getISBN());
    }

}
