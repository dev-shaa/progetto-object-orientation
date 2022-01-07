public class Book extends Publication {

    private String ISBN;

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

}
