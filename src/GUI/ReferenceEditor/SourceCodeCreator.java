package GUI.ReferenceEditor;

import GUI.Categories.CategoriesTreeManager;
import Entities.References.OnlineResources.ProgrammingLanguage;
import javax.swing.JComboBox;

import DAO.BibliographicReferenceDAO;

import java.awt.event.ActionEvent;

public class SourceCodeCreator extends OnlineResourceCreator {

    private JComboBox<ProgrammingLanguage> programmingLanguage;

    public SourceCodeCreator(CategoriesTreeManager categoriesTreeManager, BibliographicReferenceDAO referenceDAO) {
        this("Codice sorgente", categoriesTreeManager, referenceDAO);
    }

    private SourceCodeCreator(String dialogueTitle, CategoriesTreeManager categoriesTreeManager, BibliographicReferenceDAO referenceDAO) {
        super(dialogueTitle, categoriesTreeManager, referenceDAO);
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTreeManager) {
        super.setup(categoriesTreeManager);

        programmingLanguage = new JComboBox<>(ProgrammingLanguage.values());
        addComponent("Linguaggio", programmingLanguage);
    }

    @Override
    protected void onConfirmClick() {
        // TODO Auto-generated method stub

    }

}
