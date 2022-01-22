package GUI.Categories;

import Entities.Category;
import GUI.Utilities.CheckboxTreeRenderer;

import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeNode;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeSelectionModel;

import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Albero in cui è possibile selezionare più categorie.
 */
public class CategoriesCheckboxTree extends JTree {

    /**
     * TODO: commenta
     * 
     * @param categoriesTree
     * @throws IllegalArgumentException
     */
    public CategoriesCheckboxTree(CategoriesTreeManager categoriesTree) throws IllegalArgumentException {
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
                Enumeration<TreeNode> children = ((DefaultMutableTreeNode) path.getLastPathComponent()).children();

                while (children.hasMoreElements()) {
                    removeSelectionPath(path.pathByAddingChild(children.nextElement()));
                }

                super.removeSelectionPath(path);
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
     *             se {@code categoriesTree == null}
     */
    public void setCategoriesTree(CategoriesTreeManager categoriesTree) throws IllegalArgumentException {
        if (categoriesTree == null)
            throw new IllegalArgumentException("categoriesTree non può essere null");

        setModel(categoriesTree.getTreeModel());
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
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();

            if (node != null) {
                Category category = (Category) node.getUserObject();

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
