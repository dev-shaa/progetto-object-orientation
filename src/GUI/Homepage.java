package GUI;

import Entities.*;
import Entities.References.BibliographicReference;
import Entities.References.OnlineResources.*;
import Entities.References.OnlineResources.Image;
import Entities.References.PhysicalResources.*;
import Exceptions.Input.InvalidInputException;
import Utilities.MessageDisplayer;
import Utilities.Criteria.*;
import Utilities.Table.CustomTable;
import Utilities.Table.CustomTableSelectionListener;
import Utilities.Tree.*;
import Utilities.Tree.CheckboxTree.CheckboxTree;
import io.codeworth.panelmatic.PanelBuilder;
import io.codeworth.panelmatic.PanelMatic;
import io.codeworth.panelmatic.componentbehavior.Modifiers;
import Controller.Controller;

import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import java.util.List;
import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.tree.TreeSelectionModel;

import com.toedter.calendar.JDateChooser;

/**
 * Pagina principale dell'applicativo.
 * <p>
 * Mostra e offre opzioni per gestire le categorie e i riferimenti dell'utente.
 */
public class Homepage extends JFrame implements CustomTreeItemSelectionListener<Category>, CustomTableSelectionListener<BibliographicReference> {

    private Controller controller;

    private JMenuItem createCategoryButton;
    private JMenuItem updateCategoryButton;
    private JMenuItem removeCategoryButton;
    private JMenuItem updateReferenceButton;
    private JMenuItem removeReferenceButton;

    private CustomTree<Category> categoriesTree;
    private CustomTable<BibliographicReference> referencesTable;
    private JTextArea referenceInfoTextArea;

    private TagInputField tagsSearchField;
    private AuthorInputField authorsSearchField;
    private CheckboxTree<Category> categoriesSearchField;
    private JDateChooser dateFromSearchField;
    private JDateChooser dateToSearchField;

    private Collection<? extends BibliographicReference> allReferences;
    private Criteria<BibliographicReference> lastReferenceCriteriaUsed;

    /**
     * Crea una nuova pagina principale con il controller indicato.
     * 
     * @param controller
     *            controller della GUI
     * @throws IllegalArgumentException
     *             se {@code controller == null}
     */
    public Homepage(Controller controller) {
        super();
        setController(controller);
        setup();
    }

    @Override
    public void setVisible(boolean b) {
        if (b)
            reset();

        super.setVisible(b);
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

    /**
     * Imposta l'albero delle categorie dell'utente da mostrare.
     * 
     * @param categoriesTreeModel
     *            albero delle categorie
     */
    public void setCategoriesTreeModel(CustomTreeModel<Category> categoriesTreeModel) {
        categoriesTree.setModel(categoriesTreeModel);
        categoriesSearchField.setModel(categoriesTreeModel);
    }

    /**
     * Imposta tutti i riferimenti dell'utente da mostrare.
     * 
     * @param references
     *            riferimenti dell'utente
     */
    public void setReferences(Collection<? extends BibliographicReference> references) {
        this.allReferences = references;
    }

    /**
     * Ricarica gli ultimi riferimenti mostrati.
     */
    public void reloadReferences() {
        if (lastReferenceCriteriaUsed == null)
            return;

        filterShownReferences(lastReferenceCriteriaUsed);
    }

    /**
     * Reimposta la pagina.
     */
    public void reset() {
        referencesTable.clear();
        referenceInfoTextArea.setText(null);
        categoriesTree.expandAllRows();
        resetSearchField();
        setLocationRelativeTo(null);
    }

    /**
     * Reimposta i campi di ricerca.
     */
    public void resetSearchField() {
        tagsSearchField.clear();
        authorsSearchField.clear();
        categoriesSearchField.clearSelection();
        categoriesSearchField.expandAllRows();
        dateFromSearchField.setDate(null);
        dateFromSearchField.setDate(null);
    }

    // #region SETUP

    private void setup() {
        setTitle("Pagina principale");
        setMinimumSize(new Dimension(400, 400));
        setSize(800, 600);
        setupCloseOperation();

        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        setJMenuBar(setupMenuBar());

        // JSplitPane ammette solo due pannelli
        // noi ne abbiamo tre, quindi dobbiamo creare due JSplitPane
        JSplitPane subSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, setupCategoriesPanel(), setupReferencesPanel());
        subSplitPane.setResizeWeight(0.15);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subSplitPane, setupSearchPanel());
        splitPane.setResizeWeight(0.8);
        splitPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        contentPane.add(splitPane, BorderLayout.CENTER);
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

    private JScrollPane setupCategoriesPanel() {
        categoriesTree = new CustomTree<Category>(null);
        categoriesTree.setEditable(false);
        categoriesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        categoriesTree.addTreeItemSelectionListener(this);

        return new JScrollPane(categoriesTree);
    }

