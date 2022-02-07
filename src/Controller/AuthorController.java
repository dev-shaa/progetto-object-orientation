package Controller;

import java.util.Collection;
import java.util.List;

import DAO.AuthorDAO;
import Entities.Author;
import Entities.References.BibliographicReference;
import Exceptions.AuthorDatabaseException;

/**
 * Controller per gestire il recupero e l'inserimento di autori.
 */
public class AuthorController {

    private AuthorDAO authorDAO;

    /**
     * Crea un nuovo {@link #AuthorController} con il DAO indicato.
     * 
     * @param authorDAO
     *            DAO degli autori
     * @throws IllegalArgumentException
     *             se {@code authorDAO == null}
     */
    public AuthorController(AuthorDAO authorDAO) {
        setAuthorDAO(authorDAO);
    }

    /**
     * Restituisce la classe DAO usata per recuperare e salvare gli autori nel database.
     * 
     * @return DAO degli autori usato
     */
    public AuthorDAO getAuthorDAO() {
        return authorDAO;
    }

    /**
     * Imposta la classe DAO per recuperare e salvare gli autori nel database.
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
     * Salva una collezione di autori.
     * 
     * @param authors
     *            autori da salvare
     * @throws AuthorDatabaseException
     *             se il salvataggio non va a buon fine
     */
    public void save(Collection<? extends Author> authors) throws AuthorDatabaseException {
        getAuthorDAO().save(authors);
    }

    /**
     * Restituisce gli autori di un riferimento bibliografico.
     * 
     * @param reference
     *            riferimento di cui trovare gli autori
     * @return
     *         lista con gli autori del riferimento
     * @throws AuthorDatabaseException
     *             se il recupero non va a buon fine
     */
    public List<Author> get(BibliographicReference reference) throws AuthorDatabaseException {
        return getAuthorDAO().get(reference);
    }

}
