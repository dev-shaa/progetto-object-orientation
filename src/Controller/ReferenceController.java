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
import Exceptions.AuthorDatabaseException;
import Exceptions.CategoryDatabaseException;
import Exceptions.ReferenceDatabaseException;

/**
 * Controller per gestire il recupero, l'inserimento, la rimozione e la modifica di riferimenti.
 * Serve per non doversi sempre interfacciarsi col database per recuperare le categorie.
 */
public class ReferenceController {

    // FIXME: quando viene eliminata una categoria dovrebbe essere rimossa dai riferimenti

    private BibliographicReferenceDAO referenceDAO;
    private CategoryController categoryController;
    private AuthorController authorController;

    private List<BibliographicReference> references;

    private boolean needToRetrieveFromDatabase;

    /**
     * Crea un nuovo controller dei riferimenti, caricando dal database i riferimenti.
     * 
     * @param referenceDAO
     *            DAO per interfacciarsi col database
     * @param categoryController
     *            controller per recuperare le categorie associate ai riferimenti
     * @param authorController
     *            controller per recuperare gli autori associati ai riferimenti
     * @throws IllegalArgumentException
     *             se {@code referenceDAO == null}, {@code categoryController == null} o {@code authorController == null}
     */
    public ReferenceController(BibliographicReferenceDAO referenceDAO, CategoryController categoryController, AuthorController authorController) {
        setReferenceDAO(referenceDAO);
        setCategoryController(categoryController);
        setAuthorController(authorController);
    }

    /**
     * Imposta la classe DAO per la gestione dei riferimenti nel database e recupera i riferimenti dal database.
     * 
     * @param referenceDAO
     *            DAO dei riferimenti
     * @throws IllegalArgumentException
     *             se {@code referenceDAO == null}
     */
    public void setReferenceDAO(BibliographicReferenceDAO referenceDAO) {
        if (referenceDAO == null)
            throw new IllegalArgumentException("referenceDAO can't be null");

        this.referenceDAO = referenceDAO;
        forceNextRetrievalFromDatabase();
    }

    /**
     * Restituisce il DAO usato per recuperare i riferimenti.
     * 
     * @return DAO dei riferimenti
     */
    public BibliographicReferenceDAO getReferenceDAO() {
        return referenceDAO;
    }

    /**
     * Imposta il controller per recuperare le categorie associate ai riferimenti.
     * 
     * @param categoryController
     *            controller delle categorie
     * @throws IllegalArgumentException
     *             se {@code categoryController == null}
     */
    public void setCategoryController(CategoryController categoryController) {
        if (categoryController == null)
            throw new IllegalArgumentException("categoryController can't be null");

        this.categoryController = categoryController;
        forceNextRetrievalFromDatabase();
    }

    /**
     * Restituisce il controller usato per recuperare le categorie dei riferimenti.
     * 
     * @return controller delle categorie
     */
    public CategoryController getCategoryController() {
        return categoryController;
    }

    /**
     * Imposta il controller per recuperare gli autori dei riferimenti.
     * 
     * @param authorController
     *            controller degli autori
     * @throws IllegalArgumentException
     *             se {@code authorController == null}
     */
    public void setAuthorController(AuthorController authorController) {
        if (authorController == null)
            throw new IllegalArgumentException("authorController can't be null");

        this.authorController = authorController;
    }

    /**
     * Restituisce il controller usato per recuperare gli autori dei riferimenti.
     * 
     * @return controller degli autori
     */
    public AuthorController getAuthorController() {
        return authorController;
    }

