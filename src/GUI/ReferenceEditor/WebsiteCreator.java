package GUI.ReferenceEditor;

import java.awt.event.ActionEvent;

import DAO.BibliographicReferenceDAO;
import GUI.Categories.CategoriesTreeManager;

/**
 * TODO: commenta
 */
public class WebsiteCreator extends OnlineResourceCreator {

    public WebsiteCreator(CategoriesTreeManager categoriesTreeManager, BibliographicReferenceDAO referenceDAO) {
        this("Sito web", categoriesTreeManager, referenceDAO);
    }

    private WebsiteCreator(String dialogueTitle, CategoriesTreeManager categoriesTreeManager, BibliographicReferenceDAO referenceDAO) {
        super(dialogueTitle, categoriesTreeManager, referenceDAO);
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTreeManager) {
        super.setup(categoriesTreeManager);
    }

    @Override
    protected void onConfirmClick() {
        // TODO Auto-generated method stub

    }

}
