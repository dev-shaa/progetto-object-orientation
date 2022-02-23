package Entities.References;

import Entities.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Classe che rappresenta un riferimento bibliografico.
 */
public abstract class BibliographicReference {

    private Integer id;
    private String title;
    private Date pubblicationDate;
    private String DOI;
    private String description;
    private ReferenceLanguage language;
    private List<Author> authors;
    private List<Tag> tags;
    private List<Category> categories;
    private List<BibliographicReference> relatedReferences;
    private int quotationCount;

    private final Pattern doiPattern = Pattern.compile("^ *10\\.[0-9]{4,}\\/\\w{1,}(\\.\\w{1,}){1,} *$");
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

    /**
     * Crea un nuovo riferimento con il titolo indicato.
     * 
     * @param title
     *            titolo del riferimento
     * @throws IllegalArgumentException
     *             se il titolo è nullo o vuoto
     */
    public BibliographicReference(String title) {
        setID(null);
        setTitle(title);
        setLanguage(ReferenceLanguage.NOTSPECIFIED);
    }

    @Override
    public String toString() {
        return getTitle();
    }

    @Override
    public boolean equals(Object obj) {
        // due riferimenti sono uguali se hanno lo stesso id

        if (obj == this)
            return true;

        if (obj == null || !(obj instanceof BibliographicReference))
            return false;

        BibliographicReference reference = (BibliographicReference) obj;

        return (getID() == null && reference == null) || (getID() != null && getID().equals(reference.getID()));
    }

    /**
     * Imposta l'id del riferimento.
     * <p>
     * ATTENZIONE: dovrebbe essere usato solo quando viene salvato nel database.
     * </p>
     * 
     * @param id
     *            id del riferimento
     */
    public void setID(Integer id) {
        this.id = id;
    }

    /**
     * Restituisce l'id del riferimento.
     * 
     * @return
     *         id del riferimento
     */
    public Integer getID() {
        return id;
    }

