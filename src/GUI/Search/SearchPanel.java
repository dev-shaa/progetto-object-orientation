package GUI.Search;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;
import com.toedter.calendar.JDateChooser;

import Entities.Category;
import Entities.Search;
import Exceptions.EmptySearchException;
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

    private final String searchFieldSeparator = ",";
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
     * @param treeModel
     *            albero delle categorie selezionabili
     */
    public SearchPanel(CustomTreeModel<Category> treeModel) {
        super();

        categories = new PopupCheckboxTree<>(treeModel);

        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        searchPanel = new JPanel();
        searchPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.PAGE_AXIS));

        tags = new TagInputField(searchFieldSeparator);
        authors = new AuthorInputField(searchFieldSeparator);
        dateFrom = new JDateChooser();
        dateTo = new JDateChooser();

        addFieldComponent("Parole chiave", tags, null);
        addFieldComponent("Autori", authors, null);
        addFieldComponent("Categorie", categories, "Seleziona le categorie in cui cercare il riferimento");
        addFieldComponent("Da", dateFrom, "Data di inizio dell'intervallo di ricerca");
        addFieldComponent("A", dateTo, "Data di fine dell'intervallo di ricerca");

        Component spacing = Box.createVerticalGlue();
        spacing.setMaximumSize(new Dimension(100, 32));
        searchPanel.add(spacing);

        searchButton = new JButton("Cerca");
        searchButton.setIcon(new ImageIcon("images/search.png"));
        searchButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
        searchButton.setMaximumSize(new Dimension(100, 32));
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
        searchPanel.add(searchButton);

        add(new JLabel("<html><b>Ricerca riferimenti</b></html>"), BorderLayout.NORTH);
        add(searchPanel, BorderLayout.CENTER);
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
    }

    private void search() {
        try {
            Search search = new Search(dateFrom.getDate(), dateTo.getDate(), tags.getTags(), categories.getCheckboxTree().getSelectedItems(), authors.getAuthors());

            for (SearchListener listener : searchListeners)
                listener.search(search);

            reset();
        } catch (EmptySearchException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    /**
     * Imposta l'albero delle categorie selezionabili.
     * 
     * @param treeModel
     */
    public void setCategoriesTree(CustomTreeModel<Category> treeModel) {
        categories.setTreeModel(treeModel);
    }

    /**
     * Aggiunge un listener all'evento di ricerca.
     * Se {@code listener == null} o se è già registrato, non viene aggiunto.
     * 
     * @param listener
     *            listener da aggiungere
     */
    public void addReferenceSearchListener(SearchListener listener) {
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
    public void removeReferenceSearchListener(SearchListener listener) {
        if (listener == null || searchListeners == null)
            return;

        searchListeners.remove(listener);
    }

    /**
     * Resetta i campi di ricerca.
     */
    public void reset() {
        tags.setText(null);
        authors.setText(null);
        categories.getCheckboxTree().clearSelection();
        dateFrom.setDate(null);
        dateTo.setDate(null);
    }

}
