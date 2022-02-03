package GUI.Editor.Reference;

import Entities.*;
import Entities.References.*;
import GUI.Utilities.*;
import GUI.Editor.*;
import GUI.Editor.Reference.Picker.*;
import Exceptions.RequiredFieldMissingException;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JDateChooser;

import Controller.CategoryController;
import Controller.ReferenceController;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento bibliografico.
 */
public abstract class ReferenceEditor<T extends BibliographicReference> extends JDialog implements ReferencePickerListener {

    private JTextField title;
    private JTextField DOI;
    private JTextArea description;
    private JDateChooser pubblicationDate;
    private JComboBox<ReferenceLanguage> language;
    private PopupCheckboxTree<Category> categories;
    private TagInputField tags;
    private AuthorInputField authors;

    private PopupButton relatedReferencesPopupButton;
    private ReferencePicker relatedReferencesDialog;
    private ArrayList<BibliographicReference> relatedReferences;

    private JPanel fieldPanel;

    private CategoryController categoryController;
    private ReferenceController referenceController;

    private T openReference;
    // private ArrayList<SaveListener<T>> listeners;

    private final String separator = ",";
    private final Dimension maximumSize = new Dimension(Integer.MAX_VALUE, 24);
    private final Dimension spacingSize = new Dimension(0, 10);
    private final float alignment = Container.LEFT_ALIGNMENT;

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento.
     * 
     * @param owner
     *            proprietario di questa finestra di dialogo
     * @param title
     *            titolo della finestra
     * @param categoryController
     *            controller delle categorie
     * @param referenceController
     *            controller dei riferimenti
     * @throws IllegalArgumentException
     *             se {@code categoryController == null} o {@code referenceController == null}
     */
    public ReferenceEditor(Frame owner, String title, CategoryController categoryController, ReferenceController referenceController) {
        super(owner, title, true);

        setSize(500, 700);
        setResizable(false);

        setCategoryController(categoryController);
        setReferenceController(referenceController);

        setupComponents();
    }

