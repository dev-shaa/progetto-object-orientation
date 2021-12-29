import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.*;

import java.awt.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
// import java.awt.GridLayout;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Classe che si occupa di mostrare e gestire le categorie dell'utente sotto
 * forma di albero.
 * 
 * @version 0.1
 * @author Salvatore Di Gennaro
 * @see MainWindow
 */
public class CategoryPanel extends JPanel {

    // TODO: forse è il caso di implementare un tree model personalizzato per le
    // categorie

    private User user;
    private CategoryDAO categoryDAO = new CategoryDAOPostgreSQL();// TODO: cambia se diventa singleton

    private DefaultTreeModel categoriesTreeModel;
    private DefaultMutableTreeNode lastSelectedTreeNode;
    private JTree categoriesTree;

    /**
     * Crea {@code CategoryPanel} con tutte le categorie dell'utente.
     * 
     * @param user
     * @since 0.1
     * @author Salvatore Di Gennaro
     */
    public CategoryPanel(User user) {
        this.user = user;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        add(getButtonsPanel());
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(getCategoriesTreePanel());
    }

    private JPanel getButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32)); // NOTE: un'altezza di 32px mi sembrava buona

        JButton createCategoryButton = new JButton(new ImageIcon("images/folder_add.png"));
        createCategoryButton.setToolTipText("Nuova categoria");
        createCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createCategory();
            }
        });

        JButton editCategoryButton = new JButton(new ImageIcon("images/folder_edit.png"));
        editCategoryButton.setToolTipText("Modifica categoria");
        editCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: implementa modifica
            }
        });

        JButton deleteCategoryButton = new JButton(new ImageIcon("images/folder_delete.png"));
        deleteCategoryButton.setToolTipText("Elimina categoria");
        deleteCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteCategory((Category) lastSelectedTreeNode.getUserObject());
            }
        });

        buttonsPanel.add(createCategoryButton);
        buttonsPanel.add(editCategoryButton);
        buttonsPanel.add(deleteCategoryButton);

        return buttonsPanel;
    }

    private JScrollPane getCategoriesTreePanel() {
        return new JScrollPane(getCategoriesTree());
    }

    private void createCategory() {
        try {
            Category newCategory = new Category(
                    getStringFromUser("Nuova categoria"), (Category) lastSelectedTreeNode.getUserObject());

            categoryDAO.saveCategory(newCategory, user);

            DefaultMutableTreeNode newTreeNode = new DefaultMutableTreeNode(newCategory.getName());
            lastSelectedTreeNode.add(newTreeNode);

            categoriesTreeModel.reload();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile creare una nuova categoria");
        }
    }

    private void editCategory(Category category) {
        // TODO: implementa
        // https://stackoverflow.com/questions/5685068/how-to-rename-a-node-in-jtree

        try {
            String newName = getStringFromUser(category.getName());

            categoryDAO.updateCategory(category, newName);

            category.setName(newName);

            // selectedTreeNode.

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void deleteCategory(Category category) {
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
            throw new Exception("Nome vuoto");

        return categoryName;
    }

    private JTree getCategoriesTree() {

        // https://stackoverflow.com/questions/14386682/how-to-get-selected-node-from-jtree-after-selecting-it
        // http://www.javaknowledge.info/populate-jtree-from-mysql-database/

        try {
            ArrayList<Category> categories = categoryDAO.getAllUserCategory(user);
            DefaultMutableTreeNode categoriesTreeRoot = new DefaultMutableTreeNode(
                    new Category("Tutti i riferimenti", null));
            categoriesTreeModel = new DefaultTreeModel(categoriesTreeRoot);

            categoriesTree = new JTree(categoriesTreeModel);
            categoriesTree.setEditable(false);
            categoriesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            categoriesTree.setShowsRootHandles(false);
            categoriesTree.addTreeSelectionListener(new TreeSelectionListener() {
                @Override
                public void valueChanged(TreeSelectionEvent e) {
                    lastSelectedTreeNode = (DefaultMutableTreeNode) categoriesTree.getLastSelectedPathComponent();
                }
            });

            lastSelectedTreeNode = categoriesTreeRoot;

            for (Category category : categories) {

                // NOTE: il nome del nodo viene preso dal toString() dell'oggetto passato
                DefaultMutableTreeNode newTreeNode = new DefaultMutableTreeNode(category);

                // FIXME: per ora tutti i nuovi nodi sono figli di root
                lastSelectedTreeNode.add(newTreeNode);
            }

            // Espandi tutti i nodi
            // TODO: rivedi
            for (int i = 0; i < categoriesTree.getRowCount(); i++)
                categoriesTree.expandRow(i);

            return categoriesTree;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

}
