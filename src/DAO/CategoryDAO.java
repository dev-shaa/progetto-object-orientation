package DAO;

import Entities.*;
import Exceptions.*;
import GUI.Utilities.CustomTreeModel;

/**
 * Classe astratta che si occupa di gestire la parte di database relativo alle
 * categorie.
 */
public interface CategoryDAO {

    /**
     * Salva una nuova categoria nel database.
     * 
     * @param category
     *            nuova categoria da salvare.
     * @throws IllegalArgumentException
     *             se {@code category == null}
     * @throws CategoryDatabaseException
     *             se l'aggiunta della categoria al database non va a buon fine
     */
    public void addCategory(Category category) throws CategoryDatabaseException;

    /**
     * Modifica una categoria nel database.
     * 
     * @param category
     *            categoria modificata da aggiornare nel database
     * @throws IllegalArgumentException
     *             se {@code category == null}
     * @throws CategoryDatabaseException
     *             se la modifica della categoria nel database non va a buon fine
     */
    public void updateCategoryName(Category category) throws CategoryDatabaseException;

    /**
     * Elimina una categoria nel database.
     * 
     * @param category
     *            categoria da eliminare.
     * @throws IllegalArgumentException
     *             se {@code category == null}
     * @throws CategoryDatabaseException
     *             se la rimozione della categoria dal database non va a buon fine
     */
    public void removeCategory(Category category) throws CategoryDatabaseException;

    /**
     * Ottiene tutte le categorie appartenenti a un utente nel database.
     * 
     * @return albero contenente tutte le categorie dell'utente.
     * @throws CategoryDatabaseException
     *             se il recupero delle categorie dal database non va a buon fine
     */
    public CustomTreeModel<Category> getUserCategories() throws CategoryDatabaseException;

}
