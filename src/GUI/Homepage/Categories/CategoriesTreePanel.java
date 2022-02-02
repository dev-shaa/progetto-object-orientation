package GUI.Homepage.Categories;

import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import Entities.Category;
import GUI.Utilities.CustomTreeModel;
import GUI.Utilities.CustomTreeNode;

/**
 * Pannello che mostra l'albero delle categorie che l'utente può selezionare.
 */
public class CategoriesTreePanel extends JScrollPane {

    private JTree tree;
    private ArrayList<CategorySelectionListener> selectionListeners;

    /**
     * Crea un nuovo pannello con l'albero delle categorie indicato.
     * L'albero è non modificabile ed è possibile selezionare un solo nodo per volta.
     * 
     * @param categoriesTree
     *            albero delle categorie
     * @throws IllegalArgumentException
     *             se {@code categoriesTree} è nullo
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
     * @throws IllegalArgumentException
     *             se {@code categoriesTree == null}
     */
    public void setCategoriesTree(CustomTreeModel<Category> categoriesTree) {
        if (categoriesTree == null)
            throw new IllegalArgumentException("categoriesTree non può essere null");

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
                            categorySelectionListener.onCategorySelection(getSelectedNode().getUserObject());
                    }
                }
            });
        }

        tree.setModel(categoriesTree);

        for (int i = 0; i < tree.getRowCount(); i++)
            tree.expandRow(i);
    }

    /**
     * Restituisce l'ultimo nodo selezionato.
     * 
     * @return
     *         nodo selezionato
     */
    @SuppressWarnings("unchecked")
    public CustomTreeNode<Category> getSelectedNode() {
        return (CustomTreeNode<Category>) tree.getLastSelectedPathComponent();
    }

    /**
     * Deseleziona tutto
     */
    public void clearSelection() {
        tree.clearSelection();
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
            selectionListeners = new ArrayList<>(2);

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

}
