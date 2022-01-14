package GUI.ReferenceEditor;

import GUI.Categories.*;
import GUI.Homepage.Search.CategoriesSelectionPopupMenu;
import GUI.Utilities.JTermsField;
import Entities.Category;
import Entities.Tag;
import Entities.References.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JDateChooser;
import DAO.BibliographicReferenceDAO;

/**
 * TODO: commenta
 */
public abstract class ReferenceCreator extends JDialog {

    // TODO: authors

    private JTextField title;
    private JDateChooser pubblicationDate;
    private JTextField DOI;
    private JTermsField tags;
    private JTextArea description;
    private JComboBox<ReferenceLanguage> language;
    private CategoriesSelectionPopupMenu categories;

    private JPanel fieldPanel;

    private BibliographicReferenceDAO referenceDAO;

    private final String searchFieldSeparator = ",";
    private final Dimension maximumSize = new Dimension(Integer.MAX_VALUE, 24);
    private final float alignment = Container.LEFT_ALIGNMENT;

    /**
     * Crea {@code ReferenceCreator} con il titolo, le categorie e il DAO indicati.
     * 
     * @param dialogueTitle
     *            titolo della finestra di dialogo
     * @param categoriesTreeManager
     *            manager dell'albero delle categorie
     * @param referenceDAO
     *            classe DAO dei riferimenti
     * @throws IllegalArgumentException
     *             se referenceDAO non è valido
     * @see #setReferenceDAO(BibliographicReferenceDAO)
     */
    public ReferenceCreator(String dialogueTitle, CategoriesTreeManager categoriesTreeManager, BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        super();

        setTitle(dialogueTitle);
        setSize(500, 500);
        setMinimumSize(new Dimension(300, 300));

        // vogliamo che sia un'interfaccia modale
        setModal(true);

        // in questo modo le classi che ereditano ReferenceCreator possono fare l'overriding
        // e aggiungere altri elementi prima delle descrizione
        setup(categoriesTreeManager);

        // la descrizione è un elemento più grande, quindi vogliamo che sia alla fine
        setupDescription();

        // mostra automaticamente quando viene creato
        setVisible(true);
    }

    /**
     * Imposta la classe DAO per interfacciarsi col database.
     * 
     * @param referenceDAO
     *            classe DAO dei riferimenti
     * @throws IllegalArgumentException
     *             se {@code referenceDAO == null}
     */
    public void setReferenceDAO(BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        if (referenceDAO == null)
            throw new IllegalArgumentException("referenceDAO non può essere null");

        this.referenceDAO = referenceDAO;
    }

    /**
     * Restituisce la classe DAO per interfacciarsi al database dei riferimenti.
     * 
     * @return
     *         classe DAO dei riferimenti
     */
    public BibliographicReferenceDAO getReferenceDAO() {
        return referenceDAO;
    }

    /**
     * TODO: commenta
     * 
     * @param categoriesTreeManager
     */
    protected void setup(CategoriesTreeManager categoriesTreeManager) {
        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.PAGE_AXIS));
        fieldPanel.setBorder(new EmptyBorder(50, 30, 50, 30));

        JScrollPane scrollPane = new JScrollPane(fieldPanel);
        contentPane.add(scrollPane);

        title = new JTextField(); // TODO: required field
        tags = new JTermsField(searchFieldSeparator);
        DOI = new JTextField();
        pubblicationDate = new JDateChooser();
        language = new JComboBox<>(ReferenceLanguage.values());
        categories = new CategoriesSelectionPopupMenu(categoriesTreeManager);

        addComponent("Titolo", title);
        addComponent("Parole chiave", tags);
        addComponent("DOI", DOI);
        addComponent("Data di pubblicazione", pubblicationDate);
        addComponent("Lingua", language);
        addComponent("Categorie", categories);

        JButton confirmButton = new JButton("OK");
        confirmButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                onConfirmClick();
            }

        });
        contentPane.add(confirmButton, BorderLayout.SOUTH);
    }

    private void setupDescription() {
        // FIXME: con troppi caratteri comincia a comportarsi in maniera strana

        JLabel descriptionLabel = new JLabel("Descrizione");
        descriptionLabel.setMaximumSize(maximumSize);
        descriptionLabel.setAlignmentX(alignment);

        description = new JTextArea();
        description.setLineWrap(true);
        description.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        description.setAlignmentX(alignment);

        fieldPanel.add(descriptionLabel);
        fieldPanel.add(description);
    }

    protected void addComponent(JComponent component) {
        component.setMaximumSize(maximumSize);
        component.setAlignmentX(alignment);

        fieldPanel.add(component);

        Component spacing = Box.createHorizontalGlue();
        spacing.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));

        fieldPanel.add(spacing);

    }

    protected void addComponent(String label, JComponent component) {
        JLabel labelField = new JLabel(label);
        labelField.setMaximumSize(maximumSize);
        labelField.setAlignmentX(alignment);

        addComponent(labelField);
        addComponent(component);
    }

    protected void addComponent(String label, JComponent component, String tooltip) {
        component.setToolTipText(tooltip);
        addComponent(label, component);
    }

    /**
     * Funzione chiamata quando viene premuto il tasto di conferma.
     */
    protected abstract void onConfirmClick();

    protected void setReferenceTitle(String text) {
        title.setText(text);
    }

    protected String getReferenceTitle() {
        return title.getText().trim();
    }

    protected void setPubblicationDate(Date date) {
        pubblicationDate.setDate(date);
    }

    protected Date getPubblicationDate() {
        return pubblicationDate.getDate();
    }

    protected void setDOI(String doi) {
        DOI.setText(doi);
    }

    protected String getDOI() {
        return DOI.getText().trim();
    }

    protected void setDescription(String description) {
        this.description.setText(description);
    }

    protected String getDescription() {
        return description.getText().trim();
    }

    protected void setTags(Tag[] tags) {
        // TODO:
    }

    protected Tag[] getTags() {
        String[] tagsString = tags.getTerms();

        if (tagsString == null)
            return null;

        Tag[] tags = new Tag[tagsString.length];

        for (int i = 0; i < tags.length; i++) {
            tags[i] = new Tag(tagsString[i]);
        }

        return tags;
    }

    protected void setLanguage(ReferenceLanguage language) {
        this.language.setSelectedItem(language);
    }

    protected ReferenceLanguage getLanguage() {
        return (ReferenceLanguage) language.getSelectedItem();
    }

    protected void setCategories(Category[] categories) {
        // TODO:
    }

    protected Category[] getCategories() {
        return categories.getSelectedCategories();
    }

}
