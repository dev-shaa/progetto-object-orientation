import java.awt.*;
import java.awt.event.*;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * Classe che si occupa di mostrare un pannello con le categorie dell'utente
 * sotto forma di albero, con ogni nodo che rappresenta una categoria.
 * 
 * @version 0.2
 * @author Salvatore Di Gennaro
 * @see MainWindow
 * @see ReferencePanel
 */
public class CategoryPanel extends JPanel {

    private MainWindow mainWindow;
    private ReferencePanel referencePanel;

    private DefaultTreeModel categoriesTreeModel;
    private DefaultMutableTreeNode categoriesTreeRoot;
    private DefaultMutableTreeNode lastSelectedTreeNode;
    private JTree categoriesTree;
    private JButton editCategoryButton;
    private JButton removeCategoryButton;

    /**
     * Crea {@code CategoryPanel} con tutte le categorie associate dell'utente.
     * 
     * @param user
     * @since 0.1
     */
    public CategoryPanel(MainWindow mainWindow, ReferencePanel referencePanel) {
        this.mainWindow = mainWindow;
        this.referencePanel = referencePanel;

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
                mainWindow.createCategoryFromUserInput(getLastSelectedCategory());

            }
        });

        editCategoryButton = new JButton(new ImageIcon("images/folder_edit.png"));
        editCategoryButton.setToolTipText("Modifica categoria");
        editCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainWindow.changeCategoryFromUserInput(getLastSelectedCategory());

            }
        });

        removeCategoryButton = new JButton(new ImageIcon("images/folder_delete.png"));
        removeCategoryButton.setToolTipText("Elimina categoria");
        removeCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainWindow.removeCategory(getLastSelectedCategory());
            }
        });

        mainWindow.addCategoriesActionListener(new CategoriesActionListener() {
            @Override
            public void onCategoriesAdd(Category category) {
                addChildrenToSelectedNode(category);
            }

            @Override
            public void onCategoriesChange(Category category) {
                changeCategory();
            }

            @Override
            public void onCategoriesRemove(Category category) {
                removeSelectedNode();
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
            categoriesTreeRoot = mainWindow.getUserCategories();
            categoriesTreeModel = new DefaultTreeModel(categoriesTreeRoot);

            categoriesTree = new JTree(categoriesTreeModel);

            categoriesTree.setEditable(false);
            categoriesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            categoriesTree.addTreeSelectionListener(new TreeSelectionListener() {
                @Override
                public void valueChanged(TreeSelectionEvent e) {
                    lastSelectedTreeNode = (DefaultMutableTreeNode) categoriesTree.getLastSelectedPathComponent();

                    // il nodo root non esiste veramente nel database
                    // modificarlo/eliminarlo non ha senso, quindi disabilita i pulsanti
                    editCategoryButton.setEnabled(lastSelectedTreeNode != null && !lastSelectedTreeNode.isRoot());
                    removeCategoryButton.setEnabled(lastSelectedTreeNode != null && !lastSelectedTreeNode.isRoot());

                    if (lastSelectedTreeNode != null)
                        referencePanel.setReferences(getLastSelectedCategory());
                }
            });

            // seleziona il nodo root
            categoriesTree.setSelectionRow(0);

            // Espandi tutti i nodi
            for (int i = 0; i < categoriesTree.getRowCount(); i++)
                categoriesTree.expandRow(i);

            return categoriesTree;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile caricare le categorie");
            return null;
        }
    }

    private void addChildrenToSelectedNode(Category category) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(category);
        categoriesTreeModel.insertNodeInto(node, lastSelectedTreeNode, lastSelectedTreeNode.getChildCount());
        categoriesTree.setSelectionPath(new TreePath(node.getPath()));
    }

    private void changeCategory() {
        categoriesTreeModel.reload();
    }

    private void removeSelectedNode() {
        lastSelectedTreeNode.removeFromParent();
        categoriesTreeModel.reload();
    }

    private Category getLastSelectedCategory() {
        return (Category) lastSelectedTreeNode.getUserObject();
    }

}
