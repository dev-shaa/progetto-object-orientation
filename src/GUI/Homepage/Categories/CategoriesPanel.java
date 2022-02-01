package GUI.Homepage.Categories;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import Controller.CategoryController;
import Entities.Category;

/**
 * Pannello con le categorie dell'utente sotto forma di albero, con pulsanti per aggiungere, modificare o rimuovere una categoria.
 */
public class CategoriesPanel extends JPanel implements CategorySelectionListener {

    private CategoryController categoryController;
    private CategoriesTreePanel treePanel;

    private JButton addCategoryButton;
    private JButton changeCategoryButton;
    private JButton removeCategoryButton;

    /**
     * Crea un pannello con tutte le categorie associate dell'utente.
     * 
     * @param categoryController
     *            controller delle categorie
     * @throws IllegalArgumentException
     *             se {@code categoryController == null}
     */
    public CategoriesPanel(CategoryController categoryController) {
        super();

        setCategoryController(categoryController);

        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        addCategoryButton = new JButton(new ImageIcon("images/folder_add.png"));
        addCategoryButton.setToolTipText("Crea nuova categoria");
        addCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCategory();
            }
        });

        changeCategoryButton = new JButton(new ImageIcon("images/folder_edit.png"));
        changeCategoryButton.setToolTipText("Modifica categoria selezionata");
        changeCategoryButton.setEnabled(false);
        changeCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeCategory();
            }
        });

        removeCategoryButton = new JButton(new ImageIcon("images/folder_delete.png"));
        removeCategoryButton.setToolTipText("Elimina categoria selezionata");
        removeCategoryButton.setEnabled(false);
        removeCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeCategory();
            }
        });

        toolbar.add(addCategoryButton);
        toolbar.add(changeCategoryButton);
        toolbar.add(removeCategoryButton);

        add(toolbar, BorderLayout.NORTH);
        add(treePanel, BorderLayout.CENTER);
    }

    /**
     * Imposta il controller delle categorie.
     * 
     * @param categoryController
     *            controller delle categorie
     * @throws IllegalArgumentException
     *             se {@code controller == null}
     */
    public void setCategoryController(CategoryController categoryController) {
        if (categoryController == null)
            throw new IllegalArgumentException("categoryController can't be null");

        this.categoryController = categoryController;

        if (treePanel == null) {
            treePanel = new CategoriesTreePanel(getCategoryController().getCategoriesTree());
            treePanel.addSelectionListener(this);
        } else {
            treePanel.setCategoriesTree(getCategoryController().getCategoriesTree());
        }
    }

    /**
     * Restituisce il controller delle categorie.
     * 
     * @param categoryController
     *            controller delle categorie
     */
    public CategoryController getCategoryController() {
        return categoryController;
    }

    /**
     * Restituisce il pannello dell'albero delle categorie.
     * 
     * @return
     *         pannello dell'albero
     */
    public CategoriesTreePanel getTreePanel() {
        return treePanel;
    }

    @Override
    public void onCategorySelection(Category selectedCategory) {
        changeCategoryButton.setEnabled(selectedCategory != null);
        removeCategoryButton.setEnabled(selectedCategory != null);
    }

    @Override
    public void onCategoryClearSelection() {
        changeCategoryButton.setEnabled(false);
        removeCategoryButton.setEnabled(false);
    }

    private void addCategory() {
        try {
            String newCategoryName = getCategoryNameFromUser("Nuova categoria");

            if (newCategoryName != null) {
                getCategoryController().addCategory(newCategoryName, treePanel.getSelectedNode());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void changeCategory() {
        try {
            String newName = getCategoryNameFromUser(treePanel.getSelectedNode().getUserObject().getName());

            if (newName != null) {
                getCategoryController().changeCategory(treePanel.getSelectedNode(), newName);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void removeCategory() {
        try {
            int confirmDialogBoxOption = JOptionPane.showConfirmDialog(null, "Sicuro di voler eliminare questa categoria?", "Elimina categoria", JOptionPane.YES_NO_OPTION);

            if (confirmDialogBoxOption == JOptionPane.YES_OPTION) {
                getCategoryController().removeCategory(treePanel.getSelectedNode());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private String getCategoryNameFromUser(String defaultName) {
        return (String) JOptionPane.showInputDialog(null, "Inserisci il nuovo nome della categoria", "Nuova categoria", JOptionPane.PLAIN_MESSAGE, null, null, defaultName);
    }

}
