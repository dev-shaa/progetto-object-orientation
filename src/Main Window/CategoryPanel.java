import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

/**
 * Classe che si occupa di mostrare un pannello con le categorie dell'utente
 * sotto forma di albero, con ogni nodo che rappresenta una categoria.
 * 
 * @version 0.1
 * @author Salvatore Di Gennaro
 * @see MainWindow
 * @see ReferencePanel
 */
public class CategoryPanel extends JPanel {

    // private MainWindow mainWindow;
    private User user;
    private ReferencePanel referencePanel;
    private CategoryDAO categoryDAO = new CategoryDAOPostgreSQL();// TODO: cambia se diventa singleton

    private DefaultTreeModel categoriesTreeModel;
    private DefaultMutableTreeNode lastSelectedTreeNode;

    JButton editCategoryButton;
    JButton removeCategoryButton;

    /**
     * Crea {@code CategoryPanel} con tutte le categorie associate dell'utente.
     * 
     * @param user
     * @since 0.1
     * @author Salvatore Di Gennaro
     */
    public CategoryPanel(User user, ReferencePanel referencePanel) {
        this.user = user;
        this.referencePanel = referencePanel;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        add(getButtonsPanel());
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(getCategoriesTreePanel());
    }

    private JPanel getButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        JButton addCategoryButton = new JButton(new ImageIcon("images/folder_add.png"));
        addCategoryButton.setToolTipText("Nuova categoria");
        addCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCategory();
            }
        });

        editCategoryButton = new JButton(new ImageIcon("images/folder_edit.png"));
        editCategoryButton.setToolTipText("Modifica categoria");
        editCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editCategory((Category) lastSelectedTreeNode.getUserObject());
            }
        });

        removeCategoryButton = new JButton(new ImageIcon("images/folder_delete.png"));
        removeCategoryButton.setToolTipText("Elimina categoria");
        removeCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeCategory((Category) lastSelectedTreeNode.getUserObject());
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

        // http://www.javaknowledge.info/populate-jtree-from-mysql-database/

        try {
            ArrayList<Category> categories = categoryDAO.getAllUserCategory(user);

            DefaultMutableTreeNode root = new DefaultMutableTreeNode();
            categoriesTreeModel = new DefaultTreeModel(root);

            JTree categoriesTree = new JTree(categoriesTreeModel);
            categoriesTree.setEditable(false);
            categoriesTree.setShowsRootHandles(false);
            categoriesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            categoriesTree.addTreeSelectionListener(new TreeSelectionListener() {

                // azionato quando viene selezionato un nodo
                @Override
                public void valueChanged(TreeSelectionEvent e) {
                    lastSelectedTreeNode = (DefaultMutableTreeNode) categoriesTree.getLastSelectedPathComponent();

                    // il nodo root non esiste veramente nel database
                    // modificarlo/eliminarlo non ha senso, quindi disabilita i pulsanti
                    editCategoryButton.setEnabled(lastSelectedTreeNode != root);
                    removeCategoryButton.setEnabled(lastSelectedTreeNode != root);

                    // TODO: carica riferimenti presenti nella categoria selezionata
                    referencePanel.loadReferences(category, user);
                }
            });

            // seleziona il nodo root
            categoriesTree.setSelectionRow(0);

            for (Category category : categories) {

                // NOTE: il nome del nodo viene preso dal toString() dell'oggetto passato
                DefaultMutableTreeNode newTreeNode = new DefaultMutableTreeNode(category);

                // FIXME: per ora tutti i nuovi nodi sono figli di root
                lastSelectedTreeNode.add(newTreeNode);
            }

            // Espandi tutti i nodi
            for (int i = 0; i < categoriesTree.getRowCount(); i++)
                categoriesTree.expandRow(i);

            return categoriesTree;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile caricare le categorie");
            return null;
        }
    }

    private void addCategory() {
        try {
            Category newCategory = new Category(getStringFromUser("Nuova categoria"),
                    (Category) lastSelectedTreeNode.getUserObject());

            // NOTE:
            // il salvataggio nel database può andare storto, l'inserimento nell'albero no
            // quindi assicurati prima che venga salvato
            categoryDAO.saveCategory(newCategory, user);

            DefaultMutableTreeNode newTreeNode = new DefaultMutableTreeNode(newCategory);
            lastSelectedTreeNode.add(newTreeNode);

            categoriesTreeModel.reload();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile creare una nuova categoria");
        }
    }

    private void editCategory(Category category) {
        try {
            String newName = getStringFromUser(category.getName());
            categoryDAO.updateCategory(category, newName);
            category.setName(newName);
            categoriesTreeModel.reload();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile modificare la categoria");
        }
    }

    private void removeCategory(Category category) {
        try {
            categoryDAO.deleteCategory(category);
            lastSelectedTreeNode.removeFromParent();
            categoriesTreeModel.reload();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile eliminare la categoria");
        }
    }

    private void moveCategory() {
        // TODO: implementa
    }

    private String getStringFromUser(String defaultString) throws Exception {
        String categoryName = (String) JOptionPane.showInputDialog(null, "Nome categoria:",
                "Nuova categoria",
                JOptionPane.PLAIN_MESSAGE,
                null, null, defaultString);

        if (categoryName.isEmpty())
            throw new Exception("Nome vuoto"); // TODO: creazione nuovo tipo di eccezione

        return categoryName;
    }

}
