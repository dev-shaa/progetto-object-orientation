package GUI.Homepage.Categories;

import Entities.*;
import GUI.Utilities.JPopupButton;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.tree.TreePath;

import com.jidesoft.swing.CheckBoxTree;

/**
 * Un pannello che consente di selezionare più categorie.
 */
public class CategoriesSelectionPopupMenu extends JPopupButton {

    private CheckBoxTree categoriesCheckboxTree;

    /**
     * Crea {@code CategoriesSelectionPopupMenu} con l'albero delle categorie dato.
     * 
     * @param categoriesTree
     *            l'albero delle categorie
     * @throws IllegalArgumentException
     *             se {@code categoriesTreeManager == null}
     */
    public CategoriesSelectionPopupMenu(CategoryTreeModel categoriesTree) {
        setText("Premi per selezionare le categorie");

        setCategoriesTree(categoriesTree);

        addToPopupMenu(categoriesCheckboxTree);
    }

    /**
     * Imposta l'albero delle categorie.
     * 
     * @param categoriesTree
     *            l'albero delle categorie
     * @throws IllegalArgumentException
     *             se {@code categoriesTreeManager == null}
     */
    public void setCategoriesTree(CategoryTreeModel categoriesTree) {
        if (categoriesCheckboxTree == null) {
            categoriesCheckboxTree = new CheckBoxTree(categoriesTree);

            categoriesCheckboxTree.setToggleClickCount(0);
            categoriesCheckboxTree.setDigIn(false);
            categoriesCheckboxTree.setEditable(false);
            categoriesCheckboxTree.setRootVisible(false);
            categoriesCheckboxTree.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        } else {
            categoriesCheckboxTree.setModel(categoriesTree);
        }
    }

    /**
     * Restituisce le categorie selezionate dall'utente.
     * 
     * @return
     *         categorie selezionate, {@code null} se non è selezionato niente
     */
    public Category[] getSelectedCategories() {
        TreePath[] paths = categoriesCheckboxTree.getSelectionPaths();

        if (paths == null)
            return null;

        Category[] selectedCategories = new Category[paths.length];

        for (TreePath treePath : paths) {
            // TODO:
            System.out.println(treePath.getLastPathComponent());
        }

        return selectedCategories;

    }

    @Override
    protected void onPopupOpen() {
        for (int i = 0; i < categoriesCheckboxTree.getRowCount(); i++)
            categoriesCheckboxTree.expandRow(i);

        super.onPopupOpen();
    }

}
