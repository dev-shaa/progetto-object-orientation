package Database.DAO;

import java.sql.SQLException;
import java.util.List;
import Entities.*;

/**
 * Interfaccia che deve essere implementata per gestire la parte di database relativo alle categorie.
 */
public interface CategoryDAO {

    /**
     * Salva una nuova categoria nel database.
     * 
     * @param category
     *            nuova categoria da salvare.
     * @throws SQLException
     *             se l'aggiunta della categoria al database non va a buon fine
     */
    public void save(Category category) throws SQLException;

    /**
     * Modifica una categoria nel database.
     * 
     * @param category
     *            categoria modificata da aggiornare nel database
     * @throws SQLException
     *             se la modifica della categoria nel database non va a buon fine
     */
    public void update(Category category) throws SQLException;

    /**
     * Elimina una categoria nel database.
     * 
     * @param category
     *            categoria da eliminare.
     * @throws SQLException
     *             se la rimozione della categoria dal database non va a buon fine
     */
    public void remove(Category category) throws SQLException;

    /**
     * Ottiene tutte le categorie appartenenti a un utente nel database.
     * 
     * @return lista contenente tutte le categorie dell'utente
     * @throws SQLException
     *             se il recupero delle categorie dal database non va a buon fine
     */
    public List<Category> getAll() throws SQLException;

    /**
     * Ottiene gli ID di tutte le categorie a cui è associato un riferimento.
     * 
     * @param referenceID
     *            identificativo del riferimento di cui trovare le categorie
     * @return lista contenente gli ID delle categorie associate
     * @throws SQLException
     *             se il recupero degli ID dal database non va a buon fine
     */
    public List<Integer> getCategoriesIDFor(int referenceID) throws SQLException;
}