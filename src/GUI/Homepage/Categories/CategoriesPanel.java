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

    private final String addCategoryTooltip = "Crea nuova categoria";
    private final String changeCategoryTooltip = "Modifica categoria selezionata";
    private final String removeCategoryTooltip = "Elimina categoria selezionata";

    private final String categoryNameDialogTitle = "Nuova categoria";
    private final String categoryNameDialogPrompt = "Inserisci il nuovo nome della categoria";
    private final String defaultCategoryName = "Nuova categoria";

    private final String removeCategoryDialogTitle = "Elimina categoria";
    private final String removeCategoryDialogPrompt = "Sicuro di voler eliminare questa categoria?";

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
        addCategoryButton.setToolTipText(addCategoryTooltip);
        addCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCategory();
            }
        });

        changeCategoryButton = new JButton(new ImageIcon("images/folder_edit.png"));
        changeCategoryButton.setToolTipText(changeCategoryTooltip);
        changeCategoryButton.setEnabled(false);
        changeCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeCategory();
            }
        });

        removeCategoryButton = new JButton(new ImageIcon("images/folder_delete.png"));
        removeCategoryButton.setToolTipText(removeCategoryTooltip);
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
    public void onCategorySelected(Category selectedCategory) {
        changeCategoryButton.setEnabled(selectedCategory != null);
        removeCategoryButton.setEnabled(selectedCategory != null);
    }

    private void addCategory() {
        try {
            String newCategoryName = getCategoryNameFromUser(defaultCategoryName);

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
            int confirmDialogBoxOption = JOptionPane.showConfirmDialog(null, removeCategoryDialogPrompt, removeCategoryDialogTitle, JOptionPane.YES_NO_OPTION);

            if (confirmDialogBoxOption == JOptionPane.YES_OPTION) {
                getCategoryController().removeCategory(treePanel.getSelectedNode());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private String getCategoryNameFromUser(String defaultName) {
        return (String) JOptionPane.showInputDialog(null, categoryNameDialogPrompt, categoryNameDialogTitle, JOptionPane.PLAIN_MESSAGE, null, null, defaultName);
    }

}
