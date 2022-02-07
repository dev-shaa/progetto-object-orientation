package Controller;

import java.util.Collection;
import java.util.List;

import DAO.AuthorDAO;
import Entities.Author;
import Entities.References.BibliographicReference;
import Exceptions.AuthorDatabaseException;

/**
 * TODO: commenta
 */
public class AuthorController {

    private AuthorDAO authorDAO;

    /**
     * 
     * @param authorDAO
     */
    public AuthorController(AuthorDAO authorDAO) {
        setAuthorDAO(authorDAO);
    }

    /**
     * 
     * @return
     */
    public AuthorDAO getAuthorDAO() {
        return authorDAO;
    }

    /**
     * 
     * @param authorDAO
     */
    public void setAuthorDAO(AuthorDAO authorDAO) {
        if (authorDAO == null)
            throw new IllegalArgumentException("authorDAO can't be null");

        this.authorDAO = authorDAO;
    }

    /**
     * 
     * @param authors
     * @throws AuthorDatabaseException
     */
    public void save(Collection<? extends Author> authors) throws AuthorDatabaseException {
        getAuthorDAO().save(authors);
    }

    /**
     * 
     * @param reference
     * @return
     * @throws AuthorDatabaseException
     */
    public List<Author> get(BibliographicReference reference) throws AuthorDatabaseException {
        return getAuthorDAO().get(reference);
    }
}
