package Utilities.Tree;

import javax.swing.JTree;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class CustomTree<T extends Object> extends JTree {

    private EventListenerList selectionListeners = new EventListenerList();
    private CustomTreeModel<T> model;

    public CustomTree(CustomTreeModel<T> model) {
        super(model);
        this.model = model;

        addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                if (getSelectedNode() == null)
                    fireItemDeselectionEvent();
                else
                    fireItemSelectionEvent(getSelectedItem());
            }
        });
    }

    public void setModel(CustomTreeModel<T> model) {
        super.setModel(model);
        this.model = model;
    }

    @Override
    public CustomTreeModel<T> getModel() {
        return model;
    }

    public void expandAllRows() {
        for (int i = 0; i < getRowCount(); i++)
            expandRow(i);
    }

    public void addItemSelectionListener(CustomTreeItemSelectionListener<T> listener) {
        selectionListeners.add(CustomTreeItemSelectionListener.class, listener);
    }

    public void removeItemSelectionListener(CustomTreeItemSelectionListener<T> listener) {
        selectionListeners.remove(CustomTreeItemSelectionListener.class, listener);
    }

    public T getSelectedItem() {
        CustomTreeNode<T> selectedNode = getSelectedNode();
        return selectedNode == null ? null : selectedNode.getUserObject();
    }

    @SuppressWarnings("unchecked")
    private CustomTreeNode<T> getSelectedNode() {
        return (CustomTreeNode<T>) getLastSelectedPathComponent();
    }

    private void fireItemSelectionEvent(T selectedItem) {
        for (CustomTreeItemSelectionListener<T> listener : getItemSelectionListeners())
            listener.onTreeItemSelection(selectedItem);
    }

    private void fireItemDeselectionEvent() {
        for (CustomTreeItemSelectionListener<T> listener : getItemSelectionListeners())
            listener.onTreeItemDeselection();
    }

    @SuppressWarnings("unchecked")
    private CustomTreeItemSelectionListener<T>[] getItemSelectionListeners() {
        return (CustomTreeItemSelectionListener<T>[]) selectionListeners.getListeners(CustomTreeItemSelectionListener.class);
    }

}
