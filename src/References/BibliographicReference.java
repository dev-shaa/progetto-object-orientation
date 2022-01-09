import java.util.ArrayList;
import java.util.Date;

/**
 * 
 */
public abstract class BibliographicReference {
    private String title;
    private ArrayList<Author> authors;
    private Date pubblicationDate;
    private String DOI;
    private String description;
    private ReferenceLanguage language;
    private ArrayList<String> tags;
    private ArrayList<BibliographicReference> relatedReferences; // FIXME:

    /**
     * Crea un riferimento bibliografico con il titolo specificato.
     * La lingua verrà impostata come Sconosciuta.
     * 
     * @param title
     *            titolo del riferimento
     * @throws IllegalArgumentException
     *             se il titolo non è una stringa valida
     */
    public BibliographicReference(String title) throws IllegalArgumentException {
        setTitle(title);
        setLanguage(ReferenceLanguage.Sconosciuta);

        setAuthors(new ArrayList<Author>(1));
        setTags(new ArrayList<String>(5));
        setRelatedReferences(new ArrayList<BibliographicReference>(5));
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
        if (!isTitleValid(title))
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

    /**
     * 
     * @param authors
     * @throws IllegalArgumentException
     */
    public void setAuthors(ArrayList<Author> authors) throws IllegalArgumentException {
        if (authors == null)
            throw new IllegalArgumentException("authors non può essere null");

        this.authors = authors;
    }

    /**
     * Aggiunge un autore a questo riferimento.
     * 
     * @param author
     *            autore da aggiungere
     * @throws IllegalArgumentException
     */
    public void addAuthor(Author author) throws IllegalArgumentException {
        if (author == null)
            throw new IllegalArgumentException("authors non può essere null");

        authors.add(author);
    }

    /**
     * 
     * @return
     */
    public ArrayList<Author> getAuthors() {
        return authors;
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
     *         data di pubblicazione del riferimento ({@code null} se non è indicata)
     */
    public Date getPubblicationDate() {
        return pubblicationDate;
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
     *         codice DOI del riferimento ({@code null} se non è indicato)
     */
    public String getDOI() {
        return DOI;
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
     *         descrizione del riferimento ({@code null} se non è indicata)
     */
    public String getDescription() {
        return description;
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
        return language;
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
        return tags;
    }

    /**
     * TODO:
     * 
     * @param relatedReferences
     */
    public void setRelatedReferences(ArrayList<BibliographicReference> relatedReferences) {
        this.relatedReferences = relatedReferences;
    }

    /**
     * TODO:
     * 
     * @return
     */
    public ArrayList<BibliographicReference> getRelatedReferences() {
        return relatedReferences;
    }

    /**
     * Controlla se la stringa passata è un titolo valido per un riferimento.
     * 
     * @param title
     *            titolo da controllare
     * @return
     *         {@code true} se la stringa non è nulla o vuota
     */
    public boolean isTitleValid(String title) {
        return title != null && !title.isBlank();
    }

    /**
     * 
     * @return
     */
    public ArrayList<BibliographicReferenceField> getReferenceFields() {
        ArrayList<BibliographicReferenceField> fields = new ArrayList<>();

        fields.add(new BibliographicReferenceField("Titolo", getTitle()));
        fields.add(new BibliographicReferenceField("Autori", getAuthorsAsString()));
        fields.add(new BibliographicReferenceField("Data di pubblicazione", getPubblicationDate()));
        fields.add(new BibliographicReferenceField("DOI", getDOI()));
        fields.add(new BibliographicReferenceField("Descrizione", getDescription()));
        fields.add(new BibliographicReferenceField("Lingua", getLanguage()));
        fields.add(new BibliographicReferenceField("Parole chiave", getTagsAsSingleString()));

        return fields;
    }

    /**
     * Restituisce gli autori di questo riferimento come un'unica stringa.
     * Esempio: "Mario Rossi, Ciro Esposito"
     * 
     * @return
     *         gli autori come unica stringa
     */
    public String getAuthorsAsString() {
        return getAuthors().toString().substring(1).replace("]", "").trim();
    }

    /**
     * Restituisce le parole chiave di questo riferimento come un'unica stringa.
     * Esempio: "Informatica, Object Orientation"
     * 
     * @return
     *         le parole chiave come unica stringa
     */
    public String getTagsAsSingleString() {
        return getTags().toString().substring(1).replace("]", "").trim();
    }

}

enum ReferenceLanguage {
    Sconosciuta, Italiano, Inglese, Francese, Tedesco, Spagnolo, Russo, Giapponese, Cinese, Arabo, Altro
}
