package GUI.ReferenceEditor;

import java.awt.event.ActionEvent;

import GUI.Categories.CategoriesTreeManager;

/**
 * 
 */
public class WebsiteCreator extends OnlineResourceCreator {

    public WebsiteCreator(CategoriesTreeManager categoriesTreeManager) {
        this("Sito web", categoriesTreeManager);
    }

    private WebsiteCreator(String dialogueTitle, CategoriesTreeManager categoriesTreeManager) {
        super(dialogueTitle, categoriesTreeManager);
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTreeManager) {
        super.setup(categoriesTreeManager);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }

}
