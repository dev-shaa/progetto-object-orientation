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

    private final int TITLE_MAX_LENGTH = 256;
    private final int DESCRIPTION_MAX_LENGTH = 1024;
    private final Pattern DOI_PATTERN = Pattern.compile("^ *10\\.[0-9]{4,}\\/\\w{1,}(\\.\\w{1,}){1,} *$");
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

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

    /**
     * Imposta l'id del riferimento.
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
     *             se il titolo è nullo o vuoto
     */
    public void setTitle(String title) {
        if (isTitleValid(title))
            this.title = title;
        else
            throw new IllegalArgumentException("Il titolo non può essere vuoto o più lungo di " + TITLE_MAX_LENGTH + " caratteri.");
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
        if (isStringNullOrEmpty(DOI))
            this.DOI = null;
        else if (DOI_PATTERN.matcher(DOI).matches())
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
     * @throws IllegalArgumentException
     *             se la lunghezza della descrizione è maggiore di 1024 caratteri
     */
    public void setDescription(String description) {
        description = description.trim();

        if (!isDescriptionValid(description))
            throw new IllegalArgumentException("Il titolo non può essere vuoto o più lungo di " + DESCRIPTION_MAX_LENGTH + " caratteri.");

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
     * <p>
     * Se {@code null}, viene impostata su {@code NOTSPECIFIED}
     * 
     * @param language
     *            lingua del riferimento
     */
    public void setLanguage(ReferenceLanguage language) {
        if (language == null)
            this.language = ReferenceLanguage.NOTSPECIFIED;
        else
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
        return "Titolo: " + getTitle()
                + "\nAutori: " + getAuthorsAsString()
                + "\nData di pubblicazione: " + (getFormattedDate() == null ? "" : getFormattedDate())
                + "\nDOI: " + (getDOI() == null ? "" : getDOI())
                + "\nLingua: " + getLanguage()
                + "\nParole chiave: " + getTagsAsString()
                + "\nRimandi: " + getRelatedReferencesAsString()
                + "\nDescrizione: " + (getDescription() == null ? "" : getDescription());
    }

    /**
     * Controlla se la stringa è nulla o vuota
     * 
     * @param name
     *            stringa da controllare
     * @return {@code true} se {@code string == null}, {@code string.isEmpty()} o {@code string.isBlank()}
     */
    protected boolean isStringNullOrEmpty(String name) {
        return name == null || name.isEmpty() || name.isBlank();
    }

    private boolean isTitleValid(String title) {
        return !isStringNullOrEmpty(title) && title.length() <= TITLE_MAX_LENGTH;
    }

    private boolean isDescriptionValid(String description) {
        return description == null || description.length() <= DESCRIPTION_MAX_LENGTH;
    }

}