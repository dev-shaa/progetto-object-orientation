package GUI.Utilities.CheckboxTree;

import java.awt.Dimension;
import GUI.Utilities.PopupButton;
import Utilities.Tree.CustomTreeModel;

/**
 * Un {@code PopupButton} al cui interno è presente un {@code CheckBoxTree}.
 */
public class PopupCheckboxTree<T extends Object> extends PopupButton {

    private CheckboxTree<T> checkboxTree;

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

        checkboxTree = new CheckboxTree<T>(treeModel);
        checkboxTree.setToggleClickCount(0);
        checkboxTree.setEditable(false);
        checkboxTree.setRootVisible(false);
        checkboxTree.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        add(checkboxTree);
    }

    /**
     * Imposta l'albero da mostrare.
     * 
     * @param treeModel
     *            modello dell'albero da mostrare
     */
    public void setTreeModel(CustomTreeModel<T> treeModel) {
        checkboxTree.setModel(treeModel);
    }

    /**
     * Restituisce il {@code CheckboxTree} utilizzato.
     * 
     * @return albero di checkbox usato
     */
    public CheckboxTree<T> getCheckboxTree() {
        return checkboxTree;
    }

    @Override
    protected void beforePopupOpen() {
        for (int i = 0; i < checkboxTree.getRowCount(); i++)
            checkboxTree.expandRow(i);

        super.beforePopupOpen();
    }

}