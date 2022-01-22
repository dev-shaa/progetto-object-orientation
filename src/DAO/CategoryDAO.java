package DAO;

import Entities.*;
import Exceptions.*;

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
    public void addCategory(Category category) throws IllegalArgumentException, CategoryDatabaseException;

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
    public void changeCategory(Category category) throws IllegalArgumentException, CategoryDatabaseException;

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
    public void removeCategory(Category category) throws IllegalArgumentException, CategoryDatabaseException;

    /**
     * Ottiene tutte le categorie appartenenti a un utente nel database.
     * 
     * @return lista contenente tutte le categorie dell'utente.
     * @throws CategoryDatabaseException
     *             se il recupero delle categorie dal database non va a buon fine
     */
    public Category[] getUserCategories() throws CategoryDatabaseException;

}
