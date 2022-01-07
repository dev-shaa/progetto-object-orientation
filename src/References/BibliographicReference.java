import java.util.ArrayList;
import java.util.Date;

/**
 * 
 */
public abstract class BibliographicReference {
    private String title;
    private String author; // FIXME: crea classe apposita
    private Date pubblicationDate;
    private String DOI;
    private String description;
    private ReferenceLanguage language;
    private ArrayList<String> tags;

    /**
     * Crea un riferimento bibliografico con il titolo specificato.
     * 
     * @param title
     *            titolo del riferimento
     * @throws IllegalArgumentException
     *             se il titolo non è una stringa valida
     */
    public BibliographicReference(String title) throws IllegalArgumentException {
        setTitle(title);
        setLanguage(ReferenceLanguage.NOTSPECIFIED);
    }

    /**
     * Imposta il titolo del riferimento.
     * 
     * @param title
     *            titolo del riferimento
     * @throws IllegalArgumentException
     *             se il titolo non è una stringa valida
     */
    public void setTitle(String title) throws IllegalArgumentException {
        if (!isNameValid(title))
            throw new IllegalArgumentException("Il nome del riferimento non può essere nullo.");

        this.title = title;
    }

    /**
     * Restituisce il titolo del riferimento.
     * 
     * @return
     *         titolo del riferimento
     */
    public String getTitle() {
        return this.title;
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
    public void setLanguage(ReferenceLanguage language) throws IllegalArgumentException {
        if (language == null)
            throw new IllegalArgumentException("La lingua del riferimento non può essere nulla.");

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

    // TODO: commenta
    /**
     * Restituisce una stringa di testo formattato contenente tutte le informazioni relative a questo riferimento.
     * 
     * @return
     */
    public String getFormattedDetails() {
        return "Titolo:\t" + getTitle() +
                "\nAutori:\t" + getAuthorsAsSingleString() +
                "\nData di pubblicazione:\t" + getPubblicationDate().toString() +
                "\nDOI:\t" + getDOI() +
                "\nDescrizione:\t" + getDescription() +
                "\nLingua:\t" + getLanguage().toString() +
                "\nParole chiave:\t" + getTagsAsSingleString() + "\n";
    }

    private String getAuthorsAsSingleString() {
        return "";
    }

    private String getTagsAsSingleString() {
        String string = "";

        for (String tag : getTags()) {
            string += tag + ", ";
        }

        return string;
    }

}

enum ReferenceLanguage {
    NOTSPECIFIED, ENGLISH, ITALIAN, FRENCH, GERMAN, SPANISH, RUSSIAN, JAPANESE, CHINESE, ARAB, OTHER
}
