package RetrieveManagement.DAO;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import Entities.Author;

/**
 * Interfaccia che deve essere implementata per gestire la parte di database relativo agli autori.
 */
public interface AuthorDAO {

	/**
	 * Salva una collezione di autori nel database.
	 * 
	 * @param authors
	 *            autori da salvare
	 * @throws SQLException
	 *             se il salvataggio non va a buon fine
	 */
	public void save(Collection<? extends Author> authors) throws SQLException;

	/**
	 * Restituisce gli autori di un riferimento.
	 * 
	 * @param referenceID
	 *            identificativo del riferimento di cui trovare gli autori
	 * @return lista con gli autori del riferimento
	 * @throws SQLException
	 *             se il recupero non va a buon fine
	 */
	public List<Author> get(int referenceID) throws SQLException;
}