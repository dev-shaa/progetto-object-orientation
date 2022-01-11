package DAO;

import Entities.*;
import Exceptions.*;

/**
 * Classe astratta che si occupa di gestire la parte di database relativo alle
 * categorie.
 */
public abstract class CategoryDAO {

    protected User user;

    /**
     * Crea {@code CategoryDAO} per interfacciarsi al database relativo alle
     * categorie dell'utente.
     * 
     * @param user
     *             utente che accede al database
     * @throws IllegalArgumentException
     *                                  se l'utente di input è {@code null}
     */
    public CategoryDAO(User user) throws IllegalArgumentException {
        setUser(user);
    }

    /**
     * Imposta l'utente di cui recuperare le categorie.
     * 
     * @param user
     *             utente di cui recuperare le categorie.
     * @throws IllegalArgumentException
     *                                  se l'utente di input è {@code null}
     */
    public void setUser(User user) throws IllegalArgumentException {
        if (user == null)
            throw new IllegalArgumentException("L'utente non può essere nullo.");

        this.user = user;
    }

    /**
     * Restituisce l'utente che accede al database.
     * 
     * @return utente che accede al database
     */
    public User getUser() {
        return this.user;
    }

    /**
     * Salva una nuova categoria nel database.
     * 
     * @param category
     *                 nuova categoria da salvare.
     * @throws CategoryDatabaseException
     *                                   se l'aggiunta della categoria al database
     *                                   non va a buon fine
     */

    /**
     * Salva una nuova categoria nel database.
     * 
     * @param category
     *                 nuova categoria da salvare.
     * @throws IllegalArgumentException
     *                                   se {@code category == null}
     * @throws CategoryDatabaseException
     *                                   se l'aggiunta della categoria al database
     *                                   non va a buon fine
     */
    public abstract void addCategory(Category category) throws IllegalArgumentException, CategoryDatabaseException;

    /**
     * Modifica una categoria nel database.
     * 
     * @param category
     *                 categoria modificata da aggiornare nel database
     * @throws IllegalArgumentException
     *                                   se {@code category == null}
     * @throws CategoryDatabaseException
     *                                   se la modifica della categoria nel database
     *                                   non va a buon fine
     */
    public abstract void changeCategory(Category category) throws IllegalArgumentException, CategoryDatabaseException;

    /**
     * Elimina una categoria nel database.
     * 
     * @param category
     *                 categoria da eliminare.
     * @throws IllegalArgumentException
     *                                   se {@code category == null}
     * @throws CategoryDatabaseException
     *                                   se la rimozione della categoria dal
     *                                   database non va a buon fine
     */
    public abstract void removeCategory(Category category) throws IllegalArgumentException, CategoryDatabaseException;

    /**
     * Ottiene tutte le categorie appartenenti a un utente nel database.
     * 
     * @return lista contenente tutte le categorie dell'utente.
     * @throws CategoryDatabaseException
     *                                   se il recupero delle categorie dal database
     *                                   non va a buon fine
     */
    public abstract Category[] getUserCategories() throws CategoryDatabaseException;
}
