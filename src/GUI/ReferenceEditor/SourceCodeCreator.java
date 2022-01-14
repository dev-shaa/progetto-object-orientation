package GUI.ReferenceEditor;

import GUI.Categories.CategoriesTreeManager;
import Entities.References.OnlineResources.ProgrammingLanguage;
import javax.swing.JComboBox;
import java.awt.event.ActionEvent;

public class SourceCodeCreator extends OnlineResourceCreator {

    private JComboBox<ProgrammingLanguage> programmingLanguage;

    public SourceCodeCreator(CategoriesTreeManager categoriesTreeManager) {
        this("Codice sorgente", categoriesTreeManager);
    }

    private SourceCodeCreator(String dialogueTitle, CategoriesTreeManager categoriesTreeManager) {
        super(dialogueTitle, categoriesTreeManager);
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTreeManager) {
        super.setup(categoriesTreeManager);

        programmingLanguage = new JComboBox<>(ProgrammingLanguage.values());
        addComponent("Linguaggio", programmingLanguage);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO: Auto-generated method stub
    }

}
