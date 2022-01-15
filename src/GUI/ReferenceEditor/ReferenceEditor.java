package GUI.ReferenceEditor;

import GUI.Categories.*;
import GUI.Homepage.Search.CategoriesSelectionPopupMenu;
import GUI.Utilities.JTermsField;
import Entities.Category;
import Entities.Tag;
import Entities.References.*;
import Exceptions.RequiredFieldMissingException;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JDateChooser;
import DAO.BibliographicReferenceDAO;

/**
 * Pannello per la creazione o modifica di un riferimento bibliografico.
 */
public abstract class ReferenceEditor extends JDialog {

    // TODO: authors

    private JTextField title;
    private JTermsField tags;
    private JTextField DOI;
    private JTextArea description;
    private JDateChooser pubblicationDate;
    private JComboBox<ReferenceLanguage> language;
    private CategoriesSelectionPopupMenu categories;
    private JPanel fieldPanel;

    private BibliographicReferenceDAO referenceDAO;

    private final String searchFieldSeparator = ",";
    private final Dimension maximumSize = new Dimension(Integer.MAX_VALUE, 24);
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
     * @param reference
     *            riferimento da modificare (se nullo, non verrà inserito alcun valore e si considera come se si stesse creando un nuovo riferimento)
     * @throws IllegalArgumentException
     *             se referenceDAO non è un valore valido
     * 
     * @see #setReferenceDAO(BibliographicReferenceDAO)
     */
    public ReferenceEditor(String dialogueTitle, CategoriesTreeManager categoriesTree, BibliographicReferenceDAO referenceDAO, BibliographicReference reference) throws IllegalArgumentException {
        super();

        setTitle(dialogueTitle);
        setSize(500, 500);
        setMinimumSize(new Dimension(300, 300));

        setReferenceDAO(referenceDAO);

        // vogliamo che sia un'interfaccia modale
        setModal(true);

        // in questo modo le classi che ereditano ReferenceCreator possono fare l'overriding
        // e aggiungere altri elementi prima delle descrizione
        setup(categoriesTree);

        // la descrizione è un elemento più grande, quindi vogliamo che sia alla fine
        setupDescription();

        // mostra automaticamente quando viene creato
        setVisible(true);

        if (reference != null) {
            setReferenceTitle(reference.getTitle());
            setDOI(reference.getDOI());
            setDescription(reference.getDescription());
            setLanguage(reference.getLanguage());
            setPubblicationDate(reference.getPubblicationDate());
            setTags(reference.getTags());
        }
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
     * 
     * @param categoriesTreeManager
     *            categorie a cui può appartenere un riferimento
     */
    protected void setup(CategoriesTreeManager categoriesTreeManager) {
        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.PAGE_AXIS));
        fieldPanel.setBorder(new EmptyBorder(50, 30, 50, 30));

        JScrollPane scrollPane = new JScrollPane(fieldPanel);
        contentPane.add(scrollPane);

        title = new JTextField();
        tags = new JTermsField(searchFieldSeparator);
        DOI = new JTextField();
        pubblicationDate = new JDateChooser();
        language = new JComboBox<>(ReferenceLanguage.values());
        categories = new CategoriesSelectionPopupMenu(categoriesTreeManager);
        description = new JTextArea();

        addFieldComponent(title, "Titolo", "Titolo del riferimento");
        addFieldComponent(tags, "Parole chiave", "Parole chiave associate al riferimento, separate da una virgola");
        addFieldComponent(DOI, "DOI", "Codice identificativo DOI del riferimento");
        addFieldComponent(pubblicationDate, "Data di pubblicazione", "Data di pubblicazione del riferimento");
        addFieldComponent(language, "Lingua");
        addFieldComponent(categories, "Categorie");

