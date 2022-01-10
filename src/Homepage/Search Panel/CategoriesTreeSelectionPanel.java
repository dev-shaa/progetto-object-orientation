import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultTreeSelectionModel;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

/**
 * Un pannello che consente di selezionare più categorie.
 */
public class CategoriesTreeSelectionPanel extends JPanel {

    private JLabel label;
    private JButton categoriesDropdownButton;
    private JTree categoriesSelectionTree;

    /**
     * 
     * @param categoriesTree
     *            l'albero delle categorie
     * @throws IllegalArgumentException
     *             se {@code categoriesTree == null}
     */
    public CategoriesTreeSelectionPanel(CategoriesTree categoriesTree) throws IllegalArgumentException {
        if (categoriesTree == null)
            throw new IllegalArgumentException();

        setLayout(new GridLayout(0, 1));

        label = new JLabel("Categorie:");

        categoriesSelectionTree = getSelectionTree(categoriesTree);
        categoriesSelectionTree.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        categoriesSelectionTree.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                Category[] selectedCategories = getSelectedCategories();
                String textToDisplayOnButton = selectedCategories == null ? "Nessuna categoria selezionata" : Arrays.toString(selectedCategories);
                categoriesDropdownButton.setText(textToDisplayOnButton);
            }

        });

        JPopupMenu categoriesSelectionPopup = new JPopupMenu();
        categoriesSelectionPopup.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        categoriesSelectionPopup.add(categoriesSelectionTree);

        categoriesDropdownButton = new JButton("Nessuna categoria selezionata");
        categoriesDropdownButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < categoriesSelectionTree.getRowCount(); i++)
                    categoriesSelectionTree.expandRow(i);

                categoriesSelectionPopup.show(categoriesDropdownButton, 0, categoriesDropdownButton.getHeight());
            }
        });

        add(label);
        add(categoriesDropdownButton);
    }

    /**
     * Restituisce un albero
     * 
     * @param categoriesTree
     * @return
     */
    private JTree getSelectionTree(CategoriesTree categoriesTree) {
        JTree tree = new JTree(categoriesTree.getTreeModel());

        tree.setToggleClickCount(0);
        tree.setRootVisible(false);
        tree.setEditable(false);

        tree.setSelectionModel(new DefaultTreeSelectionModel() {
            @Override
            public void setSelectionPath(TreePath path) {
                addSelectionPath(path);
            }

            @Override
            public void addSelectionPath(TreePath path) {
                if (isPathSelected(path))
                    removeSelectionPath(path);
                else
                    super.addSelectionPath(path);
            }

            @Override
            public void removeSelectionPath(TreePath path) {
                super.removeSelectionPath(path);
            }

            @Override
            public void setSelectionPaths(TreePath[] pPaths) {
                super.setSelectionPaths(pPaths);
            }
        });

        return tree;
    }

    /**
     * Restituisce le categorie selezionate dall'utente.
     * 
     * @return un array di {@code Categories} con tutte le categorie selezionate,
     *         {@code null} se non è stato selezionato niente
     * @see Category
     */
    public Category[] getSelectedCategories() {
        TreePath[] selectedPaths = categoriesSelectionTree.getSelectionPaths();

        if (selectedPaths == null)
            return null;

        Category[] selectedCategories = new Category[selectedPaths.length];

        for (int i = 0; i < selectedPaths.length; i++)
            selectedCategories[i] = (Category) ((DefaultMutableTreeNode) selectedPaths[i].getLastPathComponent()).getUserObject();

        return selectedCategories;
    }

}
