package Database.DAO;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import Entities.Tag;

/**
 * Interfaccia che deve essere implementata per gestire la parte di database relativo alle parole chiave.
 */
public interface TagDAO {

    /**
     * Salva delle parole chiave associate a un riferimento nel database.
     * 
     * @param referenceID
     *            identificativo del riferimento
     * @param tags
     *            parole chiave da salvare
     * @throws SQLException
     *             se l'inserimento non va a buon fine
     */
    public void save(int referenceID, Collection<? extends Tag> tags) throws SQLException;

    /**
     * Restituisce tutte le parole chiave associate a un riferimento.
     * 
     * @param referenceID
     *            identificativo del riferimento di cui recuperare le parole chiave
     * @return lista con le parole chiave del riferimento
     * @throws SQLException
     *             se il recupero non va a buon fine
     */
    public List<Tag> getAll(int referenceID) throws SQLException;
}