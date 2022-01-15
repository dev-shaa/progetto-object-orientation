package GUI.ReferenceEditor;

import DAO.BibliographicReferenceDAO;
import GUI.Categories.CategoriesTreeManager;
import Entities.References.OnlineResources.Image;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Pannello di dialogo per la creazione o modifica di un riferimento a un'immagine.
 */
public class ImageEditor extends OnlineResourceEditor {

    private JSpinner width;
    private JSpinner height;

    /**
     * Crea un nuovo pannello di dialogo per la creazione di un riferimento a un'immagine.
     * 
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire il riferimento
     * @param referenceDAO
     *            classe DAO per salvare i riferimenti nel database
     * @throws IllegalArgumentException
     *             se referenceDAO non è un valore valido
     * 
     * @see #setReferenceDAO(BibliographicReferenceDAO)
     */
    public ImageEditor(CategoriesTreeManager categoriesTree, BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        this(categoriesTree, referenceDAO, null);
    }

    /**
     * Crea un nuovo pannello di dialogo per la modifica di un riferimento a un'immagine, inserendo i valori già presenti all'interno dei campi.
     * 
     * @param categoriesTree
     *            albero delle categorie in cui è possibile inserire il riferimento
     * @param referenceDAO
     *            classe DAO per salvare i riferimenti nel database
     * @param image
     *            immagine da modificare (se nullo, non verrà inserito alcun valore e si considera come se si stesse creando un nuovo riferimento)
     * @throws IllegalArgumentException
     *             se referenceDAO non è un valore valido
     * 
     * @see #setReferenceDAO(BibliographicReferenceDAO)
     */
    public ImageEditor(CategoriesTreeManager categoriesTree, BibliographicReferenceDAO referenceDAO, Image image) throws IllegalArgumentException {
        super("Immagine", categoriesTree, referenceDAO, image);

        if (image != null) {
            setWidthValue(image.getWidth());
            setHeightValue(image.getHeight());
        }
    }

    @Override
    protected void setup(CategoriesTreeManager categoriesTree) {
        super.setup(categoriesTree);

        width = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        height = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));

        addFieldComponent(width, "Larghezza");
        addFieldComponent(height, "Altezza");
    }

    @Override
    protected void saveReference() {
        // TODO: salva immagine nel database
    }

    /**
     * Imposta il valore iniziale della larghezza.
     * 
     * @param width
     *            larghezza iniziale dell'immagine
     * @throws IllegalArgumentException
     *             se {@code width < 1}
     */
    protected void setWidthValue(int width) throws IllegalArgumentException {
        this.width.setValue(width);
    }

    /**
     * Restituisce la larghezza inserita dall'utente.
     * 
     * @return
     *         larghezza dell'immagine
     */
    protected int getWidthValue() {
        return (int) width.getValue();
    }

    /**
     * Imposta il valore iniziale dell'altezza.
     * 
     * @param height
     *            altezza iniziale dell'immagine
     * @throws IllegalArgumentException
     *             se {@code height < 1}
     */
    protected void setHeightValue(int height) throws IllegalArgumentException {
        this.height.setValue(height);
    }

    /**
     * Restituisce l'altezza inserita dall'utente.
     * 
     * @return
     *         altezza dell'immagine
     */
    protected int getHeightValue() {
        return (int) height.getValue();
    }

}