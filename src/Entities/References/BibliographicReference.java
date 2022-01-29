package Entities.References;

import Entities.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Classe che rappresenta un riferimento bibiliografico.
 */
public abstract class BibliographicReference {

    private int id;
    private String title;
    private Date pubblicationDate;
    private String DOI;
    private String description;
    private ReferenceLanguage language;
    private List<Author> authors;
    private List<Tag> tags;
    private List<Category> categories;
    private List<BibliographicReference> relatedReferences;

    /**
     * Crea un nuovo riferimento con il titolo indicato.
     * 
     * @param title
     *            titolo del riferimento
     * @throws IllegalArgumentException
     *             se il titolo è nullo o vuoto
     */
    public BibliographicReference(String title) {
        setTitle(title);
        setLanguage(ReferenceLanguage.OTHER);
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

        if (!(obj instanceof BibliographicReference))
            return false;

        return ((BibliographicReference) obj).getID() == getID();
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
    public void setID(int id) {
        this.id = id;
    }

    /**
     * Restituisce l'id del riferimento.
     * 
     * @return
     *         id del riferimento
     */
    public int getID() {
        return id;
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
    public void setTitle(String title) {
        if (!isTitleValid(title))
            throw new IllegalArgumentException("title can't be null or empty");

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
     * Restituisce una lista contenente tutte le informazioni di questo riferimento.
     * 
     * @return
     *         lista con le informazioni del riferimento
     */
    public List<BibliographicReferenceField> getReferenceFields() {
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
        return getAuthors().toString().substring(1).replace("]", "").trim();
    }

    /**
     * Restituisce le parole chiave di questo riferimento come stringa.
     * Esempio: "Informatica, Object Orientation"
     * 
     * @return
     *         parole chiave come stringa
     */
    public String getTagsAsString() {
        return getTags().toString().substring(1).replace("]", "").trim();
    }

    /**
     * Restituisce i rimandi di questo riferimento come stringa.
     * Esempio: "Rimando1, Rimando2"
     * 
     * @return
     *         rimandi del riferimento come stringa
     */
    public String getRelatedReferencesAsString() {
        if (getRelatedReferences() == null)
            return null;

        return getRelatedReferences().toString().substring(1).replace("]", "").trim();
    }

    /**
     * Controlla se questo riferimento è stato pubblicato dopo una certa data.
     * 
     * @param date
     *            data con cui confrontare
     * @return se {@code date == null}, viene considerato come se fosse pubblicato dopo, quindi restituisce {@code true};
     *         se non è specificata la data di pubblicazione del riferimento, restituisce {@code false};
     *         altrimenti, viene restituito {@code true} se la data di pubblicazione è succesiva a {@code date}.
     */
    public boolean wasPublishedAfter(Date date) {
        if (date == null)
            return true;

        if (getPubblicationDate() == null)
            return false;

        return getPubblicationDate().after(date);
    }

    /**
     * Controlla se questo riferimento è stato pubblicato prima di una certa data.
     * 
     * @param date
     *            data con cui confrontare
     * @return se {@code date == null}, viene considerato come se fosse pubblicato prima, quindi restituisce {@code true};
     *         se non è specificata la data di pubblicazione del riferimento, restituisce {@code false};
     *         altrimenti, viene restituito {@code true} se la data di pubblicazione è precedente a {@code date}.
     */
    public boolean wasPublishedBefore(Date date) {
        if (date == null)
            return true;

        if (getPubblicationDate() == null)
            return false;

        return getPubblicationDate().before(date);
    }

    /**
     * Controlla se questo riferimento è stato pubblicato in un certo intervallo compreso tra {@code start} e {@code end}.
     * 
     * @param start
     * @param end
     * @return TODO: commenta
     */
    public boolean wasPublishedBetween(Date start, Date end) {
        return wasPublishedAfter(start) && wasPublishedBefore(end);
    }

    /**
     * Controlla se questo riferimento è stato scritto dagli autori indicati.
     * 
     * @param authors
     *            autori del riferimento
     * @return {@code true} se il riferimento è stato scritto da tutti gli autori indicati
     */
    public boolean wasWrittenBy(Collection<? extends Author> authors) {
        if (authors == null) {
            return getAuthors() == null || getAuthors().isEmpty();
        }

        return getAuthors().containsAll(authors);
    }

    /**
     * Controlla se il riferimento è associato con tutte le parole chiave indicate.
     * 
     * @param tags
     *            parole chiave da cercare
     * @return {@code true} se contiene tutte le parole chiave
     */
    public boolean isTaggedWith(Collection<? extends Tag> tags) {
        if (tags == null)
            return false;

        return getTags().containsAll(tags);
    }

    /**
     * Controlla se il riferimento è contenuto nella categoria indicata.
     * 
     * @param category
     *            categoria da cercare
     * @return {@code true} se il riferimento è presente nella categoria
     */
    public boolean isContainedIn(Category category) {
        if (category == null)
            return getCategories().isEmpty();

        return getCategories().contains(category);
    }

    /**
     * Controlla se il riferimento è contenuto nelle categorie indicate.
     * 
     * @param categories
     *            categorie da cercare
     * @return {@code true} se il riferimento è presente in tutte categorie
     */
    public boolean isContainedIn(Collection<? extends Category> categories) {
        if (categories == null)
            return getCategories().isEmpty();

        return getCategories().containsAll(categories);
    }

    private boolean isTitleValid(String title) {
        return title != null && !title.isBlank();
    }

}