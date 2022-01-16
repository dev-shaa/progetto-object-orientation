package GUI.Categories;

import Entities.Category;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeNode;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeSelectionModel;
import java.util.Enumeration;

/**
 * Albero in cui è possibile selezionare più categorie.
 */
public class CategoriesCheckboxTree extends JTree {

    public CategoriesCheckboxTree(CategoriesTreeManager categoriesTreeModel) {
        super(categoriesTreeModel.getTreeModel());

        setToggleClickCount(0);
        setRootVisible(false);
        setEditable(false);
        setCellRenderer(new CategoriesCheckboxTreeRenderer());

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
     * Restituisce le categorie selezionate dall'utente.
     * 
     * @return un array di {@code Categories} con tutte le categorie selezionate, {@code null} se non è stato selezionato niente
     */
    public Category[] getSelectedCategories() {
        TreePath[] selectedPaths = getSelectionPaths();

        // FIXME: quando viene selezionata e deselezionata una categoria, non essendo visibile il nodo root, esso rimane sempre tra i selezionati
        if (selectedPaths == null || selectedPaths.length < 2)
            return null;

        Category[] selectedCategories = new Category[selectedPaths.length];

        for (int i = 0; i < selectedPaths.length; i++)
            selectedCategories[i] = (Category) ((DefaultMutableTreeNode) selectedPaths[i].getLastPathComponent()).getUserObject();

        return selectedCategories;
    }

    /**
     * Espande tutti i nodi dell'albero.
     */
    public void expandAllRows() {
        for (int i = 0; i < getRowCount(); i++)
            expandRow(i);
    }

}
