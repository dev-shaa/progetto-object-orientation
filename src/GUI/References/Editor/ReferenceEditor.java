package GUI.References.Editor;

import Entities.*;
import Entities.References.*;
import GUI.Authors.AuthorInputField;
import GUI.Tags.TagInputField;
import GUI.Utilities.*;
import GUI.Utilities.Tree.CustomTreeModel;
import Exceptions.Input.InvalidAuthorInputException;
import Exceptions.Input.InvalidInputException;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento bibliografico.
 */
public abstract class ReferenceEditor<T extends BibliographicReference> extends JDialog {

    private JTextField title;
    private JTextField DOI;
    private JTextArea description;
    private JDateChooser pubblicationDate;
    private JComboBox<ReferenceLanguage> language;
    private PopupCheckboxTree<Category> categories;
    private DefaultListModel<BibliographicReference> referenceListModel;
    private JList<BibliographicReference> relatedReferencesList;
    private TagInputField tags;
    private AuthorInputField authors;
    private JPanel fieldPanel;

    private T referenceToChange;
    private ArrayList<ReferenceEditorListener<T>> listeners;

    private final Dimension maximumSize = new Dimension(Integer.MAX_VALUE, 24);
    private final Dimension spacingSize = new Dimension(0, 10);
    private final float alignment = Container.LEFT_ALIGNMENT;

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento.
     * 
     * @param title
     *            titolo della finestra
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire un riferimento
     * @param references
     *            riferimenti selezionabili come rimandi
     */
    public ReferenceEditor(String title, CustomTreeModel<Category> categoriesTree, Collection<? extends BibliographicReference> references) {
        super();

        setTitle(title);
        setModal(true);
        setSize(500, 700);
        setResizable(false);
        setLocationRelativeTo(null);

        listeners = new ArrayList<>(1);

        setupBaseFields();
        setDefaultValues();

        setCategoriesTree(categoriesTree);
        setReferences(references);
    }

    /**
     * Imposta l'albero delle categorie in cui è possibile inserire un riferimento.
     * 
     * @param categoriesTree
     *            albero delle categorie
     */
    public void setCategoriesTree(CustomTreeModel<Category> categoriesTree) {
        categories.setTreeModel(categoriesTree);
    }

    /**
     * Imposta i riferimenti selezionabili come rimandi.
     * 
     * @param references
     *            possibili rimandi
     */
    public void setReferences(Collection<? extends BibliographicReference> references) {
        referenceListModel.clear();

        if (references != null)
            referenceListModel.addAll(references);
    }

    /**
     * Imposta il riferimento da modificare.
     * 
     * @param referenceToChange
     *            riferimento da modificare (se {@code null}, si intende che si vuole creare un nuovo riferimento)
     */
    public void setReferenceToChange(T referenceToChange) {
        this.referenceToChange = referenceToChange;

        if (referenceToChange == null)
            setDefaultValues();
        else
            setReferenceValues(referenceToChange);
    }

