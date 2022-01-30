package GUI.Utilities;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class CustomTreeModel<T extends Object> extends DefaultTreeModel {

    public CustomTreeModel(CustomTreeNode<T> root) {
        super(root);
    }

    public CustomTreeModel(CustomTreeNode<T> root, boolean asksAllowsChildren) {
        super(root, asksAllowsChildren);
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

    public void addNode(CustomTreeNode<T> newChild, CustomTreeNode<T> parent) {
        insertNodeInto(newChild, parent, parent.getChildCount());
    }

}
