package Controller;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import DAO.BibliographicReferenceDAO;
import Entities.Category;
import Entities.Search;
import Entities.References.*;
import Entities.References.OnlineResources.*;
import Entities.References.PhysicalResources.*;
import Exceptions.ReferenceDatabaseException;

/**
 * Controller per gestire il recupero, l'inserimento, la rimozione e la modifica di riferimenti.
 * Serve per non doversi sempre interfacciarsi col database per recuperare le categorie.
 */
public class ReferenceController {
    private BibliographicReferenceDAO referenceDAO;
    private List<BibliographicReference> references;

    /**
     * Crea un nuovo controller dei riferimenti, caricando dal database i riferimenti.
     * 
     * @param referenceDAO
     *            DAO per interfacciarsi col database
     * @throws IllegalArgumentException
     *             se {@code referenceDAO == null}
     * @throws ReferenceDatabaseException
     *             se il recupero dei riferimenti dal database non va a buon fine
     */
    public ReferenceController(BibliographicReferenceDAO referenceDAO) throws ReferenceDatabaseException {
        this.setReferenceDAO(referenceDAO);
    }

    /**
     * Imposta la classe DAO per la gestione dei riferimenti nel database e recupera i riferimenti dal database.
     * 
     * @param referenceDAO
     *            DAO dei riferimenti
     * @throws IllegalArgumentException
     *             se {@code referenceDAO == null}
     * @throws ReferenceDatabaseException
     *             se il recupero dei riferimenti dal database non va a buon fine
     */
    public void setReferenceDAO(BibliographicReferenceDAO referenceDAO) throws ReferenceDatabaseException {
        if (referenceDAO == null)
            throw new IllegalArgumentException("referenceDAO can't be null");

        this.referenceDAO = referenceDAO;

        references = getReferenceDAO().getReferences();
    }

    /**
     * Restituisce la classe DAO per la gestione dei riferimenti nel database
     * 
     * @return
     *         DAO dei riferimenti
     */
    public BibliographicReferenceDAO getReferenceDAO() {
        return referenceDAO;
    }

    /**
     * Restituisce tutti i riferimenti associati all'utente che sta usando l'applicazione.
     * 
     * @return
     *         lista dei riferimenti
     */
    public List<BibliographicReference> getReferences() {
        return references;
    }

    /**
     * Restituisce tutti i riferimenti associati all'utente presenti in una categoria specificata.
     * Se {@code category == null}, verranno restituiti i riferimenti che non appartengono a nessuna categoria.
     * 
     * @param category
     *            categoria in cui cercare i riferimenti
     * @return lista dei riferimenti presenti in una categoria
     */
    public List<BibliographicReference> getReferences(Category category) {
        Predicate<BibliographicReference> categoryFilter = e -> e.isContainedIn(category);
        return getReferences(categoryFilter);
    }

    /**
     * Restituisce tutti i riferimenti associati all'utente che corrispondono ai parametri di ricerca.
     * 
     * @param search
     *            parametri di ricerca
     * @return lista dei riferimenti corrispondenti alla ricerca
     * @throws IllegalArgumentException
     *             se {@code search == null}
     */
    public List<BibliographicReference> getReferences(Search search) {
        if (search == null)
            throw new IllegalArgumentException("search can't be null");

        Predicate<BibliographicReference> searchFilter = e -> e.wasPublishedBetween(search.getFrom(), search.getTo())
                && e.wasWrittenBy(search.getAuthors())
                && e.isTaggedWith(search.getTags())
                && e.isContainedIn(search.getCategories());

        return getReferences(searchFilter);
    }

    private List<BibliographicReference> getReferences(Predicate<BibliographicReference> filter) {
        return getReferences().stream().filter(filter).collect(Collectors.toList());
    }

    /**
     * Rimuove un riferimento dal database.
     * 
     * @param reference
     *            riferimento da rimuovere
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws ReferenceDatabaseException
     *             se la rimozione non è andata a buon fine
     */
    public void removeReference(BibliographicReference reference) throws ReferenceDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        getReferenceDAO().removeReference(reference);
    }

    /**
     * Salva un riferimento nel database, creandolo o aggiornandolo se già esiste.
     * 
     * @param reference
     *            riferimento da aggiungere o modificare
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws ReferenceDatabaseException
     *             se il salvataggio non è andato a buon fine
     */
    public void saveReference(Article reference) throws ReferenceDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        getReferenceDAO().saveReference(reference);
        getReferences().add(reference);
    }

    /**
     * Salva un riferimento nel database, creandolo o aggiornandolo se già esiste.
     * 
     * @param reference
     *            riferimento da aggiungere o modificare
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws ReferenceDatabaseException
     *             se il salvataggio non è andato a buon fine
     */
    public void saveReference(Book reference) throws ReferenceDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        getReferenceDAO().saveReference(reference);
        getReferences().add(reference);
    }

    /**
     * Salva un riferimento nel database, creandolo o aggiornandolo se già esiste.
     * 
     * @param reference
     *            riferimento da aggiungere o modificare
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws ReferenceDatabaseException
     *             se il salvataggio non è andato a buon fine
     */
    public void saveReference(Thesis reference) throws ReferenceDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        getReferenceDAO().saveReference(reference);
        getReferences().add(reference);
    }

    /**
     * Salva un riferimento nel database, creandolo o aggiornandolo se già esiste.
     * 
     * @param reference
     *            riferimento da aggiungere o modificare
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws ReferenceDatabaseException
     *             se il salvataggio non è andato a buon fine
     */
    public void saveReference(Image reference) throws ReferenceDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        getReferenceDAO().saveReference(reference);
        getReferences().add(reference);
    }

    /**
     * Salva un riferimento nel database, creandolo o aggiornandolo se già esiste.
     * 
     * @param reference
     *            riferimento da aggiungere o modificare
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws ReferenceDatabaseException
     *             se il salvataggio non è andato a buon fine
     */
    public void saveReference(SourceCode reference) throws ReferenceDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        getReferenceDAO().saveReference(reference);
        getReferences().add(reference);
    }

    /**
     * Salva un riferimento nel database, creandolo o aggiornandolo se già esiste.
     * 
     * @param reference
     *            riferimento da aggiungere o modificare
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws ReferenceDatabaseException
     *             se il salvataggio non è andato a buon fine
     */
    public void saveReference(Video reference) throws ReferenceDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        getReferenceDAO().saveReference(reference);
        getReferences().add(reference);
    }

    /**
     * Salva un riferimento nel database, creandolo o aggiornandolo se già esiste.
     * 
     * @param reference
     *            riferimento da aggiungere o modificare
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws ReferenceDatabaseException
     *             se il salvataggio non è andato a buon fine
     */
    public void saveReference(Website reference) throws ReferenceDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        getReferenceDAO().saveReference(reference);
        getReferences().add(reference);
    }

}
