package GUI.Homepage.Categories;

import Entities.*;
import GUI.Utilities.CustomTreeModel;
import GUI.Utilities.CustomTreeNode;
import GUI.Utilities.JPopupButton;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.TreePath;
import com.jidesoft.swing.CheckBoxTree;

/**
 * Un pannello che consente di selezionare più categorie.
 */
public class CategoriesSelectionPopupMenu extends JPopupButton {

    private CheckBoxTree checkboxTree;
    private CustomTreeModel<Category> categoriesTree;

    /**
     * Crea {@code CategoriesSelectionPopupMenu} con l'albero delle categorie dato.
     * 
     * @param categoriesTree
     *            l'albero delle categorie
     */
    public CategoriesSelectionPopupMenu(CustomTreeModel<Category> categoriesTree) {
        super("Premi per selezionare le categorie");

        setCategoriesTree(categoriesTree);

        addToPopupMenu(checkboxTree);
    }

    /**
     * Imposta l'albero delle categorie.
     * 
     * @param categoriesTree
     *            l'albero delle categorie
     */
    public void setCategoriesTree(CustomTreeModel<Category> categoriesTree) {
        this.categoriesTree = categoriesTree;

        if (checkboxTree == null) {
            checkboxTree = new CheckBoxTree(categoriesTree);

            checkboxTree.setToggleClickCount(0);
            checkboxTree.setDigIn(false);
            checkboxTree.setEditable(false);
            checkboxTree.setRootVisible(false);
            checkboxTree.setClickInCheckBoxOnly(true);
            checkboxTree.setSelectPartialOnToggling(true);
            checkboxTree.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        } else {
            checkboxTree.setModel(categoriesTree);
        }
    }

    /**
     * Restituisce le categorie selezionate dall'utente.
     * 
     * @return
     *         categorie selezionate, {@code null} o array vuoto se non è selezionato niente
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Category> getSelectedCategories() {
        TreePath[] selectedPaths = checkboxTree.getCheckBoxTreeSelectionModel().getSelectionPaths();

        if (selectedPaths == null)
            return null;

        ArrayList<Category> selectedCategories = new ArrayList<>(selectedPaths.length);

        for (TreePath path : selectedPaths) {
            selectedCategories.add(((CustomTreeNode<Category>) path.getLastPathComponent()).getUserObject());
        }

        return selectedCategories;
    }

    /**
     * Seleziona una categoria nell'albero, se presenta.
     * 
     * @param category
     *            categoria da selezionare
     * @throws IllegalArgumentException
     *             se {@code category == null}
     */
    public void selectCategory(Category category) {
        if (category == null)
            throw new IllegalArgumentException("category can't be null");

        selectCategory(category, categoriesTree.getRoot());
    }

    /**
     * Funzione ricorsiva per cercare e selezionare una categoria.
     * 
     * @param category
     *            categoria da selezionare
     * @param startNode
     *            nodo di partenza
     * @return {@code true} se è stato trovato
     */
    private boolean selectCategory(Category category, CustomTreeNode<Category> startNode) {
        if (category == null)
            throw new IllegalArgumentException("category can't be null");

        if (startNode == null)
            return false;

        Category nodeCategory = startNode.getUserObject();

        if (nodeCategory != null && nodeCategory.equals(category)) {
            checkboxTree.getCheckBoxTreeSelectionModel().addSelectionPath(new TreePath(startNode));
            return true;
        }

        boolean found = false;
        Enumeration<CustomTreeNode<Category>> children = startNode.children();

        while (children.hasMoreElements() && !found) {
            found = selectCategory(category, children.nextElement());
        }

        return found;
    }

    @Override
    protected void onPopupOpen() {
        for (int i = 0; i < checkboxTree.getRowCount(); i++)
            checkboxTree.expandRow(i);

        super.onPopupOpen();
    }

}
