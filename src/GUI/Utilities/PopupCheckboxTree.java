package GUI.Utilities;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.tree.TreePath;
import com.jidesoft.swing.CheckBoxTree;

/**
 * Un {@code PopupButton} al cui interno è presente un {@code CheckBoxTree}.
 */
public class PopupCheckboxTree<T> extends PopupButton {

    private CheckBoxTree checkboxTree;
    private CustomTreeModel<T> categoriesTree;

    /**
     * Crea un nuovo {@code PopupCheckboxTree}.
     */
    public PopupCheckboxTree() {
        this(null);
    }

    /**
     * Crea un nuovo {@code PopupCheckboxTree} con l'albero indicato.
     * 
     * @param treeModel
     *            albero da mostrare
     */
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

    /**
     * Imposta l'albero da mostrare
     * 
     * @param treeModel
     *            modello dell'albero da mostrare
     */
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

    /**
     * Restituisce gli elementi selezionati dall'albero.
     * 
     * @return lista con gli elementi selezionati, {@code null} se non è selezionato niente
     */
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

    /**
     * Seleziona l'elemento indicato, se presente nell'albero.
     * 
     * @param item
     *            elemento da selezionare
     * @throws IllegalArgumentException
     *             se {@code item == null}
     */
    public void selectItem(T item) {
        CustomTreeNode<T> node = categoriesTree.findNode(item);

        if (node == null)
            return;

        checkboxTree.getCheckBoxTreeSelectionModel().addSelectionPath(new TreePath(categoriesTree.getPathToRoot(node)));
    }

}