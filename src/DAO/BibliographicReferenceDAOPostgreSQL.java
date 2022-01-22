package DAO;

import Entities.Category;
import Entities.User;
import Entities.References.BibliographicReference;
import Entities.References.OnlineResources.Image;
import Entities.References.OnlineResources.SourceCode;
import Entities.References.OnlineResources.Video;
import Entities.References.OnlineResources.Website;
import Entities.References.PhysicalResources.Article;
import Entities.References.PhysicalResources.Book;
import Entities.References.PhysicalResources.Thesis;
import Exceptions.ReferenceDatabaseException;
import GUI.Homepage.Search.Search;

public class BibliographicReferenceDAOPostgreSQL implements BibliographicReferenceDAO {

    private User user;

    public BibliographicReferenceDAOPostgreSQL(User user) throws IllegalArgumentException {
        setUser(user);
    }

    public void setUser(User user) throws IllegalArgumentException {
        if (user == null)
            throw new IllegalArgumentException("user non può essere null");

        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public BibliographicReference[] getReferences(Category category) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BibliographicReference[] getReferences(Search search) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removeReference(BibliographicReference reference) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveArticle(Article article) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveBook(Book book) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveThesis(Thesis thesis) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveImage(Image image) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveSourceCode(SourceCode sourceCode) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveVideo(Video video) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveWebsite(Website website) throws ReferenceDatabaseException {
        // TODO Auto-generated method stub

    }

}
