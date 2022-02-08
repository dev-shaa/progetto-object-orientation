package GUI.References.Editor;

import Entities.*;
import Entities.References.*;
import GUI.Authors.AuthorInputField;
import GUI.References.Picker.*;
import GUI.Tags.TagInputField;
import GUI.Utilities.*;
import Exceptions.CategoryDatabaseException;
import Exceptions.ReferenceDatabaseException;
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

    private T referenceToChange;

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

        setupBaseFields();
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
        if (referenceController == null)
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

    private void setReferenceToChange(T reference) {
        this.referenceToChange = reference;
    }

    private T getReferenceToChange() {
        return referenceToChange;
    }

    private void setupBaseFields() {
        fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.PAGE_AXIS));
        fieldPanel.setBorder(new EmptyBorder(50, 30, 50, 30));

        setContentPane(new JScrollPane(fieldPanel));

        title = new JTextField();
        tags = new TagInputField(separator);
        authors = new AuthorInputField(separator);
        categories = new PopupCheckboxTree<>();
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

                if (getReferenceToChange() != null)
                    referencesToExclude.add(getReferenceToChange());

                relatedReferencesDialog.setVisible(true, referencesToExclude);
            }
        });

        JPanel relatedReferencesPanel = new JPanel(new BorderLayout(10, 0));
        relatedReferencesPanel.add(relatedReferencesPopupButton, BorderLayout.CENTER);
        relatedReferencesPanel.add(addRelatedReference, BorderLayout.EAST);

        addFieldComponent(title, "Titolo (obbligatorio)", "Titolo univoco del riferimento.");
        addFieldComponent(tags, "Parole chiave", null);
        addFieldComponent(DOI, "DOI", "Codice identificativo DOI del riferimento.");
        addFieldComponent(pubblicationDate, "Data di pubblicazione", "Data di pubblicazione del riferimento.");
        addFieldComponent(language, "Lingua", "Lingua del riferimento.");
        addFieldComponent(categories, "Categorie", "Categorie a cui deve essere associato questo riferimento.");
        addFieldComponent(authors, "Autori", null);
        addFieldComponent(relatedReferencesPanel, "Rimandi", "Riferimenti menzionati all'interno del testo.");

        setupSecondaryFields();

        // la descrizione e il tasto di conferma vogliamo che siano sempre alla fine

        JLabel descriptionLabel = new JLabel("Descrizione");
        descriptionLabel.setMaximumSize(maximumSize);
        descriptionLabel.setAlignmentX(alignment);

        description.setLineWrap(true);
        description.setAlignmentX(alignment);

        JButton confirmButton = new JButton("Salva riferimento");
        confirmButton.setAlignmentX(alignment);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
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
    protected void setFieldsValues(T reference) {
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
    protected abstract void saveToDatabase(T reference) throws ReferenceDatabaseException;

    private void save() {
        try {
            T reference = createNewReference();
            saveToDatabase(reference);
            setVisible(false);
        } catch (RequiredFieldMissingException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
        } catch (ReferenceDatabaseException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Salvataggio non riuscito", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Crea un nuovo riferimenti con i valori inseriti dall'utente.
     * 
     * @return riferimento con i dati inseriti dall'utente
     * @throws RequiredFieldMissingException
     *             se i campi obbligatori non sono stati riempiti
     */
    protected T createNewReference() throws RequiredFieldMissingException {
        T reference = getNewInstance();

        if (getReferenceToChange() != null)
            reference.setID(getReferenceToChange().getID());

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
        boolean failedToLoad = false;

        if (b) {
            setReferenceToChange(reference);

            try {
                categories.setTreeModel(getCategoryController().getTree());
            } catch (CategoryDatabaseException e) {
                categories.setTreeModel(null);
                failedToLoad = true;
            }

            setFieldsValues(reference);

            setLocationRelativeTo(null);
        }

        super.setVisible(b);

        if (failedToLoad) {
            String[] choices = { "Riprova", "Chiudi" };
            int option = JOptionPane.showOptionDialog(this, "Si è verificato un errore durante il recupero delle categorie dell'utente.", "Errore recupero", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, choices, 0);

            if (option == JOptionPane.YES_OPTION) {
                setVisible(true);
            } else {
                setVisible(false);
            }
        }
    }

    // #region VALUES GETTER/SETTER

    private void setTitleValue(String text) {
        title.setText(text);
    }

    private String getTitleValue() throws RequiredFieldMissingException {
        String referenceTitle = title.getText().trim();

        if (referenceTitle == null || referenceTitle.isBlank())
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
            this.categories.getCheckboxTree().selectItem(category);
    }

    private List<Category> getCategoryValues() {
        return categories.getCheckboxTree().getSelectedItems();
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
        if (reference == null || reference.equals(getReferenceToChange()))
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

    // #endregion

}
