package Repository;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import DAO.*;
import Entities.*;
import Entities.References.*;
import Entities.References.OnlineResources.*;
import Entities.References.PhysicalResources.*;
import Exceptions.Database.*;

/**
 * Repository per gestire il recupero, l'inserimento, la rimozione e la modifica di riferimenti.
 * <p>
 * Serve per non doversi sempre interfacciarsi col database per recuperare le categorie.
 */
public class ReferenceRepository {

    private BibliographicReferenceDAO referenceDAO;
    private AuthorDAO authorDAO;
    private TagDAO tagDAO;
    private CategoryRepository categoryRepository;

    private List<BibliographicReference> references;

    private boolean needToRetrieveFromDatabase;

    /**
     * Crea un nuovo repository dei riferimenti.
     * 
     * @param referenceDAO
     *            DAO per recuperare i riferimenti dal database
     * @param authorDAO
     *            DAO per recuperare gli autori dei riferimenti dal database
     * @param tagDAO
     *            DAO per recuperare le parole chiave dei riferimenti dal database
     * @param categoryRepository
     *            controller per recuperare le categorie associate ai riferimenti
     * @throws IllegalArgumentException
     *             se {@code referenceDAO == null}, {@code authorDAO == null}, {@code tagDAO == null} o {@code categoryController == null}
     * @see #setReferenceDAO(BibliographicReferenceDAO)
     * @see #setAuthorDAO(AuthorDAO)
     * @see #setTagDAO(TagDAO)
     * @see #setCategoryRepository(CategoryRepository)
     */
    public ReferenceRepository(BibliographicReferenceDAO referenceDAO, AuthorDAO authorDAO, TagDAO tagDAO, CategoryRepository categoryRepository) {
        setReferenceDAO(referenceDAO);
        setCategoryRepository(categoryRepository);
        setAuthorDAO(authorDAO);
        setTagDAO(tagDAO);
    }

    /**
     * Imposta la classe DAO per la gestione dei riferimenti nel database.
     * <p>
     * Il recupero successivo alla chiamata di questa funzione avverrà dal database.
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
     * Imposta la classe DAO per recuperare gli autori dei riferimenti.
     * 
     * @param authorDAO
     *            DAO degli autori
     * @throws IllegalArgumentException
     *             se {@code authorDAO == null}
     */
    public void setAuthorDAO(AuthorDAO authorDAO) {
        if (authorDAO == null)
            throw new IllegalArgumentException("authorDAO can't be null");

        this.authorDAO = authorDAO;
    }

    /**
     * Imposta la classe DAO per recuperare le parole chiave dei riferimenti.
     * 
     * @param tagDAO
     *            DAO delle parole chiave
     * @throws IllegalArgumentException
     *             se {@code tagDAO == null}
     */
    public void setTagDAO(TagDAO tagDAO) {
        if (tagDAO == null)
            throw new IllegalArgumentException("tagDAO can't be null");

        this.tagDAO = tagDAO;
    }

    /**
     * Imposta il repository per recuperare le categorie associate ai riferimenti.
     * <p>
     * Il recupero successivo alla chiamata di questa funzione avverrà dal database.
     * 
     * @param categoryRepository
     *            repository delle categorie
     * @throws IllegalArgumentException
     *             se {@code categoryRepository == null}
     */
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        if (categoryRepository == null)
            throw new IllegalArgumentException("categoryRepository can't be null");