    private JSplitPane setupReferencesPanel() {
        referencesTable = new CustomTable<>(new ReferenceTableModel());
        referencesTable.setAutoCreateRowSorter(true);
        referencesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        referencesTable.addItemSelectionListener(this);

        referenceInfoTextArea = new JTextArea();
        referenceInfoTextArea.setRows(10);
        referenceInfoTextArea.setEditable(false);

        JSplitPane referenceSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(referencesTable), new JScrollPane(referenceInfoTextArea));
        referenceSplitPane.setDividerSize(10);
        referenceSplitPane.setResizeWeight(0.6f);

        return referenceSplitPane;
    }

    private JScrollPane setupSearchPanel() {
        JPanel panel = new JPanel();

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        PanelBuilder panelBuilder = PanelMatic.begin(panel);

        tagsSearchField = new TagInputField();
        panelBuilder.add(new JLabel("Parole chiave"));
        panelBuilder.add(tagsSearchField);

        authorsSearchField = new AuthorInputField();
        panelBuilder.add(new JLabel("Autori"));
        panelBuilder.add(authorsSearchField);

        categoriesSearchField = new CheckboxTree<>();
        categoriesSearchField.setRootVisible(false);
        categoriesSearchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 128));
        panelBuilder.add(new JLabel("Categorie"));
        panelBuilder.add(new JScrollPane(categoriesSearchField));

        dateFromSearchField = new JDateChooser();
        panelBuilder.add(new JLabel("Da"));
        panelBuilder.add(dateFromSearchField);

        dateToSearchField = new JDateChooser();
        panelBuilder.add(new JLabel("A"));
        panelBuilder.add(dateToSearchField);

        panelBuilder.addFlexibleSpace();

        JButton searchButton = new JButton("Cerca");
        searchButton.addActionListener((e) -> search());
        panelBuilder.add(searchButton, Modifiers.L_END, Modifiers.P_FEET);

        panelBuilder.addFlexibleSpace();
        panelBuilder.get();

        return scrollPane;
    }

    private JMenuBar setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(setupCategoriesMenu());
        menuBar.add(setupReferencesMenu());
        menuBar.add(Box.createHorizontalGlue());

        JButton logoutButton = new JButton("Esci");
        logoutButton.setBorderPainted(false);
        logoutButton.addActionListener((e) -> controller.logout());
        menuBar.add(logoutButton);

        return menuBar;
    }

    private JMenu setupCategoriesMenu() {
        JMenu categoriesMenu = new JMenu("Categorie");

        createCategoryButton = new JMenuItem("Aggiungi categoria");
        createCategoryButton.setIcon(new ImageIcon("images/folder_add.png"));
        createCategoryButton.setEnabled(false);
        createCategoryButton.setToolTipText("Aggiunge una nuova categoria come sottocategoria di quella selezionata.");
        createCategoryButton.addActionListener((e) -> createCategory());
        categoriesMenu.add(createCategoryButton);

        updateCategoryButton = new JMenuItem("Modifica categoria");
        updateCategoryButton.setIcon(new ImageIcon("images/folder_edit.png"));
        updateCategoryButton.setEnabled(false);
        updateCategoryButton.setToolTipText("Modifica la categoria selezionata.");
        updateCategoryButton.addActionListener((e) -> changeSelectedCategory());
        categoriesMenu.add(updateCategoryButton);

        removeCategoryButton = new JMenuItem("Elimina categoria");
        removeCategoryButton.setIcon(new ImageIcon("images/folder_delete.png"));
        removeCategoryButton.setEnabled(false);
        removeCategoryButton.setToolTipText("Rimuovi la categoria selezionata e tutte le sue sottocategorie.\nI riferimenti contenuti in esse non verranno eliminati.");
        removeCategoryButton.addActionListener((e) -> removeSelectedCategory());
        categoriesMenu.add(removeCategoryButton);

        return categoriesMenu;
    }

    private JMenu setupReferencesMenu() {
        JMenu referencesMenu = new JMenu("Riferimenti");

        JMenu createReferenceMenu = new JMenu("Aggiungi riferimento");
        createReferenceMenu.setIcon(new ImageIcon("images/file_add.png"));
        createReferenceMenu.setToolTipText("Crea un nuovo riferimento.");
        referencesMenu.add(createReferenceMenu);

        JMenuItem articleOption = new JMenuItem("Articolo");
        articleOption.addActionListener((e) -> controller.openArticleEditor(null));
        createReferenceMenu.add(articleOption);

        JMenuItem bookOption = new JMenuItem("Libro");
        bookOption.addActionListener((e) -> controller.openBookEditor(null));
        createReferenceMenu.add(bookOption);

        JMenuItem thesisOption = new JMenuItem("Tesi");
        thesisOption.addActionListener((e) -> controller.openThesisEditor(null));
        createReferenceMenu.add(thesisOption);

        createReferenceMenu.addSeparator();

        JMenuItem websiteOption = new JMenuItem("Sito web");
        websiteOption.addActionListener((e) -> controller.openWebsiteEditor(null));
        createReferenceMenu.add(websiteOption);

        JMenuItem sourceCodeOption = new JMenuItem("Codice sorgente");
        sourceCodeOption.addActionListener((e) -> controller.openSourceCodeEditor(null));
        createReferenceMenu.add(sourceCodeOption);

        JMenuItem imageOption = new JMenuItem("Immagine");
        imageOption.addActionListener((e) -> controller.openImageEditor(null));
        createReferenceMenu.add(imageOption);

        JMenuItem videoOption = new JMenuItem("Video");
        videoOption.addActionListener((e) -> controller.openVideoEditor(null));
        createReferenceMenu.add(videoOption);

        updateReferenceButton = new JMenuItem("Modifica riferimento");
        updateReferenceButton.setIcon(new ImageIcon("images/file_edit.png"));
        updateReferenceButton.setEnabled(false);
        updateReferenceButton.setToolTipText("Modifica il riferimento selezionato.");
        updateReferenceButton.addActionListener((e) -> changeSelectedReference());
        referencesMenu.add(updateReferenceButton);

        removeReferenceButton = new JMenuItem("Elimina riferimento");
        removeReferenceButton.setIcon(new ImageIcon("images/file_remove.png"));
        removeReferenceButton.setEnabled(false);
        removeReferenceButton.setToolTipText("Elimina il riferimento selezionato.");
        removeReferenceButton.addActionListener((e) -> removeSelectedReference());
        referencesMenu.add(removeReferenceButton);

        return referencesMenu;
    }

    // #endregion

    private void createCategory() {
        String name = getCategoryNameFromUser("Nuova categoria");

        if (name != null) {
            Category parent = categoriesTree.getSelectedItem();
            Category newCategory = new Category(name, parent);
            controller.addCategory(newCategory);
            categoriesSearchField.expandAllRows();
        }
    }

    private void changeSelectedCategory() {
        Category selectedCategory = categoriesTree.getSelectedItem();

        if (selectedCategory != null) {
            String newName = getCategoryNameFromUser(selectedCategory.getName());

            if (newName != null) {
                controller.updateCategory(selectedCategory, newName); // FIXME: non viene aggiornato l'albero della ricerca
                categoriesSearchField.expandAllRows();
            }
        }
    }

    private void removeSelectedCategory() {
        Category selectedCategory = categoriesTree.getSelectedItem();

        if (selectedCategory != null && askConfirmToUser("Elimina categoria", "Sicuro di voler eliminare questa categoria?"))
            controller.removeCategory(selectedCategory);
    }

    private void changeSelectedReference() {
        BibliographicReference selectedReference = referencesTable.getSelectedItem();

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

    private void removeSelectedReference() {
        BibliographicReference selectedReference = referencesTable.getSelectedItem();

        if (selectedReference != null && askConfirmToUser("Elimina riferimento", "Vuoi eliminare questo riferimento?"))
            controller.removeReference(selectedReference);
    }

    private String getCategoryNameFromUser(String defaultName) {
        return (String) JOptionPane.showInputDialog(this, "Inserisci il nuovo nome della categoria", "Nuova categoria", JOptionPane.PLAIN_MESSAGE, null, null, defaultName);
    }

    private boolean askConfirmToUser(String title, String message) {
        int confirmDialogBoxOption = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
        return confirmDialogBoxOption == JOptionPane.YES_OPTION;
    }

    private void filterShownReferences(Criteria<BibliographicReference> criteria) {
        if (criteria == null)
            throw new IllegalArgumentException("criteria can't be null");

        List<? extends BibliographicReference> filteredReferences = criteria.filter(allReferences);
        referencesTable.setItems(filteredReferences);
        lastReferenceCriteriaUsed = criteria;
    }

    @Override
    public void onTreeItemSelection(Category selectedCategory) {
        createCategoryButton.setEnabled(true);
        updateCategoryButton.setEnabled(selectedCategory != null);
        removeCategoryButton.setEnabled(selectedCategory != null);

        filterShownReferences(new ReferenceCriteriaCategory(selectedCategory));
    }

    @Override
    public void onTreeItemDeselection() {
        createCategoryButton.setEnabled(false);
        updateCategoryButton.setEnabled(false);
        removeCategoryButton.setEnabled(false);

        referencesTable.clear();
        referenceInfoTextArea.setText(null);
    }

    @Override
    public void onTableItemSelection(BibliographicReference selectedReference) {
        referenceInfoTextArea.setText(selectedReference == null ? null : selectedReference.getInfo());

        // se non è selezionato un riferimento, disattiviamo i pulsanti di modifica e rimozione
        boolean shouldButtonsBeEnabled = selectedReference != null;
        updateReferenceButton.setEnabled(shouldButtonsBeEnabled);
        removeReferenceButton.setEnabled(shouldButtonsBeEnabled);
    }

    private void search() {
        try {
            Search search = new Search(dateFromSearchField.getDate(),
                    dateToSearchField.getDate(),
                    tagsSearchField.getTags(),
                    categoriesSearchField.getSelectedItems(),
                    authorsSearchField.getAuthors());

            categoriesTree.clearSelection();
            filterShownReferences(new ReferenceCriteriaSearch(search));
            resetSearchField();
        } catch (InvalidInputException ex) {
            MessageDisplayer.showErrorMessage("Errore ricerca", ex.getMessage());
        }
    }

}