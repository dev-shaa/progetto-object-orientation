package GUI;

import Entities.*;
import Entities.References.BibliographicReference;
import Entities.References.OnlineResources.*;
import Entities.References.OnlineResources.Image;
import Entities.References.PhysicalResources.*;
import Exceptions.Database.CategoryDatabaseException;
import Exceptions.Database.ReferenceDatabaseException;
import GUI.Categories.CategoriesTreePanel;
import GUI.Categories.CategorySelectionListener;
import GUI.References.*;
import GUI.Search.*;
import GUI.Utilities.PopupButton;
import GUI.Utilities.Tree.CustomTreeModel;
import Controller.Controller;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Pagina principale dell'applicativo.
 * <p>
 * Mostra e offre opzioni per gestire le categorie e i riferimenti dell'utente.
 */
public class Homepage extends JFrame implements CategorySelectionListener, ReferenceSelectionListener, SearchListener {

    private Controller controller;

    private JLabel userLabel;
    private JButton logoutButton;

    private CategoriesTreePanel categoriesTreePanel;
    private JButton createCategoryButton;
    private JButton updateCategoryButton;
    private JButton removeCategoryButton;

    private ReferenceListPanel referenceListPanel;
    private ReferenceInfoPanel referenceInfoPanel;
    private JButton updateReferenceButton;
    private JButton removeReferenceButton;

    private SearchPanel referenceSearchPanel;

