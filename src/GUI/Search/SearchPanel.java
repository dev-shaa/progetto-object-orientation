package GUI.Search;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;

import Entities.Category;
import Entities.Search;
import Exceptions.Input.EmptySearchException;
import Exceptions.Input.InvalidInputException;
import GUI.Authors.AuthorInputField;
import GUI.Tags.TagInputField;
import GUI.Utilities.PopupCheckboxTree;
import GUI.Utilities.Tree.CustomTreeModel;

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

    private ArrayList<SearchListener> searchListeners;

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

        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.PAGE_AXIS));

        JScrollPane scrollPane = new JScrollPane(searchPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        tags = new TagInputField();
        authors = new AuthorInputField();
        categories = new PopupCheckboxTree<>(categoriesTree);
        dateFrom = new JDateChooser();
        dateTo = new JDateChooser();

        addFieldComponent("Parole chiave", tags, null);

        JLabel authorLabel = new JLabel("Autori");
        authorLabel.setMaximumSize(maximumSize);
        authorLabel.setAlignmentX(alignment);
        authors.setAlignmentX(alignment);
        searchPanel.add(authorLabel);
        searchPanel.add(authors);

        addFieldComponent("Categorie", categories, "Seleziona le categorie in cui cercare il riferimento.");
        addFieldComponent("Da", dateFrom, "Data di inizio dell'intervallo di ricerca.");
        addFieldComponent("A", dateTo, "Data di fine dell'intervallo di ricerca.");

        searchPanel.add(Box.createVerticalStrut(32));

        searchButton = new JButton("Cerca");
        searchButton.setIcon(new ImageIcon("images/search.png"));
        searchButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
        searchButton.setMaximumSize(new Dimension(100, 32));
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                notifyListeners();
            }
        });
        searchPanel.add(searchButton);

        add(new JLabel("<html><b>Ricerca riferimenti</b></html>"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Imposta l'albero delle categorie selezionabili.
     * 
     * @param categoriesTree
     */
    public void setCategoriesTree(CustomTreeModel<Category> categoriesTree) {
        categories.setTreeModel(categoriesTree);
    }

    /**
     * Resetta i campi di ricerca.
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
     * Se {@code listener == null} o se è già registrato, non viene aggiunto.
     * 
     * @param listener
     *            listener da aggiungere
     */
    public void addSearchListener(SearchListener listener) {
        if (listener == null)
            return;

        if (searchListeners == null)
            searchListeners = new ArrayList<>(1);

        if (!searchListeners.contains(listener))
            searchListeners.add(listener);
    }

    /**
     * Rimuove un listener dall'evento di ricerca.
     * Se {@code listener == null} o se non è registrato, non esegue nulla.
     * 
     * @param listener
     *            listener da rimuovere
     */
    public void removeSearchListener(SearchListener listener) {
        if (listener == null || searchListeners == null)
            return;

        searchListeners.remove(listener);
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

    private void notifyListeners() {
        try {
            Search search = getSearch();

            for (SearchListener listener : searchListeners)
                listener.onSearch(search);

            clear();
        } catch (InvalidInputException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private Search getSearch() throws EmptySearchException, InvalidInputException {
        return new Search(dateFrom.getDate(), dateTo.getDate(), tags.getTags(), categories.getCheckboxTree().getSelectedItems(), authors.getAuthors());
    }

}
