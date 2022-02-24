package Repository;

import java.util.List;
import java.util.concurrent.Callable;

import DAO.*;
import Entities.References.*;
import Entities.References.OnlineResources.*;
import Entities.References.PhysicalResources.*;
import Exceptions.Database.*;

/**
 * Repository per gestire il recupero, l'inserimento, la rimozione e la modifica di riferimenti.
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
     *             se {@code referenceDAO == null}, {@code authorDAO == null}, {@code tagDAO == null} o {@code categoryRepository == null}
     */
    public ReferenceRepository(BibliographicReferenceDAO referenceDAO, AuthorDAO authorDAO, TagDAO tagDAO, CategoryRepository categoryRepository) {
        setReferenceDAO(referenceDAO);
        setCategoryRepository(categoryRepository);
        setAuthorDAO(authorDAO);
        setTagDAO(tagDAO);

        forceNextRetrievalFromDatabase();
    }

    private void setReferenceDAO(BibliographicReferenceDAO referenceDAO) {
        if (referenceDAO == null)
            throw new IllegalArgumentException("referenceDAO can't be null");

        this.referenceDAO = referenceDAO;
    }

    private void setAuthorDAO(AuthorDAO authorDAO) {
        if (authorDAO == null)
            throw new IllegalArgumentException("authorDAO can't be null");

        this.authorDAO = authorDAO;
    }

    private void setTagDAO(TagDAO tagDAO) {
        if (tagDAO == null)
            throw new IllegalArgumentException("tagDAO can't be null");

        this.tagDAO = tagDAO;
    }

    private void setCategoryRepository(CategoryRepository categoryRepository) {
        if (categoryRepository == null)
            throw new IllegalArgumentException("categoryRepository can't be null");

        this.categoryRepository = categoryRepository;
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
        if (needToRetrieveFromDatabase)
            retrieveFromDatabase();

        return references;
    }

    /**
     * Rimuove un riferimento.
     * 
     * @param reference
     *            riferimento da rimuovere
     * @throws IllegalArgumentException
     *             se {@code reference == null} o se non ha un ID
     * @throws ReferenceDatabaseException
     *             se la rimozione non è andata a buon fine
     */
    public void remove(BibliographicReference reference) throws ReferenceDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        referenceDAO.remove(reference);
        references.remove(reference);

        for (BibliographicReference referenceQuotingThis : references)
            referenceQuotingThis.getRelatedReferences().remove(reference);

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
        Callable<Void> articleDAOSave = new Callable<Void>() {

            @Override
            public Void call() throws ReferenceDatabaseException {
                referenceDAO.save(reference);
                return null;
            }

        };

        save(reference, articleDAOSave);
    }

    // FIXME:
    private void save(BibliographicReference reference, Callable<Void> daoSaveFunction) throws ReferenceDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        try {
            authorDAO.save(reference.getAuthors());
            daoSaveFunction.call();
            tagDAO.save(reference);
            saveToLocal(reference);
        } catch (Exception e) {
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
        Callable<Void> bookDAOSave = new Callable<Void>() {

            @Override
            public Void call() throws ReferenceDatabaseException {
                referenceDAO.save(reference);
                return null;
            }

        };

        save(reference, bookDAOSave);
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

    private void retrieveFromDatabase() throws ReferenceDatabaseException {
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