    /**
     * Inizializza i campi comuni a tutti i tipi di riferimento.
     */
    private void setupBaseFields() {
        fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.PAGE_AXIS));
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(50, 30, 50, 30));

        setContentPane(new JScrollPane(fieldPanel));

        title = new JTextField();
        addFieldComponent(title, "Titolo (obbligatorio)", "Titolo univoco del riferimento.");

        tags = new TagInputField();
        addFieldComponent(tags, "Parole chiave", null);

        DOI = new JTextField();
        addFieldComponent(DOI, "DOI", "Codice identificativo DOI del riferimento.");

        authors = new AuthorInputField();
        addFieldComponent(authors, "Autori", null);

        categories = new PopupCheckboxTree<>();
        addFieldComponent(categories, "Categorie", "Categorie a cui deve essere associato questo riferimento.");

        pubblicationDate = new JDateChooser();
        addFieldComponent(pubblicationDate, "Data di pubblicazione", "Data di pubblicazione del riferimento.");

        language = new JComboBox<>(ReferenceLanguage.values());
        addFieldComponent(language, "Lingua", "Lingua del riferimento.");

        referenceListModel = new DefaultListModel<>();
        relatedReferencesList = new JList<>(referenceListModel);

        relatedReferencesList.setLayoutOrientation(JList.VERTICAL);
        relatedReferencesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        addFieldComponent(relatedReferencesList, "Rimandi", null);

        // gli altri campi li mettiamo prima della descrizione e del tasto di conferma
        setupSecondaryFields();

        JLabel descriptionLabel = new JLabel("Descrizione");
        descriptionLabel.setMaximumSize(maximumSize);
        descriptionLabel.setAlignmentX(alignment);
        fieldPanel.add(descriptionLabel);

        description = new JTextArea(10, 1);
        description.setLineWrap(true);
        description.setAlignmentX(alignment);
        fieldPanel.add(description);
        fieldPanel.add(Box.createVerticalStrut(20));

        JButton confirmButton = new JButton("Salva riferimento");
        confirmButton.setAlignmentX(alignment);
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    T newReference = createNewReference();
                    notifyListeners(newReference);
                } catch (InvalidInputException | IllegalArgumentException ex) {
                    showErrorMessage("Parametri inseriti non validi", ex.getMessage());
                }
            }
        });

        fieldPanel.add(confirmButton);
    }

    /**
     * Prepara i campi necessari per la creazione di un riferimento.
     */
    protected void setupSecondaryFields() {
        // serve solo come funzione per override e aggiungere altri componenti
        // non serve metterla come abstract perchè magari non si vogliono aggiungere altri componenti
    }

    /**
     * Aggiunge un componente, un'etichetta e un tooltip nel pannello dove sono presenti i campi di input.
     * Le dimensioni e l'allineamento sono impostate automaticamente.
     * 
     * @param component
     *            componente da aggiungere
     * @param label
     *            etichetta del componente
     * @param tooltip
     *            tooltip del componente
     */
    protected void addFieldComponent(JComponent component, String label, String tooltip) {
        JLabel labelField = new JLabel(label);
        labelField.setMaximumSize(maximumSize);
        labelField.setAlignmentX(alignment);

        component.setMaximumSize(maximumSize);
        component.setAlignmentX(alignment);

        if (tooltip != null)
            component.setToolTipText(tooltip);

        fieldPanel.add(labelField);
        fieldPanel.add(component);
        fieldPanel.add(Box.createRigidArea(spacingSize));
    }

    /**
     * Imposta i valori predefiniti dei campi.
     */
    protected void setDefaultValues() {
        setTitleValue(null);
        setPubblicationDateValue(null);
        setDOIValue(null);
        setDescriptionValue(null);
        setTagValues(null);
        setLanguageValue(ReferenceLanguage.NOTSPECIFIED);
        setRelatedReferences(null);
        setAuthorValues(null);
        setCategoryValues(null);
    }

    /**
     * Imposta i valori presenti nei campi recuperandoli dal riferimento indicato.
     * 
     * @param reference
     *            riferimento di cui impostare i campi
     */
    protected void setReferenceValues(T reference) {
        if (reference == null) {
            setDefaultValues();
            return;
        }

        setTitleValue(reference.getTitle());
        setPubblicationDateValue(reference.getPubblicationDate());
        setDOIValue(reference.getDOI());
        setDescriptionValue(reference.getDescription());
        setTagValues(reference.getTags());
        setLanguageValue(reference.getLanguage());
        setRelatedReferences(reference.getRelatedReferences());
        setAuthorValues(reference.getAuthors());
        setCategoryValues(reference.getCategories());
    }

    /**
     * Crea un nuovo riferimenti con i valori inseriti dall'utente.
     * 
     * @return riferimento con i dati inseriti dall'utente
     * @throws InvalidInputException
     *             se alcuni input non sono corretti
     */
    protected T createNewReference() throws InvalidInputException {
        T reference = getNewInstance();

        if (referenceToChange != null)
            reference.setID(referenceToChange.getID());

        reference.setTitle(getTitleValue());
        reference.setAuthors(getAuthorValues());
        reference.setDOI(getDOIValue());
        reference.setDescription(getDescriptionValue());
        reference.setLanguage(getLanguageValue());
        reference.setPubblicationDate(getPubblicationDateValue());
        reference.setTags(getTagValues());
        reference.setRelatedReferences(getRelatedReferenceValues());
        reference.setCategories(getCategoryValues());

        return reference;
    }

    /**
     * Restituisce un nuovo riferimento da riempire.
     * 
     * @return istanza vuota da riempire
     */
    protected abstract T getNewInstance();

    /**
     * Aggiunge un ascoltatore all'evento di creazione di un riferimento.
     * 
     * @param listener
     *            ascoltatore da aggiungere
     */
    public void addReferenceEditorListener(ReferenceEditorListener<T> listener) {
        if (listener == null)
            return;

        if (listeners == null)
            listeners = new ArrayList<>(3);

        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    /**
     * Rimuove un ascoltatore dall'veneto di creazione di un riferimento.
     * 
     * @param listener
     *            ascoltatore da rimuovere
     */
    public void removeReferenceEditorListener(ReferenceEditorListener<T> listener) {
        if (listener != null && listeners != null)
            listeners.remove(listener);
    }

    /**
     * Mostra un messaggio di errore.
     * 
     * @param title
     *            titolo della finestra di dialogo
     * @param message
     *            messaggio da mostrare
     */
    public void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private void notifyListeners(T reference) {
        if (listeners == null)
            return;

        for (ReferenceEditorListener<T> listener : listeners)
            listener.onReferenceCreation(reference);
    }

    // #region VALUES GETTER/SETTER

    private void setTitleValue(String text) {
        title.setText(text);
    }

    private String getTitleValue() {
        return title.getText().trim();
    }

    private void setPubblicationDateValue(Date date) {
        pubblicationDate.setDate(date);
    }

    private Date getPubblicationDateValue() {
        return pubblicationDate.getDate();
    }

    private void setDOIValue(String doi) {
        DOI.setText(doi);
    }

    private String getDOIValue() {
        return DOI.getText().trim();
    }

    private void setDescriptionValue(String description) {
        this.description.setText(description);
    }

    private String getDescriptionValue() {
        return description.getText().trim();
    }

    private void setTagValues(List<Tag> tags) {
        this.tags.setTags(tags);
    }

    private List<Tag> getTagValues() {
        return tags.getTags();
    }

    private void setLanguageValue(ReferenceLanguage language) {
        this.language.setSelectedItem(language);
    }

    private ReferenceLanguage getLanguageValue() {
        return (ReferenceLanguage) language.getSelectedItem();
    }

    private void setAuthorValues(List<Author> authors) {
        this.authors.setAuthors(authors);
    }

    private List<Author> getAuthorValues() throws InvalidAuthorInputException {
        return authors.getAuthors();
    }

    private void setCategoryValues(List<Category> categories) {
        if (categories == null)
            return;

        for (Category category : categories)
            this.categories.getCheckboxTree().selectItem(category);
    }

    private List<Category> getCategoryValues() {
        return categories.getCheckboxTree().getSelectedItems();
    }

    private void setRelatedReferences(List<BibliographicReference> references) {
        relatedReferencesList.clearSelection();

        if (references != null) {
            for (BibliographicReference reference : references) {
                addRelatedReference(reference);
            }
        }
    }

    private void addRelatedReference(BibliographicReference reference) {
        int index = referenceListModel.indexOf(reference);

        if (index != -1)
            relatedReferencesList.addSelectionInterval(index, index);
    }

    private List<BibliographicReference> getRelatedReferenceValues() {
        return relatedReferencesList.getSelectedValuesList();
    }

    // #endregion

}
