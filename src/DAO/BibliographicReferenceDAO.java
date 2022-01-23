package DAO;

import Entities.Category;
import Entities.References.*;
import Entities.References.OnlineResources.Image;
import Entities.References.OnlineResources.SourceCode;
import Entities.References.OnlineResources.Video;
import Entities.References.OnlineResources.Website;
import Entities.References.PhysicalResources.Article;
import Entities.References.PhysicalResources.Book;
import Entities.References.PhysicalResources.Thesis;
import Exceptions.ReferenceDatabaseException;
import GUI.Homepage.Search.Search;

public interface BibliographicReferenceDAO {

    public BibliographicReference[] getReferences();

    public BibliographicReference[] getReferences(Category category) throws ReferenceDatabaseException;

    public BibliographicReference[] getReferences(Search search) throws ReferenceDatabaseException;

    public void removeReference(BibliographicReference reference) throws ReferenceDatabaseException;

    public void saveArticle(Article article) throws ReferenceDatabaseException;

    public void saveBook(Book book) throws ReferenceDatabaseException;

    public void saveThesis(Thesis thesis) throws ReferenceDatabaseException;

    public void saveImage(Image image) throws ReferenceDatabaseException;

    public void saveSourceCode(SourceCode sourceCode) throws ReferenceDatabaseException;

    public void saveVideo(Video video) throws ReferenceDatabaseException;

    public void saveWebsite(Website website) throws ReferenceDatabaseException;

}
