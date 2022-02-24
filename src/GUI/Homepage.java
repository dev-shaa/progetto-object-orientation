package GUI;

import Entities.*;
import Entities.References.BibliographicReference;
import Entities.References.OnlineResources.*;
import Entities.References.OnlineResources.Image;
import Entities.References.PhysicalResources.*;
import GUI.Categories.CategoriesTreePanel;
import GUI.Categories.CategorySelectionListener;
import GUI.References.*;
import GUI.Search.*;
import GUI.Utilities.PopupButton;
import GUI.Utilities.Tree.CustomTreeModel;
import Controller.Controller;
import Criteria.ReferenceCriteria;
import Criteria.ReferenceCriteriaCategory;
import Criteria.ReferenceCriteriaSearch;

import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
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
    private PopupButton createReferenceButton;
    private JButton updateReferenceButton;
    private JButton removeReferenceButton;

    private SearchPanel referenceSearchPanel;

    private Collection<? extends BibliographicReference> references;
    private ReferenceCriteria lastReferenceCriteriaUsed;

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
        referenceSearchPanel.addSearchListener(this);

        // JSplitPane ammette solo due pannelli
        // noi ne abbiamo tre, quindi dobbiamo creare due JSplitPane
        JSplitPane subSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, setupCategoriesPanel(), setupReferencesPanel());
        subSplitPane.setResizeWeight(0.15);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subSplitPane, referenceSearchPanel);
        splitPane.setResizeWeight(0.8);

        contentPane.add(splitPane, BorderLayout.CENTER);
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            referenceListPanel.clear();
            referenceInfoPanel.clear();
            referenceSearchPanel.clear();

            setLocationRelativeTo(null);
        }

        super.setVisible(b);
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

    private Controller getController() {
        return controller;
    }

    /**
     * Imposta l'albero delle categorie dell'utente da mostrare.
     * 
     * @param categoriesTree
     *            albero delle categorie
     */
    public void setTreeModel(CustomTreeModel<Category> categoriesTree) {
        categoriesTreePanel.setTreeModel(categoriesTree);
        referenceSearchPanel.setCategoriesTree(categoriesTree);
    }

    /**
     * Imposta tutti i riferimenti dell'utente da mostrare.
     * 
     * @param references
     *            riferimenti dell'utente
     */
    public void setReferences(Collection<? extends BibliographicReference> references) {
        this.references = references;
    }

    private Collection<? extends BibliographicReference> getReferences() {
        return references;
    }

    /**
     * Imposta il nome da mostrare come messaggio di benvenuto.
     * 
     * @param name
     *            nome da mostrare
     */
    public void setNameToDisplay(String name) {
        userLabel.setText("<html>Benvenuto, <b>" + name + "</b></html>");
    }

    private void setupCloseOperation() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (askConfirmToUser("Esci", "Sicuro di voler uscire?"))
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }

    private JPanel setupCategoriesPanel() {
        JPanel categoriesPanel = new JPanel();

        categoriesPanel.setLayout(new BorderLayout(5, 5));
        categoriesPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        categoriesPanel.add(toolbar, BorderLayout.NORTH);

        createCategoryButton = new JButton(new ImageIcon("images/folder_add.png"));
        createCategoryButton.setToolTipText("Crea nuova categoria");
        createCategoryButton.setEnabled(false);
        createCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = getCategoryNameFromUser("Nuova categoria");

                if (name != null) {
                    Category parent = categoriesTreePanel.getSelectedCategory();
                    Category newCategory = new Category(name, parent);
                    getController().addCategory(newCategory);
                }
            }
        });
        toolbar.add(createCategoryButton);

        updateCategoryButton = new JButton(new ImageIcon("images/folder_edit.png"));
        updateCategoryButton.setToolTipText("Modifica categoria selezionata");
        updateCategoryButton.setEnabled(false);
        updateCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Category selectedCategory = categoriesTreePanel.getSelectedCategory();

                if (selectedCategory != null) {
                    String newName = getCategoryNameFromUser(selectedCategory.getName());

                    if (newName != null)
                        getController().updateCategory(selectedCategory, newName);
                }
            }
        });
        toolbar.add(updateCategoryButton);

        removeCategoryButton = new JButton(new ImageIcon("images/folder_delete.png"));
        removeCategoryButton.setToolTipText("Elimina categoria selezionata");
        removeCategoryButton.setEnabled(false);
        removeCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Category selectedCategory = categoriesTreePanel.getSelectedCategory();

                if (selectedCategory != null && askConfirmToUser("Elimina categoria", "Sicuro di voler eliminare questa categoria?"))
                    getController().removeCategory(selectedCategory);
            }
        });
        toolbar.add(removeCategoryButton);

        categoriesTreePanel = new CategoriesTreePanel();
        categoriesTreePanel.addSelectionListener(this);

        categoriesPanel.add(categoriesTreePanel, BorderLayout.CENTER);

        return categoriesPanel;

    }

    private JPanel setupReferencesPanel() {
        JPanel referencePanel = new JPanel();

        referencePanel.setLayout(new BorderLayout(5, 5));
        referencePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        referencePanel.add(toolbar, BorderLayout.NORTH);

        createReferenceButton = setupCreateReferenceButton();
        toolbar.add(createReferenceButton);

        updateReferenceButton = new JButton(new ImageIcon("images/file_edit.png"));
        updateReferenceButton.setToolTipText("Modifica riferimento");
        updateReferenceButton.setEnabled(false); // all'inizio teniamolo disattivato
        updateReferenceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeSelectedReference();
            }
        });
        toolbar.add(updateReferenceButton);

        removeReferenceButton = new JButton(new ImageIcon("images/file_remove.png"));
        removeReferenceButton.setToolTipText("Elimina riferimento");
        removeReferenceButton.setEnabled(false); // all'inizio teniamolo disattivato
        removeReferenceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BibliographicReference selectedReference = referenceListPanel.getSelectedReference();

                if (selectedReference != null && askConfirmToUser("Elimina riferimento", "Vuoi eliminare questo riferimento?")) {
                    getController().removeReference(selectedReference);
                }
            }
        });
        toolbar.add(removeReferenceButton);

        referenceInfoPanel = new ReferenceInfoPanel();
        referenceListPanel = new ReferenceListPanel();
        referenceListPanel.addReferenceSelectionListener(this);

        JSplitPane referenceSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, referenceListPanel, referenceInfoPanel);
        referenceSplitPane.setDividerSize(10);
        referenceSplitPane.setResizeWeight(0.6f);

        referencePanel.add(referenceSplitPane, BorderLayout.CENTER);

        return referencePanel;
    }

    private JPanel setupUserInfoPanel() {
        JPanel userInfoPanel = new JPanel();

        Color darkGray = Color.decode("#24292f");

        userInfoPanel.setLayout(new BorderLayout(5, 0));
        userInfoPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        userInfoPanel.setBackground(darkGray);

        userLabel = new JLabel();
        userLabel.setForeground(Color.WHITE);
        userLabel.setIcon(new ImageIcon("images/bookmark_light.png"));

        setNameToDisplay(null);
        userInfoPanel.add(userLabel, BorderLayout.WEST);

        logoutButton = new JButton(new ImageIcon("images/logout_white.png"));
        logoutButton.setToolTipText("Esci");
        logoutButton.setHorizontalAlignment(SwingConstants.RIGHT);
        logoutButton.setBackground(darkGray);
        logoutButton.setBorderPainted(false);
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getController().openLoginPage();
            }
        });

        userInfoPanel.add(logoutButton, BorderLayout.EAST);

        return userInfoPanel;
    }

    private PopupButton setupCreateReferenceButton() {
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

        return createReferenceButton;
    }

    @Override
    public void onCategorySelection(Category category) {
        createCategoryButton.setEnabled(true);
        updateCategoryButton.setEnabled(category != null);
        removeCategoryButton.setEnabled(category != null);

        setReferencesToShow(new ReferenceCriteriaCategory(category));
    }

    @Override
    public void onCategoryDeselection() {
        createCategoryButton.setEnabled(false);
        updateCategoryButton.setEnabled(false);
        removeCategoryButton.setEnabled(false);

        referenceListPanel.clear();
        referenceInfoPanel.clear();
    }

    private void changeSelectedReference() {
        BibliographicReference selectedReference = referenceListPanel.getSelectedReference();

        if (selectedReference == null)
            return;

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

    @Override
    public void onReferenceSelection(BibliographicReference reference) {
        referenceInfoPanel.showReference(reference);

        // se non è selezionato un riferimento, disattiviamo i pulsanti di modifica e rimozione
        boolean shouldButtonsBeEnabled = reference != null;
        updateReferenceButton.setEnabled(shouldButtonsBeEnabled);
        removeReferenceButton.setEnabled(shouldButtonsBeEnabled);
    }

    @Override
    public void onSearch(Search search) {
        categoriesTreePanel.clearSelection();

        setReferencesToShow(new ReferenceCriteriaSearch(search));
    }

    private boolean askConfirmToUser(String title, String message) {
        int confirmDialogBoxOption = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
        return confirmDialogBoxOption == JOptionPane.YES_OPTION;
    }

    private String getCategoryNameFromUser(String defaultName) {
        return (String) JOptionPane.showInputDialog(this, "Inserisci il nuovo nome della categoria", "Nuova categoria", JOptionPane.PLAIN_MESSAGE, null, null, defaultName);
    }

    /**
     * Mostra un messaggio di errore.
     * 
     * @param title
     *            titolo della finestra di dialogo
     * @param message
     *            messaggio da mostrare
     */
    public void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Ricarica i riferimenti mostrati.
     */
    public void reloadReferences() {
        if (lastReferenceCriteriaUsed == null)
            return;

        setReferencesToShow(lastReferenceCriteriaUsed);
    }

    private void setReferencesToShow(ReferenceCriteria criteria) {
        if (criteria == null)
            throw new IllegalArgumentException("criteria can't be null");

        List<? extends BibliographicReference> filteredReferences = criteria.filter(getReferences());
        referenceListPanel.setReferences(filteredReferences);
        lastReferenceCriteriaUsed = criteria;
    }

}
