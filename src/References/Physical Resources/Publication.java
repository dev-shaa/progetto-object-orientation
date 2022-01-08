import java.util.ArrayList;

/**
 * 
 */
public abstract class Publication extends BibliographicReference {

    private Integer pageCount;
    private String URL;
    private String publisher;

    /**
     * 
     * @param title
     *            titolo della pubblicazione
     * @throws IllegalArgumentException
     *             se il titolo non è valido
     */
    public Publication(String title) throws IllegalArgumentException {
        super(title);
    }

    /**
     * Imposta il numero di pagine della pubblicazione.
     * 
     * @param pageCount
     *            numero di pagine della pubblicazione
     * @throws IllegalArgumentException
     *             se {@code pageCount <= 0}
     */
    public void setPageCount(Integer pageCount) throws IllegalArgumentException {
        if (pageCount != null && pageCount <= 0)
            throw new IllegalArgumentException("Il numero di pagine del libro non può essere inferiore a 1");

        this.pageCount = pageCount;
    }

    /**
     * Restituisce il numero di pagine della pubblicazione.
     * 
     * @return
     *         numero di pagine della pubblicazione ({@code null} se non è indicato)
     */
    public Integer getPageCount() {
        return this.pageCount;
    }

    /**
     * Imposta l'URL del riferimento
     * 
     * @param URL
     *            URL del riferimento
     */
    public void setURL(String URL) {
        this.URL = URL;
    }

    /**
     * Restituisce l'URL del riferimento.
     * 
     * @return
     *         URL del riferimento ({@code null} se non è indicato)
     */
    public String getURL() {
        return this.URL;
    }

    /**
     * Imposta l'editore della pubblicazione.
     * 
     * @param publisher
     *            editore della pubblicazione
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * Restituisce l'editore della pubblicazione.
     * 
     * @return
     *         editore della pubblicazione ({@code null} se non è indicato)
     */
    public String getPublisher() {
        return this.publisher;
    }

    // @Override
    // public String getFormattedDetails() {
    // return super.getFormattedDetails() +
    // "Numero di pagine:\t" + getPageCount() +
    // "\nURL:\t" + getURL() +
    // "\nEditore:\t" + getPublisher() + "\n";
    // }

    @Override
    public ArrayList<BibliographicReferenceField> getInfoAsStrings() {
        ArrayList<BibliographicReferenceField> fields = super.getInfoAsStrings();

        fields.add(new BibliographicReferenceField("Numero di pagine", getPageCount()));
        fields.add(new BibliographicReferenceField("URL", getURL()));
        fields.add(new BibliographicReferenceField("Editore", getPublisher()));

        return fields;
    }

}