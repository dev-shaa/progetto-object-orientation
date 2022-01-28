package GUI.Homepage.Search;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import com.toedter.calendar.JDateChooser;

import Controller.AuthorController;
import Controller.CategoryController;
import Entities.Author;
import Entities.Tag;
import Exceptions.EmptySearchException;
import GUI.Homepage.Categories.*;
import GUI.Utilities.JTermsField;
import GUI.Utilities.PopupCheckboxList;

/**
 * Pannello per la ricerca dei riferimenti per parole chiave, autori, categorie e data.
 */
public class SearchPanel extends JPanel {

    private JTermsField tags;
    private PopupCheckboxList<Author> authors;
    private CategoriesSelectionPopupMenu categories;
    private JDateChooser dateFrom;
    private JDateChooser dateTo;
    private JButton searchButton;

    private ArrayList<ReferenceSearchListener> searchListeners;

    private final String searchFieldSeparator = ",";
    private final Dimension maximumSize = new Dimension(Integer.MAX_VALUE, 24);
    private final float alignment = Container.LEFT_ALIGNMENT;

    /**
     * Crea un pannello per la ricerca di riferimenti.
     * 
     * @param categoryController
     *            controller delle categorie che è possibile selezionare
     * @param authorController
     *            controller delle categorie che è possibile selezionare
     * @throws IllegalArgumentException
     *             se {@code categoryController} o {@code authorController} sono nulli
     */
    public SearchPanel(CategoryController categoryController, AuthorController authorController) {
        setCategoriesController(categoryController);
        setAuthorController(authorController);

        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.PAGE_AXIS));

        tags = new JTermsField(searchFieldSeparator);
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
            List<Author> selectedAuthors = authors.getSelectedElements();

            Search search = new Search(dateFrom.getDate(),
                    dateTo.getDate(),
                    stringToTags(this.tags.getTerms()),
                    categories.getSelectedCategories(),
                    selectedAuthors.toArray(new Author[selectedAuthors.size()]));

            for (ReferenceSearchListener listener : searchListeners) {
                listener.onReferenceSearch(search);
            }
        } catch (EmptySearchException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    /**
     * Imposta il controller delle categorie e reimposta l'albero delle categorie selezionabili.
     * 
     * @param controller
     *            controller delle categorie
     * @throws IllegalArgumentException
     *             se {@code controller == null}
     */
    public void setCategoriesController(CategoryController controller) {
        if (controller == null)
            throw new IllegalArgumentException("controller can't be null");

        if (categories == null)
            categories = new CategoriesSelectionPopupMenu(controller.getCategoriesTree());
        else
            categories.setCategoriesTree(controller.getCategoriesTree());
    }

    /**
     * Imposta il controller degli autori e reimposta la lista degli autori selezionabili.
     * 
     * @param controller
     *            controller degli autori
     * @throws IllegalArgumentException
     *             se {@code controller == null}
     */
    public void setAuthorController(AuthorController controller) {
        if (controller == null)
            throw new IllegalArgumentException("controller can't be null");

        if (authors == null) {
            authors = new PopupCheckboxList<Author>("Premi per selezionare gli autori", controller.getAuthors());
        } else {
            authors.setElements(controller.getAuthors());
        }
    }

    /**
     * Aggiunge un listener all'evento di ricerca.
     * 
     * @param listener
     *            listener da aggiungere
     */
    public void addReferenceSearchListener(ReferenceSearchListener listener) {
        if (listener == null)
            return;

        if (searchListeners == null)
            searchListeners = new ArrayList<>(1);

        searchListeners.add(listener);
    }

    /**
     * Rimuove un listener dall'evento di ricerca.
     * 
     * @param listener
     *            listener da rimuovere
     */
    public void removeReferenceSearchListener(ReferenceSearchListener listener) {
        if (listener == null || searchListeners == null)
            return;

        searchListeners.remove(listener);
    }

}
