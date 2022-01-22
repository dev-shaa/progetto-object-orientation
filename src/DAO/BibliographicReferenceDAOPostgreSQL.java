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
    public BibliographicReference[] getReferences(Category category) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BibliographicReference[] getReferences(Search search) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removeReference(BibliographicReference reference) {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveArticle(Article article) {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveBook(Book book) {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveThesis(Thesis thesis) {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveImage(Image image) {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveSourceCode(SourceCode sourceCode) {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveVideo(Video video) {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveWebsite(Website website) {
        // TODO Auto-generated method stub

    }

}
