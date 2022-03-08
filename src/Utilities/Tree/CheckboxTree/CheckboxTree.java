package Utilities.Tree.CheckboxTree;

import java.util.ArrayList;
import javax.swing.tree.*;

import Utilities.Tree.CustomTree;
import Utilities.Tree.CustomTreeModel;
import Utilities.Tree.CustomTreeNode;

/**
 * Un'estensione di {@code JTree} con nodi di tipo checkbox, con la possibilità di selezionare e recuperare gli elementi selezionati.
 */
public class CheckboxTree<T extends Object> extends CustomTree<T> {

    /**
     * Crea un nuovo {@code CheckboxTree} senza un modello.
     */
    public CheckboxTree() {
        this(null);
    }

    /**
     * Crea un nuovo {@code CheckboxTree} con il modello indicato.
     * 
     * @param newModel
     *            modello dell'albero da mostrare
     */
    public CheckboxTree(CustomTreeModel<T> newModel) {
        super(newModel);
        super.setCellRenderer(new CheckboxTreeRenderer());
        super.setSelectionModel(new CheckboxTreeSelectionModel());
        setModel(newModel);
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
        if (getModel() == null)
            return;

        CustomTreeNode<T> node = getModel().findNode(item);

        if (node != null) {
            TreePath path = new TreePath(getModel().getPathToRoot(node));
            getSelectionModel().addSelectionPath(path);
        }
    }

}