package GUI.Homepage.References.Editor;

import Entities.*;
import Entities.References.*;
import GUI.Utilities.*;
import GUI.Homepage.Categories.*;
import GUI.Homepage.References.Chooser.*;
import Exceptions.RequiredFieldMissingException;
import DAO.BibliographicReferenceDAO;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JDateChooser;

/**
 * Pannello per la creazione o modifica di un riferimento bibliografico.
 */
public abstract class ReferenceEditorDialog<T extends BibliographicReference> extends JDialog implements ReferenceChooserSelectionListener {

    private JTextField title;
    private JTermsField tags;
    private JTextField DOI;
    private JTextArea description;
    private JDateChooser pubblicationDate;
    private JComboBox<ReferenceLanguage> language;
    private CategoriesSelectionPopupMenu categories;
    private JPopupItemSelection<Author> authors;

    private JPopupButton relatedReferencesPopupButton;
    private ReferenceChooserDialog relatedReferencesDialog;
    private ArrayList<BibliographicReference> relatedReferences;

    private JPanel fieldPanel;
    private AuthorEditor authorEditor;
    private BibliographicReferenceDAO referenceDAO;

    private CategoryTreeModel categoriesTree;

    private final String searchFieldSeparator = ",";
    private final Dimension maximumSize = new Dimension(Integer.MAX_VALUE, 24);
    private final Dimension spacingSize = new Dimension(0, 10);
    private final float alignment = Container.LEFT_ALIGNMENT;

