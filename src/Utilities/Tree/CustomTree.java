package Utilities.Tree;

import javax.swing.JTree;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 * Un'estensione di {@code JTree} per elementi di uno stesso tipo.
 */
public class CustomTree<T extends Object> extends JTree {

    private EventListenerList selectionListeners = new EventListenerList();
    private CustomTreeModel<T> model;

    /**
     * Crea un nuovo albero.
     * 
     * @param model
     *            modello da usare
     */
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

    /**
     * Imposta il modello da usare per l'albero.
     * 
     * @param model
     *            modello da usare
     */
    public void setModel(CustomTreeModel<T> model) {
        super.setModel(model);
        this.model = model;
    }

    @Override
    public CustomTreeModel<T> getModel() {
        return model;
    }

    /**
     * Espande tutte le righe dell'albero.
     */
    public void expandAllRows() {
        for (int i = 0; i < getRowCount(); i++)
            expandRow(i);
    }

    /**
     * Aggiunge un ascoltatore all'evento di selezione di un elemento.
     * 
     * @param listener
     *            ascoltatore da aggiungere
     */
    public void addTreeItemSelectionListener(CustomTreeItemSelectionListener<T> listener) {
        selectionListeners.add(CustomTreeItemSelectionListener.class, listener);
    }

    /**
     * Rimuove un ascoltatore dall'evento di deselezione di un elemento.
     * 
     * @param listener
     *            ascoltatore da rimuovere
     */
    public void removeTreeItemSelectionListener(CustomTreeItemSelectionListener<T> listener) {
        selectionListeners.remove(CustomTreeItemSelectionListener.class, listener);
    }

    /**
     * Restituisce l'elemento del nodo attualmente selezionato.
     * 
     * @return elemento selezionato ({@code null} se non è selezionato nulla)
     */
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