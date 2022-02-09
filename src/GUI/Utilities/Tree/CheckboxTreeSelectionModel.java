package GUI.Utilities.Tree;

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

        addPathVisually(path);

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

    /**
     * Aggiunge un percorso ai selezionati, ma senza aggiungerlo alla lista dei percorsi che verranno restituiti.
     * 
     * @param path
     *            percorso da aggiungere
     */
    private void addPathVisually(TreePath path) {
        if (path == null || isPathSelected(path))
            return;

        // è stata selezionato il percorso di un nodo figlio,
        // quindi dobbiamo rimuovere questo percorso da quelli effettivamente selezionati
        selectedPaths.remove(path);

        addPathVisually(path.getParentPath());

        super.addSelectionPath(path);
    }

    private void removeChildrenPaths(TreePath path) {
        if (path == null)
            return;

        Enumeration<? extends TreeNode> children = ((MutableTreeNode) path.getLastPathComponent()).children();

        while (children.hasMoreElements()) {
            removeSelectionPath(path.pathByAddingChild(children.nextElement()));
        }

        super.removeSelectionPath(path);
        selectedPaths.remove(path);
    }

}
