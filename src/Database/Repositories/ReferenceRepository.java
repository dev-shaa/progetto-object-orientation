package Database.Repositories;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Database.Connections.ConnectionController;
import Database.DAO.*;
import Entities.References.*;
import Entities.References.OnlineResources.*;
import Entities.References.PhysicalResources.*;
import Exceptions.Database.*;
import Utilities.Functions.CheckedProcedure;

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
     *            repository per recuperare le categorie associate ai riferimenti
     * @throws IllegalArgumentException
     *             se {@code referenceDAO == null}, {@code authorDAO == null}, {@code tagDAO == null} o {@code categoryRepository == null}
     */
    public ReferenceRepository(BibliographicReferenceDAO referenceDAO, AuthorDAO authorDAO, TagDAO tagDAO, CategoryRepository categoryRepository) {
        setReferenceDAO(referenceDAO);
        setCategoryRepository(categoryRepository);
        setAuthorDAO(authorDAO);
        setTagDAO(tagDAO);

        references = new ArrayList<>(0); // array vuoto per essere sicuri di non dover gestire valori null
    }

    /**
     * Imposta il DAO dei riferimenti per recuperarli dal database.
     * 
     * @param referenceDAO
     *            DAO dei riferimenti da usare
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
     * Imposta il DAO degli autori per recuperarli dal database.
     * 
     * @param authorDAO
     *            DAO degli autori da usare
     * @throws IllegalArgumentException
     *             se {@code authorDAO == null}
     */
    public void setAuthorDAO(AuthorDAO authorDAO) {
        if (authorDAO == null)
            throw new IllegalArgumentException("authorDAO can't be null");

        this.authorDAO = authorDAO;
        forceNextRetrievalFromDatabase();
    }

    /**
     * Imposta il DAO dei tag per recuperarli dal database.
     * 
     * @param tagDAO
     *            DAO dei tag da usare
     * @throws IllegalArgumentException
     *             se {@code tagDAO == null}
     */
    public void setTagDAO(TagDAO tagDAO) {
        if (tagDAO == null)
            throw new IllegalArgumentException("tagDAO can't be null");

        this.tagDAO = tagDAO;
        forceNextRetrievalFromDatabase();
    }

    /**
     * Imposta il repository delle categorie per recuperarle.
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
     * Impone che, al prossimo recupero, i riferimenti vengano recuperati di nuovo direttamente dal database.
     */
    public void forceNextRetrievalFromDatabase() {
        needToRetrieveFromDatabase = true;
    }

    /**
     * Recupera tutte i riferimenti dell'utente.
     * <p>
     * Dopo essere state recuperati una prima volta, i riferimenti rimangono in memoria.
     * <p>
     * Il recupero dal database viene eseguito dopo aver cambiato uno delle classi per il recupero,
     * ma è possibile forzarlo chiamando prima {@link #forceNextRetrievalFromDatabase()}.
     * 
     * @return lista con i riferimenti dell'utente.
     * @throws ReferenceDatabaseException
     *             se il recupero dei riferimenti non va a buon fine
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
     *             se la rimozione non va a buon fine
     */
    public void remove(BibliographicReference reference) throws ReferenceDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        try {
            referenceDAO.remove(reference);
            references.remove(reference);

            for (BibliographicReference referenceQuotingThis : references)
                referenceQuotingThis.getRelatedReferences().remove(reference);

            decreaseQuotationCountForRelatedReferencesOf(reference);
        } catch (Exception e) {
            throw new ReferenceDatabaseException("Impossibile rimuovere riferimento.", e);
        }
    }

    /**
     * Salva un riferimento ad articolo nel database, creandolo o aggiornandolo se già esiste.
     * 
     * @param article
     *            riferimento da aggiungere o modificare
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws ReferenceDatabaseException
     *             se il salvataggio non è andato a buon fine
     */
    public void save(Article article) throws ReferenceDatabaseException {
        save(article, () -> referenceDAO.save(article));
    }

    /**
     * Salva un riferimento a libro nel database, creandolo o aggiornandolo se già esiste.
     * 
     * @param book
     *            riferimento da aggiungere o modificare
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws ReferenceDatabaseException
     *             se il salvataggio non è andato a buon fine
     */
    public void save(Book book) throws ReferenceDatabaseException {
        save(book, () -> referenceDAO.save(book));
    }

    /**
     * Salva un riferimento a tesi nel database, creandolo o aggiornandolo se già esiste.
     * 
     * @param thesis
     *            riferimento da aggiungere o modificare
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws ReferenceDatabaseException
     *             se il salvataggio non è andato a buon fine
     */
    public void save(Thesis thesis) throws ReferenceDatabaseException {
        save(thesis, () -> referenceDAO.save(thesis));
    }

    /**
     * Salva un riferimento a immagine nel database, creandolo o aggiornandolo se già esiste.
     * 
     * @param image
     *            riferimento da aggiungere o modificare
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws ReferenceDatabaseException
     *             se il salvataggio non è andato a buon fine
     */
    public void save(Image image) throws ReferenceDatabaseException {
        save(image, () -> referenceDAO.save(image));
    }

    /**
     * Salva un riferimento a video nel database, creandolo o aggiornandolo se già esiste.
     * 
     * @param video
     *            riferimento da aggiungere o modificare
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws ReferenceDatabaseException
     *             se il salvataggio non è andato a buon fine
     */
    public void save(Video video) throws ReferenceDatabaseException {
        save(video, () -> referenceDAO.save(video));
    }

    /**
     * Salva un riferimento a codice sorgente nel database, creandolo o aggiornandolo se già esiste.
     * 
     * @param sourceCode
     *            riferimento da aggiungere o modificare
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws ReferenceDatabaseException
     *             se il salvataggio non è andato a buon fine
     */
    public void save(SourceCode sourceCode) throws ReferenceDatabaseException {
        save(sourceCode, () -> referenceDAO.save(sourceCode));
    }

    /**
     * Salva un riferimento a sito web nel database, creandolo o aggiornandolo se già esiste.
     * 
     * @param website
     *            riferimento da aggiungere o modificare
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws ReferenceDatabaseException
     *             se il salvataggio non è andato a buon fine
     */
    public void save(Website website) throws ReferenceDatabaseException {
        save(website, () -> referenceDAO.save(website));
    }

    private void save(BibliographicReference reference, CheckedProcedure<SQLException> daoSave) throws ReferenceDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        int transactionKey = ConnectionController.getInstance().beginTransaction();

        try {
            authorDAO.save(reference.getAuthors());
            daoSave.call();
            tagDAO.save(reference.getID(), reference.getTags());
            saveToLocal(reference);
            ConnectionController.getInstance().commitTransaction(transactionKey);
        } catch (Exception e) {
            ConnectionController.getInstance().rollbackTransaction(transactionKey);
            throw new ReferenceDatabaseException("Impossibile salvare il riferimento.", e);
        } finally {
            ConnectionController.getInstance().closeTransaction(transactionKey);
        }
    }

    private void retrieveFromDatabase() throws ReferenceDatabaseException {
        try {
            references = referenceDAO.getAll();

            for (BibliographicReference reference : references) {
                reference.setCategories(categoryRepository.get(reference.getID()));
                reference.setAuthors(authorDAO.get(reference.getID()));
                reference.setTags(tagDAO.getAll(reference.getID()));
            }

            needToRetrieveFromDatabase = false;
        } catch (SQLException | CategoryDatabaseException e) {
            throw new ReferenceDatabaseException("Impossibile recuperare i riferimenti dal database", e);
        }
    }

    private void saveToLocal(BibliographicReference newReference) {
        int index = findIndexOfReferenceWithSameID(references, newReference.getID());

        // sostituiamo il riferimento con lo stesso id, se c'è

        if (index == -1) {
            references.add(newReference);
        } else {
            BibliographicReference oldReference = references.get(index);
            decreaseQuotationCountForRelatedReferencesOf(oldReference);
            replaceInRelatedReferences(oldReference, newReference);
            references.set(index, newReference);
        }

        increaseQuotationCountForRelatedReferencesOf(newReference);
    }

    private void increaseQuotationCountForRelatedReferencesOf(BibliographicReference reference) {
        if (reference == null)
            return;

        for (BibliographicReference relatedReference : reference.getRelatedReferences())
            relatedReference.setQuotationCount(relatedReference.getQuotationCount() + 1);
    }

    private void decreaseQuotationCountForRelatedReferencesOf(BibliographicReference reference) {
        if (reference == null)
            return;

        for (BibliographicReference relatedReference : reference.getRelatedReferences())
            relatedReference.setQuotationCount(relatedReference.getQuotationCount() - 1);
    }

    private void replaceInRelatedReferences(BibliographicReference oldReference, BibliographicReference newReference) {
        for (BibliographicReference reference : references) {
            int index = reference.getRelatedReferences().indexOf(oldReference);

            if (index != -1)
                reference.getRelatedReferences().set(index, newReference);
        }
    }

    private int findIndexOfReferenceWithSameID(List<BibliographicReference> references, Integer id) {
        int index = -1;

        for (int i = 0; i < references.size(); i++) {
            if (references.get(i).getID().equals(id)) {
                index = i;
                break;
            }
        }

        return index;
    }

}