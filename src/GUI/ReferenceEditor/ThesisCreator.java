package GUI.ReferenceEditor;

import GUI.Categories.*;

import java.awt.event.ActionEvent;

import javax.swing.JTextField;

public class ThesisCreator extends PublicationCreator {

    private JTextField university;
    private JTextField faculty;

    public ThesisCreator(CategoriesTreeManager categoriesTreeManager) {
        this("Tesi", categoriesTreeManager);
    }

    private ThesisCreator(String dialogueTitle, CategoriesTreeManager categoriesTreeManager) {
        super(dialogueTitle, categoriesTreeManager);
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
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }

}
