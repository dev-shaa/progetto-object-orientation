package GUI.Homepage.Search;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import com.toedter.calendar.JDateChooser;

import Entities.Author;
import Entities.Category;
import Entities.Tag;
import GUI.Categories.*;
import GUI.Homepage.References.*;
import GUI.Utilities.JPopupItemSelection;
import GUI.Utilities.JTermsField;

/**
 * Pannello per la ricerca dei riferimenti per parole chiave, autori, categorie
 * e data.
 */
public class SearchPanel extends JPanel {

    private JTermsField tags;
    // private JTermsField authors;
    private JPopupItemSelection<Author> authors;
    private CategoriesSelectionPopupMenu categories;
    private JDateChooser dateFrom;
    private JDateChooser dateTo;
    private JButton searchButton;

    private final String searchFieldSeparator = ",";
    private final Dimension maximumSize = new Dimension(Integer.MAX_VALUE, 24);
    private final float alignment = Container.LEFT_ALIGNMENT;

    private ReferencePanel referencePanel;
    private CategoriesTreeManager categoriesTreeModel;

    /**
     * Crea {@code ReferenceSearchPanel}.
     * 
     * @param referencePanel
     * @param categoriesTreeModel
     */
    public SearchPanel(ReferencePanel referencePanel, CategoriesTreeManager categoriesTreeModel) {
        this.referencePanel = referencePanel;
        this.categoriesTreeModel = categoriesTreeModel;

        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.PAGE_AXIS));

        tags = new JTermsField(searchFieldSeparator);
        authors = new JPopupItemSelection<>("Premi per selezionare gli autori");
        categories = new CategoriesSelectionPopupMenu(categoriesTreeModel);
        dateFrom = new JDateChooser();
        dateTo = new JDateChooser();

        addFieldComponent("Parole chiave", tags, searchPanel, "Inserisci le parole chiave da ricercare nel riferimento, delimitate da '" + searchFieldSeparator + "'");
        addFieldComponent("Autori", authors, searchPanel, "Inserisci gli autori da ricercare, delimitati da '" + searchFieldSeparator + "'");
        addFieldComponent("Categorie", categories, searchPanel, "Seleziona le categorie in cui cercare il riferimento");
        addFieldComponent("Da", dateFrom, searchPanel);
        addFieldComponent("A", dateTo, searchPanel);

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

    private void addFieldComponent(JComponent component, JPanel panel) {
        component.setMaximumSize(maximumSize);
        component.setAlignmentX(alignment);

        panel.add(component);
    }

    private void addFieldComponent(String label, JComponent component, JPanel panel) {
        JLabel labelField = new JLabel(label);
        labelField.setMaximumSize(maximumSize);
        labelField.setAlignmentX(alignment);

        addFieldComponent(labelField, panel);
        addFieldComponent(component, panel);
    }

    private void addFieldComponent(String label, JComponent component, JPanel panel, String tooltip) {
        component.setToolTipText(tooltip);
        addFieldComponent(label, component, panel);
    }

    private Tag[] stringToTags(String[] strings) {
        if (strings == null || strings.length == 0)
            return null;

        Tag[] tags = new Tag[strings.length];

        for (int i = 0; i < tags.length; i++)
            tags[i] = new Tag(strings[i].trim());

        return tags;
    }

    private void search() {
        try {
            // FIXME: autori
            // categories.getSelectedCategories();

            for (Category item : categories.getSelectedCategories()) {
                System.out.println(String.valueOf(item));
            }

            System.out.println("----");
            // Search search = new Search(dateFrom.getDate(), dateTo.getDate(), stringToTags(this.tags.getTerms()), categories.getSelectedCategories());
            // referencePanel.showReferences(search);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

}