    private void setupComponents() {
        fieldPanel = new JPanel();

        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.PAGE_AXIS));
        fieldPanel.setBorder(new EmptyBorder(50, 30, 50, 30));

        setContentPane(new JScrollPane(fieldPanel));

        title = new JTextField();
        tags = new TagInputField(separator);
        authors = new AuthorInputField(separator);
        DOI = new JTextField();
        pubblicationDate = new JDateChooser();
        language = new JComboBox<>(ReferenceLanguage.values());
        description = new JTextArea(10, 1);

        relatedReferencesDialog = new ReferencePicker(getCategoryController(), getReferenceController());
        relatedReferencesDialog.addReferencePickerListener(this);

        relatedReferencesPopupButton = new PopupButton("Premi per vedere i rimandi");
        JButton addRelatedReference = new JButton(new ImageIcon("images/button_add.png"));
        addRelatedReference.setBorderPainted(false);
        addRelatedReference.setBackground(new Color(0, 0, 0, 0));
        addRelatedReference.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<BibliographicReference> referencesToExclude = new ArrayList<>();

                if (getRelatedReferenceValues() != null)
                    referencesToExclude.addAll(getRelatedReferenceValues());

                if (getOpenReference() != null)
                    referencesToExclude.add(getOpenReference());

                relatedReferencesDialog.setVisible(true, referencesToExclude);
            }
        });

        JPanel relatedReferencesPanel = new JPanel(new BorderLayout(10, 0));
        relatedReferencesPanel.add(relatedReferencesPopupButton, BorderLayout.CENTER);
        relatedReferencesPanel.add(addRelatedReference, BorderLayout.EAST);

        addFieldComponent(title, "Titolo (obbligatorio)", "Titolo univoco del riferimento.");
        addFieldComponent(tags, "Parole chiave", "Parole chiave associate al riferimento, separate da una virgola.");
        addFieldComponent(DOI, "DOI", "Codice identificativo DOI del riferimento.");
        addFieldComponent(pubblicationDate, "Data di pubblicazione", "Data di pubblicazione del riferimento.");
        addFieldComponent(language, "Lingua", "Lingua del riferimento.");
        addFieldComponent(categories, "Categorie", "Categorie a cui deve essere associato questo riferimento.");
        addFieldComponent(authors, "Autori", "Autori del riferimento.");
        addFieldComponent(relatedReferencesPanel, "Rimandi", "Riferimenti menzionati all'interno del testo.");

        initializeFields();

        // la descrizione e il tasto di conferma vogliamo che siano sempre alla fine

        JLabel descriptionLabel = new JLabel("Descrizione");
        descriptionLabel.setMaximumSize(maximumSize);
        descriptionLabel.setAlignmentX(alignment);

        description.setLineWrap(true);
        description.setAlignmentX(alignment);

        JButton confirmButton = new JButton("Salva riferimento");
        confirmButton.setAlignmentX(alignment);

        // non possiamo usare "this" all'interno del corpo dell'action listener,
        // quindi ci serve un puntatore a se stesso
        ReferenceEditor<T> selfPointer = this;

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveReference();
                    setVisible(false);
                } catch (RequiredFieldMissingException ex) {
                    JOptionPane.showMessageDialog(selfPointer, ex.getMessage(), "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        fieldPanel.add(descriptionLabel);
        fieldPanel.add(description);
        fieldPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        fieldPanel.add(confirmButton);
    }

    /**
     * Prepara i campi necessari per la creazione di un riferimento.
     */
    protected void initializeFields() {
        // serve solo come funzione per override e aggiungere altri componenti
    }

    /**
     * Aggiunge un componente nel pannello dove sono presenti i campi di input.
     * Le dimensioni e l'allineamento sono impostate automaticamente.
     * 
     * @param component
     *            componente da aggiungere
     */
    protected void addFieldComponent(JComponent component) {
        addFieldComponent(component, true);
    }

    /**
     * Aggiunge un componente nel pannello dove sono presenti i campi di input.
     * Le dimensioni e l'allineamento sono impostate automaticamente.
     * È possibile aggiungere spazio dopo il componente.
     * 
     * @param component
     *            componente da aggiungere
     * @param addSpacing
     *            {@code true} se deve aggiungere un po' di spazio dopo l'elemento
     */
    protected void addFieldComponent(JComponent component, boolean addSpacing) {
        component.setMaximumSize(maximumSize);
        component.setAlignmentX(alignment);
        fieldPanel.add(component);

        if (addSpacing)
            fieldPanel.add(Box.createRigidArea(spacingSize));
    }

    /**
     * Aggiunge un componente e un'etichetta nel pannello dove sono presenti i campi di input.
     * Le dimensioni e l'allineamento sono impostate automaticamente.
     * 
     * @param component
     *            componente da aggiungere
     * @param label
     *            etichetta del componente
     */
    protected void addFieldComponent(JComponent component, String label) {
        JLabel labelField = new JLabel(label);
        labelField.setMaximumSize(maximumSize);
        labelField.setAlignmentX(alignment);

        addFieldComponent(labelField, false);
        addFieldComponent(component);
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
        component.setToolTipText(tooltip);
        addFieldComponent(component, label);
    }

    /**
     * Riempie i campi di input con i valori presenti nel riferimento passato.
     * Se il riferimento è {@code null}, vengono svuotati.
     * 
     * @param reference
     *            riferimento da cui prendere i valori (se {@code null}, i campi verrano resettati)
     */
    protected void setFieldsValues(T reference) {
        if (reference == null) {
            setTitleValue(null);
            setPubblicationDateValue(null);
            setDOIValue(null);
            setDescriptionValue(null);
            setTagValues(null);
            setLanguageValue(ReferenceLanguage.ENGLISH);
            setRelatedReferences(null);
            setAuthorValues(null);
            setCategoryValues(null);
        } else {
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
    }

    /**
     * Restituisce un nuovo riferimento da riempire.
     * 
     * @return
     */
    protected abstract T getNewInstance();

    private void setOpenReference(T reference) {
        this.openReference = reference;
    }

    private T getOpenReference() {
        return openReference;
    }

    private void saveReference() throws RequiredFieldMissingException {
        T reference = getOpenReference() == null ? getNewInstance() : getOpenReference();
        fillReferenceValues(reference);
    }

    @Override
    public void onReferencePick(BibliographicReference reference) {
        addRelatedReference(reference);
    }

    @Override
    public void setVisible(boolean b) {
        setVisible(b, null);
    }

    /**
     * Mostra o nasconde questo pannello a seconda del valore di {@code b}.
     * Se {@code b == true}, i campi di input verranno riempiti con i valori presenti in {@code reference}.
     * 
     * @param b
     *            se {@code true} il pannello verrà mostrato e verrano riempiti i campi con i valori di {@code reference},
     *            se {@code false} il pannello verrà nascosto
     * @param reference
     *            riferimento da mostrare (eventualmente)
     */
    public void setVisible(boolean b, T reference) {
        if (b) {
            setOpenReference(reference);
            setFieldsValues(reference);
        }

        super.setVisible(b);
    }

    /**
     * Imposta il controller delle categorie da usare per recuperare le categorie da scegliere.
     * 
     * @param categoryController
     *            nuovo controller delle categorie
     * @throws IllegalArgumentException
     *             se {@code categoryController == null}
     */
    public void setCategoryController(CategoryController categoryController) {
        if (categoryController == null)
            throw new IllegalArgumentException("categoryController can't be null");

        this.categoryController = categoryController;

        if (categories == null) {
            categories = new PopupCheckboxTree<Category>(categoryController.getCategoriesTree());
        } else {
            categories.setTreeModel(categoryController.getCategoriesTree());
        }
    }

    /**
     * Restituisce il controller delle categorie.
     * 
     * @return controller delle categorie
     */
    public CategoryController getCategoryController() {
        return categoryController;
    }

    /**
     * Imposta il controller dei riferimenti da usare per salvare i riferimenti creati.
     * 
     * @param referenceController
     *            nuovo controller dei riferimenti
     * @throws IllegalArgumentException
     *             se {@code referenceController == null}
     */
    public void setReferenceController(ReferenceController referenceController) {
        if (categoryController == null)
            throw new IllegalArgumentException("referenceController can't be null");

        this.referenceController = referenceController;
    }

    /**
     * Restituisce il controller dei riferimenti.
     * 
     * @return
     *         controller dei riferimenti
     */
    public ReferenceController getReferenceController() {
        return referenceController;
    }

    // TODO:
    // public void addListener(SaveListener<T> listener) {
    // if (listener == null)
    // return;

    // if (listeners == null)
    // listeners = new ArrayList<>(2);

    // listeners.add(listener);
    // }

    // public void removeListener(SaveListener<T> listener) {
    // if (listener == null || listeners == null)
    // return;

    // listeners.remove(listener);
    // }

    private void setTitleValue(String text) {
        title.setText(text);
    }

    private String getTitleValue() throws RequiredFieldMissingException {
        String referenceTitle = convertEmptyStringToNull(title.getText().trim());

        if (referenceTitle == null)
            throw new RequiredFieldMissingException("Il titolo del riferimento non può essere nullo.");

        return referenceTitle;
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
        return convertEmptyStringToNull(DOI.getText().trim());
    }

    private void setDescriptionValue(String description) {
        this.description.setText(description);
    }

    private String getDescriptionValue() {
        return convertEmptyStringToNull(description.getText().trim());
    }

    private void setTagValues(List<Tag> tags) {
        if (tags == null) {
            this.tags.setText(null);
            return;
        }

        this.tags.setText(tags.toString().substring(1, tags.toString().lastIndexOf(']')));
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
        if (authors == null) {
            this.authors.setText(null);
            return;
        }

        String foo = authors.toString().substring(1, authors.toString().lastIndexOf(']'));
        foo = foo.replaceAll("ORCID: ", "");

        this.authors.setText(foo);
    }

    private List<Author> getAuthorValues() {
        return authors.getAuthors();
    }

    private void setCategoryValues(List<Category> categories) {
        if (categories == null)
            return;

        for (Category category : categories)
            this.categories.selectItem(category);
    }

    private List<Category> getCategoryValues() {
        return categories.getSelectedItems();
    }

    private void setRelatedReferences(List<BibliographicReference> references) {
        if (references == null)
            return;

        relatedReferences = null;
        relatedReferencesPopupButton.removeAllFromPopupMenu();

        for (BibliographicReference reference : references) {
            addRelatedReference(reference);
        }
    }

    private void addRelatedReference(BibliographicReference reference) {
        if (reference == null || reference.equals(getOpenReference()))
            return;

        if (relatedReferences == null)
            relatedReferences = new ArrayList<>(5);

        if (relatedReferences.contains(reference))
            return;

        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBorder(new EmptyBorder(0, 20, 0, 5));
        panel.setBackground(new Color(0, 0, 0, 0));

        JButton removeButton = new JButton(new ImageIcon("images/button_remove.png"));
        removeButton.setBorderPainted(false);
        removeButton.setFocusPainted(false);
        removeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                relatedReferences.remove(reference);
                relatedReferencesPopupButton.removeFromPopupMenu(panel);
            }

        });

        panel.add(new JLabel(reference.toString()), BorderLayout.CENTER);
        panel.add(removeButton, BorderLayout.EAST);

        relatedReferences.add(reference);
        relatedReferencesPopupButton.addToPopupMenu(panel);
    }

    private List<BibliographicReference> getRelatedReferenceValues() {
        if (relatedReferences == null || relatedReferences.isEmpty())
            return null;

        return relatedReferences;
    }

    /**
     * Converte una stringa vuota in {@code null}.
     * 
     * @param string
     *            stringa da convertire
     * @return
     *         {@code null} se la stringa è già nulla o vuota, la stringa invariata altrimenti
     */
    protected String convertEmptyStringToNull(String string) {
        if (string == null || string.isBlank())
            return null;

        return string;
    }

    /**
     * Riempie i campi del riferimento passato con i valori inseriti dall'utente.
     * 
     * @param reference
     *            riferimento da riempire
     * @throws IllegalArgumentException
     *             se {@code reference == null}
     * @throws RequiredFieldMissingException
     *             se i campi obbligatori non sono stati riempiti
     */
    protected void fillReferenceValues(T reference) throws IllegalArgumentException, RequiredFieldMissingException {
        if (reference == null)
            throw new IllegalArgumentException("reference non può essere null");

        reference.setTitle(getTitleValue());
        reference.setAuthors(getAuthorValues());
        reference.setDOI(getDOIValue());
        reference.setDescription(getDescriptionValue());
        reference.setLanguage(getLanguageValue());
        reference.setPubblicationDate(getPubblicationDateValue());
        reference.setTags(getTagValues());
        reference.setRelatedReferences(getRelatedReferenceValues());
        reference.setCategories(getCategoryValues());
    }

}
