package GUI.Utilities;

import java.util.Vector;
import java.util.Enumeration;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class CustomTreeNode<T extends Object> implements MutableTreeNode {

    private T userObject;
    private CustomTreeNode<T> parent;
    private Vector<CustomTreeNode<T>> children;
    private boolean allowsChildren;

    private String label;

    public CustomTreeNode(T userObject) {
        this(userObject, null);
    }

    public CustomTreeNode(T userObject, CustomTreeNode<T> parent) {
        this(userObject, parent, true);
    }

    public CustomTreeNode(T userObject, CustomTreeNode<T> parent, boolean allowsChildren) {
        setUserObject(userObject);
        setParent(parent);
        setAllowsChildren(allowsChildren);

        children = new Vector<>();
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
            if (!allowsChildren) {
                throw new IllegalStateException("node does not allow children");
            } else if (child == null) {
                throw new IllegalArgumentException("new child is null");
            } else if (isNodeAncestor(child)) {
                throw new IllegalArgumentException("new child is an ancestor");
            }

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

    public boolean isNodeAncestor(TreeNode anotherNode) {
        if (anotherNode == null)
            return false;

        TreeNode ancestor = this;

        while (ancestor != null && !ancestor.equals(anotherNode))
            ancestor = ancestor.getParent();

        return ancestor != null;
    }

    @Override
    public String toString() {
        return label == null ? String.valueOf(getUserObject()) : label;
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
