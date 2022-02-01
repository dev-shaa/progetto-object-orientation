package GUI.Utilities;

import java.util.Vector;
import java.util.Enumeration;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * Nodo di un albero che può cambiare e conservare un oggetto di un tipo specifico.
 */
public class CustomTreeNode<T extends Object> implements MutableTreeNode {

    private T userObject;
    private CustomTreeNode<T> parent;
    private Vector<CustomTreeNode<T>> children;
    private boolean allowsChildren;

    private String label;

    /**
     * Crea un nuovo nodo senza genitore, che ammette figli e con l'oggetto indicato.
     * 
     * @param userObject
     *            oggetto da conservare
     */
    public CustomTreeNode(T userObject) {
        this(userObject, null);
    }

    /**
     * Crea un nuovo nodo con un genitore, che ammette figli e con l'oggetto indicato.
     * 
     * @param userObject
     *            oggetto da conservare
     * @param parent
     *            genitore del nodo
     */
    public CustomTreeNode(T userObject, CustomTreeNode<T> parent) {
        this(userObject, parent, true);
    }

    /**
     * Crea un nuovo nodo con un genitore, che può ammettere figli e con l'oggetto indicato.
     * 
     * @param userObject
     *            oggetto da conservare
     * @param parent
     *            genitore del nodo
     * @param allowsChildren
     *            {@code true} se questo nodo ammette figli
     */
    public CustomTreeNode(T userObject, CustomTreeNode<T> parent, boolean allowsChildren) {
        setUserObject(userObject);
        setParent(parent);
        setAllowsChildren(allowsChildren);

        children = new Vector<>();
    }

    @Override
    public String toString() {
        return label == null ? String.valueOf(getUserObject()) : label;
    }

    @Override
    public CustomTreeNode<T> getChildAt(int childIndex) {
        if (children.isEmpty())
            throw new ArrayIndexOutOfBoundsException("node has no children");

        return children.elementAt(childIndex);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public CustomTreeNode<T> getParent() {
        return parent;
    }

    @Override
    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    /**
     * Imposta se questo nodo può avere figli.
     * 
     * @param allowsChildren
     *            {@code true} se accetta figli
     */
    public void setAllowsChildren(boolean allowsChildren) {
        this.allowsChildren = allowsChildren;
    }

    @Override
    public boolean getAllowsChildren() {
        return allowsChildren;
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public Enumeration<CustomTreeNode<T>> children() {
        return children.elements();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void insert(MutableTreeNode child, int index) {
        try {
            if (!allowsChildren)
                throw new IllegalStateException("node does not allow children");
            else if (child == null)
                throw new IllegalArgumentException("new child is null");
            else if (isNodeAncestor(child))
                throw new IllegalArgumentException("new child is an ancestor");

            CustomTreeNode<T> oldParent = (CustomTreeNode<T>) child.getParent();

            if (oldParent != null)
                oldParent.remove(child);

            child.setParent(this);

            if (children == null)
                children = new Vector<>();

            children.insertElementAt((CustomTreeNode<T>) child, index);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("child non è un nodo del tipo giusto");
        }
    }

    @Override
    public void remove(int index) {
        children.remove(index);
    }

    @Override
    public void remove(MutableTreeNode node) {
        children.remove(node);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setUserObject(Object object) {
        try {
            userObject = (T) object;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("object is not of the correct type");
        }
    }

    /**
     * Restituisce l'elemento del nodo.
     * 
     * @return elemento del nodo
     */
    public T getUserObject() {
        return userObject;
    }

    @Override
    public void removeFromParent() {
        setParent(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setParent(MutableTreeNode newParent) {
        try {
            parent = (CustomTreeNode<T>) newParent;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("newParent is not of the correct type");
        }
    }

    /**
     * Controlla se questo nodo è antenato di {@code anotherNode}.
     * 
     * @param anotherNode
     *            nodo di cui controllare se è antenato
     * @return {@code true} se questo nodo è un antenato
     */
    public boolean isNodeAncestor(TreeNode anotherNode) {
        if (anotherNode == null)
            return false;

        TreeNode ancestor = this;

        while (ancestor != null && !ancestor.equals(anotherNode))
            ancestor = ancestor.getParent();

        return ancestor != null;
    }

    /**
     * Imposta l'etichetta da mostrare.
     * 
     * @param label
     *            etichetta
     */
    public void setLabel(String label) {
        this.label = label;
    }

}
