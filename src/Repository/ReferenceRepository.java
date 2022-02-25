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
     *            repository per recuperare le categorie associate ai riferimenti
     * @throws IllegalArgumentException
     *             se {@code referenceDAO == null}, {@code authorDAO == null}, {@code tagDAO == null} o {@code categoryRepository == null}
     */
    public ReferenceRepository(BibliographicReferenceDAO referenceDAO, AuthorDAO authorDAO, TagDAO tagDAO, CategoryRepository categoryRepository) {
        setReferenceDAO(referenceDAO);
        setCategoryRepository(categoryRepository);
        setAuthorDAO(authorDAO);
        setTagDAO(tagDAO);
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

        referenceDAO.remove(reference);
        references.remove(reference);

        for (BibliographicReference referenceQuotingThis : references)
            referenceQuotingThis.getRelatedReferences().remove(reference);

        decreaseQuotationCountForRelatedReferencesOf(reference);
    }

    /**
     * Salva un riferimento ad articolo nel database, creandolo o aggiornandolo se già esiste.
     * 
     * @param reference
     *            riferimento da aggiungere o modificare
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws ReferenceDatabaseException
     *             se il salvataggio non è andato a buon fine
     */
    public void save(Article reference) throws ReferenceDatabaseException {
        Callable<Void> daoSaveCallable = new Callable<Void>() {
            @Override
            public Void call() throws ReferenceDatabaseException {
                referenceDAO.save(reference);
                return null;
            }
        };

        save(reference, daoSaveCallable);
    }

    /**
     * Salva un riferimento a libro nel database, creandolo o aggiornandolo se già esiste.
     * 
     * @param reference
     *            riferimento da aggiungere o modificare
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws ReferenceDatabaseException
     *             se il salvataggio non è andato a buon fine
     */
    public void save(Book reference) throws ReferenceDatabaseException {
        Callable<Void> daoSaveCallable = new Callable<Void>() {
            @Override
            public Void call() throws ReferenceDatabaseException {
                referenceDAO.save(reference);
                return null;
            }
        };

        save(reference, daoSaveCallable);
    }

    /**
     * Salva un riferimento a tesi nel database, creandolo o aggiornandolo se già esiste.
     * 
     * @param reference
     *            riferimento da aggiungere o modificare
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws ReferenceDatabaseException
     *             se il salvataggio non è andato a buon fine
     */
    public void save(Thesis reference) throws ReferenceDatabaseException {
        Callable<Void> daoSaveCallable = new Callable<Void>() {
            @Override
            public Void call() throws ReferenceDatabaseException {
                referenceDAO.save(reference);
                return null;
            }
        };

        save(reference, daoSaveCallable);
    }

    /**
     * Salva un riferimento a immagine nel database, creandolo o aggiornandolo se già esiste.
     * 
     * @param reference
     *            riferimento da aggiungere o modificare
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws ReferenceDatabaseException
     *             se il salvataggio non è andato a buon fine
     */
    public void save(Image reference) throws ReferenceDatabaseException {
        Callable<Void> daoSaveCallable = new Callable<Void>() {
            @Override
            public Void call() throws ReferenceDatabaseException {
                referenceDAO.save(reference);
                return null;
            }
        };

        save(reference, daoSaveCallable);
    }

    /**
     * Salva un riferimento a codice sorgente nel database, creandolo o aggiornandolo se già esiste.
     * 
     * @param reference
     *            riferimento da aggiungere o modificare
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws ReferenceDatabaseException
     *             se il salvataggio non è andato a buon fine
     */
    public void save(SourceCode reference) throws ReferenceDatabaseException {
        Callable<Void> daoSaveCallable = new Callable<Void>() {
            @Override
            public Void call() throws ReferenceDatabaseException {
                referenceDAO.save(reference);
                return null;
            }
        };

        save(reference, daoSaveCallable);
    }

    /**
     * Salva un riferimento a video nel database, creandolo o aggiornandolo se già esiste.
     * 
     * @param reference
     *            riferimento da aggiungere o modificare
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws ReferenceDatabaseException
     *             se il salvataggio non è andato a buon fine
     */
    public void save(Video reference) throws ReferenceDatabaseException {
        Callable<Void> daoSaveCallable = new Callable<Void>() {
            @Override
            public Void call() throws ReferenceDatabaseException {
                referenceDAO.save(reference);
                return null;
            }
        };

        save(reference, daoSaveCallable);
    }

    /**
     * Salva un riferimento a sito web nel database, creandolo o aggiornandolo se già esiste.
     * 
     * @param reference
     *            riferimento da aggiungere o modificare
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws ReferenceDatabaseException
     *             se il salvataggio non è andato a buon fine
     */
    public void save(Website reference) throws ReferenceDatabaseException {
        Callable<Void> daoSaveCallable = new Callable<Void>() {
            @Override
            public Void call() throws ReferenceDatabaseException {
                referenceDAO.save(reference);
                return null;
            }
        };

        save(reference, daoSaveCallable);
    }

    /**
     * Salva un riferimento.
     * <p>
     * Si occupa di gestire le funzioni comuni a tutti i tipi di riferimento,
     * chiamando poi la funzione di salvataggio specifica per il tipo di riferimento.
     * 
     * @param reference
     *            riferimento da salvare
     * @param daoSaveFunction
     *            funzione per salvare con il DAO
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws ReferenceDatabaseException
     *             se il salvataggio non va a buon fine
     */
    private void save(BibliographicReference reference, Callable<Void> daoSaveFunction) throws ReferenceDatabaseException {
        if (reference == null)
            throw new IllegalArgumentException("reference can't be null");

        try {
            authorDAO.save(reference.getAuthors());
            daoSaveFunction.call();
            tagDAO.saveTagsOf(reference);
            saveToLocal(reference);
        } catch (Exception e) {
            throw new ReferenceDatabaseException(e.getMessage());
        }
    }

    /**
     * Recupera tutti i riferimenti dell'utente dal database.
     * 
     * @throws ReferenceDatabaseException
     *             se il recupero non va a buon fine
     */
    private void retrieveFromDatabase() throws ReferenceDatabaseException {
        try {
            references = referenceDAO.getAll();

            for (BibliographicReference reference : references) {
                reference.setCategories(categoryRepository.get(reference));
                reference.setAuthors(authorDAO.get(reference));
                reference.setTags(tagDAO.getTagsOf(reference));
            }

            needToRetrieveFromDatabase = false;
        } catch (DatabaseException e) {
            throw new ReferenceDatabaseException(e.getMessage());
        }
    }

    /**
     * Salva un riferimento in memoria locale.
     * 
     * @param reference
     *            riferimento da salvare
     */
    private void saveToLocal(BibliographicReference reference) {
        int index = findIndexOfReferenceWithSameID(references, reference.getID());

        if (index == -1) {
            references.add(reference);
        } else {
            // se è già contenuta nell'elenco vuol dire che stiamo aggiornando il riferimento
            // conviene prima rimuoverlo dal conteggio delle citazioni ricevute e poi aggiornarlo di nuovo

            references.set(index, reference);
            replaceInRelatedReferences(reference);
            decreaseQuotationCountForRelatedReferencesOf(reference);
        }

        increaseQuotationCountForRelatedReferencesOf(reference);
    }

    /**
     * Incrementa il conteggio delle citazioni ricevute per i rimandi di un riferimento.
     * 
     * @param reference
     *            riferimento da cui recuperare i rimandi
     */
    private void increaseQuotationCountForRelatedReferencesOf(BibliographicReference reference) {
        if (reference == null)
            return;

        for (BibliographicReference relatedReference : reference.getRelatedReferences())
            relatedReference.setQuotationCount(relatedReference.getQuotationCount() + 1);
    }

    /**
     * Diminuisce il conteggio delle citazioni ricevute per i rimandi di un riferimento.
     * 
     * @param reference
     *            riferimento da cui recuperare i rimandi
     */
    private void decreaseQuotationCountForRelatedReferencesOf(BibliographicReference reference) {
        if (reference == null)
            return;

        for (BibliographicReference relatedReference : reference.getRelatedReferences())
            relatedReference.setQuotationCount(relatedReference.getQuotationCount() - 1);
    }

    /**
     * Sostituisce il riferimento con lo stesso id di quello indicato dai rimandi degli altri riferimenti in memoria.
     * 
     * @param newReference
     *            riferimento da inserire al posto di quello vecchio
     */
    private void replaceInRelatedReferences(BibliographicReference newReference) {
        for (BibliographicReference reference : references) {
            int index = findIndexOfReferenceWithSameID(reference.getRelatedReferences(), newReference.getID());

            if (index != -1)
                reference.getRelatedReferences().set(index, newReference);
        }
    }

    /**
     * Trova l'indice del riferimento con l'id indicato.
     * 
     * @param id
     *            id del riferimento da cercare
     * @return l'indice del riferimento, {@value -1} se non è presente
     */
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