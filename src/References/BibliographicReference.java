import java.util.ArrayList;
import java.util.Date;

/**
 * 
 */
public abstract class BibliographicReference {
    private String name;
    private String author; // FIXME: crea classe apposita
    private Date pubblicationDate;
    private String DOI;
    private String description;
    private ReferenceLanguage language;
    private ArrayList<String> tags;

    /**
     * Crea un riferimento bibliografico con il titolo specificato.
     * 
     * @param name
     *            titolo del riferimento
     * @throws IllegalArgumentException
     *             se il titolo non è una stringa valida
     */
    public BibliographicReference(String name) throws IllegalArgumentException {
        setName(name);
        setLanguage(ReferenceLanguage.NOTSPECIFIED);
    }

    /**
     * Imposta il titolo del riferimento.
     * 
     * @param name
     *            titolo del riferimento
     * @throws IllegalArgumentException
     *             se il titolo non è una stringa valida
     */
    public void setName(String name) throws IllegalArgumentException {
        if (!isNameValid(name))
            throw new IllegalArgumentException("Il nome del riferimento non può essere nullo.");

        this.name = name;
    }

    /**
     * Restituisce il titolo del riferimento.
     * 
     * @return
     *         titolo del riferimento
     */
    public String getName() {
        return this.name;
    }

    public void setAuthors() {
        // TODO: implementa
    }

    public String getAuthors() {
        // TODO: implementa
        return null;
    }

    /**
     * Imposta la data di pubblicazione del riferimento.
     * 
     * @param pubblicationDate
     *            data di pubblicazione del riferimento
     */
    public void setPubblicationDate(Date pubblicationDate) {
        this.pubblicationDate = pubblicationDate;
    }

    /**
     * Restituisce la data di pubblicazione del riferimento.
     * 
     * @return
     *         data di pubblicazione del riferimento ({@code null} se non è presente)
     */
    public Date getPubblicationDate() {
        return this.pubblicationDate;
    }

    /**
     * Imposta il codice identificativo DOI del riferimento.
     * 
     * @param DOI
     *            codice DOI del riferimento
     */
    public void setDOI(String DOI) {
        this.DOI = DOI;
    }

    /**
     * Restituisce il codice identificativo DOI del riferimento.
     * 
     * @return
     *         codice DOI del riferimento
     */
    public String getDOI() {
        return this.DOI;
    }

    /**
     * Imposta la descrizione del riferimento.
     * 
     * @param description
     *            descrizione del riferimento
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Restituisce la descrizione del riferimento.
     * 
     * @return
     *         descrizione del riferimento ({@code null} se non è presente)
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Imposta la lingua del riferimento.
     * 
     * @param language
     *            lingua del riferimento
     */
    public void setLanguage(ReferenceLanguage language) {
        this.language = language;
    }

    /**
     * Restituisce la lingua del riferimento.
     * 
     * @return
     *         lingua del riferimento
     */
    public ReferenceLanguage getLanguage() {
        return this.language;
    }

    /**
     * Imposta le parole chiave associate al riferimento.
     * 
     * @param tags
     *            parole chiave del riferimento
     */
    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    /**
     * Restituisce le parole chiave associate al riferimento.
     * 
     * @return
     *         parole chiave del riferimento
     */
    public ArrayList<String> getTags() {
        return this.tags;
    }

    /**
     * Controlla se la stringa passata è un nome valido per un riferimento.
     * 
     * @param name
     *            nome da controllare
     * @return
     *         {@code true} se la stringa non è nulla o vuota
     */
    private boolean isNameValid(String name) {
        return name != null && !name.isBlank();
    }

    /**
     * 
     * @return
     */
    public String getFormattedDetails() {
        return "Nome:\t" + getName() + "\nAutore:\t" + this.author + "\nData:\t" + getPubblicationDate().toString();
    }

}

enum ReferenceLanguage {
    NOTSPECIFIED, ENGLISH, ITALIAN, FRENCH, GERMAN, SPANISH, RUSSIAN, JAPANESE, CHINESE, ARAB, OTHER
}
