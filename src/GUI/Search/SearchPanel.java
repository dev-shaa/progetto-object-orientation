package GUI.Search;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.EventListenerList;
import com.toedter.calendar.JDateChooser;
import Entities.Category;
import Entities.Search;
import Exceptions.Input.EmptySearchException;
import Exceptions.Input.InvalidInputException;
import GUI.Authors.AuthorInputField;
import GUI.Tags.TagInputField;
import Utilities.MessageDisplayer;
import Utilities.Tree.CustomTreeModel;
import Utilities.Tree.CheckboxTree.CheckboxTree;
import io.codeworth.panelmatic.PanelBuilder;
import io.codeworth.panelmatic.PanelMatic;
import io.codeworth.panelmatic.componentbehavior.Modifiers;

/**
 * Pannello per la ricerca dei riferimenti per parole chiave, autori, categorie e data.
 */
public class SearchPanel extends JPanel {

    private TagInputField tags;
    private AuthorInputField authors;
    private CheckboxTree<Category> categories;
    private JDateChooser dateFrom;
    private JDateChooser dateTo;

    private EventListenerList searchListeners;

    /**
     * Crea un pannello per la ricerca di riferimenti.
     */
    public SearchPanel() {
        this(null);
    }

    /**
     * Crea un pannello per la ricerca di riferimenti.
     * 
     * @param categoriesTree
     *            albero delle categorie selezionabili
     */
    public SearchPanel(CustomTreeModel<Category> categoriesTree) {
        super();
        setup(categoriesTree);
        searchListeners = new EventListenerList();
    }

    /**
     * Imposta l'albero delle categorie selezionabili (può essere {@code null}).
     * 
     * @param categoriesTree
     *            albero delle categorie selezionabili
     */
    public void setCategoriesTree(CustomTreeModel<Category> categoriesTree) {
        categories.setModel(categoriesTree);

        for (int i = 0; i < categories.getRowCount(); i++)
            categories.expandRow(i);
    }

    /**
     * Reimposta i campi di ricerca.
     */
    public void clear() {
        tags.clear();
        authors.clear();
        categories.clearSelection();
        dateFrom.setDate(null);
        dateTo.setDate(null);
    }

    /**
     * Aggiunge un listener all'evento di ricerca.
     * 
     * @param listener
     *            listener da aggiungere
     */
    public void addSearchListener(SearchListener listener) {
        searchListeners.add(SearchListener.class, listener);
    }

    /**
     * Rimuove un listener dall'evento di ricerca.
     * 
     * @param listener
     *            listener da rimuovere
     */
    public void removeSearchListener(SearchListener listener) {
        searchListeners.remove(SearchListener.class, listener);
    }

    private void setup(CustomTreeModel<Category> treeModel) {
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(scrollPane);

        PanelBuilder panelBuilder = PanelMatic.begin(panel);

        tags = new TagInputField();
        panelBuilder.add(new JLabel("Parole chiave"));
        panelBuilder.add(tags);

        authors = new AuthorInputField();
        panelBuilder.add(new JLabel("Autori"));
        panelBuilder.add(authors);

        categories = new CheckboxTree<>(treeModel);
        categories.setRootVisible(false);
        panelBuilder.add(new JLabel("Categorie"));
        panelBuilder.add(new JScrollPane(categories));

        dateFrom = new JDateChooser();
        panelBuilder.add(new JLabel("Da"));
        panelBuilder.add(dateFrom);

        dateTo = new JDateChooser();
        panelBuilder.add(new JLabel("A"));
        panelBuilder.add(dateTo);

        panelBuilder.addFlexibleSpace();

        JButton searchButton = new JButton("Cerca");
        searchButton.addActionListener((e) -> search());
        panelBuilder.add(searchButton, Modifiers.L_END, Modifiers.P_FEET);

        panelBuilder.addFlexibleSpace();
        panelBuilder.get();
    }

    private void search() {
        try {
            fireSearchEvent(getSearch());
            clear();
        } catch (InvalidInputException ex) {
            MessageDisplayer.showErrorMessage("Errore ricerca", ex.getMessage());
        }
    }

    private void fireSearchEvent(Search search) {
        SearchListener[] listeners = searchListeners.getListeners(SearchListener.class);

        for (SearchListener listener : listeners)
            listener.onSearch(search);
    }

    private Search getSearch() throws EmptySearchException, InvalidInputException {
        return new Search(dateFrom.getDate(), dateTo.getDate(), tags.getTags(), categories.getSelectedItems(), authors.getAuthors());
    }

}