package Entities;

import java.util.Collection;
import java.util.Date;

import Exceptions.Input.EmptySearchException;

/**
 * Classe contenente i parametri di una ricerca.
 */
public class Search {
    private Date from;
    private Date to;
    private Collection<? extends Tag> tags;
    private Collection<? extends Author> authors;
    private Collection<? extends Category> categories;

    /**
     * Crea una nuova ricerca dai termini indicati.
     * Almeno un valore deve essere diverso da {@code null}.
     * 
     * @param from
     *            data di inizio dell'intervallo di ricerca
     * @param to
     *            data di fine dell'intervallo di ricerca
     * @param tags
     *            parole chiave della ricerca
     * @param categories
     *            categorie della ricerca
     * @throws EmptySearchException
     *             se tutti gli elementi sono {@code null} (in caso di array anche se hanno lunghezza 0)
     */
    public Search(Date from, Date to, Collection<? extends Tag> tags, Collection<? extends Category> categories, Collection<? extends Author> authors) throws EmptySearchException {
        if (areAllTermsNull(from, to, tags, categories, authors))
            throw new EmptySearchException("Almeno un elemento di ricerca deve essere specificato");

        setFrom(from);
        setTo(to);
        setTags(tags);
        setCategories(categories);
        setAuthors(authors);
    }

    /**
     * Restituisce gli autori specificati in questa ricerca.
     * 
     * @return
     *         autori di ricerca (può essere {@code null})
     */
    public Collection<? extends Author> getAuthors() {
        return authors;
    }

    /**
     * Imposta gli autori di questa ricerca.
     * 
     * @param categories
     *            autori di ricerca
     */
    public void setAuthors(Collection<? extends Author> authors) {
        this.authors = authors;
    }

    /**
     * Restituisce le categorie specificate in questa ricerca.
     * 
     * @return
     *         categorie di ricerca (può essere {@code null})
     */
    public Collection<? extends Category> getCategories() {
        return categories;
    }

    /**
     * Imposta le categorie di questa ricerca.
     * 
     * @param categories
     *            categorie di ricerca
     */
    private void setCategories(Collection<? extends Category> categories) {
        this.categories = categories;
    }

    /**
     * Restituisce la data di inizio dell'intervallo di ricerca.
     * 
     * @return
     *         data di inizio dell'intervallo (può essere {@code null})
     */
    public Date getFrom() {
        return from;
    }

    /**
     * Imposta la data di inizio dell'intervallo di ricerca.
     * 
     * @param from
     *            data di inizio dell'intervallo
     */
    private void setFrom(Date from) {
        this.from = from;
    }

    /**
     * Restituisce la data di fine dell'intervallo di ricerca.
     * 
     * @return
     *         data di fine dell'intervallo (può essere {@code null})
     */
    public Date getTo() {
        return to;
    }

    /**
     * Imposta la data di fine dell'intervallo di ricerca.
     * 
     * @param to
     *            data di fine dell'intervallo
     */
    private void setTo(Date to) {
        this.to = to;
    }

    /**
     * Restituisce le parole chiave di questa ricerca.
     * 
     * @return
     *         parole chiave della ricerca (può essere {@code null})
     */
    public Collection<? extends Tag> getTags() {
        return tags;
    }

    /**
     * Imposta le parole chiave di questa ricerca.
     * 
     * @param tags
     *            parole chiave della ricerca
     */
    private void setTags(Collection<? extends Tag> tags) {
        this.tags = tags;
    }

    private boolean areAllTermsNull(Date from, Date to, Collection<? extends Tag> tags, Collection<? extends Category> categories, Collection<? extends Author> authors) {
        return from == null && to == null && (tags == null || tags.isEmpty()) && (categories == null || categories.isEmpty()) && (authors == null || authors.isEmpty());
    }

}
