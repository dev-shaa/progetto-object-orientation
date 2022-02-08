package DAO;

import java.util.List;

import Entities.Tag;
import Entities.References.BibliographicReference;
import Exceptions.TagDatabaseException;

/**
 * Interfaccia che deve essere implementata per gestire la parte di database relativo alle parole chiave.
 */
public interface TagDAO {

    /**
     * Salva le parole associate a un riferimento nel database.
     * 
     * @param reference
     *            riferimenti di cui salvare le parole chiave
     * @throws TagDatabaseException
     *             se l'inserimento non va a buon fine
     */
    public void save(BibliographicReference reference) throws TagDatabaseException;

    /**
     * Restituisce tutte le parole chiave associate a un riferimento.
     * 
     * @param reference
     *            riferimento di cui recuperare le parole chiave
     * @return lista con le parole chiave del riferimento
     * @throws TagDatabaseException
     *             se il recupero non va a buon fine
     */
    public List<Tag> get(BibliographicReference reference) throws TagDatabaseException;
}
