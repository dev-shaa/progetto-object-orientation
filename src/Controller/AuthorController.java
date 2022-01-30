package Controller;

import java.util.ArrayList;
import java.util.List;

import DAO.AuthorDAO;
import Entities.Author;
import Exceptions.AuthorDatabaseException;

/**
 * Controller per gestire il recupero, l'inserimento, la rimozione e la modifica di autori.
 */
public class AuthorController {

    private AuthorDAO authorDAO;
    private ArrayList<Author> authors;

    /**
     * Crea un nuovo controller degli autori.
     * 
     * @param authorDAO
     *            DAO degli autori
     * @throws IllegalArgumentException
     *             se {@code authorDAO == null}
     * @throws AuthorDatabaseException
     *             se non è possibile recuperare gli autori dal database
     */
    public AuthorController(AuthorDAO authorDAO) throws AuthorDatabaseException {
        setAuthorDAO(authorDAO);
    }

    /**
     * Restituisce il DAO degli autori.
     * 
     * @return DAO degli autori
     */
    public AuthorDAO getAuthorDAO() {
        return authorDAO;
    }

    /**
     * Imposta il DAO degli autori.
     * <p>
     * Questo funziona anche da "reset", in quanto verranno recuperati di nuovo dal database gli autori con il DAO assegnato.
     * 
     * @param authorDAO
     *            DAO degli autori
     * @throws IllegalArgumentException
     *             se {@code authorDAO == null}
     * @throws AuthorDatabaseException
     *             se non è possibile recuperare gli autori dal database
     */
    public void setAuthorDAO(AuthorDAO authorDAO) throws AuthorDatabaseException {
        if (authorDAO == null)
            throw new IllegalArgumentException("authorDAO can't be null");

        this.authorDAO = authorDAO;

        // TODO: recupera dal database
    }

    /**
     * Restituisce tutti gli autori presenti nel database.
     * 
     * @return
     *         lista di autori
     */
    public List<Author> getAuthors() {
        return authors;
    }

    /**
     * Salva un autore nel database.
     * 
     * @param author
     *            autore da salvare
     */
    public void saveAuthor(Author author) {
        authorDAO.SaveAuthor(author);
    }
}
