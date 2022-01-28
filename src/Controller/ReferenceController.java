package Controller;

import java.util.ArrayList;
import java.util.List;

import DAO.BibliographicReferenceDAO;
import Entities.Category;
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

public class ReferenceController {
    private BibliographicReferenceDAO referenceDAO;
    private ArrayList<BibliographicReference> references;

    public ReferenceController(BibliographicReferenceDAO referenceDAO) {
        this.setReferenceDAO(referenceDAO);
    }

    public BibliographicReferenceDAO getReferenceDAO() {
        return referenceDAO;
    }

    public void setReferenceDAO(BibliographicReferenceDAO referenceDAO) {
        if (referenceDAO == null)
            throw new IllegalArgumentException("referenceDAO can't be null");

        this.referenceDAO = referenceDAO;
    }

    public List<BibliographicReference> getReferences() {
        return references;
    }

    public List<BibliographicReference> getReferences(Category category) {
        return references;
    }

    public List<BibliographicReference> getReferences(Search search) {
        return references;
    }

    public void removeReference(BibliographicReference reference) {

    }

    public void saveReference(Article reference) throws ReferenceDatabaseException {
        referenceDAO.saveArticle(reference);
        references.add(reference);
    }

    public void saveReference(Book reference) throws ReferenceDatabaseException {

    }

    public void saveReference(Thesis reference) throws ReferenceDatabaseException {

    }

    public void saveReference(Image reference) throws ReferenceDatabaseException {

    }

    public void saveReference(SourceCode reference) throws ReferenceDatabaseException {

    }

    public void saveReference(Video reference) throws ReferenceDatabaseException {

    }

    public void saveReference(Website reference) throws ReferenceDatabaseException {

    }

}
