package GUI.Homepage.Categories;

import Entities.Category;
import GUI.Utilities.CheckboxTreeRenderer;

import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeNode;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.MutableTreeNode;

import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Albero in cui è possibile selezionare più categorie tramite checkbox.
 */
public class CategoriesCheckboxTree extends JTree {

    /**
     * Crea un nuovo albero delle categorie con checkbox con le categorie indicate.
     * 
     * @param categoriesTree
     *            albero delle categorie da selezionare
     * @throws IllegalArgumentException
     *             se {@code categoriesTree} è nullo
     * @see #setCategoriesTree(CategoriesTreeManager)
     */
    public CategoriesCheckboxTree(CategoryTreeModel categoriesTree) {
        super();

        setCategoriesTree(categoriesTree);

        setToggleClickCount(0);
        setRootVisible(false);
        setEditable(false);
        setCellRenderer(new CheckboxTreeRenderer());

        setSelectionModel(new DefaultTreeSelectionModel() {

            @Override
            public void setSelectionPath(TreePath path) {
                if (isPathSelected(path)) {
                    removeSelectionPath(path);
                } else {
                    addSelectionPath(path);
                }
            }

            @Override
            public void addSelectionPath(TreePath path) {
                if (path == null)
                    return;

                addSelectionPath(path.getParentPath());

                super.addSelectionPath(path);
            }

            @Override
            public void removeSelectionPath(TreePath path) {
                // FIXME:
                // Enumeration<TreeNode> children = ((MutableTreeNode) path.getLastPathComponent()).children();

                // while (children.hasMoreElements()) {
                // removeSelectionPath(path.pathByAddingChild(children.nextElement()));
                // }

                // super.removeSelectionPath(path);
            }

            @Override
            public void setSelectionPaths(TreePath[] pPaths) {
                // super.setSelectionPaths(pPaths);
            }

        });

    }

    /**
     * Imposta l'albero delle categorie che si possono selezionare.
     * 
     * @param categoriesTree
     *            albero delle categorie
     * @throws IllegalArgumentException
     *             se {@code newModel} è nullo
     */
    public void setCategoriesTree(CategoryTreeModel categoriesTree) {
        if (categoriesTree == null)
            throw new IllegalArgumentException("newModel is not of type CategoryTreeModel");

        setModel(categoriesTree);
    }

    /**
     * Restituisce le categorie selezionate dall'utente.
     * 
     * @return un array di {@code Categories} con tutte le categorie selezionate
     */
    public Category[] getSelectedCategories() {
        TreePath[] selectedPaths = getSelectionPaths();

        if (selectedPaths == null)
            return new Category[0];

        ArrayList<Category> selectedCategories = new ArrayList<>();

        for (TreePath path : selectedPaths) {
            CategoryTreeNode node = (CategoryTreeNode) path.getLastPathComponent();

            if (node != null) {
                Category category = node.getUserObject();

                if (category != null) {
                    selectedCategories.add(category);
                    selectedCategories.remove(category.getParent());
                }
            }
        }

        return selectedCategories.toArray(new Category[selectedCategories.size()]);
    }

    /**
     * Espande tutti i nodi dell'albero.
     */
    public void expandAllRows() {
        for (int i = 0; i < getRowCount(); i++)
            expandRow(i);
    }

}
