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
import GUI.Homepage.Search.Search;

public interface BibliographicReferenceDAO {

    public BibliographicReference[] getReferences(Category category);

    public BibliographicReference[] getReferences(Search search);

    public void removeReference(BibliographicReference reference);

    public void saveArticle(Article article);

    public void saveBook(Book book);

    public void saveThesis(Thesis thesis);

    public void saveImage(Image image);

    public void saveSourceCode(SourceCode sourceCode);

    public void saveVideo(Video video);

    public void saveWebsite(Website website);

}
