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
// import java.util.Date;
import java.util.HashMap;

public class CategoryPanel extends JPanel {
    private CategoryDAO categoryDAO = new CategoryDAOPostgreSQL();// TODO: cambia se diventa singleton
    private User user;

    private DefaultTreeModel categoriesTreeModel;
    private DefaultMutableTreeNode selectedTreeNode;
    private HashMap<DefaultMutableTreeNode, Category> categoriesTreeHashMap;

    public CategoryPanel(User user) {
        this.user = user;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

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
                editCategory();
            }
        });

        JButton deleteCategoryButton = new JButton(new ImageIcon("images/folder_delete.png"));
        deleteCategoryButton.setToolTipText("Elimina categoria");
        deleteCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteCategory();
            }
        });

        buttonsPanel.add(createCategoryButton);
        buttonsPanel.add(editCategoryButton);
        buttonsPanel.add(deleteCategoryButton);

        // https://stackoverflow.com/questions/14386682/how-to-get-selected-node-from-jtree-after-selecting-it

        JScrollPane categoriesPanel = new JScrollPane(getCategoriesTree());

        add(buttonsPanel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(categoriesPanel);
    }

    private void createCategory() {
        try {
            Category newCategory = new Category(getStringFromUser());

            categoryDAO.saveCategory(newCategory);

            DefaultMutableTreeNode newTreeNode = new DefaultMutableTreeNode(newCategory.getName());
            selectedTreeNode.add(newTreeNode);

            categoriesTreeModel.reload();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile creare una nuova categoria");
        }
    }

    private void editCategory() {

    }

    private void deleteCategory() {
        try {
            // TODO: rimuovi dal database la categoria e le sue sottocategorie

            selectedTreeNode.removeFromParent();
            categoriesTreeModel.reload();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Impossibile eliminare la categoria");
        }
    }

    private void moveCategory() {

    }

    private String getStringFromUser() throws Exception {
        String categoryName = (String) JOptionPane.showInputDialog(null, "Nome categoria:",
                "Nuova categoria",
                JOptionPane.PLAIN_MESSAGE,
                null, null, "Nuova categoria");

        if (categoryName.isEmpty())
            throw new Exception("Nome vuoto");

        return categoryName;
    }

    private JTree getCategoriesTree() {

        ArrayList<Category> categories = categoryDAO.getAllCategory(user);
        categoriesTreeHashMap = new HashMap<DefaultMutableTreeNode, Category>();

        DefaultMutableTreeNode categoriesTreeRoot = new DefaultMutableTreeNode("Tutti i riferimenti");
        categoriesTreeModel = new DefaultTreeModel(categoriesTreeRoot);

        JTree categoriesTree = new JTree(categoriesTreeModel);
        categoriesTree.setEditable(false);
        categoriesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        categoriesTree.setShowsRootHandles(false);
        categoriesTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                selectedTreeNode = (DefaultMutableTreeNode) categoriesTree.getLastSelectedPathComponent();
            }
        });

        for (Category category : categories) {

            DefaultMutableTreeNode newTreeNode = new DefaultMutableTreeNode(category.getName());
            selectedTreeNode.add(newTreeNode);

            // categoriesTreeHashMap.put(key, value)
        }

        return categoriesTree;
    }
}
