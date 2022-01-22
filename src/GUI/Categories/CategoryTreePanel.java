package GUI.Categories;

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

    public CategoryTreePanel(CategoriesTreeManager categoriesTree) throws IllegalArgumentException {
        super();

        tree = new JTree();
        selectionListeners = new ArrayList<>();

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
    public void setCategoriesTree(CategoriesTreeManager categoriesTree) throws IllegalArgumentException {
        if (categoriesTree == null)
            throw new IllegalArgumentException("categoriesTree non può essere null");

        this.tree.setModel(categoriesTree.getTreeModel());
    }

    /**
     * Aggiunge un listener all'evento di selezione di un nodo.
     * 
     * @param listener
     *            listener da aggiungere
     */
    public void addSelectionListener(CategorySelectionListener listener) {
        if (listener != null)
            selectionListeners.add(listener);
    }

    /**
     * Rimuove un listener dall'evento di selezione di un nodo.
     * 
     * @param listener
     *            listener da rimuovere
     */
    public void removeSelectionListener(CategorySelectionListener listener) {
        if (listener != null)
            selectionListeners.remove(listener);
    }

    /**
     * Restituisce l'ultimo nodo selezionato.
     * 
     * @return
     *         nodo selezionato
     */
    public CategoryMutableTreeNode getSelectedNode() {
        return (CategoryMutableTreeNode) tree.getLastSelectedPathComponent();
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        if (getSelectedNode() != null) {
            for (CategorySelectionListener categorySelectionListener : selectionListeners) {
                categorySelectionListener.onCategorySelected(getSelectedNode().getUserObject());
            }
        }
    }

}