        this.categoryRepository = categoryRepository;
        forceNextRetrievalFromDatabase();
    }

    /**
     * Restituisce tutti i riferimenti associati all'utente che sta usando l'applicazione.
     * 
     * @return
     *         lista dei riferimenti
     * @throws ReferenceDatabaseException
     *             se il recupero non va a buon fine
     */
    public List<BibliographicReference> getAll() throws ReferenceDatabaseException {
        if (needToRetrieveFromDatabase) {
            try {
                references = referenceDAO.getAll();

                for (BibliographicReference reference : references) {
                    reference.setCategories(categoryRepository.get(reference));
                    reference.setAuthors(authorDAO.get(reference));
                    reference.setTags(tagDAO.get(reference));
                }

                needToRetrieveFromDatabase = false;
            } catch (DatabaseException e) {
                throw new ReferenceDatabaseException(e.getMessage());
            }
        }

        return references;
    }

    /**
     * Restituisce tutti i riferimenti associati all'utente presenti in una categoria specificata.
     * <p>
     * Se {@code category == null}, verranno restituiti i riferimenti che non appartengono a nessuna categoria.
     * 
     * @param category
     *            categoria in cui cercare i riferimenti
     * @return lista dei riferimenti presenti in una categoria
     * @throws ReferenceDatabaseException
     *             se il recupero non va a buon fine
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
     * @throws ReferenceDatabaseException
     *             se il recupero non va a buon fine
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

    /**
     * Rimuove un riferimento.
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

        // rimuovi dal database
        referenceDAO.remove(reference);

        // rimuovi dalla memoria locale
        references.remove(reference);

        // rimovi dalle liste di rimandi degli altri riferimenti
        for (BibliographicReference referenceQuotingThis : references)
            referenceQuotingThis.getRelatedReferences().remove(reference);

        // tutti i rimandi citati hanno un riferimento in meno che li pensa
        removeFromQuotationCount(reference);
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
            authorDAO.save(reference.getAuthors());
            referenceDAO.save(reference);
            tagDAO.save(reference);
            saveToLocal(reference);
        } catch (DatabaseException e) {
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
            authorDAO.save(reference.getAuthors());
            referenceDAO.save(reference);
            tagDAO.save(reference);
            saveToLocal(reference);
        } catch (DatabaseException e) {
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
            authorDAO.save(reference.getAuthors());
            referenceDAO.save(reference);
            tagDAO.save(reference);
            saveToLocal(reference);
        } catch (DatabaseException e) {
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
            authorDAO.save(reference.getAuthors());
            referenceDAO.save(reference);
            tagDAO.save(reference);
            saveToLocal(reference);
        } catch (DatabaseException e) {
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
            authorDAO.save(reference.getAuthors());
            referenceDAO.save(reference);
            tagDAO.save(reference);
            saveToLocal(reference);
        } catch (AuthorDatabaseException | TagDatabaseException e) {
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
            authorDAO.save(reference.getAuthors());
            referenceDAO.save(reference);
            tagDAO.save(reference);
            saveToLocal(reference);
        } catch (AuthorDatabaseException | TagDatabaseException e) {
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
            authorDAO.save(reference.getAuthors());
            referenceDAO.save(reference);
            tagDAO.save(reference);
            saveToLocal(reference);
        } catch (AuthorDatabaseException | TagDatabaseException e) {
            throw new ReferenceDatabaseException(e.getMessage());
        }
    }

    /**
     * Impone che, al prossimo recupero, i riferimenti vengano recuperati di nuovo direttamente dal database.
     */
    public void forceNextRetrievalFromDatabase() {
        needToRetrieveFromDatabase = true;
    }

    private List<BibliographicReference> get(Predicate<BibliographicReference> filter) throws ReferenceDatabaseException {
        return getAll().stream().filter(filter).collect(Collectors.toList());
    }

    private void saveToLocal(BibliographicReference reference) {
        int index = references.indexOf(reference);

        if (index == -1) {
            references.add(reference);
        } else {
            // se è già contenuta nell'elenco vuol dire che stiamo aggiornando il riferimento
            // conviene prima rimuoverlo dal conteggio delle citazioni ricevute e poi aggiornarlo di nuovo

            references.set(index, reference);
            replaceInRelatedReferences(reference);
            removeFromQuotationCount(reference);
        }

        addToQuotationCount(reference);
    }

    private void addToQuotationCount(BibliographicReference reference) {
        if (reference == null)
            return;

        for (BibliographicReference bibliographicReference : reference.getRelatedReferences())
            bibliographicReference.setQuotationCount(bibliographicReference.getQuotationCount() + 1);
    }

    private void removeFromQuotationCount(BibliographicReference referenceToRemove) {
        if (referenceToRemove == null)
            return;

        for (BibliographicReference reference : referenceToRemove.getRelatedReferences())
            reference.setQuotationCount(reference.getQuotationCount() - 1);
    }

    private void replaceInRelatedReferences(BibliographicReference newReference) {
        for (BibliographicReference reference : references) {
            int index = reference.getRelatedReferences().indexOf(newReference);

            if (index != -1)
                reference.getRelatedReferences().set(index, newReference);
        }
    }

}
