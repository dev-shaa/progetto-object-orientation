package DAO;

import java.util.List;

import Entities.References.*;
import Entities.References.OnlineResources.Image;
import Entities.References.OnlineResources.SourceCode;
import Entities.References.OnlineResources.Video;
import Entities.References.OnlineResources.Website;
import Entities.References.PhysicalResources.Article;
import Entities.References.PhysicalResources.Book;
import Entities.References.PhysicalResources.Thesis;
import Exceptions.ReferenceDatabaseException;

/**
 * TODO: commenta
 */
public interface BibliographicReferenceDAO {

    public List<BibliographicReference> getReferences();

    public void removeReference(BibliographicReference reference) throws ReferenceDatabaseException;

    public void saveReference(Article article) throws ReferenceDatabaseException;

    public void saveReference(Book book) throws ReferenceDatabaseException;

    public void saveReference(Thesis thesis) throws ReferenceDatabaseException;

    public void saveReference(Image image) throws ReferenceDatabaseException;

    public void saveReference(SourceCode sourceCode) throws ReferenceDatabaseException;

    public void saveReference(Video video) throws ReferenceDatabaseException;

    public void saveReference(Website website) throws ReferenceDatabaseException;

}
