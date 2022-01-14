package GUI.Homepage.Search;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import com.toedter.calendar.JDateChooser;

import Entities.Tag;
import GUI.Categories.*;
import GUI.Homepage.References.*;
import GUI.Utilities.JTextSearchField;

/**
 * Pannello per la ricerca dei riferimenti per parole chiave, autori, categorie
 * e data.
 */
public class SearchPanel extends JPanel {

    private JTextSearchField tags;
    private JTextSearchField authors;
    private CategoriesSelectionPopupMenu categories;
    private JDateChooser dateFrom;
    private JDateChooser dateTo;
    private JButton searchButton;

    private final Dimension maximumSize = new Dimension(Integer.MAX_VALUE, 24);
    private final String searchFieldSeparator = ",";
    private final float alignment = Container.LEFT_ALIGNMENT;

    private CategoriesTreeManager categoriesTreeModel;

    /**
     * Crea {@code ReferenceSearchPanel}.
     * 
     * @param referencePanel
     * @param categoriesTreeModel
     */
    public SearchPanel(ReferencePanel referencePanel, CategoriesTreeManager categoriesTreeModel) {
        this.categoriesTreeModel = categoriesTreeModel;

        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        add(getPanelLabel(), BorderLayout.NORTH);
        add(getSearchFieldPanel(), BorderLayout.CENTER);
    }

    private JLabel getPanelLabel() {
        return new JLabel("<html><b>Ricerca riferimenti</b></html>");
    }

    private JPanel getSearchFieldPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.PAGE_AXIS));

        tags = new JTextSearchField(searchFieldSeparator);
        authors = new JTextSearchField(searchFieldSeparator);
        categories = new CategoriesSelectionPopupMenu(categoriesTreeModel);
        dateFrom = new JDateChooser();
        dateTo = new JDateChooser();

        addComponent("Parole chiave", tags, searchPanel, "Inserisci le parole chiave da ricercare nel riferimento, delimitate da '" + searchFieldSeparator + "'");
        addComponent("Autori", authors, searchPanel, "Inserisci gli autori da ricercare, delimitati da '" + searchFieldSeparator + "'");
        addComponent("Categorie", categories, searchPanel, "Seleziona le categorie in cui cercare il riferimento");
        addComponent("Da", dateFrom, searchPanel);
        addComponent("A", dateTo, searchPanel);

        Component spacing = Box.createVerticalGlue();
        spacing.setMaximumSize(new Dimension(100, 32));
        searchPanel.add(spacing);

        setupSearchButton();
        searchPanel.add(searchButton);

        return searchPanel;
    }

    private void addComponent(JComponent component, JPanel panel) {
        component.setMaximumSize(maximumSize);
        component.setAlignmentX(alignment);

        panel.add(component);
    }

    private void addComponent(String label, JComponent component, JPanel panel) {
        JLabel labelField = new JLabel(label);
        labelField.setMaximumSize(maximumSize);
        labelField.setAlignmentX(alignment);
        panel.add(labelField);

        addComponent(component, panel);
    }

    private void addComponent(String label, JComponent component, JPanel panel, String tooltip) {
        component.setToolTipText(tooltip);
        addComponent(label, component, panel);
    }

    private void setupSearchButton() {
        searchButton = new JButton("Cerca");
        searchButton.setIcon(new ImageIcon("images/search.png"));
        searchButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
        searchButton.setMaximumSize(new Dimension(100, 32));
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
    }

    // TODO:
    private Tag[] stringToTags(String[] strings) {
        if (strings == null || strings.length == 0)
            return null;

        Tag[] tags = new Tag[strings.length];

        for (int i = 0; i < tags.length; i++)
            tags[i] = new Tag(strings[i].trim());

        return tags;
    }

    private void search() {
        // TODO: cerca

        Tag[] tags = stringToTags(this.tags.getSearchTerms());

        // Search search = new Search(datePicker.getStartDate(),
        // datePicker.getEndDate(),
        // stringToTags(tagSearchField.getSearchTerms()),
        // categoriesSearchField.getCategories());

    }

}
