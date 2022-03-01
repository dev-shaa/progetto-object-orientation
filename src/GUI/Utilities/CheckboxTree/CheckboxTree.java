package GUI.Utilities.CheckboxTree;

import java.util.ArrayList;
import javax.swing.JTree;
import javax.swing.tree.*;
import Utilities.Tree.CustomTreeModel;
import Utilities.Tree.CustomTreeNode;

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
        super();
        super.setCellRenderer(new CheckboxTreeRenderer());
        super.setSelectionModel(new CheckboxTreeSelectionModel());
        setModel(newModel);
    }

    /**
     * Imposta il modello dell'albero da mostrare.
     * 
     * @param newModel
     *            nuovo modello dell'albero
     */
    public void setModel(CustomTreeModel<T> newModel) {
        super.setModel(newModel);
        this.treeModel = newModel;
    }

    /**
     * Restituisce gli elementi selezionati dall'albero.
     * 
     * @return lista con gli elementi selezionati, {@code null} se non è selezionato niente
     */
    @SuppressWarnings("unchecked")
    public ArrayList<T> getSelectedItems() {
        TreePath[] selectedPaths = getSelectionModel().getSelectionPaths();

        if (selectedPaths == null)
            return null;

        ArrayList<T> selectedItems = new ArrayList<>(selectedPaths.length);

        for (TreePath path : selectedPaths)
            selectedItems.add(((CustomTreeNode<T>) path.getLastPathComponent()).getUserObject());

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

        if (node != null) {
            TreePath path = new TreePath(treeModel.getPathToRoot(node));
            getSelectionModel().addSelectionPath(path);
        }
    }

}