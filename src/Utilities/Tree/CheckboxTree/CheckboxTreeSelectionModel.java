package Utilities.Tree.CheckboxTree;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * Modello di selezione per un {@code CheckboxTree}.
 * <p>
 * Quando viene selezionato un nodo, tutti i nodi superiori fino al nodo root vengono selezionati.
 * <p>
 * Quando viene deselezionato un nodo, tutti i nodi discendenti vengono deselezionati.
 * <p>
 * I percorsi selezionati sono solo quelli delle foglie.
 */
public class CheckboxTreeSelectionModel extends DefaultTreeSelectionModel {

    private ArrayList<TreePath> selectedPaths;

    /**
     * Crea un nuovo modello di selezione.
     */
    public CheckboxTreeSelectionModel() {
        super();
        selectedPaths = new ArrayList<>();
    }

    @Override
    public void setSelectionPath(TreePath path) {
        if (isPathSelected(path))
            removeSelectionPath(path);
        else
            addSelectionPath(path);
    }

    @Override
    public void addSelectionPath(TreePath path) {
        if (path == null || isPathSelected(path))
            return;

        addAncestorPaths(path);
        selectedPaths.add(path);
    }

    @Override
    public void removeSelectionPath(TreePath path) {
        if (path == null || !isPathSelected(path))
            return;

        removeChildrenPaths(path);
        addSelectionPath(path.getParentPath());
    }

    @Override
    public void setSelectionPaths(TreePath[] pPaths) {
        // non fare niente
    }

    /**
     * Vengono restituiti i percorsi dei nodi foglia selezionati.
     */
    @Override
    public TreePath[] getSelectionPaths() {
        return selectedPaths.toArray(new TreePath[selectedPaths.size()]);
    }

    @Override
    public void clearSelection() {
        super.clearSelection();
        selectedPaths.clear();
    }

    private void addAncestorPaths(TreePath path) {
        // questa funzione seleziona tutti i percorsi dalla radice fino a path
        // ma non li aggiunge effettivamente alla lista di nodi selezionati
        // è solo per un aspetto visivo

        while (path != null) {
            selectedPaths.remove(path);
            super.addSelectionPath(path);
            path = path.getParentPath();
        }
    }

    private void removeChildrenPaths(TreePath path) {
        if (path == null)
            return;

        Enumeration<? extends TreeNode> children = ((MutableTreeNode) path.getLastPathComponent()).children();

        while (children.hasMoreElements())
            removeSelectionPath(path.pathByAddingChild(children.nextElement()));

        super.removeSelectionPath(path);
        selectedPaths.remove(path);
    }

}