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
 * Pannello di dialogo per la creazione o modifica di un riferimento a un'immagine.
 */
public class ImageEditor extends OnlineResourceEditor<Image> {

    private Image image;
    private JSpinner width;
    private JSpinner height;

    /**
     * TODO: commenta
     * 
     * @param categoryController
     * @param referenceController
     * @param authorController
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
            Image imageToFill = image == null ? new Image("placeholder", "placeholder") : image;
            fillReferenceValues(imageToFill);
            getReferenceController().saveReference(imageToFill);
        } catch (RequiredFieldMissingException e) {
            JOptionPane.showMessageDialog(this, "Uno o più campi obbligatori non sono stati inseriti.", "Campi obbligatori mancanti", JOptionPane.ERROR_MESSAGE);
        } catch (ReferenceDatabaseException e) {
            JOptionPane.showMessageDialog(this, "Si è verificato un errore durante il salvataggio", "Errore database", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void fillReferenceValues(Image reference) throws RequiredFieldMissingException {
        super.fillReferenceValues(reference);

        image.setWidth(getWidthValue());
        image.setHeight(getHeightValue());
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
