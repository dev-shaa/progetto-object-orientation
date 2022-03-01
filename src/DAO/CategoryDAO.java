package DAO;

import java.util.List;
import Entities.*;
import Exceptions.Database.CategoryDatabaseException;

/**
 * Interfaccia che deve essere implementata per gestire la parte di database relativo alle categorie.
 */
public interface CategoryDAO {

    /**
     * Salva una nuova categoria nel database.
     * 
     * @param category
     *            nuova categoria da salvare.
     * @throws CategoryDatabaseException
     *             se l'aggiunta della categoria al database non va a buon fine
     */
    public void save(Category category) throws CategoryDatabaseException;

    /**
     * Modifica una categoria nel database.
     * 
     * @param category
     *            categoria modificata da aggiornare nel database
     * @throws CategoryDatabaseException
     *             se la modifica della categoria nel database non va a buon fine
     */
    public void update(Category category) throws CategoryDatabaseException;

    /**
     * Elimina una categoria nel database.
     * 
     * @param category
     *            categoria da eliminare.
     * @throws CategoryDatabaseException
     *             se la rimozione della categoria dal database non va a buon fine
     */
    public void remove(Category category) throws CategoryDatabaseException;

    /**
     * Ottiene tutte le categorie appartenenti a un utente nel database.
     * 
     * @return lista contenente tutte le categorie dell'utente
     * @throws CategoryDatabaseException
     *             se il recupero delle categorie dal database non va a buon fine
     */
    public List<Category> getAll() throws CategoryDatabaseException;

    /**
     * Ottiene gli ID di tutte le categorie a cui è associato un riferimento.
     * 
     * @param referenceID
     *            identificativo del riferimento di cui trovare le categorie
     * @return lista contenente gli ID delle categorie associate
     * @throws CategoryDatabaseException
     *             se il recupero degli ID dal database non va a buon fine
     */
    public List<Integer> getIDs(int referenceID) throws CategoryDatabaseException;
}