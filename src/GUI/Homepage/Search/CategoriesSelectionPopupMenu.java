package GUI.Homepage.Search;

import Entities.*;
import GUI.Categories.*;
import GUI.Utilities.JPopupButton;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

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

        setText("Categorie");

        categoriesSelectionTree = new CategoriesCheckboxTree(categoriesTreeManager);
        categoriesSelectionTree.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        addComponentToPopupMenu(categoriesSelectionTree);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        categoriesSelectionTree.expandAllRows();
        super.actionPerformed(e);
    }

    /**
     * TODO: commenta
     * 
     * @return
     */
    public Category[] getSelectedCategories() {
        return categoriesSelectionTree.getSelectedCategories();
    }

}
