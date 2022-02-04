package DAO;

import java.util.Collection;
import java.util.List;

import Entities.Tag;
import Entities.References.BibliographicReference;
import Exceptions.TagDatabaseException;

/**
 * Interfaccia che deve essere implementata per gestire la parte di database relativo ai tag.
 */
public interface TagDAO {

    /**
     * Salva una parola chiave nel database.
     * 
     * @param tag
     *            parola chiave da salvare
     * @throws TagDatabaseException
     *             se il salvataggio non va a buon fine
     */
    public void save(Tag tag) throws TagDatabaseException;

    /**
     * Salva una collezione di parole chiave nel database.
     * 
     * @param tag
     *            parole chiave da salvare
     * @throws TagDatabaseException
     *             se il salvataggio non va a buon fine
     */
    public void save(Collection<? extends Tag> tags) throws TagDatabaseException;

    /**
     * Recupera le parole chiave associate a un riferimento.
     * 
     * @param reference
     *            riferimento di cui cercare le parole chiave
     * @return lista contenente le parole chiave
     * @throws TagDatabaseException
     *             se il recupero non va a buon fine
     */
    public List<Tag> get(BibliographicReference reference) throws TagDatabaseException;
}
