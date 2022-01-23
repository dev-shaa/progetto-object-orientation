package GUI.Homepage.Categories;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import java.util.ArrayList;

/**
 * Pannello che mostra l'albero delle categorie che l'utente può selezionare.
 */
public class CategoryTreePanel extends JScrollPane implements TreeSelectionListener {

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
    public CategoryTreePanel(CategoryTreeModel categoriesTree) {
        super();

        tree = new JTree();

        setCategoriesTree(categoriesTree);

        tree.setEditable(false);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(this);

        for (int i = 0; i < tree.getRowCount(); i++)
            tree.expandRow(i);

        tree.setSelectionRow(0);

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
    public void setCategoriesTree(CategoryTreeModel categoriesTree) {
        if (categoriesTree == null)
            throw new IllegalArgumentException("categoriesTree non può essere null");

        tree.setModel(categoriesTree);
    }

    /**
     * Aggiunge un listener all'evento di selezione di un nodo.
     * 
     * @param listener
     *            listener da aggiungere
     */
    public void addSelectionListener(CategorySelectionListener listener) {
        if (listener == null)
            return;

        if (selectionListeners == null)
            selectionListeners = new ArrayList<>(5);

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

    /**
     * Restituisce l'ultimo nodo selezionato.
     * 
     * @return
     *         nodo selezionato
     */
    public CategoryTreeNode getSelectedNode() {
        return (CategoryTreeNode) tree.getLastSelectedPathComponent();
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        if (getSelectedNode() == null || selectionListeners == null)
            return;

        for (CategorySelectionListener categorySelectionListener : selectionListeners) {
            categorySelectionListener.onCategorySelected(getSelectedNode().getUserObject());
        }
    }

}
