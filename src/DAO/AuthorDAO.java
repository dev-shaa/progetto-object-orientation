package DAO;

import java.util.Collection;
import java.util.List;
import Entities.Author;
import Entities.References.BibliographicReference;
import Exceptions.Database.AuthorDatabaseException;

/**
 * Interfaccia che deve essere implementata per gestire la parte di database relativo agli autori.
 */
public interface AuthorDAO {

	/**
	 * Salva una collezione di autori nel database.
	 * 
	 * @param authors
	 *            autori da salvare
	 * @throws AuthorDatabaseException
	 *             se il salvataggio non va a buon fine
	 */
	public void save(Collection<? extends Author> authors) throws AuthorDatabaseException;

	/**
	 * Restituisce gli autori di un riferimento.
	 * 
	 * @param reference
	 *            riferimenti di cui trovare gli autori
	 * @return lista con gli autori del riferimento
	 * @throws AuthorDatabaseException
	 *             se il recupero non va a buon fine
	 */
	public List<Author> get(BibliographicReference reference) throws AuthorDatabaseException;

}