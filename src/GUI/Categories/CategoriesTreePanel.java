package GUI.Categories;

import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import Entities.Category;
import GUI.Utilities.Tree.CustomTreeModel;
import GUI.Utilities.Tree.CustomTreeNode;

/**
 * Pannello che mostra l'albero delle categorie che l'utente può selezionare.
 */
public class CategoriesTreePanel extends JScrollPane {

    private JTree tree;
    private ArrayList<CategorySelectionListener> selectionListeners;

    /**
     * Crea un nuovo pannello vuoto.
     */
    public CategoriesTreePanel() {
        this(null);
    }

    /**
     * Crea un nuovo pannello con l'albero delle categorie indicato.
     * L'albero è non modificabile ed è possibile selezionare un solo nodo per volta.
     * 
     * @param categoriesTree
     *            albero delle categorie
     */
    public CategoriesTreePanel(CustomTreeModel<Category> categoriesTree) {
        super();
        setCategoriesTree(categoriesTree);
        setViewportView(tree);
    }

    /**
     * Imposta l'albero delle categorie dell'utente.
     * 
     * @param categoriesTree
     *            albero delle categorie
     */
    public void setCategoriesTree(CustomTreeModel<Category> categoriesTree) {
        if (tree == null) {
            tree = new JTree();

            tree.setEditable(false);
            tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            tree.addTreeSelectionListener(new TreeSelectionListener() {
                @Override
                public void valueChanged(TreeSelectionEvent e) {
                    if (selectionListeners == null)
                        return;

                    if (getSelectedNode() == null) {
                        for (CategorySelectionListener categorySelectionListener : selectionListeners)
                            categorySelectionListener.onCategoryClearSelection();
                    } else {
                        for (CategorySelectionListener categorySelectionListener : selectionListeners)
                            categorySelectionListener.onCategorySelection(getSelectedCategory());
                    }
                }
            });
        }

        tree.setModel(categoriesTree);

        for (int i = 0; i < tree.getRowCount(); i++)
            tree.expandRow(i);
    }

    /**
     * Deseleziona tutto
     */
    public void clearSelection() {
        tree.clearSelection();
    }

    /**
     * Restituisce l'ultima categoria selezionata.
     * 
     * @return ultima categoria selezionata
     */
    public Category getSelectedCategory() {
        CustomTreeNode<Category> selectedNode = getSelectedNode();

        return selectedNode == null ? null : selectedNode.getUserObject();
    }

    /**
     * Aggiunge un listener all'evento di selezione di un nodo.
     * Se {@code listener == null} o se è già registrato all'evento, non succede niente.
     * 
     * @param listener
     *            listener da aggiungere
     */
    public void addSelectionListener(CategorySelectionListener listener) {
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
    public void removeSelectionListener(CategorySelectionListener listener) {
        if (listener == null || selectionListeners == null)
            return;

        selectionListeners.remove(listener);
    }

    @SuppressWarnings("unchecked")
    private CustomTreeNode<Category> getSelectedNode() {
        return (CustomTreeNode<Category>) tree.getLastSelectedPathComponent();
    }

}
