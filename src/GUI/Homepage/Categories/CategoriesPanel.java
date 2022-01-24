package GUI.Homepage.Categories;

import Entities.Category;
import DAO.CategoryDAO;
import Exceptions.CategoryDatabaseException;
import GUI.Utilities.CustomTreeNode;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Pannello con le categorie dell'utente sotto forma di albero, con pulsanti per aggiungere, modificare o rimuovere una categoria.
 */
public class CategoriesPanel extends JPanel implements CategorySelectionListener {

    private CategoryTreeModel categoriesTree;
    private CategoryTreePanel treePanel;
    private CategoryDAO categoryDAO;

    private JButton addCategoryButton;
    private JButton changeCategoryButton;
    private JButton removeCategoryButton;

    /**
     * Crea un pannello recuperando tutte le categorie associate dell'utente.
     * 
     * @param categoryDAO
     *            classe DAO per interfacciarsi al database
     * @throws IllegalArgumentException
     *             se {@code categoryDAO} è nullo
     * @throws CategoryDatabaseException
     *             se il recupero delle categorie non va a buon fine
     * @see #setCategoryDAO(CategoryDAO)
     */
    public CategoriesPanel(CategoryDAO categoryDAO) throws CategoryDatabaseException {
        super();

        setCategoryDAO(categoryDAO);

        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        addCategoryButton = new JButton(new ImageIcon("images/folder_add.png"));
        addCategoryButton.setToolTipText("Nuova categoria");
        addCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCategory();
            }
        });

        changeCategoryButton = new JButton(new ImageIcon("images/folder_edit.png"));
        changeCategoryButton.setToolTipText("Modifica categoria");
        changeCategoryButton.setEnabled(false);
        changeCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeCategory();
            }
        });

        removeCategoryButton = new JButton(new ImageIcon("images/folder_delete.png"));
        removeCategoryButton.setToolTipText("Elimina categoria");
        removeCategoryButton.setEnabled(false);
        removeCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeCategory();
            }
        });

        toolbar.add(addCategoryButton);
        toolbar.add(changeCategoryButton);
        toolbar.add(removeCategoryButton);

        treePanel = new CategoryTreePanel(getCategoriesTree());
        treePanel.addSelectionListener(this);

        add(toolbar, BorderLayout.NORTH);
        add(treePanel, BorderLayout.CENTER);
    }

    /**
     * Imposta la classe DAO per interfacciarsi al database e carica le categorie associate all'utente.
     * 
     * @param categoryDAO
     *            classe DAO per le categorie
     * @throws IllegalArgumentException
     *             se {@code categoryDAO == null}
     * @throws CategoryDatabaseException
     *             se il recupero delle categorie non va a buon fine
     */
    public void setCategoryDAO(CategoryDAO categoryDAO) throws CategoryDatabaseException {
        if (categoryDAO == null)
            throw new IllegalArgumentException("categoryDAO can't be null");

        this.categoryDAO = categoryDAO;

        categoriesTree = categoryDAO.getUserCategories();
    }

    /**
     * Restituisce l'albero delle categorie.
     * 
     * @return
     *         albero delle categorie dell'utente
     */
    public CategoryTreeModel getCategoriesTree() {
        return categoriesTree;
    }

    /**
     * Restituisce il pannello dell'albero delle categorie.
     * 
     * @return
     *         pannello dell'albero
     */
    public CategoryTreePanel getTreePanel() {
        return treePanel;
    }

    /**
     * Mostra una finestra di dialogo in modo che l'utente possa scegliere un nome per la nuova categoria,
     * che sarà inserita come figlia della categoria selezionata.
     * Successivamente tenta di salvare nel database.
     * In caso di fallimento, mostra una finestra di dialogo che comunica l'errore all'utente.
     */
    private void addCategory() {
        try {
            String newCategoryName = getCategoryNameFromUser("Nuova categoria");

            if (newCategoryName != null) {
                Category newCategory = new Category(newCategoryName, treePanel.getSelectedNode().getUserObject());

                categoryDAO.addCategory(newCategory);

                categoriesTree.addNode(new CustomTreeNode<Category>(newCategory), treePanel.getSelectedNode());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    /**
     * Mostra una finestra di dialogo in modo che l'utente possa scegliere un nuovo nome per la categoria selezionata.
     * Successivamente tenta di salvare nel database.
     * In caso di fallimento, mostra una finestra di dialogo che comunica l'errore all'utente.
     */
    private void changeCategory() {
        Category category = treePanel.getSelectedNode().getUserObject();
        String oldName = category.getName();

        try {
            String newName = getCategoryNameFromUser(oldName);

            if (newName != null)
                categoryDAO.updateCategoryName(category);
        } catch (Exception e) {
            category.setName(oldName);
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    /**
     * Mostra una finestra di dialogo in modo che l'utente possa confermare se eliminare la categoria.
     * In caso di fallimento, mostra una finestra di dialogo che comunica l'errore all'utente.
     */
    private void removeCategory() {
        try {
            int confirmDialogBoxOption = JOptionPane.showConfirmDialog(null, "Sicuro di volere eliminare questa categoria?", "Elimina categoria", JOptionPane.YES_NO_OPTION);

            if (confirmDialogBoxOption == JOptionPane.YES_OPTION) {
                categoryDAO.removeCategory(treePanel.getSelectedNode().getUserObject());
                categoriesTree.removeNodeFromParent(treePanel.getSelectedNode());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    /**
     * Mostra un pannello in cui l'utente può inserire una stringa.
     * 
     * @param defaultName
     *            il testo di default che appare come input
     * @return
     *         restituisce il testo inserito, {@code null} se l'utente annulla l'operazione
     */
    private String getCategoryNameFromUser(String defaultName) {
        return (String) JOptionPane.showInputDialog(null, "Inserisci il nome della categoria", "Nuova categoria", JOptionPane.PLAIN_MESSAGE, null, null, defaultName);
    }

    @Override
    public void onCategorySelected(Category selectedCategory) {
        changeCategoryButton.setEnabled(selectedCategory != null);
        removeCategoryButton.setEnabled(selectedCategory != null);
    }

}
