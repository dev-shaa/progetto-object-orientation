package GUI.Utilities.Tree;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class CustomCheckboxTree<T extends Object> extends JTree {

    private CustomTreeModel<T> treeModel;

    /**
     * TODO: commenta
     * 
     * @param newModel
     */
    public CustomCheckboxTree(CustomTreeModel<T> newModel) {
        super(newModel);

        treeModel = newModel;

        super.setCellRenderer(new CustomCheckboxTreeRenderer());
        super.setSelectionModel(new CheckboxTreeSelectionModel());
    }

    @Override
    public void setSelectionModel(TreeSelectionModel selectionModel) {
        // non fare niente
    }

    /**
     * Restituisce gli elementi selezionati dall'albero.
     * 
     * @return lista con gli elementi selezionati, {@code null} se non è selezionato niente
     */
    @SuppressWarnings("unchecked")
    public List<T> getSelectedItems() {
        TreePath[] selectedPaths = getSelectionModel().getSelectionPaths();

        if (selectedPaths == null)
            return null;

        ArrayList<T> selectedItems = new ArrayList<>(selectedPaths.length);

        for (TreePath path : selectedPaths) {
            selectedItems.add(((CustomTreeNode<T>) path.getLastPathComponent()).getUserObject());
        }

        return selectedItems;
    }

    /**
     * Seleziona l'elemento indicato, se presente nell'albero.
     * 
     * @param item
     *            elemento da selezionare
     */
    public void selectItem(T item) {
        CustomTreeNode<T> node = treeModel.findNode(item);

        if (node == null)
            return;

        TreePath path = new TreePath(treeModel.getPathToRoot(node));
        getSelectionModel().addSelectionPath(path);
    }

}
