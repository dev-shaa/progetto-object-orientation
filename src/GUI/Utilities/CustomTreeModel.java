package GUI.Utilities;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * Modello di albero che estende {@code DefaultTreeModel} e che usa {@code CustomTreeNode} di un tipo specifico.
 */
public class CustomTreeModel<T extends Object> extends DefaultTreeModel {

    /**
     * Crea un nuovo albero con la radice indicata.
     * 
     * @param root
     *            nodo radice
     */
    public CustomTreeModel(CustomTreeNode<T> root) {
        super(root);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CustomTreeNode<T> getChild(Object parent, int index) {
        return (CustomTreeNode<T>) super.getChild(parent, index);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CustomTreeNode<T> getRoot() {
        return (CustomTreeNode<T>) super.getRoot();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setRoot(TreeNode root) throws IllegalArgumentException {
        try {
            super.setRoot((CustomTreeNode<T>) root);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("root is not a valid type of node for this tree");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void insertNodeInto(MutableTreeNode newChild, MutableTreeNode parent, int index) throws IllegalArgumentException {
        try {
            super.insertNodeInto((CustomTreeNode<T>) newChild, (CustomTreeNode<T>) parent, index);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("newChild or parent are not a valid type of node for this tree");
        }
    }

    /**
     * Aggiunge un nodo figlio e chiama tutti gli eventi necessari.
     * 
     * @param newChild
     *            nodo figlio da aggiungere
     * @param parent
     *            genitore a cui aggiungere il nodo
     */
    public void addNode(CustomTreeNode<T> newChild, CustomTreeNode<T> parent) {
        insertNodeInto(newChild, parent, parent.getChildCount());
    }

}
