import java.awt.*;
import java.awt.event.*;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.tree.DefaultMutableTreeNode;
// import javax.swing.tree.DefaultTreeModel;
// import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * Classe che si occupa di mostrare un pannello con le categorie dell'utente
 * sotto forma di albero, con ogni nodo che rappresenta una categoria.
 * 
 * @version 0.2
 * @author Salvatore Di Gennaro
 * @see MainWindowFrame
 * @see ReferencePanel
 */
public class CategoriesPanel extends JPanel {

    private MainWindowFrame mainWindow;
    private ReferencePanel referencePanel;
    private CategoriesTree categoriesManager;

    private DefaultMutableTreeNode lastSelectedNode;
    private JTree categoriesTree;
    private JButton editCategoryButton;
    private JButton removeCategoryButton;

    /**
     * Crea {@code CategoryPanel} con tutte le categorie associate dell'utente.
     * 
     * @param user
     * @since 0.1
     */
    public CategoriesPanel(MainWindowFrame mainWindow, ReferencePanel referencePanel) {
        this.mainWindow = mainWindow;
        this.referencePanel = referencePanel;

        try {
            this.categoriesManager = new CategoriesTree(mainWindow.getUser());
        } catch (Exception e) {
            // TODO: handle exception
        }

        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        add(getButtonsPanel(), BorderLayout.NORTH);
        add(getCategoriesTreePanel(), BorderLayout.CENTER);
    }

    private JPanel getButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        JButton addCategoryButton = new JButton(new ImageIcon("images/folder_add.png"));
        addCategoryButton.setToolTipText("Nuova categoria");
        addCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = getCategoryNameFromUser("Nuova categoria");

                    if (name != null) {
                        DefaultMutableTreeNode newNode = categoriesManager.addCategoryNode(lastSelectedNode, name);
                        selectTreeNode(newNode);
                    }
                } catch (IllegalArgumentException exception) {
                    exception.printStackTrace();
                    JOptionPane.showMessageDialog(null, exception.getMessage());
                } catch (Exception exception) {
                    exception.printStackTrace();
                    JOptionPane.showMessageDialog(null, exception.getMessage());
                }
            }
        });

        editCategoryButton = new JButton(new ImageIcon("images/folder_edit.png"));
        editCategoryButton.setToolTipText("Modifica categoria");
        editCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = getCategoryNameFromUser(categoriesManager.getCategoryFromNode(lastSelectedNode).getName());

                    if (name != null) {
                        categoriesManager.changeCategoryNode(lastSelectedNode, name);
                    }
                } catch (IllegalArgumentException exception) {
                    exception.printStackTrace();
                    JOptionPane.showMessageDialog(null, exception.getMessage());
                } catch (Exception exception) {
                    exception.printStackTrace();
                    JOptionPane.showMessageDialog(null, exception.getMessage());
                }
            }
        });

        removeCategoryButton = new JButton(new ImageIcon("images/folder_delete.png"));
        removeCategoryButton.setToolTipText("Elimina categoria");
        removeCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    categoriesManager.removeCategoryNode(lastSelectedNode);
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage());
                }
            }
        });

        buttonsPanel.add(addCategoryButton);
        buttonsPanel.add(editCategoryButton);
        buttonsPanel.add(removeCategoryButton);

        return buttonsPanel;
    }

    private JScrollPane getCategoriesTreePanel() {
        return new JScrollPane(getCategoriesTree());
    }

    private JTree getCategoriesTree() {
        try {
            categoriesTree = new JTree(categoriesManager.getTree());

            categoriesTree.setEditable(false);
            categoriesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            categoriesTree.addTreeSelectionListener(new TreeSelectionListener() {
                @Override
                public void valueChanged(TreeSelectionEvent e) {
                    lastSelectedNode = (DefaultMutableTreeNode) categoriesTree.getLastSelectedPathComponent();

                    // il nodo root non esiste veramente nel database
                    // modificarlo/eliminarlo non ha senso, quindi disabilita i pulsanti
                    editCategoryButton.setEnabled(canNodeBeChanged(lastSelectedNode));
                    removeCategoryButton.setEnabled(canNodeBeChanged(lastSelectedNode));

                    if (lastSelectedNode != null)
                        referencePanel.setReferences(categoriesManager.getCategoryFromNode(lastSelectedNode));
                }
            });

            // seleziona il nodo root
            categoriesTree.setSelectionRow(0);

            // espandi tutti i no di
            for (int i = 0; i < categoriesTree.getRowCount(); i++)
                categoriesTree.expandRow(i);

            return categoriesTree;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile caricare le categorie");
            return null;
        }
    }

    private void selectTreeNode(DefaultMutableTreeNode node) {
        categoriesTree.setSelectionPath(new TreePath(node.getPath()));
    }

    private boolean canNodeBeChanged(DefaultMutableTreeNode node) {
        return node != null && !node.isRoot();
    }

    private String getCategoryNameFromUser(String defaultString) {
        return (String) JOptionPane.showInputDialog(null, "Nome categoria:", "Nuova categoria", JOptionPane.PLAIN_MESSAGE, null, null, defaultString);
    }

}
