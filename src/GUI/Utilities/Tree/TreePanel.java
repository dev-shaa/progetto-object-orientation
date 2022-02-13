package GUI.Utilities.Tree;

import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

/**
 * Pannello che mostra un albero.
 */
public class TreePanel<T extends Object> extends JScrollPane {

    private JTree tree;
    private ArrayList<TreePanelSelectionListener<T>> selectionListeners;

    /**
     * Crea un nuovo pannello vuoto.
     */
    public TreePanel() {
        this(null);
    }

    /**
     * Crea un nuovo pannello con il modello di albero indicato.
     * L'albero è non modificabile ed è possibile selezionare un solo nodo per volta.
     * 
     * @param treeModel
     *            modello dell'albero da usare
     */
    public TreePanel(CustomTreeModel<T> treeModel) {
        super();

        tree = new JTree();
        tree.setEditable(false);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                notifyListener();
            }
        });

        setTreeModel(treeModel);
        setViewportView(tree);
    }

    /**
     * Imposta il modello dell'albero.
     * 
     * @param treeModel
     *            modello dell'albero da usare
     */
    public void setTreeModel(CustomTreeModel<T> treeModel) {
        tree.setModel(treeModel);

        for (int i = 0; i < tree.getRowCount(); i++)
            tree.expandRow(i);

        clearSelection();
    }

    /**
     * Deseleziona tutto.
     */
    public void clearSelection() {
        tree.clearSelection();
    }

    /**
     * Restituisce l'oggetto dell'ultimo nodo selezionato.
     * 
     * @return oggetto dell'ultimo nodo selezionato, {@code null} se non è selezionato nulla
     */
    public T getSelectedObject() {
        CustomTreeNode<T> selectedNode = getSelectedNode();

        return selectedNode == null ? null : selectedNode.getUserObject();
    }

    /**
     * Aggiunge un listener all'evento di selezione di un nodo.
     * Se {@code listener == null} o se è già registrato all'evento, non succede niente.
     * 
     * @param listener
     *            listener da aggiungere
     */
    public void addSelectionListener(TreePanelSelectionListener<T> listener) {
        if (listener == null)
            return;

        if (selectionListeners == null)
            selectionListeners = new ArrayList<>();

        if (selectionListeners.contains(listener))
            return;

        selectionListeners.add(listener);
    }

    /**
     * Rimuove un listener dall'evento di selezione di un nodo.
     * 
     * @param listener
     *            listener da rimuovere
     */
    public void removeSelectionListener(TreePanelSelectionListener<T> listener) {
        if (listener == null || selectionListeners == null)
            return;

        selectionListeners.remove(listener);
    }

    @SuppressWarnings("unchecked")
    private CustomTreeNode<T> getSelectedNode() {
        return (CustomTreeNode<T>) tree.getLastSelectedPathComponent();
    }

    private void notifyListener() {
        if (selectionListeners == null)
            return;

        if (getSelectedNode() == null) {
            for (TreePanelSelectionListener<T> categorySelectionListener : selectionListeners)
                categorySelectionListener.onTreePanelClearSelection();
        } else {
            for (TreePanelSelectionListener<T> categorySelectionListener : selectionListeners)
                categorySelectionListener.onTreePanelSelection(getSelectedObject());
        }
    }

}
