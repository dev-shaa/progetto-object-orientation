import java.util.ArrayList;

/**
 * 
 */
public class Article extends Publication {

    private String ISSN;

    /**
     * Crea un riferimento a un articolo di una pubblicazione con un titolo indicato.
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
     *         codice identificativo ISSN ({@code null} se non è indicato)
     */
    public String getISSN() {
        return this.ISSN;
    }

    // @Override
    // public String getFormattedDetails() {
    // return super.getFormattedDetails() +
    // "ISSN:\t" + getISSN() + "\n";
    // }

    @Override
    public ArrayList<BibliographicReferenceField> getInfoAsStrings() {
        ArrayList<BibliographicReferenceField> fields = super.getInfoAsStrings();

        fields.add(new BibliographicReferenceField("ISSN", getISSN()));

        return fields;
    }
}