    /**
     * Crea un nuovo pannello di dialogo per la modifica di un riferimento, inserendo i valori già presenti all'interno dei campi.
     * 
     * @param dialogueTitle
     *            titolo della schermata di dialogo
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire il riferimento
     * @param referenceDAO
     *            classe DAO per salvare i riferimenti nel database
     * @throws IllegalArgumentException
     *             se referenceDAO non è un valore valido
     * 
     * @see #setReferenceDAO(BibliographicReferenceDAO)
     */
    public ReferenceEditorDialog(String dialogueTitle, CategoryTreeModel categoriesTree, BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        super();

        setTitle(dialogueTitle);
        setSize(500, 500);
        setResizable(false);

        setReferenceDAO(referenceDAO);

        // vogliamo che sia un'interfaccia modale
        setModal(true);

        this.categoriesTree = categoriesTree;

        // in questo modo le classi che ereditano ReferenceCreator possono fare l'overriding
        // e aggiungere altri elementi prima delle descrizione
        setup();

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
                saveReference();
                dispose();
            }
        });

        fieldPanel.add(descriptionLabel);
        fieldPanel.add(description);
        fieldPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        fieldPanel.add(confirmButton);

        resetFields(null);
    }

    /**
     * Imposta la classe DAO per interfacciarsi col database.
     * 
     * @param referenceDAO
     *            classe DAO dei riferimenti
     * @throws IllegalArgumentException
     *             se {@code referenceDAO == null}
     */
    private void setReferenceDAO(BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
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
     * Prepara i campi necessari per la creazione di un riferimento.
     */
    protected void setup() {
        fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.PAGE_AXIS));
        fieldPanel.setBorder(new EmptyBorder(50, 30, 50, 30));

        setContentPane(new JScrollPane(fieldPanel));

        title = new JTextField();
        tags = new JTermsField(searchFieldSeparator);
        DOI = new JTextField();
        pubblicationDate = new JDateChooser();
        language = new JComboBox<>(ReferenceLanguage.values());
        categories = new CategoriesSelectionPopupMenu(categoriesTree);
        description = new JTextArea(10, 1);
        authorEditor = new AuthorEditor();

        // TODO: carica dal database gli autori
        authors = new JPopupItemSelection<Author>("Premi per selezionare gli autori");

        JButton addAuthor = new JButton("+");
        addAuthor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authorEditor.setVisible(true);
            }
        });

        JPanel authorsPanel = new JPanel(new BorderLayout(10, 0));
        authorsPanel.add(authors, BorderLayout.CENTER);
        authorsPanel.add(addAuthor, BorderLayout.EAST);

        // relatedReferencesDialog = new ReferenceChooserDialog(categoriesTree, null);
        // relatedReferencesDialog.addReferenceChooserSelectionListener(this);

        relatedReferencesPopupButton = new JPopupButton("Rimandi selezionati");
        JButton addRelatedReference = new JButton("+");
        addRelatedReference.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // relatedReferencesDialog.setVisible(true);
            }
        });

        JPanel relatedReferencesPanel = new JPanel(new BorderLayout(10, 0));
        relatedReferencesPanel.add(relatedReferencesPopupButton, BorderLayout.CENTER);
        relatedReferencesPanel.add(addRelatedReference, BorderLayout.EAST);

        addFieldComponent(title, "Titolo*", "Titolo del riferimento");
        addFieldComponent(tags, "Parole chiave", "Parole chiave associate al riferimento, separate da una virgola");
        addFieldComponent(DOI, "DOI", "Codice identificativo DOI del riferimento");
        addFieldComponent(pubblicationDate, "Data di pubblicazione", "Data di pubblicazione del riferimento");
        addFieldComponent(language, "Lingua");
        addFieldComponent(categories, "Categorie");
        addFieldComponent(authorsPanel, "Autori");
        addFieldComponent(relatedReferencesPanel, "Rimandi");
    }

    /**
     * Riempie i campi di input con i valori presenti nel riferimento passato.
     * Se il riferimento è {@code null}, vengono svuotati.
     * 
     * @param reference
     *            riferimento da cui prendere i valori (se {@code null}, i campi verrano resettati)
     */
    protected void resetFields(T reference) {
        if (reference == null) {
            setTitleValue(null);
            setPubblicationDateValue(null);
            setDOIValue(null);
            setDescriptionValue(null);
            setTagValues(null);
            setLanguageValue(ReferenceLanguage.ENGLISH);
            setRelatedReferences(null);
        } else {
            setTitleValue(reference.getTitle());
            setPubblicationDateValue(reference.getPubblicationDate());
            setDOIValue(reference.getDOI());
            setDescriptionValue(reference.getDescription());
            setTagValues(reference.getTags());
            setLanguageValue(reference.getLanguage());
            setRelatedReferences(reference.getRelatedReferences());
            // setCategoryValues(reference);
        }
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
            resetFields(reference);
        }

        super.setVisible(b);
    }

    /**
     * Funzione chiamata quando viene premuto il tasto di conferma.
     */
    protected abstract void saveReference();

    @Override
    public void onReferenceChooserSelection(BibliographicReference reference) {
        addRelatedReference(reference);
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

    // #region getter/setter

    /**
     * Imposta il valore iniziale del titolo.
     * 
     * @param text
     *            titolo iniziale del riferimento
     */
    protected void setTitleValue(String text) {
        title.setText(text);
    }

    /**
     * Restituisce il titolo inserito dall'utente.
     * 
     * @return
     *         titolo del riferimento
     * @throws RequiredFieldMissingException
     *             se non è stato inserito un titolo
     */
    protected String getTitleValue() throws RequiredFieldMissingException {
        String referenceTitle = convertEmptyStringToNull(title.getText().trim());

        if (title == null)
            throw new RequiredFieldMissingException("Il titolo del riferimento non può essere nullo.");

        return referenceTitle;
    }

    /**
     * Imposta il valore iniziale della data di pubblicazione.
     * 
     * @param date
     *            data di pubblicazione iniziale del riferimento
     */
    protected void setPubblicationDateValue(Date date) {
        pubblicationDate.setDate(date);
    }

    /**
     * Restituisce la data di pubblicazione inserita dall'utente.
     * 
     * @return
     *         data di pubblicazione del riferimento, {@code null} se non è stato inserito niente
     */
    protected Date getPubblicationDateValue() {
        return pubblicationDate.getDate();
    }

    /**
     * Imposta il valore iniziale del DOI.
     * 
     * @param doi
     *            DOI iniziale del riferimento
     */
    protected void setDOIValue(String doi) {
        DOI.setText(doi);
    }

    /**
     * Restituisce il DOI inserito dall'utente.
     * 
     * @return
     *         DOI del riferimento, {@code null} se non è stato inserito niente
     */
    protected String getDOIValue() {
        return convertEmptyStringToNull(DOI.getText().trim());
    }

    /**
     * Imposta il valore iniziale della descrizione.
     * 
     * @param description
     *            descrizione iniziale del riferimento
     */
    protected void setDescriptionValue(String description) {
        this.description.setText(description);
    }

    /**
     * Restituisce la descrizione inserito dall'utente.
     * 
     * @return
     *         descrizione del riferimento, {@code null} se non è stato inserito niente
     */
    protected String getDescriptionValue() {
        return convertEmptyStringToNull(description.getText().trim());
    }

    /**
     * Imposta le parole chiave iniziali del riferimento.
     * 
     * @param tags
     *            parole chiave del riferimento
     */
    protected void setTagValues(Tag[] tags) {
        if (tags == null) {
            this.tags.setText(null);
            return;
        }

        String text = "";

        for (Tag tag : tags) {
            text += tag.getName() + ", ";
        }

        this.tags.setText(text);
    }

    /**
     * Restituisce le parole chiave inserite dall'utente.
     * 
     * @return
     *         parole chiave del riferimento, {@code null} se non è stato inserito niente
     */
    protected Tag[] getTagValues() {
        String[] tagsString = tags.getTerms();

        if (tagsString == null)
            return null;

        Tag[] tags = new Tag[tagsString.length];

        for (int i = 0; i < tags.length; i++) {
            tags[i] = new Tag(tagsString[i]);
        }

        return tags;
    }

    /**
     * Imposta il valore iniziale della lingua.
     * 
     * @param language
     *            lingua del riferimento
     */
    protected void setLanguageValue(ReferenceLanguage language) {
        this.language.setSelectedItem(language);
    }

    /**
     * Restituisce la lingua inserito dall'utente.
     * 
     * @return
     *         lingua del riferimento
     */
    protected ReferenceLanguage getLanguageValue() {
        return (ReferenceLanguage) language.getSelectedItem();
    }

    /**
     * Imposta gli autori iniziali del riferimento.
     * 
     * @param authors
     *            autori del riferimento
     */
    protected void setAuthorValues(Author[] authors) {
        for (Author author : authors) {
            this.authors.selectElement(author);
        }
    }

    /**
     * Restituisce gli autori selezionati dall'utente.
     * 
     * @return
     *         autori del riferimento, {@code null} se non è stato inserito niente
     */
    protected Author[] getAuthorValues() {
        ArrayList<Author> selectedAuthors = authors.getSelectedElements();

        if (selectedAuthors == null || selectedAuthors.size() == 0)
            return null;

        return selectedAuthors.toArray(new Author[selectedAuthors.size()]);
    }

    /**
     * Imposta le categorie iniziali del riferimento.
     * 
     * @param categories
     *            categorie del riferimento
     */
    protected void setCategoryValues(Category[] categories) {
        // TODO: seleziona categorie
    }

    /**
     * Restituisce le categorie selezionate dall'utente.
     * 
     * @return
     *         categorie del riferimento, {@code null} se non è stato inserito niente
     */
    protected Category[] getCategoryValues() {
        return categories.getSelectedCategories();
    }

    /**
     * Imposta i rimandi iniziali associati al riferimento.
     * 
     * @param reference
     *            rimandi associati al riferimento
     */
    protected void setRelatedReferences(BibliographicReference[] references) {
        if (references == null)
            return;

        relatedReferences = null;
        relatedReferencesPopupButton.removeAllFromPopupMenu();

        for (BibliographicReference reference : references) {
            addRelatedReference(reference);
        }
    }

    /**
     * Aggiunge un rimando associato a questo riferimento. In caso {@code reference} sia nullo o già presente, non viene inserito.
     * 
     * @param reference
     *            rimando da associare
     */
    protected void addRelatedReference(BibliographicReference reference) {
        if (reference == null)
            return;

        if (relatedReferences == null)
            relatedReferences = new ArrayList<>(5);

        if (relatedReferences.contains(reference))
            return;

        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBorder(new EmptyBorder(0, 20, 0, 5));
        panel.setBackground(new Color(0, 0, 0, 0));

        JButton removeButton = new JButton(new ImageIcon("images/delete_icon.png"));
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

    /**
     * Restituisce i rimandi associati a questo riferimento selezionati dall'utente.
     * 
     * @return
     *         rimandi del riferimento, {@code null} se non è stato inserito niente
     */
    protected BibliographicReference[] getRelatedReferenceValues() {
        if (relatedReferences == null || relatedReferences.isEmpty())
            return null;

        return relatedReferences.toArray(new BibliographicReference[relatedReferences.size()]);
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

        // FIXME: reference è sempre null per qualche motivo

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
    }

    // #endregion

}
