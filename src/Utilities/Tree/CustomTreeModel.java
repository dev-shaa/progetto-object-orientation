package Utilities.Tree;

import java.util.HashMap;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * Modello di albero che estende {@code DefaultTreeModel} e che usa {@code CustomTreeNode} di un tipo specifico.
 */
public class CustomTreeModel<T extends Object> extends DefaultTreeModel {

    private HashMap<T, CustomTreeNode<T>> itemToNode;

    /**
     * Crea un nuovo albero con la radice indicata.
     * 
     * @param root
     *            nodo radice
     */
    public CustomTreeModel(CustomTreeNode<T> root) {
        super(root);

        itemToNode = new HashMap<>();
        itemToNode.put(root.getUserObject(), root);
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
    public void setRoot(TreeNode root) {
        super.setRoot((CustomTreeNode<T>) root);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void insertNodeInto(MutableTreeNode newChild, MutableTreeNode parent, int index) {
        CustomTreeNode<T> _child = (CustomTreeNode<T>) newChild;
        CustomTreeNode<T> _parent = (CustomTreeNode<T>) parent;
        super.insertNodeInto(_child, _parent, index);

        itemToNode.put(_child.getUserObject(), _child);
        reload(_parent);
    }

    /**
     * Aggiunge un nodo figlio e chiama tutti gli eventi necessari.
     * 
     * @param newChild
     *            nodo figlio da aggiungere
     * @param parent
     *            genitore a cui aggiungere il nodo
     */
    public void add(CustomTreeNode<T> newChild, CustomTreeNode<T> parent) {
        insertNodeInto(newChild, parent, parent.getChildCount());
    }

    /**
     * Rimuove un nodo.
     * 
     * @param node
     *            nodo da rimuovere
     */
    public void remove(CustomTreeNode<T> node) {
        removeNodeFromParent(node);
        itemToNode.remove(node.getUserObject());
    }

    /**
     * Trova il nodo a cui è associato l'elemento, se presente.
     * 
     * @param item
     *            elemento del nodo da cercare
     * @return nodo con l'elemento passato, {@code null} se non è presente
     */
    public CustomTreeNode<T> findNode(T item) {
        return itemToNode.get(item);
    }

}