    /**
     * Crea una nuova pagina principale con il controller indicato.
     * 
     * @param controller
     *            controller della GUI
     * @throws IllegalArgumentException
     *             se {@code controller == null}
     * @see #setController(Controller)
     */
    public Homepage(Controller controller) {
        super();

        setController(controller);

        setTitle("Pagina principale");
        setMinimumSize(new Dimension(400, 400));
        setSize(800, 600);
        setupCloseOperation();

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        setContentPane(contentPane);

        contentPane.add(setupUserInfoPanel(), BorderLayout.NORTH);

        referenceSearchPanel = new SearchPanel();
        referenceSearchPanel.addReferenceSearchListener(this);

        // JSplitPane ammette solo due pannelli
        // noi ne abbiamo tre, quindi dobbiamo creare due JSplitPane
        JSplitPane subSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, setupCategoriesPanel(), setupReferencePanel());
        subSplitPane.setResizeWeight(0.15);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subSplitPane, referenceSearchPanel);
        splitPane.setResizeWeight(0.8);

        contentPane.add(splitPane, BorderLayout.CENTER);
    }

    @Override
    public void setVisible(boolean b) {

        // mostra un messaggio dopo l'apertura della finestra,
        // se si è verificato un errore durante l'accesso al database

        boolean failedToLoad = false;

        if (b) {
            updateUserLabelText();

            try {
                CustomTreeModel<Category> tree = getController().getCategoryRepository().getTree();

                categoriesTreePanel.setTreeModel(tree);
                referenceSearchPanel.setCategoriesTree(tree);
            } catch (CategoryDatabaseException e) {
                categoriesTreePanel.setTreeModel(null);
                referenceSearchPanel.setCategoriesTree(null);
                failedToLoad = true;
            }

            referenceListPanel.clear();
            referenceInfoPanel.clear();
            referenceSearchPanel.clear();

            setLocationRelativeTo(null);
        }

        super.setVisible(b);

        if (failedToLoad) {
            String[] choices = { "Riprova", "Esci" };
            int option = JOptionPane.showOptionDialog(this, "Si è verificato un errore durante il recupero dei dati dell'utente.", "Errore recupero", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, choices, 0);

            if (option == JOptionPane.YES_OPTION)
                setVisible(true);
            else
                getController().openLoginPage();
        }
    }

    /**
     * Imposta il controller della GUI.
     * 
     * @param controller
     *            controller della GUI
     * @throws IllegalArgumentException
     *             se {@code controller == null}
     */
    public void setController(Controller controller) {
        if (controller == null)
            throw new IllegalArgumentException("controller can't be null");

        this.controller = controller;
    }

    /**
     * Restituisce il controller della GUI.
     * 
     * @return controller
     */
    public Controller getController() {
        return controller;
    }

    private void setupCloseOperation() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirmDialogBoxOption = JOptionPane.showConfirmDialog(null, "Sicuro di volere uscire?", "Esci", JOptionPane.YES_NO_OPTION);

                if (confirmDialogBoxOption == JOptionPane.YES_OPTION)
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }

    // #region CATEGORIES

    private JPanel setupCategoriesPanel() {
        JPanel categoriesPanel = new JPanel();

        categoriesTreePanel = new CategoriesTreePanel();
        categoriesTreePanel.addSelectionListener(this);

        categoriesPanel.setLayout(new BorderLayout(5, 5));
        categoriesPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        createCategoryButton = new JButton(new ImageIcon("images/folder_add.png"));
        createCategoryButton.setToolTipText("Crea nuova categoria");
        createCategoryButton.setEnabled(false);
        createCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCategory();
            }
        });

        updateCategoryButton = new JButton(new ImageIcon("images/folder_edit.png"));
        updateCategoryButton.setToolTipText("Modifica categoria selezionata");
        updateCategoryButton.setEnabled(false);
        updateCategoryButton.addActionListener(new ActionListener() {
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

        toolbar.add(createCategoryButton);
        toolbar.add(updateCategoryButton);
        toolbar.add(removeCategoryButton);

        categoriesPanel.add(toolbar, BorderLayout.NORTH);
        categoriesPanel.add(categoriesTreePanel, BorderLayout.CENTER);

        return categoriesPanel;
    }

    private void addCategory() {
        String newCategoryName = getCategoryNameFromUser("Nuova categoria");

        if (newCategoryName == null)
            return;

        Category newCategory = new Category(newCategoryName, categoriesTreePanel.getSelectedCategory());

        try {
            getController().getCategoryRepository().save(newCategory);
        } catch (CategoryDatabaseException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errore salvataggio categoria", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changeCategory() {
        Category selectedCategory = categoriesTreePanel.getSelectedCategory();

        if (selectedCategory == null)
            return;

        String newName = getCategoryNameFromUser(selectedCategory.getName());

        if (newName == null)
            return;

        try {
            getController().getCategoryRepository().update(selectedCategory, newName);
        } catch (CategoryDatabaseException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errore modifica categoria", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeCategory() {
        Category selectedCategory = categoriesTreePanel.getSelectedCategory();

        if (selectedCategory == null)
            return;

        int confirmDialogBoxOption = JOptionPane.showConfirmDialog(null, "Sicuro di voler eliminare questa categoria?", "Elimina categoria", JOptionPane.YES_NO_OPTION);

        if (confirmDialogBoxOption != JOptionPane.YES_OPTION)
            return;

        try {
            getController().getCategoryRepository().remove(selectedCategory);
            getController().getReferenceRepository().forceNextRetrievalFromDatabase();
        } catch (CategoryDatabaseException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errore eliminazione categoria", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getCategoryNameFromUser(String defaultName) {
        return (String) JOptionPane.showInputDialog(this, "Inserisci il nuovo nome della categoria", "Nuova categoria", JOptionPane.PLAIN_MESSAGE, null, null, defaultName);
    }

    @Override
    public void onCategorySelection(Category category) {
        createCategoryButton.setEnabled(true);
        updateCategoryButton.setEnabled(category != null);
        removeCategoryButton.setEnabled(category != null);

        try {
            referenceListPanel.setReferences(getController().getReferenceRepository().get(category));
        } catch (ReferenceDatabaseException e) {
            referenceListPanel.clear();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errore recupero riferimenti", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void onCategoryClearSelection() {
        createCategoryButton.setEnabled(false);
        updateCategoryButton.setEnabled(false);
        removeCategoryButton.setEnabled(false);

        referenceListPanel.clear();
        referenceInfoPanel.clear();
    }

    // #endregion

    // #region REFERENCES

    private JPanel setupReferencePanel() {
        JPanel referencePanel = new JPanel();

        referencePanel.setLayout(new BorderLayout(5, 5));
        referencePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        PopupButton createReferenceButton = new PopupButton(new ImageIcon("images/file_add.png"));
        createReferenceButton.setToolTipText("Crea riferimento");

        JMenuItem articleOption = new JMenuItem("Articolo");
        articleOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getController().openArticleEditor(null);
            }
        });
        createReferenceButton.addToPopupMenu(articleOption);

        JMenuItem bookOption = new JMenuItem("Libro");
        bookOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getController().openBookEditor(null);
            }
        });
        createReferenceButton.addToPopupMenu(bookOption);

        JMenuItem thesisOption = new JMenuItem("Tesi");
        thesisOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getController().openThesisEditor(null);
            }
        });
        createReferenceButton.addToPopupMenu(thesisOption);
        createReferenceButton.addPopupSeparator();

        JMenuItem websiteOption = new JMenuItem("Sito web");
        websiteOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getController().openWebsiteEditor(null);
            }
        });
        createReferenceButton.addToPopupMenu(websiteOption);

        JMenuItem sourceCodeOption = new JMenuItem("Codice sorgente");
        sourceCodeOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getController().openSourceCodeEditor(null);
            }
        });
        createReferenceButton.addToPopupMenu(sourceCodeOption);

        JMenuItem imageOption = new JMenuItem("Immagine");
        imageOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getController().openImageEditor(null);
            }
        });
        createReferenceButton.addToPopupMenu(imageOption);

        JMenuItem videoOption = new JMenuItem("Video");
        videoOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getController().openVideoEditor(null);
            }
        });
        createReferenceButton.addToPopupMenu(videoOption);

        updateReferenceButton = new JButton(new ImageIcon("images/file_edit.png"));
        updateReferenceButton.setToolTipText("Modifica riferimento");
        updateReferenceButton.setEnabled(false); // all'inizio teniamolo disattivato
        updateReferenceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeSelectedReference();
            }
        });

        removeReferenceButton = new JButton(new ImageIcon("images/file_remove.png"));
        removeReferenceButton.setToolTipText("Elimina riferimento");
        removeReferenceButton.setEnabled(false); // all'inizio teniamolo disattivato
        removeReferenceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeSelectedReference();
            }
        });

        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.add(createReferenceButton);
        toolbar.add(updateReferenceButton);
        toolbar.add(removeReferenceButton);
        referencePanel.add(toolbar, BorderLayout.NORTH);

        referenceInfoPanel = new ReferenceInfoPanel();
        referenceListPanel = new ReferenceListPanel();
        referenceListPanel.addReferenceSelectionListener(this);

        JSplitPane referenceSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, referenceListPanel, referenceInfoPanel);
        referenceSplitPane.setDividerSize(10);
        referenceSplitPane.setResizeWeight(0.6f);

        referencePanel.add(referenceSplitPane, BorderLayout.CENTER);

        return referencePanel;
    }

    private void changeSelectedReference() {
        BibliographicReference selectedReference = referenceListPanel.getSelectedReference();

        if (selectedReference == null)
            return;

        categoriesTreePanel.clearSelection();

        if (selectedReference instanceof Article)
            getController().openArticleEditor((Article) selectedReference);
        else if (selectedReference instanceof Book)
            getController().openBookEditor((Book) selectedReference);
        else if (selectedReference instanceof Image)
            getController().openImageEditor((Image) selectedReference);
        else if (selectedReference instanceof SourceCode)
            getController().openSourceCodeEditor((SourceCode) selectedReference);
        else if (selectedReference instanceof Thesis)
            getController().openThesisEditor((Thesis) selectedReference);
        else if (selectedReference instanceof Video)
            getController().openVideoEditor((Video) selectedReference);
        else if (selectedReference instanceof Website)
            getController().openWebsiteEditor((Website) selectedReference);
    }

    private void removeSelectedReference() {
        BibliographicReference selectedReference = referenceListPanel.getSelectedReference();

        if (selectedReference == null)
            return;

        int result = JOptionPane.showConfirmDialog(this, "Vuoi eliminare questo riferimento?", "Elimina riferimento", JOptionPane.YES_NO_OPTION);

        if (result != JOptionPane.YES_OPTION)
            return;

        try {
            getController().getReferenceRepository().remove(referenceListPanel.getSelectedReference());
            referenceListPanel.removeSelectedReference();
        } catch (ReferenceDatabaseException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errore salvataggio riferimento", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void onReferenceSelection(BibliographicReference reference) {
        referenceInfoPanel.showReference(reference);

        // se non è selezionato un riferimento, disattiviamo i pulsanti di modifica e rimozione
        updateReferenceButton.setEnabled(reference != null);
        removeReferenceButton.setEnabled(reference != null);
    }

    // #endregion

    // #region SEARCH

    @Override
    public void search(Search search) {
        categoriesTreePanel.clearSelection();

        try {
            List<BibliographicReference> references = getController().getReferenceRepository().get(search);
            referenceListPanel.setReferences(references);

            String searchResultMessage = references.size() == 1 ? "È stato trovato un riferimento." : "Sono stati trovati " + references.size() + " riferimenti.";
            JOptionPane.showMessageDialog(this, searchResultMessage, "Risultati ricerca", JOptionPane.INFORMATION_MESSAGE);
        } catch (ReferenceDatabaseException e) {
            referenceListPanel.clear();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errore recupero riferimenti", JOptionPane.ERROR_MESSAGE);
        }
    }

    // #endregion

    // #region USER

    private JPanel setupUserInfoPanel() {
        JPanel userInfoPanel = new JPanel();

        Color darkGray = Color.decode("#24292f");

        userInfoPanel.setLayout(new BorderLayout(5, 0));
        userInfoPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        userInfoPanel.setBackground(darkGray);

        userLabel = new JLabel();
        userLabel.setForeground(Color.WHITE);
        userLabel.setIcon(new ImageIcon("images/bookmark_light.png"));

        updateUserLabelText();

        logoutButton = new JButton(new ImageIcon("images/logout_white.png"));
        logoutButton.setToolTipText("Esci");
        logoutButton.setHorizontalAlignment(SwingConstants.RIGHT);
        logoutButton.setBackground(darkGray);
        logoutButton.setBorderPainted(false);
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        userInfoPanel.add(userLabel, BorderLayout.WEST);
        userInfoPanel.add(logoutButton, BorderLayout.EAST);

        return userInfoPanel;
    }

    private void updateUserLabelText() {
        // il nome dell'utente lo mettiamo in grassetto
        userLabel.setText("<html>Benvenuto, <b>" + getController().getUser() + "</b></html>");
    }

    private void logout() {
        getController().openLoginPage();
    }

    // #endregion

}
