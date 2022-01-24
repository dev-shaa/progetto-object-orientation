package GUI.Homepage.References.Editor;

import DAO.BibliographicReferenceDAO;
import Entities.References.OnlineResources.Image;
import Exceptions.ReferenceDatabaseException;
import Exceptions.RequiredFieldMissingException;
import GUI.Homepage.Categories.CategoryTreeModel;

import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Pannello di dialogo per la creazione o modifica di un riferimento a un'immagine.
 */
public class ImageEditor extends OnlineResourceEditor<Image> {

    private Image image;
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
    public ImageEditor(CategoryTreeModel categoriesTree, BibliographicReferenceDAO referenceDAO) throws IllegalArgumentException {
        super("Immagine", categoriesTree, referenceDAO);
    }

    @Override
    protected void setup() {
        super.setup();

        width = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        height = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));

        addFieldComponent(width, "Larghezza");
        addFieldComponent(height, "Altezza");
    }

    @Override
    protected void resetFields(Image reference) {
        super.resetFields(reference);

        if (reference == null) {
            setWidthValue(1);
            setHeightValue(1);
        } else {
            setWidthValue(reference.getWidth());
            setHeightValue(reference.getHeight());
        }
    }

    @Override
    protected void saveReference() {
        Image imageToFill = image == null ? new Image("placeholder", "placeholder") : image;

        try {
            fillReferenceValues(imageToFill);

            getReferenceDAO().saveImage(imageToFill);
        } catch (RequiredFieldMissingException e) {
            JOptionPane.showMessageDialog(this, "Uno o più campi obbligatori non sono stati inseriti.", "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
        } catch (ReferenceDatabaseException e) {
            JOptionPane.showMessageDialog(this, "Si è verificato un errore durante il salvataggio", "Errore database", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void fillReferenceValues(Image reference) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillReferenceValues(reference);

        image.setWidth(getWidthValue());
        image.setHeight(getHeightValue());
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
