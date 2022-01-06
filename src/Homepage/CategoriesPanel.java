import java.awt.*;
import java.awt.event.*;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * Pannello con le categorie dell'utente sotto forma di albero, con ogni nodo che rappresenta una categoria.
 * 
 * @version 0.3
 * @author Salvatore Di Gennaro
 * @see Homepage
 * @see ReferencePanel
 */
public class CategoriesPanel extends JPanel {

    private ReferencePanel referencePanel;
    private CategoriesTree categoriesTree;

    private CategoryMutableTreeNode lastSelectedNode;
    private JTree displayTree;
    private JButton changeCategoryButton;
    private JButton removeCategoryButton;

    /**
     * Crea {@code CategoryPanel} con tutte le categorie associate dell'utente.
     * 
     * @param mainWindow
     * @param referencePanel
     * @since 0.3
     */
    public CategoriesPanel(User user, ReferencePanel referencePanel, CategoryDAO categoryDAO) throws Exception {
        this.referencePanel = referencePanel;

        try {
            this.categoriesTree = new CategoriesTree(categoryDAO, user);
            setLayout(new BorderLayout(5, 5));
            setBorder(new EmptyBorder(5, 5, 5, 5));

            add(getButtonsPanel(), BorderLayout.NORTH);
            add(getCategoriesTreePanel(), BorderLayout.CENTER);
        } catch (Exception e) {
            throw e;
        }
    }

    private JPanel getButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        JButton addCategoryButton = new JButton(new ImageIcon("images/folder_add.png"));
        addCategoryButton.setToolTipText("Nuova categoria");
        addCategoryButton.setBorderPainted(false);
        addCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tryAddCategory();
            }
        });

        changeCategoryButton = new JButton(new ImageIcon("images/folder_edit.png"));
        changeCategoryButton.setToolTipText("Modifica categoria");
        changeCategoryButton.setBorderPainted(false);
        changeCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tryChangeCategory();
            }
        });

        removeCategoryButton = new JButton(new ImageIcon("images/folder_delete.png"));
        removeCategoryButton.setToolTipText("Elimina categoria");
        removeCategoryButton.setBorderPainted(false);
        removeCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tryRemoveCategory();
            }
        });

        buttonsPanel.add(addCategoryButton);
        buttonsPanel.add(changeCategoryButton);
        buttonsPanel.add(removeCategoryButton);

        return buttonsPanel;
    }

    private JScrollPane getCategoriesTreePanel() {
        displayTree = new JTree(categoriesTree.getTree());

        displayTree.setEditable(false);
        displayTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        displayTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                lastSelectedNode = (CategoryMutableTreeNode) displayTree.getLastSelectedPathComponent();

                // il nodo root non esiste veramente nel database
                // modificarlo/eliminarlo non ha senso, quindi disabilita i pulsanti

                boolean nodeCanBeChanged = categoriesTree.canNodeBeChanged(lastSelectedNode);

                changeCategoryButton.setEnabled(nodeCanBeChanged);
                removeCategoryButton.setEnabled(nodeCanBeChanged);

                if (lastSelectedNode != null)
                    referencePanel.getDisplayedReferences().setReferences(lastSelectedNode.getCategory());
            }
        });

        // seleziona il nodo root
        displayTree.setSelectionRow(0);

        // espandi tutti i no di
        for (int i = 0; i < displayTree.getRowCount(); i++)
            displayTree.expandRow(i);

        return new JScrollPane(displayTree);
    }

    private void tryAddCategory() {
        try {
            String name = getCategoryNameFromUser("Nuova categoria");

            if (name != null) {
                DefaultMutableTreeNode newNode = categoriesTree.addCategoryNode(lastSelectedNode, name);
                displayTree.setSelectionPath(new TreePath(newNode.getPath()));
            }
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    private void tryChangeCategory() {
        try {
            String name = getCategoryNameFromUser(lastSelectedNode.getCategory().getName());

            if (name != null) {
                categoriesTree.changeCategoryNodeName(lastSelectedNode, name);
            }
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    private void tryRemoveCategory() {
        try {
            int confirmDialogBoxOption = JOptionPane.showConfirmDialog(null, "Sicuro di volere eliminare questa categoria?", "Elimina categoria", JOptionPane.YES_NO_OPTION);

            if (confirmDialogBoxOption == JOptionPane.YES_OPTION)
                categoriesTree.removeCategoryNode(lastSelectedNode);
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    private String getCategoryNameFromUser(String defaultString) {
        return (String) JOptionPane.showInputDialog(null, "Nome categoria:", "Nuova categoria", JOptionPane.PLAIN_MESSAGE, null, null, defaultString);
    }

}
