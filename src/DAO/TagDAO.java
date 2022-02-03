package DAO;

import java.util.List;

import Entities.Tag;
import Entities.References.BibliographicReference;
import Exceptions.TagDatabaseException;

/**
 * Interfaccia che deve essere implementata per gestire la parte di database relativo ai tag.
 */
public interface TagDAO {

    /**
     * TODO: commenta
     * 
     * @param tag
     * @throws TagDatabaseException
     */
    public void save(Tag tag) throws TagDatabaseException;

    /**
     * 
     * @param reference
     * @return
     * @throws TagDatabaseException
     */
    public List<Tag> get(BibliographicReference reference) throws TagDatabaseException;
}
