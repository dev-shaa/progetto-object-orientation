package GUI.Utilities;

import java.awt.Dimension;

import GUI.Utilities.Tree.CustomCheckboxTree;
import GUI.Utilities.Tree.CustomTreeModel;

/**
 * Un {@code PopupButton} al cui interno è presente un {@code CheckBoxTree}.
 */
public class PopupCheckboxTree<T extends Object> extends PopupButton {

    private CustomCheckboxTree<T> checkboxTree;

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
        if (checkboxTree == null) {
            checkboxTree = new CustomCheckboxTree<T>(treeModel);

            checkboxTree.setToggleClickCount(0);
            checkboxTree.setEditable(false);
            checkboxTree.setRootVisible(false);
            checkboxTree.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        } else {
            checkboxTree.setModel(treeModel);
        }
    }

    public CustomCheckboxTree<T> getCheckboxTree() {
        return checkboxTree;
    }

}