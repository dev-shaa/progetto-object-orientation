package GUI.Homepage.Search;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;
import com.toedter.calendar.JDateChooser;

import Entities.Category;
import Entities.Search;
import Exceptions.EmptySearchException;
import GUI.Editor.AuthorInputField;
import GUI.Editor.TagInputField;
import GUI.Utilities.CustomTreeModel;
import GUI.Utilities.PopupCheckboxTree;

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

    private ArrayList<ReferenceSearchListener> searchListeners;

    private final String searchFieldSeparator = ",";
    private final Dimension maximumSize = new Dimension(Integer.MAX_VALUE, 24);
    private final float alignment = Container.LEFT_ALIGNMENT;

    /**
     * Crea un pannello per la ricerca di riferimenti.
     * 
     * @param treeModel
     *            albero delle categorie selezionabili
     * @throws IllegalArgumentException
     *             se {@code treeModel == null}
     */
    public SearchPanel(CustomTreeModel<Category> treeModel) {
        setTreeModel(treeModel);

        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        searchPanel = new JPanel();
        searchPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.PAGE_AXIS));

        tags = new TagInputField(searchFieldSeparator);
        authors = new AuthorInputField(searchFieldSeparator);
        dateFrom = new JDateChooser();
        dateTo = new JDateChooser();

        addFieldComponent("Parole chiave", tags, "Inserisci le parole chiave da ricercare nel riferimento, delimitate da '" + searchFieldSeparator + "'");
        addFieldComponent("Autori", authors, "Inserisci gli autori da ricercare, delimitati da '" + searchFieldSeparator + "'");
        addFieldComponent("Categorie", categories, "Seleziona le categorie in cui cercare il riferimento");
        addFieldComponent("Da", dateFrom);
        addFieldComponent("A", dateTo);

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

        reset();
    }

    private void addFieldComponent(JComponent component) {
        component.setMaximumSize(maximumSize);
        component.setAlignmentX(alignment);
        searchPanel.add(component);
    }

    private void addFieldComponent(String label, JComponent component) {
        JLabel labelField = new JLabel(label);
        labelField.setMaximumSize(maximumSize);
        labelField.setAlignmentX(alignment);

        addFieldComponent(labelField);
        addFieldComponent(component);
    }

    private void addFieldComponent(String label, JComponent component, String tooltip) {
        component.setToolTipText(tooltip);
        addFieldComponent(label, component);
    }

    private void reset() {
        tags.setText(null);
        authors.setText(null);
        categories.clearSelection();
        dateFrom.setDate(null);
        dateTo.setDate(null);
    }

    private void search() {
        try {
            Search search = new Search(dateFrom.getDate(), dateTo.getDate(), tags.getTags(), categories.getSelectedItems(), authors.getAuthors());

            for (ReferenceSearchListener listener : searchListeners)
                listener.onReferenceSearch(search);

            reset();
        } catch (EmptySearchException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    /**
     * Imposta l'albero delle categorie selezionabili.
     * 
     * @param treeModel
     * @throws IllegalArgumentException
     *             se {@code treeModel == null}
     */
    public void setTreeModel(CustomTreeModel<Category> treeModel) {
        if (treeModel == null)
            throw new IllegalArgumentException("treeModel can't be null");

        if (categories == null)
            categories = new PopupCheckboxTree<Category>(treeModel);
        else
            categories.setTreeModel(treeModel);
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
