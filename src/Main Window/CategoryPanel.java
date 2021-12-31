import java.util.ArrayList;
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

import Exceptions.InvalidInputException;

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

    private User user;
    private ReferencePanel referencePanel;
    private CategoryDAO categoryDAO = new CategoryDAOPostgreSQL();// TODO: cambia se diventa singleton

    private DefaultTreeModel categoriesTreeModel;
    private DefaultMutableTreeNode lastSelectedTreeNode;
    private JTree categoriesTree;
    private JButton editCategoryButton;
    private JButton removeCategoryButton;

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
                removeCategory();
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
            ArrayList<Category> categories = categoryDAO.getAllUserCategory(user);

            DefaultMutableTreeNode root = new DefaultMutableTreeNode();
            categoriesTreeModel = new DefaultTreeModel(root);

            categoriesTree = new JTree(categoriesTreeModel);
            categoriesTree.setEditable(false);
            categoriesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            categoriesTree.addTreeSelectionListener(new TreeSelectionListener() {
                @Override
                public void valueChanged(TreeSelectionEvent e) {
                    lastSelectedTreeNode = (DefaultMutableTreeNode) categoriesTree.getLastSelectedPathComponent();

                    // il nodo root non esiste veramente nel database
                    // modificarlo/eliminarlo non ha senso, quindi disabilita i pulsanti
                    editCategoryButton.setEnabled(lastSelectedTreeNode != null && lastSelectedTreeNode != root);
                    removeCategoryButton.setEnabled(lastSelectedTreeNode != null && lastSelectedTreeNode != root);

                    if (lastSelectedTreeNode != null)
                        referencePanel.loadReferencesListFromCategory((Category) lastSelectedTreeNode.getUserObject(),
                                user);
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

            categoriesTreeModel.insertNodeInto(newTreeNode, lastSelectedTreeNode, lastSelectedTreeNode.getChildCount());
            categoriesTree.setSelectionPath(new TreePath(newTreeNode.getPath()));
        } catch (InvalidInputException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
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
        } catch (InvalidInputException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile modificare la categoria");
        }
    }

    private void removeCategory() {
        try {
            int confirmDialogBoxOption = JOptionPane.showConfirmDialog(null,
                    "Sicuro di volere eliminare questa categoria?", "Elimina categoria",
                    JOptionPane.YES_NO_OPTION);

            if (confirmDialogBoxOption == JOptionPane.YES_OPTION) {
                categoryDAO.deleteCategory((Category) lastSelectedTreeNode.getUserObject());
                lastSelectedTreeNode.removeFromParent();
                categoriesTreeModel.reload();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile eliminare la categoria");
        }
    }

    private void moveCategory() {
        // TODO: implementa
    }

    private String getStringFromUser(String defaultString) throws Exception {
        String categoryName = (String) JOptionPane.showInputDialog(null, "Nome categoria:", "Nuova categoria",
                JOptionPane.PLAIN_MESSAGE, null, null, defaultString);

        if (categoryName.isEmpty())
            throw new InvalidInputException("Il nome della categoria non può essere vuoto.");

        return categoryName;
    }

}
