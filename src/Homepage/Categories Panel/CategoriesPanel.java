import java.awt.*;
import java.awt.event.*;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * Pannello con le categorie dell'utente sotto forma di albero, con ogni nodo che rappresenta una categoria.
 * Sono presenti dei pulsanti per aggiungere, modificare o rimuovere una categoria.
 * 
 * @version 0.3
 * @author Salvatore Di Gennaro
 * @see Homepage
 * @see ReferencePanel
 */
public class CategoriesPanel extends JPanel {

    // TODO: commenta

    private CategoriesTree categoriesTree;

    private JTree displayTree;
    private CategoryMutableTreeNode selectedNode;

    private JButton addCategoryButton;
    private JButton changeCategoryButton;
    private JButton removeCategoryButton;

    /**
     * Crea {@code CategoryPanel} con tutte le categorie associate dell'utente.
     * 
     * @param mainWindow
     * @param referencePanel
     * @since 0.3
     */
    public CategoriesPanel(CategoriesTree categoriesTree, ReferencePanel referencePanel) throws IllegalArgumentException {
        if (categoriesTree == null || referencePanel == null)
            throw new IllegalArgumentException();

        setCategoriesTree(categoriesTree);

        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        add(getButtonsPanel(), BorderLayout.NORTH);

        add(getCategoriesTreePanel(referencePanel), BorderLayout.CENTER);
    }

    // FIXME:
    public void setCategoriesTree(CategoriesTree categoriesTree) {
        if (categoriesTree == null)
            throw new IllegalArgumentException("categoriesTree non può essere null");

        this.categoriesTree = categoriesTree;
    }

    private JPanel getButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        addCategoryButton = new JButton(new ImageIcon("images/folder_add.png"));
        addCategoryButton.setToolTipText("Nuova categoria");
        addCategoryButton.setBorderPainted(false);
        addCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCategory();
            }
        });

        changeCategoryButton = new JButton(new ImageIcon("images/folder_edit.png"));
        changeCategoryButton.setToolTipText("Modifica categoria");
        changeCategoryButton.setBorderPainted(false);
        changeCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeCategory();
            }
        });

        removeCategoryButton = new JButton(new ImageIcon("images/folder_delete.png"));
        removeCategoryButton.setToolTipText("Elimina categoria");
        removeCategoryButton.setBorderPainted(false);
        removeCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeCategory();
            }
        });

        buttonsPanel.add(addCategoryButton);
        buttonsPanel.add(changeCategoryButton);
        buttonsPanel.add(removeCategoryButton);

        return buttonsPanel;
    }

    private JScrollPane getCategoriesTreePanel(ReferencePanel referencePanel) {
        displayTree = new JTree(categoriesTree.getTreeModel());

        displayTree.setEditable(false);
        displayTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        displayTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                selectedNode = (CategoryMutableTreeNode) displayTree.getLastSelectedPathComponent();

                boolean nodeCanBeChanged = isSelectedNodeNotNull() && selectedNode.canBeChanged();
                changeCategoryButton.setEnabled(nodeCanBeChanged);
                removeCategoryButton.setEnabled(nodeCanBeChanged);

                if (isSelectedNodeNotNull())
                    referencePanel.showReferences(selectedNode.getUserObject());
            }
        });

        for (int i = 0; i < displayTree.getRowCount(); i++)
            displayTree.expandRow(i);

        displayTree.setSelectionRow(0);

        return new JScrollPane(displayTree);
    }

    private void addCategory() {
        try {
            String newCategoryName = getCategoryNameFromUser("Nuova categoria");

            if (newCategoryName != null) {
                categoriesTree.addNode(new Category(newCategoryName), selectedNode);
                selectCategory(categoriesTree.getLastAddedNode());
            }
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    private void changeCategory() {
        try {
            String name = getCategoryNameFromUser(selectedNode.getUserObject().getName());

            if (name != null) {
                categoriesTree.changeNode(selectedNode, name);
            }
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    private void removeCategory() {
        try {
            int confirmDialogBoxOption = JOptionPane.showConfirmDialog(null, "Sicuro di volere eliminare questa categoria?", "Elimina categoria", JOptionPane.YES_NO_OPTION);

            if (confirmDialogBoxOption == JOptionPane.YES_OPTION)
                categoriesTree.removeNode(selectedNode);
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    private void selectCategory(CategoryMutableTreeNode node) {
        if (node == null)
            displayTree.setSelectionPath(null);
        else
            displayTree.setSelectionPath(new TreePath(node.getPath()));
    }

    private String getCategoryNameFromUser(String defaultString) {
        return (String) JOptionPane.showInputDialog(null, "Nome categoria:", "Nuova categoria", JOptionPane.PLAIN_MESSAGE, null, null, defaultString);
    }

    /**
     * 
     * @return
     */
    public boolean isSelectedNodeNotNull() {
        return selectedNode != null;
    }

}
