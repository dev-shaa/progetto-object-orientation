package GUI.Utilities;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.TreePath;
import com.jidesoft.swing.CheckBoxTree;

public class PopupCheckboxTree<T> extends PopupButton {

    private CheckBoxTree checkboxTree;
    private CustomTreeModel<T> categoriesTree;

    public PopupCheckboxTree(CustomTreeModel<T> treeModel) {
        super("Premi per selezionare");

        setTreeModel(treeModel);

        addToPopupMenu(checkboxTree);
    }

    @Override
    protected void onPopupOpen() {
        for (int i = 0; i < checkboxTree.getRowCount(); i++)
            checkboxTree.expandRow(i);

        super.onPopupOpen();
    }

    public void setTreeModel(CustomTreeModel<T> treeModel) {
        this.categoriesTree = treeModel;

        if (checkboxTree == null) {
            checkboxTree = new CheckBoxTree(treeModel);

            checkboxTree.setToggleClickCount(0);
            checkboxTree.setDigIn(false);
            checkboxTree.setEditable(false);
            checkboxTree.setRootVisible(false);
            checkboxTree.setClickInCheckBoxOnly(true);
            checkboxTree.setSelectPartialOnToggling(true);
            checkboxTree.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        } else {
            checkboxTree.setModel(treeModel);
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<T> getSelectedItems() {
        TreePath[] selectedPaths = checkboxTree.getCheckBoxTreeSelectionModel().getSelectionPaths();

        if (selectedPaths == null)
            return null;

        ArrayList<T> selectedItems = new ArrayList<>(selectedPaths.length);

        for (TreePath path : selectedPaths) {
            selectedItems.add(((CustomTreeNode<T>) path.getLastPathComponent()).getUserObject());
        }

        return selectedItems;
    }

    /**
     * Deseleziona tutti gli elementi.
     */
    public void clearSelection() {
        checkboxTree.getCheckBoxTreeSelectionModel().clearSelection();
    }

    public void selectItem(T item) {
        if (item == null)
            throw new IllegalArgumentException("category can't be null");

        selectItem(item, categoriesTree.getRoot());
    }

    private boolean selectItem(T item, CustomTreeNode<T> startNode) {
        if (item == null)
            throw new IllegalArgumentException("category can't be null");

        if (startNode == null)
            return false;

        T nodeCategory = startNode.getUserObject();

        if (nodeCategory != null && nodeCategory.equals(item)) {
            checkboxTree.getCheckBoxTreeSelectionModel().addSelectionPath(new TreePath(categoriesTree.getPathToRoot(startNode)));
            return true;
        }

        boolean found = false;
        Enumeration<CustomTreeNode<T>> children = startNode.children();

        while (children.hasMoreElements() && !found) {
            found = selectItem(item, children.nextElement());
        }

        return found;
    }

}