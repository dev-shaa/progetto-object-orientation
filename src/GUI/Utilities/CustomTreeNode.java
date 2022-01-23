package GUI.Utilities;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class CustomTreeNode<T extends Object> implements MutableTreeNode {

    private T userObject;
    private CustomTreeNode<T> parent;
    private Vector<CustomTreeNode<T>> children;
    private boolean allowsChildren;

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
    }

    @Override
    public CustomTreeNode<T> getChildAt(int childIndex) {
        if (children == null)
            throw new ArrayIndexOutOfBoundsException("node has no children");

        return children.elementAt(childIndex);
    }

    @Override
    public int getChildCount() {
        if (children == null)
            return 0;

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
        return children == null || children.isEmpty();
    }

    @Override
    public Enumeration<CustomTreeNode<T>> children() {
        return children.elements();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void insert(MutableTreeNode child, int index) {
        try {
            if (child == null)
                throw new IllegalArgumentException("child can't be null");
            else if (!getAllowsChildren())
                throw new IllegalArgumentException("this node doesn't allow childen");
            else if (isNodeAncestor(child))
                throw new IllegalStateException("new child is an ancestor of this node");

            if (children == null)
                children = new Vector<>();

            children.insertElementAt((CustomTreeNode<T>) child, index);

        } catch (ClassCastException e) {
            throw new IllegalArgumentException("child non è un nodo del tipo giusto");
        }
    }

    @Override
    public void remove(int index) {
        if (children != null)
            children.remove(index);
    }

    @Override
    public void remove(MutableTreeNode node) {
        if (children != null)
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

        if (anotherNode == this)
            return true;

        TreeNode parent = getParent();

        while (parent != null && parent.equals(anotherNode)) {
            parent = getParent();
        }

        return parent != null;
    }

}
