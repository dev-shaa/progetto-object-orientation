package GUI.Categories;

import Entities.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Pannello con le categorie dell'utente sotto forma di albero, con ogni nodo che rappresenta una categoria.
 * Sono presenti dei pulsanti per aggiungere, modificare o rimuovere una categoria.
 * 
 * @see CategoriesTreeManager
 * @see CategoryTreePanel
 */
public class CategoriesPanel extends JPanel {

    private CategoriesTreeManager categoriesTree;
    private CategoryTreePanel treePanel;

    private JButton addCategoryButton;
    private JButton changeCategoryButton;
    private JButton removeCategoryButton;

    private ArrayList<CategorySelectionListener> selectionListeners;

    /**
     * Crea un pannello con tutte le categorie associate dell'utente.
     * 
     * @param categoriesTree
     *            l'albero delle categorie dell'utente
     * @throws IllegalArgumentException
     *             se categoriesTree non è valido
     * @see #setCategoriesTree(CategoriesTreeManager)
     */
    public CategoriesPanel(CategoriesTreeManager categoriesTree) throws IllegalArgumentException {
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
        changeCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeCategory();
            }
        });

        removeCategoryButton = new JButton(new ImageIcon("images/folder_delete.png"));
        removeCategoryButton.setToolTipText("Elimina categoria");
        removeCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeCategory();
            }
        });

        toolbar.add(addCategoryButton);
        toolbar.add(changeCategoryButton);
        toolbar.add(removeCategoryButton);

        treePanel = new CategoryTreePanel(categoriesTree);

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
    public void setCategoriesTree(CategoriesTreeManager categoriesTree) throws IllegalArgumentException {
        if (categoriesTree == null)
            throw new IllegalArgumentException("categoriesTree non può essere null");

        this.categoriesTree = categoriesTree;
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
                categoriesTree.addNode(new Category(newCategoryName), treePanel.getSelectedNode());

                // così è più comodo per l'utente
                // displayTree.expandPath(new TreePath(selectedNode.getPath()));
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

            if (name != null)
                categoriesTree.changeNode(treePanel.getSelectedNode(), name);
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
                categoriesTree.removeNode(treePanel.getSelectedNode());
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

    /**
     * Aggiunge un listener all'evento di selezione di un nodo.
     * 
     * @param listener
     *            listener da aggiungere
     */
    public void addCategorySelectionListener(CategorySelectionListener listener) {
        if (listener == null)
            return;

        if (selectionListeners == null)
            selectionListeners = new ArrayList<>(3);

        selectionListeners.add(listener);
    }

    /**
     * Rimuove un listener dall'evento di selezione di un nodo.
     * 
     * @param listener
     *            listener da rimuovere
     */
    public void removeCategorySelectionListener(CategorySelectionListener listener) {
        if (listener == null || selectionListeners == null)
            return;

        selectionListeners.remove(listener);
    }

}
