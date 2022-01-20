package GUI.Homepage;

import Entities.*;
import GUI.Categories.CategoriesTreeManager;
import GUI.Categories.CategoryMutableTreeNode;
import GUI.Categories.CategorySelectionListener;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * Pannello con le categorie dell'utente sotto forma di albero, con ogni nodo che rappresenta una categoria.
 * Sono presenti dei pulsanti per aggiungere, modificare o rimuovere una categoria.
 * 
 * @see CategoriesTreeManager
 */
public class CategoriesPanel extends JPanel implements TreeSelectionListener {

    private CategoriesTreeManager categoriesTree;

    private JTree displayTree;
    private CategoryMutableTreeNode selectedNode;

    private JButton addCategoryButton;
    private JButton changeCategoryButton;
    private JButton removeCategoryButton;

    private ArrayList<CategorySelectionListener> selectionListeners;

    /**
     * Crea {@code CategoryPanel} con tutte le categorie associate dell'utente.
     * 
     * @param categoriesTree
     *            l'albero delle categorie dell'utente
     * @throws IllegalArgumentException
     *             se categoriesTree non è valido
     * @see #setCategoriesTree(CategoriesTreeManager)
     */
    public CategoriesPanel(CategoriesTreeManager categoriesTree) throws IllegalArgumentException {
        selectionListeners = new ArrayList<>(3);

        setCategoriesTree(categoriesTree);

        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        toolbar.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

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

        displayTree = new JTree(categoriesTree.getTreeModel());

        displayTree.setEditable(false);
        displayTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        displayTree.addTreeSelectionListener(this);

        for (int i = 0; i < displayTree.getRowCount(); i++)
            displayTree.expandRow(i);

        displayTree.setSelectionRow(0);

        JScrollPane treeScrollPane = new JScrollPane(displayTree);

        add(toolbar, BorderLayout.NORTH);
        add(treeScrollPane, BorderLayout.CENTER);
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
                categoriesTree.addNode(new Category(newCategoryName), selectedNode);

                // così è più comodo per l'utente
                displayTree.expandPath(new TreePath(selectedNode.getPath()));
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
            String name = getCategoryNameFromUser(selectedNode.getUserObject().getName());

            if (name != null) {
                categoriesTree.changeNode(selectedNode, name);
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
                categoriesTree.removeNode(selectedNode);
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
     * Aggiunge un ascoltatore all'evento di selezione di un nodo.
     * 
     * @param listener
     *            ascoltatore da aggiungere
     */
    public void addSelectionListener(CategorySelectionListener listener) {
        if (listener != null)
            selectionListeners.add(listener);
    }

    /**
     * Rimuove un ascoltatore all'evento di selezione di un nodo.
     * 
     * @param listener
     *            ascoltatore da rimuovere
     */
    public void removeSelectionListener(CategorySelectionListener listener) {
        if (listener != null)
            selectionListeners.remove(listener);
    }

    private void triggerSelectionListener(Category category) {
        for (CategorySelectionListener categorySelectionListener : selectionListeners) {
            categorySelectionListener.onCategorySelected(category);
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        selectedNode = (CategoryMutableTreeNode) displayTree.getLastSelectedPathComponent();

        boolean nodeCanBeChanged = selectedNode != null && selectedNode.canBeChanged();
        changeCategoryButton.setEnabled(nodeCanBeChanged);
        removeCategoryButton.setEnabled(nodeCanBeChanged);

        if (selectedNode != null) {
            triggerSelectionListener(selectedNode.getUserObject());
        }
    }
}
