package GUI.Utilities.Tree;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * Un'estensione di {@code JTree} con nodi di tipo checkbox, con la possibilità di selezionare e recuperare gli elementi selezionati.
 */
public class CheckboxTree<T extends Object> extends JTree {

    private CustomTreeModel<T> treeModel;

    /**
     * Crea un nuovo {@code CheckboxTree} con il modello indicato.
     * 
     * @param newModel
     *            modello dell'albero da mostrare
     */
    public CheckboxTree(CustomTreeModel<T> newModel) {
        super(newModel);

        treeModel = newModel;

        super.setCellRenderer(new CheckboxTreeRenderer());
        super.setSelectionModel(new CheckboxTreeSelectionModel());
    }

    @Override
    public void setModel(TreeModel newModel) {

    }

    public void setModel(CustomTreeModel<T> newModel) {
        super.setModel(newModel);
        this.treeModel = newModel;
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
        if (treeModel == null)
            return;

        CustomTreeNode<T> node = treeModel.findNode(item);

        if (node == null)
            return;

        TreePath path = new TreePath(treeModel.getPathToRoot(node));
        getSelectionModel().addSelectionPath(path);
    }

}
