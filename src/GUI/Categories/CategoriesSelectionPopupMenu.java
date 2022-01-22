package GUI.Categories;

import Entities.*;
import GUI.Utilities.JPopupButton;

import java.awt.*;
import java.awt.event.*;

/**
 * Un pannello che consente di selezionare più categorie.
 */
public class CategoriesSelectionPopupMenu extends JPopupButton {

    private CategoriesCheckboxTree categoriesSelectionTree;

    /**
     * Crea {@code CategoriesSelectionPopupMenu} con l'albero delle categorie dato.
     * 
     * @param categoriesTreeManager
     *            l'albero delle categorie
     * @throws IllegalArgumentException
     *             se {@code categoriesTreeManager == null}
     */
    public CategoriesSelectionPopupMenu(CategoriesTreeManager categoriesTreeManager) throws IllegalArgumentException {
        if (categoriesTreeManager == null)
            throw new IllegalArgumentException();

        setText("Premi per selezionare le categorie");

        categoriesSelectionTree = new CategoriesCheckboxTree(categoriesTreeManager);
        categoriesSelectionTree.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        addToPopupMenu(categoriesSelectionTree);
    }

    /**
     * Invocato quando viene premuto il pulsante per aprire il popup.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        categoriesSelectionTree.expandAllRows();
        super.actionPerformed(e);
    }

    /**
     * Restituisce le categorie selezionate dall'utente.
     * 
     * @return
     *         categorie selezionate, {@code null} se non è selezionato niente
     */
    public Category[] getSelectedCategories() {
        return categoriesSelectionTree.getSelectedCategories();
    }

}
