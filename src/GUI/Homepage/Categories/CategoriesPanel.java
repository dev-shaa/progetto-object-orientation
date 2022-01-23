package GUI.Homepage.Categories;

import Entities.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Pannello con le categorie dell'utente sotto forma di albero, con pulsanti per aggiungere, modificare o rimuovere una categoria.
 * 
 * @see CategoriesTreeManager
 * @see CategoryTreePanel
 */
public class CategoriesPanel extends JPanel implements CategorySelectionListener {

    private CategoryTreeModel categoriesTree;
    private CategoryTreePanel treePanel;

    private JButton addCategoryButton;
    private JButton changeCategoryButton;
    private JButton removeCategoryButton;

    /**
     * Crea un pannello con tutte le categorie associate dell'utente.
     * 
     * @param categoriesTree
     *            l'albero delle categorie dell'utente
     * @throws IllegalArgumentException
     *             se categoriesTree è nullo.
     * @see #setCategoriesTree(CategoriesTreeManager)
     */
    public CategoriesPanel(CategoryTreeModel categoriesTree) {
        super();

        setCategoriesTree(categoriesTree);

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

        add(toolbar, BorderLayout.NORTH);
        add(treePanel, BorderLayout.CENTER);
    }

    /**
     * Imposta l'albero delle categorie dell'utente.
     * 
     * @param categoriesTree
     *            albero delle categorie
     * @throws IllegalArgumentException
     *             se {@code categoriesTree == null}
     */
    public void setCategoriesTree(CategoryTreeModel categoriesTree) {
        if (treePanel == null) {
            treePanel = new CategoryTreePanel(categoriesTree);
            treePanel.addSelectionListener(this);
        } else {
            treePanel.setCategoriesTree(categoriesTree);
        }

        this.categoriesTree = categoriesTree;
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
     * Mostra una finestra di dialogo in modo che l'utente possa scegliere un nome
     * per la nuova categoria,
     * che sarà inserita come figlia della categoria selezionata.
     * Successivamente tenta di salvare nel database.
     * In caso di fallimento, mostra una finestra di dialogo che comunica l'errore
     * all'utente.
     */
    private void addCategory() {
        try {
            String newCategoryName = getCategoryNameFromUser("Nuova categoria");

            if (newCategoryName != null) {
                categoriesTree.addNode(new CategoryTreeNode(new Category(newCategoryName)), treePanel.getSelectedNode());

                // TODO: aggiungi al database
            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    /**
     * Mostra una finestra di dialogo in modo che l'utente possa scegliere un nuovo nome per la categoria selezionata.
     * Successivamente tenta di salvare nel database.
     * In caso di fallimento, mostra una finestra di dialogo che comunica l'errore all'utente.
     */
    private void changeCategory() {
        try {
            String name = getCategoryNameFromUser(treePanel.getSelectedNode().getUserObject().getName());

            if (name != null) {
                // TODO: cambia nodo
            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
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
                categoriesTree.removeNodeFromParent(treePanel.getSelectedNode());

                // TODO: rimuovi dal database
            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
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
