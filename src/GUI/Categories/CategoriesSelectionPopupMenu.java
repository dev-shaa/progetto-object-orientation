package GUI.Categories;

import Entities.*;
import GUI.Utilities.JPopupButton;
import java.awt.Dimension;

/**
 * Un pannello che consente di selezionare più categorie.
 */
public class CategoriesSelectionPopupMenu extends JPopupButton {

    private CategoriesCheckboxTree categoriesCheckboxTree;

    /**
     * Crea {@code CategoriesSelectionPopupMenu} con l'albero delle categorie dato.
     * 
     * @param categoriesTree
     *            l'albero delle categorie
     * @throws IllegalArgumentException
     *             se {@code categoriesTreeManager == null}
     */
    public CategoriesSelectionPopupMenu(CategoriesTreeManager categoriesTree) throws IllegalArgumentException {
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
    public void setCategoriesTree(CategoriesTreeManager categoriesTree) throws IllegalArgumentException {
        if (categoriesTree == null)
            throw new IllegalArgumentException();

        if (categoriesCheckboxTree == null) {
            categoriesCheckboxTree = new CategoriesCheckboxTree(categoriesTree);
            categoriesCheckboxTree.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        } else {
            categoriesCheckboxTree.setCategoriesTree(categoriesTree);
        }
    }

    /**
     * Restituisce le categorie selezionate dall'utente.
     * 
     * @return
     *         categorie selezionate, {@code null} se non è selezionato niente
     */
    public Category[] getSelectedCategories() {
        return categoriesCheckboxTree.getSelectedCategories();
    }

    @Override
    protected void onPopupOpen() {
        categoriesCheckboxTree.expandAllRows();
        super.onPopupOpen();
    }

}
