package GUI.ReferenceEditor;

import GUI.Categories.*;
import DAO.BibliographicReferenceDAO;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

/**
 * TODO: commenta
 */
public class ThesisCreator extends PublicationCreator {

    private JTextField university;
    private JTextField faculty;

    public ThesisCreator(CategoriesTreeManager categoriesTreeManager, BibliographicReferenceDAO referenceDAO) {
        this("Tesi", categoriesTreeManager, referenceDAO);
    }

    private ThesisCreator(String dialogueTitle, CategoriesTreeManager categoriesTreeManager, BibliographicReferenceDAO referenceDAO) {
        super(dialogueTitle, categoriesTreeManager, referenceDAO);
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTreeManager) {
        super.setup(categoriesTreeManager);

        university = new JTextField();
        faculty = new JTextField();

        addComponent("Università", university);
        addComponent("Facoltà", faculty);
    }

    @Override
    protected void onConfirmClick() {
        // TODO Auto-generated method stub

    }

}
