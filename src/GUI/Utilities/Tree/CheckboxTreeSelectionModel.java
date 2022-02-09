package GUI.Utilities.Tree;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * Modello di selezione per un {@code CheckboxTree}.
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
        addAncestorPath(path.getParentPath());
        super.addSelectionPath(path);
        selectedPaths.add(path);
    }

    @Override
    public void removeSelectionPath(TreePath path) {
        Enumeration<? extends TreeNode> children = ((MutableTreeNode) path.getLastPathComponent()).children();

        while (children.hasMoreElements()) {
            removeSelectionPath(path.pathByAddingChild(children.nextElement()));
        }

        super.removeSelectionPath(path);
        selectedPaths.remove(path);
    }

    @Override
    public void setSelectionPaths(TreePath[] pPaths) {
        // non fare niente
    }

    @Override
    public TreePath[] getSelectionPaths() {
        return selectedPaths.toArray(new TreePath[selectedPaths.size()]);
    }

    @Override
    public void clearSelection() {
        super.clearSelection();
        selectedPaths.clear();
    }

    private void addAncestorPath(TreePath path) {
        if (path == null)
            return;

        selectedPaths.remove(path);

        addAncestorPath(path.getParentPath());

        super.addSelectionPath(path);
    }

}
