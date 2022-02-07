package DAO;

import java.util.Collection;
import java.util.List;
import Entities.Author;
import Entities.References.BibliographicReference;
import Exceptions.AuthorDatabaseException;

public interface AuthorDAO {

	public void save(Author author) throws AuthorDatabaseException;

	public void save(Collection<? extends Author> authors) throws AuthorDatabaseException;

	public List<Author> getAll() throws AuthorDatabaseException;

	public List<Author> get(BibliographicReference reference) throws AuthorDatabaseException;

}