    /**
     * Restituisce tutti i riferimenti associati all'utente che sta usando l'applicazione.
     * 
     * @return
     *         lista dei riferimenti
     */
    public List<BibliographicReference> getAll() throws ReferenceDatabaseException {
        if (needToRetrieveFromDatabase) {
            try {
                references = referenceDAO.getAll();

                for (BibliographicReference reference : references) {
                    reference.setCategories(getCategoryController().get(reference));
                    reference.setAuthors(getAuthorController().get(reference));
                }

                needToRetrieveFromDatabase = false;
            } catch (ReferenceDatabaseException | CategoryDatabaseException | AuthorDatabaseException e) {
                throw new ReferenceDatabaseException(e.getMessage());
            }
        }

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
    public List<BibliographicReference> get(Category category) throws ReferenceDatabaseException {
        Predicate<BibliographicReference> categoryFilter = e -> e.isContainedIn(category);
        return get(categoryFilter);
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
    public List<BibliographicReference> get(Search search) throws ReferenceDatabaseException {
        if (search == null)
            throw new IllegalArgumentException("search can't be null");

        Predicate<BibliographicReference> searchFilter = e -> e.wasPublishedBetween(search.getFrom(), search.getTo())
                && e.wasWrittenBy(search.getAuthors())
                && e.isTaggedWith(search.getTags())
                && e.isContainedIn(search.getCategories());

        return get(searchFilter);
    }

    private List<BibliographicReference> get(Predicate<BibliographicReference> filter) throws ReferenceDatabaseException {
        return getAll().stream().filter(filter).collect(Collectors.toList());
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
    public void remove(BibliographicReference reference) throws ReferenceDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        getReferenceDAO().remove(reference);
        removeFromLocal(reference);
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
    public void save(Article reference) throws ReferenceDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        try {
            getAuthorController().save(reference.getAuthors());
            getReferenceDAO().save(reference);
            saveToLocal(reference);
        } catch (AuthorDatabaseException e) {
            throw new ReferenceDatabaseException(e.getMessage());
        }
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
    public void save(Book reference) throws ReferenceDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        try {
            getAuthorController().save(reference.getAuthors());
            getReferenceDAO().save(reference);
            saveToLocal(reference);
        } catch (AuthorDatabaseException e) {
            throw new ReferenceDatabaseException(e.getMessage());
        }
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
    public void save(Thesis reference) throws ReferenceDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        try {
            getAuthorController().save(reference.getAuthors());
            getReferenceDAO().save(reference);
            saveToLocal(reference);
        } catch (AuthorDatabaseException e) {
            throw new ReferenceDatabaseException(e.getMessage());
        }
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
    public void save(Image reference) throws ReferenceDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        try {
            getAuthorController().save(reference.getAuthors());
            getReferenceDAO().save(reference);
            saveToLocal(reference);
        } catch (AuthorDatabaseException e) {
            throw new ReferenceDatabaseException(e.getMessage());
        }
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
    public void save(SourceCode reference) throws ReferenceDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        try {
            getAuthorController().save(reference.getAuthors());
            getReferenceDAO().save(reference);
            saveToLocal(reference);
        } catch (AuthorDatabaseException e) {
            throw new ReferenceDatabaseException(e.getMessage());
        }
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
    public void save(Video reference) throws ReferenceDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        try {
            getAuthorController().save(reference.getAuthors());
            getReferenceDAO().save(reference);
            saveToLocal(reference);
        } catch (AuthorDatabaseException e) {
            throw new ReferenceDatabaseException(e.getMessage());
        }
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
    public void save(Website reference) throws ReferenceDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        try {
            getAuthorController().save(reference.getAuthors());
            getReferenceDAO().save(reference);
            saveToLocal(reference);
        } catch (AuthorDatabaseException e) {
            throw new ReferenceDatabaseException(e.getMessage());
        }
    }

    /**
     * Impone che, al prossimo recupero, i riferimenti vengano recuperati di nuovo direttamente dal database.
     */
    public void forceNextRetrievalFromDatabase() {
        needToRetrieveFromDatabase = true;
    }

    private void saveToLocal(BibliographicReference reference) throws ReferenceDatabaseException {
        if (getAll().contains(reference)) {
            // se è già contenuta nell'elenco vuol dire che stiamo aggiornando il riferimento
            // conviene prima rimuoverlo dal conteggio delle citazioni ricevute e poi aggiornarlo di nuovo

            removeFromQuotationCount(reference);
        } else {
            getAll().add(reference);
        }

        addToQuotationCount(reference);
    }

    private void removeFromLocal(BibliographicReference referenceToRemove) throws ReferenceDatabaseException {
        getAll().remove(referenceToRemove);

        for (BibliographicReference reference : references) {
            reference.getRelatedReferences().remove(referenceToRemove);
        }

        removeFromQuotationCount(referenceToRemove);
    }

    private void addToQuotationCount(BibliographicReference reference) {
        if (reference == null)
            return;

        for (BibliographicReference bibliographicReference : reference.getRelatedReferences()) {
            bibliographicReference.setQuotationCount(bibliographicReference.getQuotationCount() + 1);
        }
    }

    private void removeFromQuotationCount(BibliographicReference referenceToRemove) {
        if (referenceToRemove == null)
            return;

        for (BibliographicReference reference : referenceToRemove.getRelatedReferences()) {
            reference.setQuotationCount(reference.getQuotationCount() - 1);
        }
    }

}
