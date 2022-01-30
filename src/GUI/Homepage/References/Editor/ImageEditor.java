package GUI.Homepage.References.Editor;

import Entities.References.OnlineResources.Image;

import Exceptions.ReferenceDatabaseException;
import Exceptions.RequiredFieldMissingException;

import Controller.AuthorController;
import Controller.CategoryController;
import Controller.ReferenceController;

import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Finestra di dialogo per la creazione o modifica di un riferimento a un'immagine.
 */
public class ImageEditor extends OnlineResourceEditor<Image> {

    private JSpinner width;
    private JSpinner height;

    /**
     * Crea una nuova finestra di dialogo per la creazione o modifica di un riferimento a un'immagine.
     * 
     * @param categoryController
     *            controller delle categorie
     * @param referenceController
     *            controller dei riferimenti
     * @param authorController
     *            controller degli autori
     * @throws IllegalArgumentException
     *             se {@code categoryController == null}, {@code referenceController == null} o {@code authorController == null}
     */
    public ImageEditor(CategoryController categoryController, ReferenceController referenceController, AuthorController authorController) {
        super("Immagine", categoryController, referenceController, authorController);
    }

    @Override
    protected void initialize() {
        super.initialize();

        width = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        height = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));

        addFieldComponent(width, "Larghezza");
        addFieldComponent(height, "Altezza");
    }

    @Override
    protected void setFieldsValues(Image reference) {
        super.setFieldsValues(reference);

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
        try {
            Image imageToSave = getOpenReference() == null ? new Image("temp", "temp") : getOpenReference();
            fillReferenceValues(imageToSave);
            getReferenceController().saveReference(imageToSave);
        } catch (RequiredFieldMissingException e) {
            JOptionPane.showMessageDialog(this, "Uno o più campi obbligatori non sono stati inseriti.", "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
        } catch (ReferenceDatabaseException e) {
            JOptionPane.showMessageDialog(this, "Si è verificato un errore durante il salvataggio", "Errore database", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void fillReferenceValues(Image reference) throws IllegalArgumentException, RequiredFieldMissingException {
        super.fillReferenceValues(reference);

        reference.setWidth(getWidthValue());
        reference.setHeight(getHeightValue());
    }

    private void setWidthValue(int width) {
        this.width.setValue(width);
    }

    private int getWidthValue() {
        return (int) width.getValue();
    }

    private void setHeightValue(int height) {
        this.height.setValue(height);
    }

    private int getHeightValue() {
        return (int) height.getValue();
    }

}
