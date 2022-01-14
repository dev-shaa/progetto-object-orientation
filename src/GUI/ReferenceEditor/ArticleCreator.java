package GUI.ReferenceEditor;

import javax.swing.JTextField;

import DAO.BibliographicReferenceDAO;
import Entities.References.PhysicalResources.Article;
import GUI.Categories.CategoriesTreeManager;

/**
 * Pannello di dialogo per la creazione di un nuovo riferimento a un articolo.
 */
public class ArticleCreator extends PublicationCreator {

    private JTextField ISSN;

    /**
     * TODO:
     * 
     * @param categoriesTreeManager
     * @param referenceDAO
     * @throws IllegalArgumentException
     */
    public ArticleCreator(CategoriesTreeManager categoriesTreeManager, BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        super("Articolo", categoriesTreeManager, referenceDAO);
    }

    public ArticleCreator(CategoriesTreeManager categoriesTreeManager, BibliographicReferenceDAO referenceDAO, Article article) throws IllegalArgumentException {
        this(categoriesTreeManager, referenceDAO);

        setReferenceTitle(article.getTitle());
        // TODO:
    }

    private ArticleCreator(String dialogueTitle, CategoriesTreeManager categoriesTreeManager, BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        super(dialogueTitle, categoriesTreeManager, referenceDAO);
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTreeManager) {
        super.setup(categoriesTreeManager);

        ISSN = new JTextField();

        addComponent("ISSN", ISSN);
    }

    protected String getISSN() {
        return ISSN.getText().trim();
    }

    @Override
    protected void onConfirmClick() {
        // TODO: salva articolo

        // TODO: autore, page count

        Article article = new Article(getReferenceTitle());
        article.setDOI(getDOI());
        article.setDescription(getDescription());
        article.setISSN(getISSN());
        article.setLanguage(getLanguage());
        article.setPubblicationDate(getPubblicationDate());
        article.setPublisher(getPublisher());
        article.setTags(getTags());
        article.setURL(getURL());

    }

}
