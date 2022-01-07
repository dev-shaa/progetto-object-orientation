/**
 * 
 */
public class Article extends Publication {

    private String ISSN;

    /**
     * 
     * @param title
     *            titolo dell'articolo
     * @throws IllegalArgumentException
     *             se il titolo dell'articolo non è valido
     */
    public Article(String title) throws IllegalArgumentException {
        super(title);
    }

    /**
     * Imposta il codice identificativo ISSN dell'articolo.
     * 
     * @param ISSN
     *            codice identificativo ISSN
     */
    public void setISSN(String ISSN) {
        this.ISSN = ISSN;
    }

    /**
     * Restituisce il codice identificativo ISSN dell'articolo.
     * 
     * @return
     *         codice identificativo ISSN
     */
    public String getISSN() {
        return this.ISSN;
    }

    @Override
    public String getFormattedDetails() {
        return super.getFormattedDetails() + "ISSN:\t" + getISSN() + "\n";
    }

}
