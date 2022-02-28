package GUI;

import Entities.*;
import Entities.References.BibliographicReference;
import Entities.References.OnlineResources.*;
import Entities.References.OnlineResources.Image;
import Entities.References.PhysicalResources.*;
import GUI.Categories.CategoriesTreePanel;
import GUI.Categories.CategorySelectionListener;
import GUI.References.List.*;
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
    private JTextArea referenceInfoTextArea;
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
        setup();
    }

    /**
     * Imposta il controller della pagina principale.
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

    @Override
    public void setVisible(boolean b) {
        if (b) {
            referenceListPanel.clear();
            referenceSearchPanel.clear();
            referenceInfoTextArea.setText(null);

            setLocationRelativeTo(null);
        }

        super.setVisible(b);
    }

    /**
     * Imposta l'albero delle categorie dell'utente da mostrare.
     * 
     * @param categoriesTree
     *            albero delle categorie
     */
    public void setCategoriesTreeModel(CustomTreeModel<Category> categoriesTree) {
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

    /**
     * Imposta il nome da mostrare come messaggio di benvenuto.
     * 
     * @param name
     *            nome da mostrare
     */
    public void setNameToDisplay(String name) {
        userLabel.setText("<html>Benvenuto, <b>" + name + "</b></html>");
    }

    /**
     * Inizializza la pagina.
     */
    private void setup() {
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

    /**
     * Imposta l'operazione di chiusura.
     */
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

    /**
     * Inizializza il pannello delle categorie.
     * 
     * @return pannello inizializzato
     */
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
                    controller.addCategory(newCategory);
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
                        controller.updateCategory(selectedCategory, newName);
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
                    controller.removeCategory(selectedCategory);
            }
        });
        toolbar.add(removeCategoryButton);

        categoriesTreePanel = new CategoriesTreePanel();
        categoriesTreePanel.addCategorySelectionListener(this);

        categoriesPanel.add(categoriesTreePanel, BorderLayout.CENTER);

        return categoriesPanel;
    }

    /**
     * Inizializza il pannello dei riferimenti.
     * 
     * @return pannello inizializzato
     */
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
                    controller.removeReference(selectedReference);
                }
            }
        });
        toolbar.add(removeReferenceButton);

        referenceInfoTextArea = new JTextArea();
        referenceInfoTextArea.setRows(10);
        referenceInfoTextArea.setEditable(false);
        JScrollPane referenceInfoScrollPane = new JScrollPane(referenceInfoTextArea);

        referenceListPanel = new ReferenceListPanel();
        referenceListPanel.addReferenceSelectionListener(this);

        JSplitPane referenceSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, referenceListPanel, referenceInfoScrollPane);
        referenceSplitPane.setDividerSize(10);
        referenceSplitPane.setResizeWeight(0.6f);

        referencePanel.add(referenceSplitPane, BorderLayout.CENTER);

        return referencePanel;
    }

    /**
     * Inizializza il pannello delle informazioni dell'utente.
     * 
     * @return pannello inizializzato
     */
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
                controller.logout();
            }
        });

        userInfoPanel.add(logoutButton, BorderLayout.EAST);

        return userInfoPanel;
    }

    /**
     * Inizializza il pulsante per l'apertura degli editor.
     * 
     * @return pulsante inizializzato
     */
    private PopupButton setupCreateReferenceButton() {
        PopupButton createReferenceButton = new PopupButton(new ImageIcon("images/file_add.png"));
        createReferenceButton.setToolTipText("Crea riferimento");

        JMenuItem articleOption = new JMenuItem("Articolo");
        articleOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.openArticleEditor(null);
            }
        });
        createReferenceButton.addToPopupMenu(articleOption);

        JMenuItem bookOption = new JMenuItem("Libro");
        bookOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.openBookEditor(null);
            }
        });
        createReferenceButton.addToPopupMenu(bookOption);

        JMenuItem thesisOption = new JMenuItem("Tesi");
        thesisOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.openThesisEditor(null);
            }
        });
        createReferenceButton.addToPopupMenu(thesisOption);
        createReferenceButton.addPopupSeparator();

        JMenuItem websiteOption = new JMenuItem("Sito web");
        websiteOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.openWebsiteEditor(null);
            }
        });
        createReferenceButton.addToPopupMenu(websiteOption);

        JMenuItem sourceCodeOption = new JMenuItem("Codice sorgente");
        sourceCodeOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.openSourceCodeEditor(null);
            }
        });
        createReferenceButton.addToPopupMenu(sourceCodeOption);

        JMenuItem imageOption = new JMenuItem("Immagine");
        imageOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.openImageEditor(null);
            }
        });
        createReferenceButton.addToPopupMenu(imageOption);

        JMenuItem videoOption = new JMenuItem("Video");
        videoOption.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.openVideoEditor(null);
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
        referenceInfoTextArea.setText(null);
    }

    @Override
    public void onReferenceSelection(BibliographicReference reference) {
        referenceInfoTextArea.setText(reference == null ? null : reference.getInfo());

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

    /**
     * Ricarica gli ultimi riferimenti mostrati.
     */
    public void reloadReferences() {
        if (lastReferenceCriteriaUsed == null)
            return;

        setReferencesToShow(lastReferenceCriteriaUsed);
    }

    private void changeSelectedReference() {
        BibliographicReference selectedReference = referenceListPanel.getSelectedReference();

        if (selectedReference == null)
            return;

        if (selectedReference instanceof Article)
            controller.openArticleEditor((Article) selectedReference);
        else if (selectedReference instanceof Book)
            controller.openBookEditor((Book) selectedReference);
        else if (selectedReference instanceof Image)
            controller.openImageEditor((Image) selectedReference);
        else if (selectedReference instanceof SourceCode)
            controller.openSourceCodeEditor((SourceCode) selectedReference);
        else if (selectedReference instanceof Thesis)
            controller.openThesisEditor((Thesis) selectedReference);
        else if (selectedReference instanceof Video)
            controller.openVideoEditor((Video) selectedReference);
        else if (selectedReference instanceof Website)
            controller.openWebsiteEditor((Website) selectedReference);
    }

    private String getCategoryNameFromUser(String defaultName) {
        return (String) JOptionPane.showInputDialog(this, "Inserisci il nuovo nome della categoria", "Nuova categoria", JOptionPane.PLAIN_MESSAGE, null, null, defaultName);
    }

    private boolean askConfirmToUser(String title, String message) {
        int confirmDialogBoxOption = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
        return confirmDialogBoxOption == JOptionPane.YES_OPTION;
    }

    private void setReferencesToShow(ReferenceCriteria criteria) {
        if (criteria == null)
            throw new IllegalArgumentException("criteria can't be null");

        List<? extends BibliographicReference> filteredReferences = criteria.filter(references);
        referenceListPanel.setReferences(filteredReferences);
        lastReferenceCriteriaUsed = criteria;
    }

}