        JButton confirmButton = new JButton("Salva");
        confirmButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveReference();
            }

        });
        contentPane.add(confirmButton, BorderLayout.SOUTH);
    }

    /**
     * Imposta il campo della descrizione.
     */
    private void setupDescription() {
        // FIXME: con troppi caratteri comincia a comportarsi in maniera strana

        JLabel descriptionLabel = new JLabel("Descrizione");
        descriptionLabel.setMaximumSize(maximumSize);
        descriptionLabel.setAlignmentX(alignment);

        description.setLineWrap(true);
        description.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        description.setAlignmentX(alignment);

        fieldPanel.add(descriptionLabel);
        fieldPanel.add(description);
    }

    /**
     * Aggiunge un componente nel pannello dove sono presenti i campi di input.
     * Le dimensioni e l'allineamento sono impostate automaticamente.
     * 
     * @param component
     *            componente da aggiungere
     */
    protected void addFieldComponent(JComponent component) {
        component.setMaximumSize(maximumSize);
        component.setAlignmentX(alignment);
        fieldPanel.add(component);
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

        addFieldComponent(labelField);
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
     * Funzione chiamata quando viene premuto il tasto di conferma.
     */
    protected abstract void saveReference();

    /**
     * Imposta il valore iniziale del titolo.
     * 
     * @param text
     *            titolo iniziale del riferimento
     */
    protected void setReferenceTitle(String text) {
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
    protected String getReferenceTitle() throws RequiredFieldMissingException {
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
    protected void setPubblicationDate(Date date) {
        pubblicationDate.setDate(date);
    }

    /**
     * Restituisce la data di pubblicazione inserita dall'utente.
     * 
     * @return
     *         data di pubblicazione del riferimento, {@code null} se non è stato inserito niente
     */
    protected Date getPubblicationDate() {
        return pubblicationDate.getDate();
    }

    /**
     * Imposta il valore iniziale del DOI.
     * 
     * @param doi
     *            DOI iniziale del riferimento
     */
    protected void setDOI(String doi) {
        DOI.setText(doi);
    }

    /**
     * Restituisce il DOI inserito dall'utente.
     * 
     * @return
     *         DOI del riferimento, {@code null} se non è stato inserito niente
     */
    protected String getDOI() {
        return convertEmptyStringToNull(DOI.getText().trim());
    }

    /**
     * Imposta il valore iniziale della descrizione.
     * 
     * @param description
     *            descrizione iniziale del riferimento
     */
    protected void setDescription(String description) {
        this.description.setText(description);
    }

    /**
     * Restituisce la descrizione inserito dall'utente.
     * 
     * @return
     *         descrizione del riferimento, {@code null} se non è stato inserito niente
     */
    protected String getDescription() {
        return convertEmptyStringToNull(description.getText().trim());
    }

    /**
     * Imposta le parole chiave iniziali del riferimento.
     * 
     * @param tags
     *            parole chiave del riferimento
     */
    protected void setTags(Tag[] tags) {
        // TODO:
    }

    /**
     * Restituisce le parole chiave inserite dall'utente.
     * 
     * @return
     *         parole chiave del riferimento, {@code null} se non è stato inserito niente
     */
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

    /**
     * Imposta il valore iniziale della lingua.
     * 
     * @param language
     *            lingua del riferimento
     */
    protected void setLanguage(ReferenceLanguage language) {
        this.language.setSelectedItem(language);
    }

    /**
     * Restituisce la lingua inserito dall'utente.
     * 
     * @return
     *         lingua del riferimento
     */
    protected ReferenceLanguage getLanguage() {
        return (ReferenceLanguage) language.getSelectedItem();
    }

    /**
     * Imposta le categorie iniziali del riferimento.
     * 
     * @param categories
     *            categorie del riferimento
     */
    protected void setCategories(Category[] categories) {
        // TODO:
    }

    /**
     * Restituisce le categorie selezionate dall'utente.
     * 
     * @return
     *         categorie del riferimento, {@code null} se non è stato inserito niente
     */
    protected Category[] getCategories() {
        return categories.getSelectedCategories();
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

}