    /**
     * Imposta il titolo del riferimento.
     * 
     * @param title
     *            titolo del riferimento
     * @throws IllegalArgumentException
     *             se il titolo non è una stringa valida
     * @see #isStringNullOrEmpty(String)
     */
    public void setTitle(String title) {
        if (!isTitleValid(title))
            throw new IllegalArgumentException("Il titolo di un riferimento non può essere nullo o vuoto.");

        this.title = title.trim();
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
     *         data di pubblicazione del riferimento ({@code null} se non è
     *         indicata)
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
        if (isStringNullOrEmpty(DOI))
            this.DOI = null;
        else if (isDOIValid(DOI))
            this.DOI = DOI.trim();
        else
            throw new IllegalArgumentException("Il DOI non è valido");
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
        if (isStringNullOrEmpty(description))
            this.description = null;
        else
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
    public void setLanguage(ReferenceLanguage language) {
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
     * Imposta gli autori di questo riferimento.
     * 
     * @param authors
     *            autori del riferimento
     */
    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    /**
     * Restituisce gli autori di questo riferimento.
     * 
     * @return gli autori del riferimento (la lista può essere vuota, ma non {@code null})
     */
    public List<Author> getAuthors() {
        if (authors == null)
            authors = new ArrayList<>(0);

        return authors;
    }

    /**
     * Imposta le parole chiave associate al riferimento.
     * 
     * @param tags
     *            parole chiave del riferimento
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     * Restituisce le parole chiave associate al riferimento.
     * 
     * @return
     *         parole chiave del riferimento (la lista può essere vuota, ma non {@code null})
     */
    public List<Tag> getTags() {
        if (tags == null)
            tags = new ArrayList<>(0);

        return tags;
    }

    /**
     * Imposta i rimandi associati a questo riferimento.
     * 
     * @param relatedReferences
     *            rimandi di questo riferimento
     */
    public void setRelatedReferences(List<BibliographicReference> relatedReferences) {
        this.relatedReferences = relatedReferences;
    }

    /**
     * Restituisce i rimandi associati a questo riferimento.
     * 
     * @return
     *         rimandi del riferimento (la lista può essere vuota, ma non {@code null})
     */
    public List<BibliographicReference> getRelatedReferences() {
        if (relatedReferences == null)
            relatedReferences = new ArrayList<>(0);

        return relatedReferences;
    }

    /**
     * Imposta le categorie a cui appartiene questo riferimento.
     * 
     * @param categories
     *            categorie del riferimento
     */
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    /**
     * Restituisce le categorie a cui appartiene questo riferimento.
     * 
     * @return
     *         categorie del riferimento (la lista può essere vuota, ma non {@code null})
     */
    public List<Category> getCategories() {
        if (categories == null)
            categories = new ArrayList<>(0);

        return categories;
    }

    /**
     * Imposta il numero di citazioni ricevute da altri riferimenti.
     * 
     * @param quotationCount
     *            numero di citazioni ricevute
     */
    public void setQuotationCount(int quotationCount) {
        if (quotationCount < 0)
            this.quotationCount = 0;
        else
            this.quotationCount = quotationCount;
    }

    /**
     * Restituisce il numero di citazioni ricevute da altri riferimenti.
     * 
     * @return numero di citazioni ricevute
     */
    public int getQuotationCount() {
        return quotationCount;
    }

    /**
     * Restituisce gli autori di questo riferimento come un'unica stringa.
     * <p>
     * Esempio: "Mario Rossi, Ciro Esposito".
     * 
     * @return
     *         autori come stringa
     */
    public String getAuthorsAsString() {
        String authors = getAuthors().toString();
        return authors.substring(1, authors.lastIndexOf(']'));
    }

    /**
     * Restituisce le parole chiave di questo riferimento come stringa.
     * <p>
     * Esempio: "Informatica, Object Orientation".
     * 
     * @return
     *         parole chiave come stringa
     */
    public String getTagsAsString() {
        String tags = getTags().toString();
        return tags.substring(1, tags.lastIndexOf(']'));
    }

    /**
     * Restituisce i rimandi di questo riferimento come stringa.
     * <p>
     * Esempio: "Rimando1, Rimando2".
     * 
     * @return
     *         rimandi del riferimento come stringa
     */
    public String getRelatedReferencesAsString() {
        String relatedReferences = getRelatedReferences().toString();
        return relatedReferences.substring(1, relatedReferences.lastIndexOf(']'));
    }

    /**
     * Restituisce la data di pubblicazione del riferimento formattata come {@code yyyy/MM/dd}.
     * 
     * @return {@code null} se non è indicata la data di pubblicazione, la data formattata altrimenti
     */
    public String getFormattedDate() {
        return getPubblicationDate() == null ? null : DATE_FORMAT.format(getPubblicationDate());
    }

    /**
     * Restituisce tutte le informazioni di questo riferimento
     * 
     * @return stringa con le informazioni
     */
    public String getInfo() {
        // FIXME:

        String info = "Titolo: %s\nDOI: %s";

        info = String.format(info, getTitle(), getDOI());

        return info;

        // return "Titolo: " + getTitle()
        // + "\nAutori: " + getAuthorsAsString()
        // + "\nData di pubblicazione: " + getFormattedDate()
        // + "\nDOI: " + getDOI()
        // + "\nLingua: " + getLanguage()
        // + "\nParole chiave: " + getTagsAsString()
        // + "\nRimandi: " + getRelatedReferencesAsString()
        // + "\nDescrizione: " + getDescription();
    }

    /**
     * Controlla se la stringa è nulla o vuota
     * 
     * @param string
     *            stringa da controllare
     * @return {@code true} se {@code string == null}, {@code string.isEmpty()} o {@code string.isBlank()}
     */
    protected boolean isStringNullOrEmpty(String string) {
        return string == null || string.isEmpty() || string.isBlank();
    }

    private boolean isTitleValid(String title) {
        // si potrebbe chiamare direttamente la funzione sotto, ma metti caso che un giorno decidessimo di cambiare
        // qual è il titolo valido
        return !isStringNullOrEmpty(title);
    }

    private boolean isDOIValid(String doi) {
        if (isStringNullOrEmpty(doi))
            return true;

        return doiPattern.matcher(doi).matches();
    }

}