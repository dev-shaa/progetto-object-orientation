package GUI.Editors;

import Entities.*;
import Entities.References.*;
import Utilities.Tree.CustomTreeModel;
import Utilities.Tree.CheckboxTree.CheckboxTree;
import io.codeworth.panelmatic.PanelBuilder;
import io.codeworth.panelmatic.PanelMatic;
import io.codeworth.panelmatic.componentbehavior.Modifiers;
import Exceptions.Input.InvalidAuthorInputException;
import Exceptions.Input.InvalidInputException;
import Exceptions.Input.InvalidTagInputException;
import GUI.AuthorInputField;
import GUI.TagInputField;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.event.EventListenerList;
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
    private CheckboxTree<Category> categoriesCheckboxTree;
    private DefaultListModel<BibliographicReference> referenceListModel;
    private JList<BibliographicReference> relatedReferencesList;
    private TagInputField tags;
    private AuthorInputField authors;

    private T referenceToChange;
    private Collection<? extends BibliographicReference> references;

    private EventListenerList creationListeners = new EventListenerList();

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento,
     * ma senza categorie o rimandi selezionabili.
     * 
     * @param title
     *            titolo della finestra
     */
    public ReferenceEditor(String title) {
        this(title, null, null);
    }

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

        setup(title);
        setCategoriesTree(categoriesTree);
        setReferencesToPickAsQuotation(references);
    }

    /**
     * Imposta l'albero delle categorie in cui è possibile inserire un riferimento.
     * 
     * @param categoriesTree
     *            albero delle categorie
     */
    public void setCategoriesTree(CustomTreeModel<Category> categoriesTree) {
        categoriesCheckboxTree.setModel(categoriesTree);
    }

    /**
     * Imposta i riferimenti selezionabili come rimandi.
     * 
     * @param references
     *            possibili rimandi
     */
    public void setReferencesToPickAsQuotation(Collection<? extends BibliographicReference> references) {
        this.references = references;
    }

    /**
     * Imposta il riferimento da modificare.
     * 
     * @param referenceToChange
     *            riferimento da modificare (se {@code null}, si intende che si vuole creare un nuovo riferimento)
     */
    public void setReferenceToChange(T referenceToChange) {
        this.referenceToChange = referenceToChange;
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            // i rimandi selezionabili sono tutti tranne il riferimento da cambiare
            referenceListModel.clear();

            if (references != null)
                referenceListModel.addAll(references);

            if (referenceToChange != null)
                referenceListModel.removeElement(referenceToChange);

            if (referenceToChange == null)
                setDefaultValues();
            else
                setReferenceValues(referenceToChange);

            categoriesCheckboxTree.expandAllRows();
        }

        super.setVisible(b);
    }

    private void setup(String windowTitle) {
        setTitle(windowTitle);
        setModal(true);
        setSize(500, 700);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel fieldPanel = new JPanel();
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 50, 30));
        PanelBuilder panelBuilder = PanelMatic.begin(fieldPanel);

        title = new JTextField(BibliographicReference.TITLE_MAX_LENGTH);
        title.setToolTipText("Titolo univoco del riferimento.");
        panelBuilder.add(new JLabel("Titolo (obbligatorio)"));
        panelBuilder.add(title);

        tags = new TagInputField();
        panelBuilder.add(new JLabel("Parole chiave"));
        panelBuilder.add(tags);

        DOI = new JTextField();
        DOI.setToolTipText("Codice identificativo DOI del riferimento.\nEsempio: \"10.1234/567.89\".");
        panelBuilder.add(new JLabel("DOI"));
        panelBuilder.add(DOI);

        authors = new AuthorInputField();
        panelBuilder.add(new JLabel("Autori"));
        panelBuilder.add(authors);

        pubblicationDate = new JDateChooser();
        panelBuilder.add(new JLabel("Data di pubblicazione"));
        panelBuilder.add(pubblicationDate);

        language = new JComboBox<>(ReferenceLanguage.values());
        language.setToolTipText("Lingua del riferimento.");
        panelBuilder.add(new JLabel("Lingua"));
        panelBuilder.add(language);

        setupSecondaryFields(panelBuilder);

        categoriesCheckboxTree = new CheckboxTree<>();
        categoriesCheckboxTree.setRootVisible(false);
        JPanel categoriesPanel = new JPanel(new BorderLayout());
        JScrollPane categoriesScrollPane = new JScrollPane(categoriesCheckboxTree);
        categoriesPanel.add(categoriesScrollPane);
        panelBuilder.add(new JLabel("Categorie"));
        panelBuilder.add(categoriesPanel, Modifiers.GROW);

        referenceListModel = new DefaultListModel<>();
        relatedReferencesList = new JList<>(referenceListModel);
        relatedReferencesList.setLayoutOrientation(JList.VERTICAL);
        relatedReferencesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        relatedReferencesList.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(relatedReferencesList);
        JPanel referenceListPanel = new JPanel(new BorderLayout());
        referenceListPanel.add(listScrollPane);

        panelBuilder.add(new JLabel("Rimandi"));
        panelBuilder.add(referenceListPanel, Modifiers.GROW);

        description = new JTextArea(10, 1);
        description.setLineWrap(true);
        description.setToolTipText("Breve descrizione testuale del riferimento.");
        panelBuilder.add(new JLabel("Descrizione"));
        panelBuilder.add(description, Modifiers.GROW);

        JButton confirmButton = new JButton("Salva riferimento");
        confirmButton.addActionListener(e -> onConfirmButton());
        panelBuilder.add(confirmButton, Modifiers.L_CENTER);

        panelBuilder.get();
        fieldPanel.setPreferredSize(new Dimension(500, fieldPanel.getPreferredSize().height));

        setContentPane(new JScrollPane(fieldPanel));
    }

    /**
     * Prepara i campi necessari per la creazione di un riferimento.
     */
    protected void setupSecondaryFields(PanelBuilder panelBuilder) {
        // serve solo come funzione per override e aggiungere altri componenti
    }

    private void onConfirmButton() {
        try {
            T newReference = createNewReference();
            fireReferenceCreationEvent(newReference);
        } catch (Exception e) {
            showErrorMessage("Parametri inseriti non validi", e.getMessage());
        }
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
     * @return istanza vuota da riempire, non nulla
     */
    protected abstract T getNewInstance();

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

    // #region LISTENER

    /**
     * Aggiunge un ascoltatore all'evento di creazione di un riferimento.
     * 
     * @param listener
     *            ascoltatore da aggiungere
     */
    public void addReferenceEditorListener(ReferenceEditorListener<T> listener) {
        creationListeners.add(ReferenceEditorListener.class, listener);
    }

    /**
     * Rimuove un ascoltatore dall'evento di creazione di un riferimento.
     * 
     * @param listener
     *            ascoltatore da rimuovere
     */
    public void removeReferenceEditorListener(ReferenceEditorListener<T> listener) {
        creationListeners.remove(ReferenceEditorListener.class, listener);
    }

    @SuppressWarnings("unchecked")
    private void fireReferenceCreationEvent(T newReference) {
        ReferenceEditorListener<T>[] listeners = (ReferenceEditorListener<T>[]) creationListeners.getListeners(ReferenceEditorListener.class);

        for (ReferenceEditorListener<T> listener : listeners)
            listener.onReferenceCreation(newReference);
    }

    // #endregion

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

    private List<Tag> getTagValues() throws InvalidTagInputException {
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
            this.categoriesCheckboxTree.selectItem(category);
    }

    private ArrayList<Category> getCategoryValues() {
        return categoriesCheckboxTree.getSelectedItems();
    }

    private void setRelatedReferences(List<BibliographicReference> references) {
        relatedReferencesList.clearSelection();

        if (references != null) {
            for (BibliographicReference reference : references)
                addRelatedReference(reference);
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