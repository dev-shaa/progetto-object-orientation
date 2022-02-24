package GUI.Categories;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import Entities.Category;
import GUI.Utilities.Tree.CustomTreeModel;
import GUI.Utilities.Tree.CustomTreeNode;

/**
 * Pannello che mostra un albero delle categorie.
 */
public class CategoriesTreePanel extends JScrollPane {

    private JTree tree;
    private EventListenerList selectionListeners;

    /**
     * Crea un nuovo pannello vuoto.
     */
    public CategoriesTreePanel() {
        this(null);
    }

    /**
     * Crea un nuovo pannello con il modello di albero indicato.
     * L'albero è non modificabile ed è possibile selezionare un solo nodo per volta.
     * 
     * @param treeModel
     *            modello dell'albero da usare
     */
    public CategoriesTreePanel(CustomTreeModel<Category> treeModel) {
        super();

        selectionListeners = new EventListenerList();

        tree = new JTree();
        tree.setEditable(false);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                if (getSelectedCategory() == null)
                    fireCategoryDeselectionEvent();
                else
                    fireCategorySelectionEvent();
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
    public void setTreeModel(CustomTreeModel<Category> treeModel) {
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
     * Restituisce la categoria dell'ultimo nodo selezionato.
     * 
     * @return categoria dell'ultimo nodo selezionato, {@code null} se non è selezionato nulla
     */
    public Category getSelectedCategory() {
        CustomTreeNode<Category> selectedNode = getSelectedNode();
        return selectedNode == null ? null : selectedNode.getUserObject();
    }

    /**
     * Aggiunge un listener all'evento di selezione di una categoria.
     * 
     * @param listener
     *            listener da aggiungere
     */
    public void addSelectionListener(CategorySelectionListener listener) {
        selectionListeners.add(CategorySelectionListener.class, listener);
    }

    /**
     * Rimuove un listener dall'evento di selezione di una categoria.
     * 
     * @param listener
     *            listener da rimuovere
     */
    public void removeSelectionListener(CategorySelectionListener listener) {
        selectionListeners.remove(CategorySelectionListener.class, listener);
    }

    @SuppressWarnings("unchecked")
    private CustomTreeNode<Category> getSelectedNode() {
        return (CustomTreeNode<Category>) tree.getLastSelectedPathComponent();
    }

    private void fireCategorySelectionEvent() {
        Category selectedCategory = getSelectedCategory();
        CategorySelectionListener[] listeners = selectionListeners.getListeners(CategorySelectionListener.class);

        for (CategorySelectionListener listener : listeners)
            listener.onCategorySelection(selectedCategory);
    }

    private void fireCategoryDeselectionEvent() {
        CategorySelectionListener[] listeners = selectionListeners.getListeners(CategorySelectionListener.class);

        for (CategorySelectionListener listener : listeners)
            listener.onCategoryDeselection();
    }

}
