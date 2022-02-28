package GUI.Search;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.EventListenerList;
import com.toedter.calendar.JDateChooser;
import Entities.Category;
import Entities.Search;
import Exceptions.Input.EmptySearchException;
import Exceptions.Input.InvalidInputException;
import GUI.Authors.AuthorInputField;
import GUI.Tags.TagInputField;
import GUI.Utilities.CheckboxTree.PopupCheckboxTree;
import Utilities.Tree.CustomTreeModel;

/**
 * Pannello per la ricerca dei riferimenti per parole chiave, autori, categorie e data.
 */
public class SearchPanel extends JPanel {

    private TagInputField tags;
    private AuthorInputField authors;
    private PopupCheckboxTree<Category> categories;
    private JDateChooser dateFrom;
    private JDateChooser dateTo;
    private JButton searchButton;
    private JPanel searchPanel;

    private EventListenerList searchListeners;

    private final Dimension maximumSize = new Dimension(Integer.MAX_VALUE, 24);
    private final float alignment = Container.LEFT_ALIGNMENT;

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
        categories.setTreeModel(categoriesTree);
    }

    /**
     * Reimposta i campi di ricerca.
     */
    public void clear() {
        tags.clear();
        authors.clear();
        categories.getCheckboxTree().clearSelection();
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
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.PAGE_AXIS));

        JScrollPane scrollPane = new JScrollPane(searchPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        tags = new TagInputField();
        addFieldComponent("Parole chiave", tags, null);

        authors = new AuthorInputField();
        JLabel authorLabel = new JLabel("Autori");
        authorLabel.setMaximumSize(maximumSize);
        authorLabel.setAlignmentX(alignment);
        authors.setAlignmentX(alignment);
        searchPanel.add(authorLabel);
        searchPanel.add(authors);

        categories = new PopupCheckboxTree<>();
        addFieldComponent("Categorie", categories, "Seleziona le categorie in cui cercare il riferimento.");

        dateFrom = new JDateChooser();
        addFieldComponent("Da", dateFrom, "Data di inizio dell'intervallo di ricerca.");

        dateTo = new JDateChooser();
        addFieldComponent("A", dateTo, "Data di fine dell'intervallo di ricerca.");

        searchPanel.add(Box.createVerticalStrut(32));

        searchButton = new JButton("Cerca");
        searchButton.setIcon(new ImageIcon("images/search.png"));
        searchButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
        searchButton.setMaximumSize(new Dimension(100, 32));
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Search search = getSearch();
                    fireSearchEvent(search);
                    clear();
                } catch (InvalidInputException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });
        searchPanel.add(searchButton);

        add(new JLabel("<html><b>Ricerca riferimenti</b></html>"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void fireSearchEvent(Search search) {
        SearchListener[] listeners = searchListeners.getListeners(SearchListener.class);

        for (SearchListener listener : listeners)
            listener.onSearch(search);
    }

    private void addFieldComponent(String label, JComponent component, String tooltip) {
        JLabel labelField = new JLabel(label);
        labelField.setMaximumSize(maximumSize);
        labelField.setAlignmentX(alignment);
        searchPanel.add(labelField);

        component.setMaximumSize(maximumSize);
        component.setAlignmentX(alignment);

        if (tooltip != null)
            component.setToolTipText(tooltip);

        searchPanel.add(component);
        searchPanel.add(Box.createVerticalStrut(5));
    }

    private Search getSearch() throws EmptySearchException, InvalidInputException {
        return new Search(dateFrom.getDate(), dateTo.getDate(), tags.getTags(), categories.getCheckboxTree().getSelectedItems(), authors.getAuthors());
    }

}