package DAO;

import java.util.Collection;
import java.util.List;

import Entities.Tag;
import Entities.References.BibliographicReference;
import Exceptions.TagDatabaseException;

public interface TagDAO {
    public void save(Collection<? extends Tag> tags, BibliographicReference reference) throws TagDatabaseException;

    public List<Tag> get(BibliographicReference reference) throws TagDatabaseException;
}
