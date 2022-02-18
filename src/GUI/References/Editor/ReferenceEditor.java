package GUI.References.Editor;

import Entities.*;
import Entities.References.*;
import GUI.Authors.AuthorInputField;
import GUI.References.Picker.*;
import GUI.Tags.TagInputField;
import GUI.Utilities.*;
import Repository.CategoryRepository;
import Repository.ReferenceRepository;
import Exceptions.Database.CategoryDatabaseException;
import Exceptions.Database.ReferenceDatabaseException;
import Exceptions.Input.InvalidAuthorInputException;
import Exceptions.Input.InvalidInputException;
import Exceptions.Input.RequiredFieldMissingException;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
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
    private TagInputField tags;
    private AuthorInputField authors;

    private ReferencePicker relatedReferencesPicker;
    private PopupButtonList<BibliographicReference> relatedReferences;

    private JPanel fieldPanel;

    private CategoryRepository categoryRepository;
    private ReferenceRepository referenceRepository;

    private T referenceToChange;

    private final Dimension maximumSize = new Dimension(Integer.MAX_VALUE, 24);
    private final Dimension spacingSize = new Dimension(0, 10);
    private final float alignment = Container.LEFT_ALIGNMENT;

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento.
     * 
     * @param title
     *            titolo della finestra
     * @param categoryRepository
     *            repository delle categorie
     * @param referenceRepository
     *            repository dei riferimenti
     * @throws IllegalArgumentException
     *             se {@code categoryRepository == null} o {@code referenceRepository == null}
     */
    public ReferenceEditor(String title, CategoryRepository categoryRepository, ReferenceRepository referenceRepository) {
        super();

        setTitle(title);
        setModal(true);
        setSize(500, 700);
        setResizable(false);

        relatedReferencesPicker = new ReferencePicker(this, categoryRepository, referenceRepository);
        relatedReferencesPicker.addReferencePickerListener(new ReferencePickerListener() {
            @Override
            public void onReferencePick(BibliographicReference reference) {
                addRelatedReference(reference);
            }
        });

        setCategoryRepository(categoryRepository);
        setReferenceRepository(referenceRepository);

        setupBaseFields();
    }

    @Override
    public void setVisible(boolean b) {
        setVisible(b, null);
    }

    /**
     * Mostra o nasconde questo pannello a seconda del valore di {@code b}.
     * <p>
     * Se {@code b == true}, i campi di input verranno riempiti con i valori presenti in {@code reference}.
     * <p>
     * In caso si verifichi un errore nel recupero dell'albero delle categorie, viene mostrato un messaggio di errore.
     * 
     * @param b
     *            se {@code true} il pannello verrà mostrato e verrano riempiti i campi con i valori di {@code reference},
     *            se {@code false} il pannello verrà nascosto
     * @param reference
     *            riferimento da mostrare (può essere {@code null})
     */
    public void setVisible(boolean b, T reference) {
        boolean failedToLoad = false;

        if (b) {
            referenceToChange = reference;

            try {
                categories.setTreeModel(getCategoryRepository().getTree());
                setFieldsInitialValues(reference);
            } catch (CategoryDatabaseException e) {
                categories.setTreeModel(null);
                failedToLoad = true;
            }

            setLocationRelativeTo(null);
        }

        super.setVisible(b);

        if (failedToLoad) {
            String[] choices = { "Riprova", "Chiudi" };
            int option = JOptionPane.showOptionDialog(this, "Si è verificato un errore durante il recupero delle categorie dell'utente.", "Errore recupero", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, choices, 0);
            setVisible(option == JOptionPane.YES_OPTION, reference);
        }
    }

    /**
     * Imposta il repository delle categorie da usare per recuperare le categorie da scegliere.
     * 
     * @param categoryRepository
     *            nuovo repository delle categorie
     * @throws IllegalArgumentException
     *             se {@code categoryRepository == null}
     */
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        if (categoryRepository == null)
            throw new IllegalArgumentException("categoryRepository can't be null");

        this.categoryRepository = categoryRepository;
        relatedReferencesPicker.setCategoryRepository(categoryRepository);
    }

    /**
     * Restituisce il controller delle categorie.
     * 
     * @return controller delle categorie
     */
    public CategoryRepository getCategoryRepository() {
        return categoryRepository;
    }

    /**
     * Imposta il controller dei riferimenti da usare per salvare i riferimenti creati.
     * 
     * @param referenceRepository
     *            nuovo controller dei riferimenti
     * @throws IllegalArgumentException
     *             se {@code referenceController == null}
     */
    public void setReferenceRepository(ReferenceRepository referenceRepository) {
        if (referenceRepository == null)
            throw new IllegalArgumentException("referenceRepository can't be null");

        this.referenceRepository = referenceRepository;
        relatedReferencesPicker.setReferenceRepository(referenceRepository);
    }

    /**
     * Restituisce il controller dei riferimenti.
     * 
     * @return
     *         controller dei riferimenti
     */
    public ReferenceRepository getReferenceRepository() {
        return referenceRepository;
    }

    // #region GUI

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

        relatedReferences = new PopupButtonList<>("Premi per vedere i rimandi");

        JButton addRelatedReference = new JButton(new ImageIcon("images/button_add.png"));
        addRelatedReference.setBorderPainted(false);
        addRelatedReference.setBackground(new Color(0, 0, 0, 0));
        addRelatedReference.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openReferencePicker();
            }
        });

        JPanel relatedReferencesPanel = new JPanel(new BorderLayout(10, 0));
        relatedReferencesPanel.add(relatedReferences, BorderLayout.CENTER);
        relatedReferencesPanel.add(addRelatedReference, BorderLayout.EAST);

        addFieldComponent(relatedReferencesPanel, "Rimandi", "Riferimenti menzionati all'interno del testo.");

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
                save();
            }
        });

        fieldPanel.add(confirmButton);
    }

    /**
     * Prepara i campi necessari per la creazione di un riferimento.
     */
    protected void setupSecondaryFields() {
        // serve solo come funzione per override e aggiungere altri componenti
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
     * Riempie i campi di input con i valori presenti nel riferimento passato.
     * Se il riferimento è {@code null}, vengono svuotati.
     * 
     * @param reference
     *            riferimento da cui prendere i valori (se {@code null}, i campi verrano resettati)
     */
    protected void setFieldsInitialValues(T reference) {
        if (reference == null) {
            setTitleValue(null);
            setPubblicationDateValue(null);
            setDOIValue(null);
            setDescriptionValue(null);
            setTagValues(null);
            setLanguageValue(ReferenceLanguage.NOTSPECIFIED);
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

    private void openReferencePicker() {
        List<BibliographicReference> referencesToExclude = new ArrayList<>();

        if (getRelatedReferenceValues() != null)
            referencesToExclude.addAll(getRelatedReferenceValues());

        if (referenceToChange != null)
            referencesToExclude.add(referenceToChange);

        relatedReferencesPicker.setVisible(true, referencesToExclude);
    }

    // #endregion

    private void save() {
        try {
            T reference = createNewReference();
            save(reference);
            setVisible(false);
        } catch (InvalidInputException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Parametri inseriti non validi", JOptionPane.ERROR_MESSAGE);
        } catch (ReferenceDatabaseException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Salvataggio non riuscito", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Crea un nuovo riferimenti con i valori inseriti dall'utente.
     * 
     * @return riferimento con i dati inseriti dall'utente
     * @throws RequiredFieldMissingException
     *             se i campi obbligatori non sono stati riempiti
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
     * Salva il riferimento.
     * 
     * @param reference
     *            riferimento da salvare
     * @throws ReferenceDatabaseException
     *             se il salvataggio non va a buon fine
     */
    protected abstract void save(T reference) throws ReferenceDatabaseException;

    // #region VALUES GETTER/SETTER

    private void setTitleValue(String text) {
        title.setText(text);
    }

    private String getTitleValue() throws RequiredFieldMissingException {
        String referenceTitle = title.getText().trim();

        if (referenceTitle == null || referenceTitle.isEmpty() || referenceTitle.isBlank())
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
        relatedReferences.setItems(references);
    }

    private void addRelatedReference(BibliographicReference reference) {
        relatedReferences.addItem(reference);
    }

    private List<BibliographicReference> getRelatedReferenceValues() {
        return relatedReferences.getItems();
    }

    // #endregion

}
