import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Classe che rappresenta un riferimento bibiliografico.
 */
public abstract class BibliographicReference {
    private String title;
    private ArrayList<Author> authors;
    private Date pubblicationDate;
    private String DOI;
    private String description;
    private ReferenceLanguage language;
    private ArrayList<String> tags;
    private ArrayList<BibliographicReference> relatedReferences;

    /**
     * Crea un riferimento bibliografico con il titolo specificato.
     * La lingua verrà impostata come Sconosciuta.
     * 
     * @param title
     *            titolo del riferimento
     * @throws IllegalArgumentException
     *             se il titolo non è una stringa valida
     * @see #setTitle(String)
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
     * @see #isTitleValid(String)
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
     * Imposta gli autori di questo riferimento.
     * 
     * @param authors
     *            autori del riferimento
     * @throws IllegalArgumentException
     *             se {@code authors == null}
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
     *             se {@code author == null}
     */
    public void addAuthor(Author author) throws IllegalArgumentException {
        if (author == null)
            throw new IllegalArgumentException("authors non può essere null");

        authors.add(author);
    }

    /**
     * Restituisce gli autori di questo riferimento.
     * 
     * @return un array di {@code Author} contenente tutti gli autori
     */
    public Author[] getAuthors() {
        return authors.toArray(new Author[authors.size()]);
    }

    /**
     * Imposta la data di pubblicazione del riferimento.
     * 
     * @param pubblicationDate
     *            data di pubblicazione del riferimento ({@code null} se non è indicato)
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
        // TODO: controllo regex

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
     * @throws IllegalArgumentException
     *             se {@code language == null}
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
    public String[] getTags() {
        return tags.toArray(new String[tags.size()]);
    }

    /**
     * Imposta i rimandi associati a questo riferimento.
     * 
     * @param relatedReferences
     *            rimandi di questo riferimento
     */
    public void setRelatedReferences(ArrayList<BibliographicReference> relatedReferences) {
        this.relatedReferences = relatedReferences;
    }

    /**
     * Restituisce i rimandi associati a questo riferimento.
     * 
     * @return
     *         rimandi di questo riferimento
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
     *         {@code false} se la stringa è nulla o vuota
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
        fields.add(new BibliographicReferenceField("Parole chiave", getTagsAsString()));
        fields.add(new BibliographicReferenceField("Rimandi", getRelatedReferencesAsString()));

        return fields;
    }

    /**
     * Restituisce gli autori di questo riferimento come un'unica stringa.
     * Esempio: "Mario Rossi, Ciro Esposito"
     * 
     * @return
     *         autori come stringa
     */
    public String getAuthorsAsString() {
        return Arrays.toString(getAuthors()).substring(1).replace("]", "").trim();
    }

    /**
     * Restituisce le parole chiave di questo riferimento come stringa.
     * Esempio: "Informatica, Object Orientation"
     * 
     * @return
     *         parole chiave come stringa
     */
    public String getTagsAsString() {
        return Arrays.toString(getTags()).substring(1).replace("]", "").trim();
    }

    /**
     * Restituisce i rimandi di questo riferimento come stringa.
     * Esempio: "Rimando1, Rimando2"
     * 
     * @return
     *         rimandi del riferimento come stringa
     */
    public String getRelatedReferencesAsString() {
        return getRelatedReferences().toString().substring(1).replace("]", "").trim();
    }

    @Override
    public String toString() {
        return getTitle();
    }

}

enum ReferenceLanguage {
    Sconosciuta, Italiano, Inglese, Francese, Tedesco, Spagnolo, Russo, Giapponese, Cinese, Arabo, Altro
}
