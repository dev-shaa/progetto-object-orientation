package GUI.ReferenceEditor;

import DAO.BibliographicReferenceDAO;
import GUI.Categories.CategoriesTreeManager;
import Entities.References.PhysicalResources.Publication;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 * Pannello di dialogo per la creazione o modifica di un riferimento a una pubblicazione.
 */
public abstract class PublicationEditor extends ReferenceEditor {

    private JSpinner pageCount;
    private JTextField URL;
    private JTextField publisher;

    /**
     * Crea un nuovo pannello di dialogo per la modifica di un riferimento a una pubblicazione, inserendo i valori già presenti all'interno dei campi.
     * 
     * @param dialogueTitle
     *            titolo della schermata di dialogo
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire il riferimento
     * @param referenceDAO
     *            classe DAO per salvare i riferimenti nel database
     * @param publication
     *            pubblicazione da modificare (se nullo, non verrà inserito alcun valore e si considera come se si stesse creando un nuovo riferimento)
     * @throws IllegalArgumentException
     *             se referenceDAO non è un valore valido
     * 
     * @see #setReferenceDAO(BibliographicReferenceDAO)
     */
    public PublicationEditor(String dialogueTitle, CategoriesTreeManager categoriesTree, BibliographicReferenceDAO referenceDAO, Publication publication) throws IllegalArgumentException {
        super(dialogueTitle, categoriesTree, referenceDAO, publication);

        if (publication != null) {
            setPageCountValue(publication.getPageCount());
            setURLValue(publication.getURL());
            setPublisherValue(publication.getPublisher());
        }
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTree) {
        super.setup(categoriesTree);

        pageCount = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        URL = new JTextField();
        publisher = new JTextField();

        addFieldComponent(pageCount, "Pagine");
        addFieldComponent(URL, "URL");
        addFieldComponent(publisher, "Editore");
    }

    /**
     * Imposta il valore iniziale delle pagine.
     * 
     * @param pageCount
     *            numero di pagine iniziale del riferimento
     */
    protected void setPageCountValue(int pageCount) {
        this.pageCount.setValue(pageCount);
    }

    /**
     * Restituisce il numero di pagine inserito dall'utente.
     * 
     * @return
     *         numero di pagine del riferimento
     */
    protected int getPageCountValue() {
        return (int) pageCount.getValue();
    }

    /**
     * Imposta il valore iniziale dell'URL.
     * 
     * @param URL
     *            URL iniziale del riferimento
     */
    protected void setURLValue(String URL) {
        this.URL.setText(URL);
    }

    /**
     * Restituisce l'URL inserito dall'utente.
     * 
     * @return
     *         URL del riferimento, {@code null} se non è stato inserito niente
     */
    protected String getURLValue() {
        return convertEmptyStringToNull(URL.getText().trim());
    }

    /**
     * Imposta l'editore iniziale presente nel campo dell'editore.
     * 
     * @param publisher
     *            editore iniziale
     */
    protected void setPublisherValue(String publisher) {
        this.publisher.setText(publisher);
    }

    /**
     * Restituisce l'editore inserito dall'utente.
     * 
     * @return
     *         editore della pubblicazione, {@code null} se non è stato inserito niente
     */
    protected String getPublisherValue() {
        return convertEmptyStringToNull(publisher.getText().trim());
    }

}
